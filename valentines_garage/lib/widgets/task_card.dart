import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:valentines_garage/theme/app_colors.dart';

class TaskCard extends StatelessWidget {
  final String description;
  final bool completed;
  final String? mechanicNote;
  final VoidCallback? onToggle;
  final VoidCallback? onTap;

  const TaskCard({
    super.key,
    required this.description,
    this.completed = false,
    this.mechanicNote,
    this.onToggle,
    this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 4),
        decoration: BoxDecoration(
          color: AppColors.surface,
          borderRadius: BorderRadius.circular(12),
          border: Border.all(
            color: completed
                ? AppColors.success.withValues(alpha: 0.3)
                : AppColors.border,
            width: 1,
          ),
        ),
        child: Padding(
          padding: const EdgeInsets.all(14),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              // Header row with icon, description, and status
              Row(
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  // Wrench icon
                  Container(
                    width: 34,
                    height: 34,
                    decoration: BoxDecoration(
                      color: completed
                          ? AppColors.success.withValues(alpha: 0.1)
                          : AppColors.primary.withValues(alpha: 0.1),
                      borderRadius: BorderRadius.circular(8),
                    ),
                    child: Icon(
                      Icons.build_rounded,
                      size: 18,
                      color: completed ? AppColors.success : AppColors.primary,
                    ),
                  ),
                  const SizedBox(width: 12),
                  // Description
                  Expanded(
                    child: Text(
                      description,
                      style: GoogleFonts.rajdhani(
                        fontSize: 15,
                        fontWeight: FontWeight.w700,
                        color: completed
                            ? AppColors.textSecondary
                            : AppColors.textPrimary,
                        letterSpacing: 0.5,
                        decoration:
                            completed ? TextDecoration.lineThrough : null,
                      ),
                    ),
                  ),
                  const SizedBox(width: 8),
                  // Status dot + toggle
                  GestureDetector(
                    onTap: onToggle,
                    child: Container(
                      width: 28,
                      height: 28,
                      decoration: BoxDecoration(
                        shape: BoxShape.circle,
                        color: completed
                            ? AppColors.success.withValues(alpha: 0.15)
                            : AppColors.cardHighlight,
                        border: Border.all(
                          color: completed
                              ? AppColors.success
                              : AppColors.primary.withValues(alpha: 0.5),
                          width: 2,
                        ),
                      ),
                      child: completed
                          ? const Icon(
                              Icons.check_rounded,
                              size: 16,
                              color: AppColors.success,
                            )
                          : null,
                    ),
                  ),
                ],
              ),
              // Mechanic note
              if (mechanicNote != null && mechanicNote!.isNotEmpty) ...[
                const SizedBox(height: 10),
                Container(
                  width: double.infinity,
                  padding:
                      const EdgeInsets.symmetric(horizontal: 12, vertical: 10),
                  decoration: BoxDecoration(
                    color: AppColors.cardHighlight.withValues(alpha: 0.5),
                    borderRadius: BorderRadius.circular(8),
                    border: Border(
                      left: BorderSide(
                        color: AppColors.primary.withValues(alpha: 0.4),
                        width: 2,
                      ),
                    ),
                  ),
                  child: Text(
                    mechanicNote!,
                    style: GoogleFonts.inter(
                      fontSize: 12,
                      fontStyle: FontStyle.italic,
                      color: AppColors.textSecondary,
                      height: 1.4,
                    ),
                  ),
                ),
              ],
            ],
          ),
        ),
      ),
    );
  }
}
