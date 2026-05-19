import 'dart:convert';
import 'package:supabase_flutter/supabase_flutter.dart';
import 'package:valentines_garage/models/truck.dart';
import 'package:valentines_garage/models/check_in.dart';
import 'package:valentines_garage/models/service_task.dart';
import 'package:valentines_garage/models/task_entry.dart';
import 'package:valentines_garage/models/condition_photo.dart';
import 'package:valentines_garage/models/profile.dart';

class SupabaseService {
  static final SupabaseService _instance = SupabaseService._internal();
  factory SupabaseService() => _instance;
  SupabaseService._internal();

  SupabaseClient get _client => Supabase.instance.client;

  // ─── TRUCKS ──────────────────────────────────────────────────────────

  Future<List<Truck>> getTrucks() async {
    final response = await _client.functions.invoke(
      'get_trucks',
      method: HttpMethod.get,
    );
    final data = _decodeResponseList(response);
    return data.map((json) => Truck.fromJson(json)).toList();
  }

  Future<Truck> createTruck({
    required String vin,
    required String make,
    required String model,
    String? colour,
    String? engineNumber,
  }) async {
    final response = await _client.functions.invoke(
      'create_truck',
      body: {
        'vin': vin,
        'make': make,
        'model': model,
        'colour': colour,
        'engine_number': engineNumber,
      },
    );
    final data = _decodeResponseMap(response);
    return Truck.fromJson(data);
  }

  Future<Truck> updateTruck({
    required String id,
    String? vin,
    String? make,
    String? model,
    String? colour,
    String? engineNumber,
  }) async {
    final body = <String, dynamic>{'id': id};
    if (vin != null) body['vin'] = vin;
    if (make != null) body['make'] = make;
    if (model != null) body['model'] = model;
    if (colour != null) body['colour'] = colour;
    if (engineNumber != null) body['engine_number'] = engineNumber;

    final response = await _client.functions.invoke(
      'update_truck',
      body: body,
    );
    final data = _decodeResponseMap(response);
    return Truck.fromJson(data);
  }

  Future<void> deleteTruck(String id) async {
    await _client.functions.invoke(
      'delete_truck',
      body: {'id': id},
    );
  }

  // ─── CHECK-INS ──────────────────────────────────────────────────────

  Future<List<CheckIn>> getCheckIns() async {
    final response = await _client.functions.invoke(
      'get_check_ins',
      method: HttpMethod.get,
    );
    final data = _decodeResponseList(response);
    return data.map((json) => CheckIn.fromJson(json)).toList();
  }

  Future<CheckIn> createCheckIn({
    required String truckId,
    required DateTime checkInDate,
    String? conditionDescription,
    int? odometerKm,
  }) async {
    final response = await _client.functions.invoke(
      'create_check_in',
      body: {
        'truck_id': truckId,
        'check_in_date': checkInDate.toIso8601String(),
        'condition_description': conditionDescription,
        'odometer_km': odometerKm,
      },
    );
    final data = _decodeResponseMap(response);
    return CheckIn.fromJson(data);
  }

  // ─── SERVICE TASKS ───────────────────────────────────────────────────

  Future<List<ServiceTask>> getServiceTasks() async {
    final response = await _client.functions.invoke(
      'get_service_tasks',
      method: HttpMethod.get,
    );
    final data = _decodeResponseList(response);
    return data.map((json) => ServiceTask.fromJson(json)).toList();
  }

  Future<ServiceTask> createServiceTask({
    required String description,
  }) async {
    final response = await _client.functions.invoke(
      'create_service_task',
      body: {'description': description},
    );
    final data = _decodeResponseMap(response);
    return ServiceTask.fromJson(data);
  }

  // ─── TASK ENTRIES ────────────────────────────────────────────────────

  Future<List<TaskEntry>> getTaskEntries({String? checkInId}) async {
    final response = await _client.functions.invoke(
      'get_task_entries',
      method: HttpMethod.get,
    );
    final data = _decodeResponseList(response);
    var entries = data.map((json) => TaskEntry.fromJson(json)).toList();
    if (checkInId != null) {
      entries = entries.where((e) => e.checkInId == checkInId).toList();
    }
    return entries;
  }

  Future<TaskEntry> createTaskEntry({
    required String checkInId,
    required String serviceTaskId,
    String? notes,
    String? mechanicId,
  }) async {
    final response = await _client.functions.invoke(
      'create_task_entry',
      body: {
        'check_in_id': checkInId,
        'service_task_id': serviceTaskId,
        'notes': notes,
        'mechanic_id': mechanicId,
      },
    );
    final data = _decodeResponseMap(response);
    return TaskEntry.fromJson(data);
  }

  Future<TaskEntry> updateTaskEntry({
    required String id,
    bool? completed,
    String? notes,
  }) async {
    final body = <String, dynamic>{'id': id};
    if (completed != null) body['completed'] = completed;
    if (notes != null) body['notes'] = notes;

    final response = await _client.functions.invoke(
      'update_task_entry',
      body: body,
    );
    final data = _decodeResponseMap(response);
    return TaskEntry.fromJson(data);
  }

  // ─── CONDITION PHOTOS ────────────────────────────────────────────────

  Future<List<ConditionPhoto>> getConditionPhotos({String? checkInId}) async {
    final response = await _client.functions.invoke(
      'get_condition_photos',
      method: HttpMethod.get,
    );
    final data = _decodeResponseList(response);
    var photos = data.map((json) => ConditionPhoto.fromJson(json)).toList();
    if (checkInId != null) {
      photos = photos.where((p) => p.checkInId == checkInId).toList();
    }
    return photos;
  }

  Future<ConditionPhoto> addConditionPhoto({
    required String checkInId,
    required String photoUrl,
  }) async {
    final response = await _client.functions.invoke(
      'add_condition_photo',
      body: {
        'check_in_id': checkInId,
        'photo_url': photoUrl,
      },
    );
    final data = _decodeResponseMap(response);
    return ConditionPhoto.fromJson(data);
  }

  // ─── USER PROFILE ───────────────────────────────────────────────────

  Future<Profile?> getUserProfile() async {
    final user = _client.auth.currentUser;
    if (user == null) return null;

    try {
      final response = await _client
          .from('profiles')
          .select()
          .eq('id', user.id)
          .single();
      return Profile.fromJson(response);
    } catch (e) {
      return null;
    }
  }

  // ─── HELPERS ─────────────────────────────────────────────────────────

  List<Map<String, dynamic>> _decodeResponseList(FunctionResponse response) {
    final decoded = _decodeBody(response);
    if (decoded is List) {
      return decoded.cast<Map<String, dynamic>>();
    }
    if (decoded is Map && decoded.containsKey('data')) {
      final data = decoded['data'];
      if (data is List) return data.cast<Map<String, dynamic>>();
    }
    return [];
  }

  Map<String, dynamic> _decodeResponseMap(FunctionResponse response) {
    final decoded = _decodeBody(response);
    if (decoded is Map<String, dynamic>) {
      if (decoded.containsKey('data') && decoded['data'] is Map) {
        return decoded['data'] as Map<String, dynamic>;
      }
      return decoded;
    }
    throw Exception('Unexpected response format');
  }

  dynamic _decodeBody(FunctionResponse response) {
    final data = response.data;
    if (data is String) {
      return jsonDecode(data);
    }
    return data;
  }
}
