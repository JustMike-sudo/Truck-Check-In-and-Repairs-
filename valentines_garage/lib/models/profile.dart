class Profile {
  final String id;
  final String role;

  const Profile({
    required this.id,
    required this.role,
  });

  factory Profile.fromJson(Map<String, dynamic> json) {
    return Profile(
      id: json['id'] as String,
      role: json['role'] as String? ?? 'mechanic',
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'role': role,
    };
  }

  bool get isAdmin => role == 'admin';
  bool get isMechanic => role == 'mechanic';

  Profile copyWith({
    String? id,
    String? role,
  }) {
    return Profile(
      id: id ?? this.id,
      role: role ?? this.role,
    );
  }

  @override
  String toString() => 'Profile(id: $id, role: $role)';

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is Profile && runtimeType == other.runtimeType && id == other.id;

  @override
  int get hashCode => id.hashCode;
}
