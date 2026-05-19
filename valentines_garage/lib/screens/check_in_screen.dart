import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:valentines_garage/theme/app_colors.dart';
import 'package:valentines_garage/services/supabase_service.dart';

class CheckInScreen extends StatefulWidget {
  const CheckInScreen({super.key});

  @override
  State<CheckInScreen> createState() => _CheckInScreenState();
}

class _CheckInScreenState extends State<CheckInScreen> {
  final _formKey = GlobalKey<FormState>();
  final _supabaseService = SupabaseService();

  final _vinController = TextEditingController();
  final _licensePlateController = TextEditingController();
  final _modelController = TextEditingController();
  final _mileageController = TextEditingController();
  final _conditionController = TextEditingController();

  bool _isSubmitting = false;
  bool _submitted = false;
  final List<String> _photoUrls = [];

  @override
  void dispose() {
    _vinController.dispose();
    _licensePlateController.dispose();
    _modelController.dispose();
    _mileageController.dispose();
    _conditionController.dispose();
    super.dispose();
  }

  Future<void> _submitCheckIn() async {
    if (!_formKey.currentState!.validate()) return;

    setState(() => _isSubmitting = true);

    try {
      // Parse make/model from the model field
      final modelParts = _modelController.text.trim().split('/');
      final makeModel = modelParts[0].trim().split(' ');
      final make = makeModel.isNotEmpty ? makeModel[0] : 'Unknown';
      final model = makeModel.length > 1
          ? makeModel.sublist(1).join(' ')
          : (modelParts.length > 1 ? modelParts[1].trim() : 'Unknown');

      // Create the truck first
      final truck = await _supabaseService.createTruck(
        vin: _vinController.text.trim(),
        make: make,
        model: model,
      );

      // Create the check-in
      final mileage = int.tryParse(_mileageController.text.replaceAll(',', ''));
      final checkIn = await _supabaseService.createCheckIn(
        truckId: truck.id,
        checkInDate: DateTime.now(),
        conditionDescription: _conditionController.text.trim().isNotEmpty
            ? _conditionController.text.trim()
            : null,
        odometerKm: mileage,
      );

      // Add photos if any
      for (final url in _photoUrls) {
        await _supabaseService.addConditionPhoto(
          checkInId: checkIn.id,
          photoUrl: url,
        );
      }

      setState(() {
        _submitted = true;
        _isSubmitting = false;
      });

      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Row(
              children: [
                const Icon(Icons.check_circle, color: AppColors.success, size: 18),
                const SizedBox(width: 8),
                Text(
                  'Vehicle checked in successfully',
                  style: GoogleFonts.inter(fontSize: 13),
                ),
              ],
            ),
          ),
        );

        // Reset form after delay
        Future.delayed(const Duration(seconds: 2), () {
          if (mounted) {
            _formKey.currentState?.reset();
            _vinController.clear();
            _licensePlateController.clear();
            _modelController.clear();
            _mileageController.clear();
            _conditionController.clear();
            setState(() {
              _photoUrls.clear();
              _submitted = false;
            });
          }
        });
      }
    } catch (e) {
      setState(() => _isSubmitting = false);
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Error: $e')),
        );
      }
    }
  }

  void _addPhotoPlaceholder() {
    setState(() {
      _photoUrls.add('https://via.placeholder.com/150?text=Truck+${_photoUrls.length + 1}');
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.background,
      body: SingleChildScrollView(
        padding: const EdgeInsets.fromLTRB(20, 16, 20, 100),
        child: Form(
          key: _formKey,
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              // ─── HEADER ──────────────────────────────────────────
              Row(
                children: [
                  Container(
                    width: 4,
                    height: 28,
                    decoration: BoxDecoration(
                      color: AppColors.primary,
                      borderRadius: BorderRadius.circular(2),
                    ),
                  ),
                  const SizedBox(width: 10),
                  Text(
                    'VEHICLE INTAKE',
                    style: GoogleFonts.rajdhani(
                      fontSize: 26,
                      fontWeight: FontWeight.w700,
                      color: AppColors.textPrimary,
                      letterSpacing: 1.5,
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 6),
              Text(
                'Log incoming equipment and assess immediate repair needs.',
                style: GoogleFonts.inter(
                  fontSize: 13,
                  color: AppColors.textSecondary,
                  height: 1.4,
                ),
              ),
              const SizedBox(height: 28),

              // ─── VIN NUMBER ──────────────────────────────────────
              _FieldLabel(text: 'VIN NUMBER'),
              const SizedBox(height: 6),
              TextFormField(
                controller: _vinController,
                style: GoogleFonts.sourceCodePro(
                  fontSize: 14,
                  color: AppColors.textPrimary,
                  letterSpacing: 0.5,
                ),
                textCapitalization: TextCapitalization.characters,
                maxLength: 17,
                decoration: const InputDecoration(
                  hintText: '17-Digit Identifier',
                  counterText: '',
                ),
                validator: (v) {
                  if (v == null || v.trim().isEmpty) return 'VIN is required';
                  if (v.trim().length < 5) return 'VIN too short';
                  return null;
                },
              ),
              const SizedBox(height: 18),

              // ─── LICENSE PLATE ────────────────────────────────────
              _FieldLabel(text: 'LICENSE PLATE'),
              const SizedBox(height: 6),
              TextFormField(
                controller: _licensePlateController,
                style: GoogleFonts.sourceCodePro(
                  fontSize: 14,
                  color: AppColors.textPrimary,
                  letterSpacing: 1,
                ),
                textCapitalization: TextCapitalization.characters,
                decoration: const InputDecoration(
                  hintText: 'ABC-1234',
                ),
              ),
              const SizedBox(height: 18),

              // ─── MODEL / CONFIGURATION ────────────────────────────
              _FieldLabel(text: 'MODEL / CONFIGURATION'),
              const SizedBox(height: 6),
              TextFormField(
                controller: _modelController,
                style: GoogleFonts.inter(
                  fontSize: 14,
                  color: AppColors.textPrimary,
                ),
                decoration: const InputDecoration(
                  hintText: 'e.g. Peterbilt 579 / Sleeper',
                ),
                validator: (v) {
                  if (v == null || v.trim().isEmpty) return 'Model is required';
                  return null;
                },
              ),
              const SizedBox(height: 28),

              // ─── CRITICAL DATA SECTION ────────────────────────────
              Container(
                padding: const EdgeInsets.all(16),
                decoration: BoxDecoration(
                  color: AppColors.surface,
                  borderRadius: BorderRadius.circular(12),
                  border: Border.all(
                    color: AppColors.primary.withValues(alpha: 0.6),
                    width: 1.5,
                  ),
                ),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Row(
                      children: [
                        Icon(Icons.warning_amber_rounded,
                            color: AppColors.primary, size: 18),
                        const SizedBox(width: 6),
                        Text(
                          'CRITICAL DATA REQUIRED',
                          style: GoogleFonts.rajdhani(
                            fontSize: 13,
                            fontWeight: FontWeight.w700,
                            color: AppColors.primary,
                            letterSpacing: 1.5,
                          ),
                        ),
                      ],
                    ),
                    const SizedBox(height: 16),

                    // Current Mileage
                    _FieldLabel(text: 'CURRENT MILEAGE'),
                    const SizedBox(height: 6),
                    TextFormField(
                      controller: _mileageController,
                      keyboardType: TextInputType.number,
                      inputFormatters: [FilteringTextInputFormatter.digitsOnly],
                      style: GoogleFonts.rajdhani(
                        fontSize: 22,
                        fontWeight: FontWeight.w600,
                        color: AppColors.textPrimary,
                        letterSpacing: 1,
                      ),
                      decoration: InputDecoration(
                        hintText: '000,000',
                        hintStyle: GoogleFonts.rajdhani(
                          fontSize: 22,
                          fontWeight: FontWeight.w400,
                          color: AppColors.textSecondary.withValues(alpha: 0.3),
                        ),
                        suffixText: 'MI',
                        suffixStyle: GoogleFonts.rajdhani(
                          fontSize: 14,
                          fontWeight: FontWeight.w600,
                          color: AppColors.textSecondary,
                        ),
                        filled: true,
                        fillColor: AppColors.cardHighlight,
                        border: OutlineInputBorder(
                          borderRadius: BorderRadius.circular(10),
                          borderSide: BorderSide(
                            color: AppColors.primary.withValues(alpha: 0.3),
                          ),
                        ),
                        enabledBorder: OutlineInputBorder(
                          borderRadius: BorderRadius.circular(10),
                          borderSide: BorderSide(
                            color: AppColors.primary.withValues(alpha: 0.3),
                          ),
                        ),
                        focusedBorder: OutlineInputBorder(
                          borderRadius: BorderRadius.circular(10),
                          borderSide: const BorderSide(
                            color: AppColors.primary,
                            width: 1.5,
                          ),
                        ),
                      ),
                    ),
                    const SizedBox(height: 16),

                    // Vehicle Condition / Damage
                    _FieldLabel(text: 'VEHICLE CONDITION / DAMAGE'),
                    const SizedBox(height: 6),
                    TextFormField(
                      controller: _conditionController,
                      maxLines: 4,
                      style: GoogleFonts.inter(
                        fontSize: 14,
                        color: AppColors.textPrimary,
                        height: 1.5,
                      ),
                      decoration: InputDecoration(
                        hintText:
                            'Describe physical state, leaks, or body damage...',
                        alignLabelWithHint: true,
                        filled: true,
                        fillColor: AppColors.cardHighlight,
                        border: OutlineInputBorder(
                          borderRadius: BorderRadius.circular(10),
                          borderSide: BorderSide(
                            color: AppColors.primary.withValues(alpha: 0.3),
                          ),
                        ),
                        enabledBorder: OutlineInputBorder(
                          borderRadius: BorderRadius.circular(10),
                          borderSide: BorderSide(
                            color: AppColors.primary.withValues(alpha: 0.3),
                          ),
                        ),
                        focusedBorder: OutlineInputBorder(
                          borderRadius: BorderRadius.circular(10),
                          borderSide: const BorderSide(
                            color: AppColors.primary,
                            width: 1.5,
                          ),
                        ),
                      ),
                    ),
                  ],
                ),
              ),
              const SizedBox(height: 24),

              // ─── CONDITION PHOTOS ─────────────────────────────────
              _FieldLabel(text: 'CONDITION PHOTOS'),
              const SizedBox(height: 10),
              SizedBox(
                height: 90,
                child: ListView(
                  scrollDirection: Axis.horizontal,
                  children: [
                    ..._photoUrls.asMap().entries.map(
                          (entry) => _PhotoThumbnail(
                            index: entry.key,
                            onRemove: () {
                              setState(() => _photoUrls.removeAt(entry.key));
                            },
                          ),
                        ),
                    // Add photo button
                    GestureDetector(
                      onTap: _addPhotoPlaceholder,
                      child: Container(
                        width: 90,
                        height: 90,
                        margin: const EdgeInsets.only(right: 10),
                        decoration: BoxDecoration(
                          color: AppColors.surface,
                          borderRadius: BorderRadius.circular(10),
                          border: Border.all(
                            color: AppColors.border,
                            width: 1,
                          ),
                        ),
                        child: Column(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: [
                            Icon(Icons.add_a_photo_outlined,
                                color: AppColors.textSecondary.withValues(alpha: 0.6),
                                size: 24),
                            const SizedBox(height: 4),
                            Text(
                              'Add Photo',
                              style: GoogleFonts.inter(
                                fontSize: 9,
                                color: AppColors.textSecondary.withValues(alpha: 0.6),
                              ),
                            ),
                          ],
                        ),
                      ),
                    ),
                  ],
                ),
              ),
              const SizedBox(height: 32),

              // ─── SUBMIT BUTTON ────────────────────────────────────
              SizedBox(
                width: double.infinity,
                height: 54,
                child: ElevatedButton(
                  onPressed: _isSubmitting || _submitted ? null : _submitCheckIn,
                  style: ElevatedButton.styleFrom(
                    backgroundColor: _submitted ? AppColors.success : AppColors.primary,
                    disabledBackgroundColor: _submitted
                        ? AppColors.success.withValues(alpha: 0.8)
                        : AppColors.primary.withValues(alpha: 0.3),
                  ),
                  child: _isSubmitting
                      ? const SizedBox(
                          width: 22,
                          height: 22,
                          child: CircularProgressIndicator(
                            strokeWidth: 2.5,
                            color: AppColors.background,
                          ),
                        )
                      : Row(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: [
                            Icon(
                              _submitted
                                  ? Icons.check_circle
                                  : Icons.login_rounded,
                              size: 20,
                              color: AppColors.background,
                            ),
                            const SizedBox(width: 8),
                            Text(
                              _submitted ? 'CHECKED IN' : 'SUBMIT CHECK-IN',
                              style: GoogleFonts.rajdhani(
                                fontSize: 16,
                                fontWeight: FontWeight.w700,
                                color: AppColors.background,
                                letterSpacing: 2,
                              ),
                            ),
                          ],
                        ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

// ─── HELPER WIDGETS ──────────────────────────────────────────────────────────

class _FieldLabel extends StatelessWidget {
  final String text;
  const _FieldLabel({required this.text});

  @override
  Widget build(BuildContext context) {
    return Text(
      text,
      style: GoogleFonts.rajdhani(
        fontSize: 12,
        fontWeight: FontWeight.w700,
        color: AppColors.textSecondary,
        letterSpacing: 1.5,
      ),
    );
  }
}

class _PhotoThumbnail extends StatelessWidget {
  final int index;
  final VoidCallback onRemove;
  const _PhotoThumbnail({required this.index, required this.onRemove});

  @override
  Widget build(BuildContext context) {
    return Container(
      width: 90,
      height: 90,
      margin: const EdgeInsets.only(right: 10),
      decoration: BoxDecoration(
        color: AppColors.cardHighlight,
        borderRadius: BorderRadius.circular(10),
        border: Border.all(color: AppColors.border),
      ),
      child: Stack(
        children: [
          Center(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Icon(Icons.image_outlined,
                    color: AppColors.textSecondary.withValues(alpha: 0.4), size: 28),
                const SizedBox(height: 2),
                Text(
                  'Photo ${index + 1}',
                  style: GoogleFonts.inter(
                    fontSize: 9,
                    color: AppColors.textSecondary.withValues(alpha: 0.5),
                  ),
                ),
              ],
            ),
          ),
          Positioned(
            top: 4,
            right: 4,
            child: GestureDetector(
              onTap: onRemove,
              child: Container(
                width: 20,
                height: 20,
                decoration: BoxDecoration(
                  color: AppColors.danger.withValues(alpha: 0.8),
                  shape: BoxShape.circle,
                ),
                child: const Icon(Icons.close, size: 12, color: Colors.white),
              ),
            ),
          ),
          Positioned(
            bottom: 4,
            right: 4,
            child: Container(
              padding: const EdgeInsets.all(3),
              decoration: BoxDecoration(
                color: AppColors.surface.withValues(alpha: 0.8),
                borderRadius: BorderRadius.circular(4),
              ),
              child: const Icon(Icons.crop_free_rounded,
                  size: 14, color: AppColors.textSecondary),
            ),
          ),
        ],
      ),
    );
  }
}
