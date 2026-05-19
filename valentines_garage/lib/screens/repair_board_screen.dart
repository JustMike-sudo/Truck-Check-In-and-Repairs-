import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:valentines_garage/theme/app_colors.dart';
import 'package:valentines_garage/services/supabase_service.dart';
import 'package:valentines_garage/models/check_in.dart';
import 'package:valentines_garage/models/task_entry.dart';
import 'package:valentines_garage/models/service_task.dart';
import 'package:valentines_garage/widgets/vehicle_card.dart';
import 'package:valentines_garage/widgets/task_card.dart';
import 'package:valentines_garage/widgets/section_header.dart';

class RepairBoardScreen extends StatefulWidget {
  const RepairBoardScreen({super.key});

  @override
  State<RepairBoardScreen> createState() => _RepairBoardScreenState();
}

class _RepairBoardScreenState extends State<RepairBoardScreen> {
  final _supabaseService = SupabaseService();

  List<CheckIn> _checkIns = [];
  List<TaskEntry> _taskEntries = [];
  List<ServiceTask> _serviceTasks = [];
  bool _isLoading = true;
  String? _selectedCheckInId;

  @override
  void initState() {
    super.initState();
    _loadData();
  }

  Future<void> _loadData() async {
    setState(() => _isLoading = true);
    try {
      final results = await Future.wait([
        _supabaseService.getCheckIns(),
        _supabaseService.getTaskEntries(),
        _supabaseService.getServiceTasks(),
      ]);
      setState(() {
        _checkIns = results[0] as List<CheckIn>;
        _taskEntries = results[1] as List<TaskEntry>;
        _serviceTasks = results[2] as List<ServiceTask>;
        _isLoading = false;
      });
    } catch (e) {
      setState(() => _isLoading = false);
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Failed to load data: $e')),
        );
      }
    }
  }

  String _getStatus(CheckIn checkIn) {
    final entries = _taskEntries.where((e) => e.checkInId == checkIn.id);
    if (entries.isEmpty) return 'WAITING';
    final allCompleted = entries.every((e) => e.completed);
    if (allCompleted) return 'COMPLETED';
    final anyStarted = entries.any((e) => e.completed || (e.notes?.isNotEmpty ?? false));
    if (anyStarted) return 'IN SERVICE';
    return 'STAGED';
  }

  List<TaskEntry> _getEntriesForCheckIn(String checkInId) {
    return _taskEntries.where((e) => e.checkInId == checkInId).toList();
  }

  Future<void> _showAddTaskSheet() async {
    if (_selectedCheckInId == null) return;

    final selectedTask = await showModalBottomSheet<ServiceTask>(
      context: context,
      backgroundColor: AppColors.surface,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(20)),
      ),
      builder: (context) => _AddTaskSheet(serviceTasks: _serviceTasks),
    );

    if (selectedTask != null && mounted) {
      try {
        await _supabaseService.createTaskEntry(
          checkInId: _selectedCheckInId!,
          serviceTaskId: selectedTask.id,
        );
        _loadData();
      } catch (e) {
        if (mounted) {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(content: Text('Failed to add task: $e')),
          );
        }
      }
    }
  }

  Future<void> _toggleTaskCompletion(TaskEntry entry) async {
    try {
      await _supabaseService.updateTaskEntry(
        id: entry.id,
        completed: !entry.completed,
      );
      _loadData();
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Failed to update task: $e')),
        );
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    final selectedCheckIn = _selectedCheckInId != null
        ? _checkIns.where((c) => c.id == _selectedCheckInId).firstOrNull
        : null;
    final selectedEntries =
        _selectedCheckInId != null ? _getEntriesForCheckIn(_selectedCheckInId!) : <TaskEntry>[];

    return Scaffold(
      backgroundColor: AppColors.background,
      floatingActionButton: _selectedCheckInId != null
          ? FloatingActionButton(
              onPressed: _showAddTaskSheet,
              backgroundColor: AppColors.primary,
              foregroundColor: AppColors.background,
              child: const Icon(Icons.add, size: 28),
            )
          : null,
      body: _isLoading
          ? const Center(
              child: CircularProgressIndicator(color: AppColors.primary),
            )
          : RefreshIndicator(
              onRefresh: _loadData,
              color: AppColors.primary,
              child: CustomScrollView(
                slivers: [
                  // Header
                  SliverToBoxAdapter(
                    child: Padding(
                      padding: const EdgeInsets.fromLTRB(20, 16, 20, 0),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          const SectionHeader(
                            title: 'ACTIVE REPAIRS',
                            subtitle:
                                "Real-time collaboration for Valentine's Garage technicians.",
                          ),
                          const SizedBox(height: 20),
                          Row(
                            children: [
                              Text(
                                'GARAGE QUEUE (${_checkIns.length})',
                                style: GoogleFonts.rajdhani(
                                  fontSize: 12,
                                  fontWeight: FontWeight.w600,
                                  color: AppColors.textSecondary,
                                  letterSpacing: 1.2,
                                ),
                              ),
                              const Spacer(),
                              Icon(
                                Icons.filter_list_rounded,
                                color: AppColors.textSecondary,
                                size: 20,
                              ),
                            ],
                          ),
                        ],
                      ),
                    ),
                  ),
                  const SliverToBoxAdapter(child: SizedBox(height: 8)),

                  // Vehicle list
                  SliverList(
                    delegate: SliverChildBuilderDelegate(
                      (context, index) {
                        final checkIn = _checkIns[index];
                        final status = _getStatus(checkIn);
                        final truckName = checkIn.truck?.displayName ?? 'Unknown Truck';
                        final vin = checkIn.truck?.vin ?? checkIn.truckId;

                        return VehicleCard(
                          name: truckName,
                          vin: vin.length > 12 ? '${vin.substring(0, 12)}...' : vin,
                          status: status,
                          identifier: '#${(index + 100).toString()}',
                          isSelected: checkIn.id == _selectedCheckInId,
                          eta: 'ETA: ${TimeOfDay.fromDateTime(checkIn.checkInDate).format(context)}',
                          onTap: () {
                            setState(() {
                              _selectedCheckInId =
                                  _selectedCheckInId == checkIn.id ? null : checkIn.id;
                            });
                          },
                        );
                      },
                      childCount: _checkIns.length,
                    ),
                  ),

                  // Active Work Order (when a card is selected)
                  if (selectedCheckIn != null) ...[
                    SliverToBoxAdapter(
                      child: Padding(
                        padding: const EdgeInsets.fromLTRB(20, 24, 20, 0),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            const Divider(),
                            const SizedBox(height: 12),
                            Row(
                              children: [
                                Container(
                                  width: 4,
                                  height: 20,
                                  decoration: BoxDecoration(
                                    color: AppColors.primary,
                                    borderRadius: BorderRadius.circular(2),
                                  ),
                                ),
                                const SizedBox(width: 8),
                                Text(
                                  'ACTIVE WORK ORDER',
                                  style: GoogleFonts.rajdhani(
                                    fontSize: 12,
                                    fontWeight: FontWeight.w700,
                                    color: AppColors.primary,
                                    letterSpacing: 1.5,
                                  ),
                                ),
                              ],
                            ),
                            const SizedBox(height: 4),
                            Text(
                              '${selectedCheckIn.truck?.displayName ?? "TRUCK"} TASK LIST',
                              style: GoogleFonts.rajdhani(
                                fontSize: 22,
                                fontWeight: FontWeight.w700,
                                color: AppColors.textPrimary,
                                letterSpacing: 0.5,
                              ),
                            ),
                            const SizedBox(height: 12),
                          ],
                        ),
                      ),
                    ),
                    if (selectedEntries.isEmpty)
                      SliverToBoxAdapter(
                        child: Padding(
                          padding: const EdgeInsets.symmetric(horizontal: 20),
                          child: Container(
                            padding: const EdgeInsets.all(20),
                            decoration: BoxDecoration(
                              color: AppColors.surface,
                              borderRadius: BorderRadius.circular(12),
                              border: Border.all(color: AppColors.border),
                            ),
                            child: Column(
                              children: [
                                Icon(Icons.assignment_outlined,
                                    size: 36, color: AppColors.textSecondary.withValues(alpha: 0.5)),
                                const SizedBox(height: 8),
                                Text(
                                  'No tasks assigned yet',
                                  style: GoogleFonts.inter(
                                    fontSize: 14,
                                    color: AppColors.textSecondary,
                                  ),
                                ),
                                const SizedBox(height: 4),
                                Text(
                                  'Tap + to add a service task',
                                  style: GoogleFonts.inter(
                                    fontSize: 12,
                                    color: AppColors.textSecondary.withValues(alpha: 0.5),
                                  ),
                                ),
                              ],
                            ),
                          ),
                        ),
                      )
                    else
                      SliverList(
                        delegate: SliverChildBuilderDelegate(
                          (context, index) {
                            final entry = selectedEntries[index];
                            return TaskCard(
                              taskName:
                                  entry.serviceTask?.description ?? 'Service Task',
                              isCompleted: entry.completed,
                              mechanicNote: entry.notes,
                              onToggleComplete: () => _toggleTaskCompletion(entry),
                            );
                          },
                          childCount: selectedEntries.length,
                        ),
                      ),
                  ],

                  // Empty state
                  if (_checkIns.isEmpty && !_isLoading)
                    SliverFillRemaining(
                      child: Center(
                        child: Column(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: [
                            Icon(Icons.local_shipping_outlined,
                                size: 48, color: AppColors.textSecondary.withValues(alpha: 0.3)),
                            const SizedBox(height: 12),
                            Text(
                              'No active repairs',
                              style: GoogleFonts.rajdhani(
                                fontSize: 18,
                                fontWeight: FontWeight.w600,
                                color: AppColors.textSecondary,
                              ),
                            ),
                            Text(
                              'Check in a vehicle to get started',
                              style: GoogleFonts.inter(
                                fontSize: 13,
                                color: AppColors.textSecondary.withValues(alpha: 0.6),
                              ),
                            ),
                          ],
                        ),
                      ),
                    ),

                  const SliverToBoxAdapter(child: SizedBox(height: 100)),
                ],
              ),
            ),
    );
  }
}

// ─── ADD TASK BOTTOM SHEET ──────────────────────────────────────────────────

class _AddTaskSheet extends StatelessWidget {
  final List<ServiceTask> serviceTasks;
  const _AddTaskSheet({required this.serviceTasks});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(20),
      child: Column(
        mainAxisSize: MainAxisSize.min,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Center(
            child: Container(
              width: 40,
              height: 4,
              decoration: BoxDecoration(
                color: AppColors.border,
                borderRadius: BorderRadius.circular(2),
              ),
            ),
          ),
          const SizedBox(height: 16),
          Text(
            'ADD SERVICE TASK',
            style: GoogleFonts.rajdhani(
              fontSize: 18,
              fontWeight: FontWeight.w700,
              color: AppColors.primary,
              letterSpacing: 1.5,
            ),
          ),
          const SizedBox(height: 4),
          Text(
            'Select a task to assign to this work order',
            style: GoogleFonts.inter(
              fontSize: 13,
              color: AppColors.textSecondary,
            ),
          ),
          const SizedBox(height: 16),
          if (serviceTasks.isEmpty)
            Padding(
              padding: const EdgeInsets.symmetric(vertical: 20),
              child: Center(
                child: Text(
                  'No service tasks available. Create one first.',
                  style: GoogleFonts.inter(
                    fontSize: 13,
                    color: AppColors.textSecondary,
                  ),
                ),
              ),
            )
          else
            ...serviceTasks.map(
              (task) => ListTile(
                contentPadding: const EdgeInsets.symmetric(horizontal: 4),
                leading: Container(
                  width: 36,
                  height: 36,
                  decoration: BoxDecoration(
                    color: AppColors.primary.withValues(alpha: 0.1),
                    borderRadius: BorderRadius.circular(8),
                  ),
                  child: const Icon(Icons.settings_rounded,
                      color: AppColors.primary, size: 18),
                ),
                title: Text(
                  task.description.toUpperCase(),
                  style: GoogleFonts.rajdhani(
                    fontSize: 14,
                    fontWeight: FontWeight.w700,
                    color: AppColors.textPrimary,
                    letterSpacing: 0.8,
                  ),
                ),
                trailing: const Icon(Icons.add_circle_outline,
                    color: AppColors.primary, size: 20),
                onTap: () => Navigator.pop(context, task),
              ),
            ),
          const SizedBox(height: 16),
        ],
      ),
    );
  }
}
