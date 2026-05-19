import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:valentines_garage/theme/app_colors.dart';
import 'package:valentines_garage/widgets/status_badge.dart';

class VehicleCard extends StatelessWidget {
  final String name;
  final String vin;
  final String status;
  final String? eta;
  final String? identifier;
  final VoidCallback? onTap;
  final bool isSelected;

  const VehicleCard({
    super.key,
    required this.name,
    required this.vin,
    required this.status,
    this.eta,
    this.identifier,
    this.onTap,
    this.isSelected = false,
  });

  @override
  Widget build(BuildContext context) {
    final isInService =
        status.toUpperCase() == 'IN SERVICE' ||
        status.toUpperCase() == 'IN_SERVICE' ||
        status.toUpperCase() == 'ACTIVE';

    return GestureDetector(
      onTap: onTap,
      child: AnimatedContainer(
        duration: const Duration(milliseconds: 200),
        margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 5),
        decoration: BoxDecoration(
          color: isSelected ? AppColors.cardHighlight : AppColors.surface,
          borderRadius: BorderRadius.circular(12),
          border: Border.all(
            color: isInService
                ? AppColors.primary.withValues(alpha: 0.6)
                : AppColors.border,
            width: isInService ? 1.5 : 1,
          ),
        ),
        child: Padding(
          padding: const EdgeInsets.all(14),
          child: Row(
            children: [
              // Vehicle icon
              Container(
                width: 42,
                height: 42,
                decoration: BoxDecoration(
                  color: isInService
                      ? AppColors.primary.withValues(alpha: 0.12)
                      : AppColors.cardHighlight,
                  borderRadius: BorderRadius.circular(10),
                ),
                child: Icon(
                  Icons.local_shipping_rounded,
                  color: isInService ? AppColors.primary : AppColors.textSecondary,
                  size: 22,
                ),
              ),
              const SizedBox(width: 12),
              // Truck info
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Row(
                      children: [
                        Flexible(
                          child: Text(
                            identifier != null ? '$name $identifier' : name,
                            style: GoogleFonts.rajdhani(
                              fontSize: 16,
                              fontWeight: FontWeight.w700,
                              color: AppColors.textPrimary,
                              letterSpacing: 0.5,
                            ),
                            overflow: TextOverflow.ellipsis,
                          ),
                        ),
                      ],
                    ),
                    const SizedBox(height: 2),
                    Row(
                      children: [
                        Text(
                          '#',
                          style: GoogleFonts.inter(
                            fontSize: 12,
                            fontWeight: FontWeight.w500,
                            color: AppColors.primary.withValues(alpha: 0.7),
                          ),
                        ),
                        const SizedBox(width: 2),
                        Flexible(
                          child: Text(
                            vin,
                            style: GoogleFonts.sourceCodePro(
                              fontSize: 11,
                              fontWeight: FontWeight.w400,
                              color: AppColors.textSecondary,
                              letterSpacing: 0.5,
                            ),
                            overflow: TextOverflow.ellipsis,
                          ),
                        ),
                      ],
                    ),
                    if (eta != null) ...[
                      const SizedBox(height: 4),
                      Row(
                        children: [
                          Icon(
                            Icons.schedule_rounded,
                            size: 12,
                            color: AppColors.textSecondary.withValues(alpha: 0.7),
                          ),
                          const SizedBox(width: 4),
                          Text(
                            eta!,
                            style: GoogleFonts.inter(
                              fontSize: 11,
                              color: AppColors.textSecondary,
                            ),
                          ),
                        ],
                      ),
                    ],
                  ],
                ),
              ),
              const SizedBox(width: 8),
              StatusBadge(status: status),
            ],
          ),
        ),
      ),
    );
  }
}
