class Truck {
  final String id;
  final String vin;
  final String make;
  final String model;
  final String? colour;
  final String? engineNumber;

  const Truck({
    required this.id,
    required this.vin,
    required this.make,
    required this.model,
    this.colour,
    this.engineNumber,
  });

  factory Truck.fromJson(Map<String, dynamic> json) {
    return Truck(
      id: json['id'] as String,
      vin: json['vin'] as String,
      make: json['make'] as String,
      model: json['model'] as String,
      colour: json['colour'] as String?,
      engineNumber: json['engine_number'] as String?,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'vin': vin,
      'make': make,
      'model': model,
      'colour': colour,
      'engine_number': engineNumber,
    };
  }

  Truck copyWith({
    String? id,
    String? vin,
    String? make,
    String? model,
    String? colour,
    String? engineNumber,
  }) {
    return Truck(
      id: id ?? this.id,
      vin: vin ?? this.vin,
      make: make ?? this.make,
      model: model ?? this.model,
      colour: colour ?? this.colour,
      engineNumber: engineNumber ?? this.engineNumber,
    );
  }

  String get displayName => '$make $model';

  @override
  String toString() => 'Truck($displayName, VIN: $vin)';

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is Truck && runtimeType == other.runtimeType && id == other.id;

  @override
  int get hashCode => id.hashCode;
}
