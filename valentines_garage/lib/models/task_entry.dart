import 'check_in.dart';
import 'service_task.dart';

class TaskEntry {
  final String id;
  final String checkInId;
  final String serviceTaskId;
  final bool completed;
  final String? notes;
  final String? mechanicId;
  final CheckIn? checkIn;
  final ServiceTask? serviceTask;

  const TaskEntry({
    required this.id,
    required this.checkInId,
    required this.serviceTaskId,
    this.completed = false,
    this.notes,
    this.mechanicId,
    this.checkIn,
    this.serviceTask,
  });

  factory TaskEntry.fromJson(Map<String, dynamic> json) {
    return TaskEntry(
      id: json['id'] as String,
      checkInId: json['check_in_id'] as String,
      serviceTaskId: json['service_task_id'] as String,
      completed: json['completed'] as bool? ?? false,
      notes: json['notes'] as String?,
      mechanicId: json['mechanic_id'] as String?,
      checkIn: json['check_in'] != null
          ? CheckIn.fromJson(json['check_in'] as Map<String, dynamic>)
          : null,
      serviceTask: json['service_task'] != null
          ? ServiceTask.fromJson(json['service_task'] as Map<String, dynamic>)
          : null,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'check_in_id': checkInId,
      'service_task_id': serviceTaskId,
      'completed': completed,
      'notes': notes,
      'mechanic_id': mechanicId,
    };
  }

  TaskEntry copyWith({
    String? id,
    String? checkInId,
    String? serviceTaskId,
    bool? completed,
    String? notes,
    String? mechanicId,
    CheckIn? checkIn,
    ServiceTask? serviceTask,
  }) {
    return TaskEntry(
      id: id ?? this.id,
      checkInId: checkInId ?? this.checkInId,
      serviceTaskId: serviceTaskId ?? this.serviceTaskId,
      completed: completed ?? this.completed,
      notes: notes ?? this.notes,
      mechanicId: mechanicId ?? this.mechanicId,
      checkIn: checkIn ?? this.checkIn,
      serviceTask: serviceTask ?? this.serviceTask,
    );
  }

  String get statusLabel => completed ? 'COMPLETED' : 'IN PROGRESS';

  @override
  String toString() =>
      'TaskEntry(task: $serviceTaskId, completed: $completed)';

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is TaskEntry &&
          runtimeType == other.runtimeType &&
          id == other.id;

  @override
  int get hashCode => id.hashCode;
}
