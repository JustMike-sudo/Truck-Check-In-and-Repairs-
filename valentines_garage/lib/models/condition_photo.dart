class ConditionPhoto {
  final String id;
  final String checkInId;
  final String photoUrl;

  const ConditionPhoto({
    required this.id,
    required this.checkInId,
    required this.photoUrl,
  });

  factory ConditionPhoto.fromJson(Map<String, dynamic> json) {
    return ConditionPhoto(
      id: json['id'] as String,
      checkInId: json['check_in_id'] as String,
      photoUrl: json['photo_url'] as String,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'check_in_id': checkInId,
      'photo_url': photoUrl,
    };
  }

  ConditionPhoto copyWith({
    String? id,
    String? checkInId,
    String? photoUrl,
  }) {
    return ConditionPhoto(
      id: id ?? this.id,
      checkInId: checkInId ?? this.checkInId,
      photoUrl: photoUrl ?? this.photoUrl,
    );
  }

  @override
  String toString() => 'ConditionPhoto(checkInId: $checkInId)';

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is ConditionPhoto &&
          runtimeType == other.runtimeType &&
          id == other.id;

  @override
  int get hashCode => id.hashCode;
}
