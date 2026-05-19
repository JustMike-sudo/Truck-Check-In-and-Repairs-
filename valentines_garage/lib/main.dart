import 'package:flutter/material.dart';
import 'package:supabase_flutter/supabase_flutter.dart';
import 'package:valentines_garage/theme/app_theme.dart';
import 'package:valentines_garage/screens/login_screen.dart';
import 'package:valentines_garage/screens/home_shell.dart';
import 'package:valentines_garage/services/auth_service.dart';

// ──────────────────────────────────────────────────────────────────────────────
// TODO: Replace these with your actual Supabase project credentials.
// Find them in your Supabase Dashboard → Settings → API.
// ──────────────────────────────────────────────────────────────────────────────
const String supabaseUrl = 'https://oiduhfpuxxhccotwggrm.supabase.co';
const String supabaseAnonKey = 'YOUR_SUPABASE_ANON_KEY';

Future<void> main() async {
  WidgetsFlutterBinding.ensureInitialized();

  await Supabase.initialize(
    url: supabaseUrl,
    anonKey: supabaseAnonKey,
  );

  runApp(const ValentinesGarageApp());
}

class ValentinesGarageApp extends StatelessWidget {
  const ValentinesGarageApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: "Valentine's Garage Service Hub",
      debugShowCheckedModeBanner: false,
      theme: AppTheme.darkTheme,
      home: const AuthGate(),
    );
  }
}

/// Listens to auth state and routes to Login or Home accordingly.
class AuthGate extends StatefulWidget {
  const AuthGate({super.key});

  @override
  State<AuthGate> createState() => _AuthGateState();
}

class _AuthGateState extends State<AuthGate> {
  final _authService = AuthService();

  @override
  void initState() {
    super.initState();
    // Pre-fetch profile if already logged in
    if (_authService.isAuthenticated) {
      _authService.getCurrentProfile();
    }
  }

  @override
  Widget build(BuildContext context) {
    return StreamBuilder<AuthState>(
      stream: _authService.authStateChanges,
      builder: (context, snapshot) {
        // While checking auth state, show a branded splash
        if (snapshot.connectionState == ConnectionState.waiting) {
          return const _SplashScreen();
        }

        final session = snapshot.data?.session;
        if (session != null) {
          return const HomeShell();
        }

        return const LoginScreen();
      },
    );
  }
}

/// Minimal branded splash screen shown during auth check.
class _SplashScreen extends StatelessWidget {
  const _SplashScreen();

  @override
  Widget build(BuildContext context) {
    return const Scaffold(
      backgroundColor: Color(0xFF0D0D14),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(Icons.build_rounded, color: Color(0xFFFFD600), size: 48),
            SizedBox(height: 16),
            CircularProgressIndicator(
              color: Color(0xFFFFD600),
              strokeWidth: 2,
            ),
          ],
        ),
      ),
    );
  }
}
