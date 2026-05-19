import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:valentines_garage/theme/app_colors.dart';
import 'package:valentines_garage/services/auth_service.dart';
import 'package:valentines_garage/screens/check_in_screen.dart';
import 'package:valentines_garage/screens/repair_board_screen.dart';
import 'package:valentines_garage/screens/reports_screen.dart';
import 'package:valentines_garage/screens/login_screen.dart';
import 'package:valentines_garage/widgets/bottom_nav_bar.dart';

class HomeShell extends StatefulWidget {
  const HomeShell({super.key});

  @override
  State<HomeShell> createState() => _HomeShellState();
}

class _HomeShellState extends State<HomeShell> {
  int _currentIndex = 0;
  final _authService = AuthService();

  final List<Widget> _screens = const [
    CheckInScreen(),
    RepairBoardScreen(),
    ReportsScreen(),
  ];

  final List<String> _titles = [
    "VALENTINE'S GARAGE",
    "VALENTINE'S GARAGE",
    "VALENTINE'S GARAGE",
  ];

  void _onSignOut() async {
    await _authService.signOut();
    if (mounted) {
      Navigator.of(context).pushAndRemoveUntil(
        MaterialPageRoute(builder: (_) => const LoginScreen()),
        (route) => false,
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    final isAdmin = _authService.isAdmin;
    final role = _authService.profile?.role ?? 'mechanic';

    return Scaffold(
      backgroundColor: AppColors.background,
      appBar: AppBar(
        backgroundColor: AppColors.background,
        elevation: 0,
        scrolledUnderElevation: 0,
        leading: Padding(
          padding: const EdgeInsets.only(left: 14),
          child: Center(
            child: Container(
              width: 32,
              height: 32,
              decoration: BoxDecoration(
                color: AppColors.primary.withValues(alpha: 0.12),
                borderRadius: BorderRadius.circular(8),
              ),
              child: const Icon(Icons.build_rounded,
                  color: AppColors.primary, size: 18),
            ),
          ),
        ),
        title: Text(
          _titles[_currentIndex],
          style: GoogleFonts.rajdhani(
            fontSize: 18,
            fontWeight: FontWeight.w700,
            color: AppColors.primary,
            letterSpacing: 1.5,
          ),
        ),
        actions: [
          if (_currentIndex == 2) ...[
            IconButton(
              icon: const Icon(Icons.search_rounded, size: 22),
              color: AppColors.textSecondary,
              onPressed: () {},
            ),
          ],
          if (_currentIndex == 1) ...[
            IconButton(
              icon: const Icon(Icons.notifications_none_rounded, size: 22),
              color: AppColors.textSecondary,
              onPressed: () {},
            ),
          ],
          // Profile menu
          PopupMenuButton<String>(
            icon: Container(
              width: 32,
              height: 32,
              decoration: BoxDecoration(
                shape: BoxShape.circle,
                color: AppColors.surface,
                border: Border.all(color: AppColors.border),
              ),
              child: const Icon(Icons.person_outline,
                  color: AppColors.textSecondary, size: 18),
            ),
            color: AppColors.surface,
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(12),
              side: const BorderSide(color: AppColors.border),
            ),
            offset: const Offset(0, 48),
            itemBuilder: (context) => [
              PopupMenuItem(
                enabled: false,
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      _authService.currentUser?.email ?? 'User',
                      style: GoogleFonts.inter(
                        fontSize: 13,
                        color: AppColors.textPrimary,
                        fontWeight: FontWeight.w500,
                      ),
                    ),
                    const SizedBox(height: 2),
                    Container(
                      padding: const EdgeInsets.symmetric(
                          horizontal: 8, vertical: 2),
                      decoration: BoxDecoration(
                        color: isAdmin
                            ? AppColors.primary.withValues(alpha: 0.15)
                            : AppColors.cardHighlight,
                        borderRadius: BorderRadius.circular(4),
                      ),
                      child: Text(
                        role.toUpperCase(),
                        style: GoogleFonts.rajdhani(
                          fontSize: 10,
                          fontWeight: FontWeight.w700,
                          color: isAdmin
                              ? AppColors.primary
                              : AppColors.textSecondary,
                          letterSpacing: 1,
                        ),
                      ),
                    ),
                  ],
                ),
              ),
              const PopupMenuDivider(),
              PopupMenuItem(
                value: 'signout',
                child: Row(
                  children: [
                    const Icon(Icons.logout_rounded,
                        size: 18, color: AppColors.danger),
                    const SizedBox(width: 8),
                    Text(
                      'Sign Out',
                      style: GoogleFonts.inter(
                        fontSize: 13,
                        color: AppColors.danger,
                      ),
                    ),
                  ],
                ),
              ),
            ],
            onSelected: (value) {
              if (value == 'signout') _onSignOut();
            },
          ),
          const SizedBox(width: 8),
        ],
      ),
      body: IndexedStack(
        index: _currentIndex,
        children: _screens,
      ),
      bottomNavigationBar: BottomNavBar(
        currentIndex: _currentIndex,
        onTap: (index) => setState(() => _currentIndex = index),
      ),
    );
  }
}
