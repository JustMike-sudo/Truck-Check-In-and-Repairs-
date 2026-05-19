import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:valentines_garage/theme/app_colors.dart';

class MetricCard extends StatelessWidget {
  final String label;
  final String value;
  final String? subtitle;
  final bool isPositiveTrend;
  final bool showIndicator;
  final Color? indicatorColor;
  final IconData? icon;

  const MetricCard({
    super.key,
    required this.label,
    required this.value,
    this.subtitle,
    this.isPositiveTrend = true,
    this.showIndicator = false,
    this.indicatorColor,
    this.icon,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: AppColors.surface,
        borderRadius: BorderRadius.circular(12),
        border: Border.all(color: AppColors.border, width: 1),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        mainAxisSize: MainAxisSize.min,
        children: [
          // Label row
          Row(
            children: [
              if (icon != null) ...[
                Icon(
                  icon,
                  size: 14,
                  color: AppColors.textSecondary,
                ),
                const SizedBox(width: 6),
              ],
              Expanded(
                child: Text(
                  label.toUpperCase(),
                  style: GoogleFonts.rajdhani(
                    fontSize: 11,
                    fontWeight: FontWeight.w600,
                    color: AppColors.textSecondary,
                    letterSpacing: 1.2,
                  ),
                  overflow: TextOverflow.ellipsis,
                ),
              ),
            ],
          ),
          const SizedBox(height: 8),
          // Value
          Text(
            value,
            style: GoogleFonts.rajdhani(
              fontSize: 28,
              fontWeight: FontWeight.w700,
              color: AppColors.textPrimary,
              height: 1.1,
            ),
          ),
          if (subtitle != null || showIndicator) ...[
            const SizedBox(height: 8),
            if (subtitle != null)
              Text(
                subtitle!,
                style: GoogleFonts.inter(
                  fontSize: 11,
                  fontWeight: FontWeight.w500,
                  color: isPositiveTrend ? AppColors.success : AppColors.danger,
                ),
              ),
            if (showIndicator) ...[
              const SizedBox(height: 6),
              _buildIndicatorBar(),
            ],
          ],
        ],
      ),
    );
  }

  Widget _buildIndicatorBar() {
    final color = indicatorColor ?? AppColors.primary;
    return Container(
      height: 4,
      decoration: BoxDecoration(
        color: AppColors.cardHighlight,
        borderRadius: BorderRadius.circular(2),
      ),
      child: FractionallySizedBox(
        alignment: Alignment.centerLeft,
        widthFactor: 0.72,
        child: Container(
          decoration: BoxDecoration(
            color: color,
            borderRadius: BorderRadius.circular(2),
          ),
        ),
      ),
    );
  }
}
