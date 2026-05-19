import 'truck.dart';

class CheckIn {
  final String id;
  final String truckId;
  final DateTime checkInDate;
  final String? conditionDescription;
  final int? odometerKm;
  final Truck? truck;

  const CheckIn({
    required this.id,
    required this.truckId,
    required this.checkInDate,
    this.conditionDescription,
    this.odometerKm,
    this.truck,
  });

  factory CheckIn.fromJson(Map<String, dynamic> json) {
    return CheckIn(
      id: json['id'] as String,
      truckId: json['truck_id'] as String,
      checkInDate: DateTime.parse(json['check_in_date'] as String),
      conditionDescription: json['condition_description'] as String?,
      odometerKm: json['odometer_km'] as int?,
      truck: json['truck'] != null
          ? Truck.fromJson(json['truck'] as Map<String, dynamic>)
          : null,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'truck_id': truckId,
      'check_in_date': checkInDate.toIso8601String(),
      'condition_description': conditionDescription,
      'odometer_km': odometerKm,
    };
  }

  CheckIn copyWith({
    String? id,
    String? truckId,
    DateTime? checkInDate,
    String? conditionDescription,
    int? odometerKm,
    Truck? truck,
  }) {
    return CheckIn(
      id: id ?? this.id,
      truckId: truckId ?? this.truckId,
      checkInDate: checkInDate ?? this.checkInDate,
      conditionDescription: conditionDescription ?? this.conditionDescription,
      odometerKm: odometerKm ?? this.odometerKm,
      truck: truck ?? this.truck,
    );
  }

  bool get hasSevereDamage {
    if (conditionDescription == null) return false;
    final lower = conditionDescription!.toLowerCase();
    return lower.contains('severe') ||
        lower.contains('critical') ||
        lower.contains('major damage') ||
        lower.contains('unsafe');
  }

  @override
  String toString() => 'CheckIn(truckId: $truckId, date: $checkInDate)';

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is CheckIn && runtimeType == other.runtimeType && id == other.id;

  @override
  int get hashCode => id.hashCode;
}
