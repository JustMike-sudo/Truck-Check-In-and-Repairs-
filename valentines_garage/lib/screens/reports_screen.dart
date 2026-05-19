import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:valentines_garage/theme/app_colors.dart';
import 'package:valentines_garage/services/supabase_service.dart';
import 'package:valentines_garage/models/check_in.dart';
import 'package:valentines_garage/models/task_entry.dart';
import 'package:valentines_garage/models/truck.dart';
import 'package:valentines_garage/widgets/metric_card.dart';
import 'package:valentines_garage/widgets/section_header.dart';

class ReportsScreen extends StatefulWidget {
  const ReportsScreen({super.key});

  @override
  State<ReportsScreen> createState() => _ReportsScreenState();
}

class _ReportsScreenState extends State<ReportsScreen> {
  final _supabaseService = SupabaseService();

  List<Truck> _trucks = [];
  List<CheckIn> _checkIns = [];
  List<TaskEntry> _taskEntries = [];
  bool _isLoading = true;
  String _periodFilter = 'Today';
  String _staffFilter = 'All';

  @override
  void initState() {
    super.initState();
    _loadData();
  }

  Future<void> _loadData() async {
    setState(() => _isLoading = true);
    try {
      final results = await Future.wait([
        _supabaseService.getTrucks(),
        _supabaseService.getCheckIns(),
        _supabaseService.getTaskEntries(),
      ]);
      setState(() {
        _trucks = results[0] as List<Truck>;
        _checkIns = results[1] as List<CheckIn>;
        _taskEntries = results[2] as List<TaskEntry>;
        _isLoading = false;
      });
    } catch (e) {
      setState(() => _isLoading = false);
    }
  }

  int get _severeDamageCount =>
      _checkIns.where((c) => c.hasSevereDamage).length;

  double get _completionRate {
    if (_taskEntries.isEmpty) return 0;
    final completed = _taskEntries.where((e) => e.completed).length;
    return (completed / _taskEntries.length) * 100;
  }

  String get _avgTurnaround {
    if (_checkIns.length < 2) return 'N/A';
    // Compute average hours between check-ins as a proxy for turnaround
    final now = DateTime.now();
    final todayCheckIns = _checkIns.where((c) {
      final diff = now.difference(c.checkInDate);
      return diff.inHours < 24;
    }).toList();
    if (todayCheckIns.isEmpty) return 'N/A';
    final totalHours = todayCheckIns.fold<double>(
      0.0,
      (sum, c) => sum + now.difference(c.checkInDate).inMinutes / 60.0,
    );
    return '${(totalHours / todayCheckIns.length).toStringAsFixed(1)}h';
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.background,
      body: _isLoading
          ? const Center(
              child: CircularProgressIndicator(color: AppColors.primary),
            )
          : RefreshIndicator(
              onRefresh: _loadData,
              color: AppColors.primary,
              child: SingleChildScrollView(
                physics: const AlwaysScrollableScrollPhysics(),
                padding: const EdgeInsets.fromLTRB(20, 16, 20, 100),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    // ─── HEADER ──────────────────────────────────────
                    const SectionHeader(
                      title: 'GARAGE PERFORMANCE REPORT',
                      subtitle: 'REAL-TIME OPERATIONAL METRICS',
                    ),
                    const SizedBox(height: 20),

                    // ─── FLEET PRIORITY ALERT ────────────────────────
                    Container(
                      padding: const EdgeInsets.all(16),
                      decoration: BoxDecoration(
                        color: AppColors.surface,
                        borderRadius: BorderRadius.circular(12),
                        border: Border.all(
                          color: _severeDamageCount > 0
                              ? AppColors.danger.withValues(alpha: 0.6)
                              : AppColors.border,
                          width: _severeDamageCount > 0 ? 1.5 : 1,
                        ),
                      ),
                      child: Row(
                        children: [
                          Expanded(
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Text(
                                  'FLEET PRIORITY',
                                  style: GoogleFonts.rajdhani(
                                    fontSize: 12,
                                    fontWeight: FontWeight.w700,
                                    color: AppColors.danger,
                                    letterSpacing: 1.5,
                                  ),
                                ),
                                const SizedBox(height: 4),
                                Text(
                                  _severeDamageCount > 0
                                      ? '$_severeDamageCount Trucks with Severe Damage'
                                      : 'No Critical Issues',
                                  style: GoogleFonts.rajdhani(
                                    fontSize: 18,
                                    fontWeight: FontWeight.w700,
                                    color: AppColors.textPrimary,
                                  ),
                                ),
                              ],
                            ),
                          ),
                          Container(
                            width: 40,
                            height: 40,
                            decoration: BoxDecoration(
                              color: _severeDamageCount > 0
                                  ? AppColors.danger.withValues(alpha: 0.15)
                                  : AppColors.success.withValues(alpha: 0.15),
                              borderRadius: BorderRadius.circular(10),
                            ),
                            child: Icon(
                              _severeDamageCount > 0
                                  ? Icons.warning_rounded
                                  : Icons.check_circle_rounded,
                              color: _severeDamageCount > 0
                                  ? AppColors.danger
                                  : AppColors.success,
                              size: 22,
                            ),
                          ),
                        ],
                      ),
                    ),
                    const SizedBox(height: 20),

                    // ─── ACTIVE FILTERS ──────────────────────────────
                    Row(
                      children: [
                        Text(
                          'ACTIVE FILTERS',
                          style: GoogleFonts.rajdhani(
                            fontSize: 12,
                            fontWeight: FontWeight.w600,
                            color: AppColors.textSecondary,
                            letterSpacing: 1.2,
                          ),
                        ),
                        const Spacer(),
                        GestureDetector(
                          onTap: () {
                            setState(() {
                              _periodFilter = 'Today';
                              _staffFilter = 'All';
                            });
                          },
                          child: Text(
                            'RESET',
                            style: GoogleFonts.rajdhani(
                              fontSize: 12,
                              fontWeight: FontWeight.w600,
                              color: AppColors.textSecondary,
                              letterSpacing: 1.0,
                            ),
                          ),
                        ),
                      ],
                    ),
                    const SizedBox(height: 10),
                    Row(
                      children: [
                        _FilterChip(
                          label: 'PERIOD',
                          value: _periodFilter,
                          onTap: () {
                            setState(() {
                              _periodFilter = _periodFilter == 'Today'
                                  ? 'This Week'
                                  : 'Today';
                            });
                          },
                        ),
                        const SizedBox(width: 12),
                        _FilterChip(
                          label: 'STAFF',
                          value: _staffFilter,
                          onTap: () {
                            setState(() {
                              _staffFilter =
                                  _staffFilter == 'All' ? 'Active' : 'All';
                            });
                          },
                        ),
                      ],
                    ),
                    const SizedBox(height: 20),

                    // ─── METRICS GRID ────────────────────────────────
                    Row(
                      children: [
                        Expanded(
                          child: MetricCard(
                            label: 'VEHICLES',
                            value: '${_trucks.length}',
                            subtitle: 'Total Fleet',
                            indicatorType: MetricIndicatorType.bar,
                            indicatorColor: AppColors.primary,
                          ),
                        ),
                        const SizedBox(width: 12),
                        Expanded(
                          child: MetricCard(
                            label: 'COMPLETION',
                            value: '${_completionRate.toStringAsFixed(1)}%',
                            subtitle: _completionRate > 90
                                ? '+${(_completionRate - 90).toStringAsFixed(1)}% vs Avg'
                                : 'Task Rate',
                            subtitleColor: _completionRate > 90
                                ? AppColors.success
                                : AppColors.textSecondary,
                          ),
                        ),
                      ],
                    ),
                    const SizedBox(height: 12),
                    Row(
                      children: [
                        Expanded(
                          child: MetricCard(
                            label: 'ACTIVE JOBS',
                            value: '${_checkIns.length}',
                            subtitle: '${_taskEntries.where((e) => !e.completed).length} Tasks Pending',
                            subtitleColor: _taskEntries.any((e) => !e.completed)
                                ? AppColors.danger
                                : AppColors.success,
                          ),
                        ),
                        const SizedBox(width: 12),
                        Expanded(
                          child: MetricCard(
                            label: 'REPAIR TAT',
                            value: _avgTurnaround,
                            subtitle: 'Avg. Turnaround',
                          ),
                        ),
                      ],
                    ),
                    const SizedBox(height: 28),

                    // ─── ACTIVITY LOG ────────────────────────────────
                    Container(
                      padding: const EdgeInsets.all(16),
                      decoration: BoxDecoration(
                        color: AppColors.surface,
                        borderRadius: BorderRadius.circular(12),
                        border: Border.all(color: AppColors.border),
                      ),
                      child: Column(
                        children: [
                          Row(
                            children: [
                              Text(
                                'RECENT ACTIVITY LOG',
                                style: GoogleFonts.rajdhani(
                                  fontSize: 14,
                                  fontWeight: FontWeight.w700,
                                  color: AppColors.textPrimary,
                                  letterSpacing: 1.2,
                                ),
                              ),
                              const Spacer(),
                              Icon(Icons.filter_list_rounded,
                                  size: 18, color: AppColors.textSecondary),
                            ],
                          ),
                          const SizedBox(height: 12),
                          const Divider(),
                          if (_checkIns.isEmpty)
                            Padding(
                              padding: const EdgeInsets.symmetric(vertical: 16),
                              child: Text(
                                'No recent activity',
                                style: GoogleFonts.inter(
                                  fontSize: 13,
                                  color: AppColors.textSecondary,
                                ),
                              ),
                            )
                          else
                            ...(_checkIns.take(5).map((checkIn) {
                              final time = TimeOfDay.fromDateTime(
                                      checkIn.checkInDate)
                                  .format(context);
                              final truckName =
                                  checkIn.truck?.displayName ?? 'Unknown Truck';
                              return _ActivityRow(
                                time: time,
                                description:
                                    'Check-in: $truckName',
                                icon: Icons.login_rounded,
                              );
                            }).toList()),
                          if (_taskEntries.isNotEmpty) ...[
                            const Divider(),
                            ...(_taskEntries
                                .where((e) => e.completed)
                                .take(3)
                                .map((entry) {
                              return _ActivityRow(
                                time: '--:--',
                                description:
                                    'Completed: ${entry.serviceTask?.description ?? "Task"}',
                                icon: Icons.check_circle_outline,
                                iconColor: AppColors.success,
                              );
                            }).toList()),
                          ],
                        ],
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

class _FilterChip extends StatelessWidget {
  final String label;
  final String value;
  final VoidCallback onTap;

  const _FilterChip({
    required this.label,
    required this.value,
    required this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    return Expanded(
      child: GestureDetector(
        onTap: onTap,
        child: Container(
          padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 12),
          decoration: BoxDecoration(
            color: AppColors.surface,
            borderRadius: BorderRadius.circular(10),
            border: Border.all(color: AppColors.border),
          ),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                label,
                style: GoogleFonts.rajdhani(
                  fontSize: 10,
                  fontWeight: FontWeight.w600,
                  color: AppColors.textSecondary,
                  letterSpacing: 1.2,
                ),
              ),
              const SizedBox(height: 2),
              Text(
                value,
                style: GoogleFonts.rajdhani(
                  fontSize: 16,
                  fontWeight: FontWeight.w700,
                  color: AppColors.textPrimary,
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

class _ActivityRow extends StatelessWidget {
  final String time;
  final String description;
  final IconData icon;
  final Color? iconColor;

  const _ActivityRow({
    required this.time,
    required this.description,
    required this.icon,
    this.iconColor,
  });

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 8),
      child: Row(
        children: [
          Text(
            time,
            style: GoogleFonts.sourceCodePro(
              fontSize: 12,
              fontWeight: FontWeight.w500,
              color: AppColors.primary.withValues(alpha: 0.7),
            ),
          ),
          const SizedBox(width: 12),
          Icon(icon, size: 16, color: iconColor ?? AppColors.textSecondary),
          const SizedBox(width: 8),
          Expanded(
            child: Text(
              description,
              style: GoogleFonts.inter(
                fontSize: 13,
                color: AppColors.textPrimary,
              ),
              overflow: TextOverflow.ellipsis,
            ),
          ),
        ],
      ),
    );
  }
}
