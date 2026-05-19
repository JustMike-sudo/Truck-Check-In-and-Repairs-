class ServiceTask {
  final String id;
  final String description;

  const ServiceTask({
    required this.id,
    required this.description,
  });

  factory ServiceTask.fromJson(Map<String, dynamic> json) {
    return ServiceTask(
      id: json['id'] as String,
      description: json['description'] as String,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'description': description,
    };
  }

  ServiceTask copyWith({
    String? id,
    String? description,
  }) {
    return ServiceTask(
      id: id ?? this.id,
      description: description ?? this.description,
    );
  }

  @override
  String toString() => 'ServiceTask($description)';

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is ServiceTask &&
          runtimeType == other.runtimeType &&
          id == other.id;

  @override
  int get hashCode => id.hashCode;
}
