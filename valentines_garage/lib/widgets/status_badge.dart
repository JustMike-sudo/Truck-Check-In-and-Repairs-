import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:valentines_garage/theme/app_colors.dart';

class StatusBadge extends StatelessWidget {
  final String status;
  final double fontSize;

  const StatusBadge({
    super.key,
    required this.status,
    this.fontSize = 11,
  });

  @override
  Widget build(BuildContext context) {
    final config = _getConfig();

    if (config.isOutlined) {
      return Container(
        padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
        decoration: BoxDecoration(
          borderRadius: BorderRadius.circular(6),
          border: Border.all(color: config.borderColor, width: 1),
        ),
        child: Text(
          status.toUpperCase(),
          style: GoogleFonts.rajdhani(
            fontSize: fontSize,
            fontWeight: FontWeight.w700,
            color: config.textColor,
            letterSpacing: 1.0,
          ),
        ),
      );
    }

    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
      decoration: BoxDecoration(
        color: config.backgroundColor,
        borderRadius: BorderRadius.circular(6),
      ),
      child: Text(
        status.toUpperCase(),
        style: GoogleFonts.rajdhani(
          fontSize: fontSize,
          fontWeight: FontWeight.w700,
          color: config.textColor,
          letterSpacing: 1.0,
        ),
      ),
    );
  }

  _BadgeConfig _getConfig() {
    switch (status.toUpperCase()) {
      case 'IN SERVICE':
      case 'IN_SERVICE':
      case 'ACTIVE':
        return _BadgeConfig(
          backgroundColor: AppColors.primary,
          textColor: AppColors.background,
          borderColor: AppColors.primary,
          isOutlined: false,
        );
      case 'WAITING':
      case 'PENDING':
      case 'QUEUED':
        return _BadgeConfig(
          backgroundColor: AppColors.surfaceLight,
          textColor: AppColors.textSecondary,
          borderColor: AppColors.border,
          isOutlined: false,
        );
      case 'STAGED':
      case 'SCHEDULED':
        return _BadgeConfig(
          backgroundColor: Colors.transparent,
          textColor: AppColors.textSecondary,
          borderColor: AppColors.border,
          isOutlined: true,
        );
      case 'COMPLETED':
      case 'DONE':
        return _BadgeConfig(
          backgroundColor: AppColors.success.withValues(alpha: 0.15),
          textColor: AppColors.success,
          borderColor: AppColors.success,
          isOutlined: false,
        );
      case 'CRITICAL':
      case 'URGENT':
        return _BadgeConfig(
          backgroundColor: AppColors.danger.withValues(alpha: 0.15),
          textColor: AppColors.danger,
          borderColor: AppColors.danger,
          isOutlined: false,
        );
      default:
        return _BadgeConfig(
          backgroundColor: AppColors.surfaceLight,
          textColor: AppColors.textSecondary,
          borderColor: AppColors.border,
          isOutlined: false,
        );
    }
  }
}

class _BadgeConfig {
  final Color backgroundColor;
  final Color textColor;
  final Color borderColor;
  final bool isOutlined;

  const _BadgeConfig({
    required this.backgroundColor,
    required this.textColor,
    required this.borderColor,
    required this.isOutlined,
  });
}
