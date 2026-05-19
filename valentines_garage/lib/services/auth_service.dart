import 'package:supabase_flutter/supabase_flutter.dart';
import 'package:valentines_garage/models/profile.dart';

class AuthService {
  static final AuthService _instance = AuthService._internal();
  factory AuthService() => _instance;
  AuthService._internal();

  SupabaseClient get _client => Supabase.instance.client;

  Profile? _cachedProfile;

  User? get currentUser => _client.auth.currentUser;
  bool get isAuthenticated => currentUser != null;
  bool get isAdmin => _cachedProfile?.isAdmin ?? false;
  Profile? get profile => _cachedProfile;

  Stream<AuthState> get authStateChanges => _client.auth.onAuthStateChange;

  Future<AuthResponse> signIn({
    required String email,
    required String password,
  }) async {
    final response = await _client.auth.signInWithPassword(
      email: email,
      password: password,
    );
    await _fetchAndCacheProfile();
    return response;
  }

  Future<AuthResponse> signUp({
    required String email,
    required String password,
    required String role,
  }) async {
    final response = await _client.auth.signUp(
      email: email,
      password: password,
      data: {'role': role},
    );

    if (response.user != null) {
      try {
        await _client.from('profiles').upsert({
          'id': response.user!.id,
          'role': role,
        });
      } catch (_) {
        // Profile may be created by a trigger; ignore errors here.
      }
      await _fetchAndCacheProfile();
    }

    return response;
  }

  Future<void> signOut() async {
    _cachedProfile = null;
    await _client.auth.signOut();
  }

  User? getCurrentUser() {
    return _client.auth.currentUser;
  }

  Future<Profile?> getCurrentProfile() async {
    if (_cachedProfile != null) return _cachedProfile;
    return _fetchAndCacheProfile();
  }

  Future<Profile?> _fetchAndCacheProfile() async {
    final user = currentUser;
    if (user == null) return null;

    try {
      final data = await _client
          .from('profiles')
          .select()
          .eq('id', user.id)
          .single();
      _cachedProfile = Profile.fromJson(data);
      return _cachedProfile;
    } catch (e) {
      // If profile doesn't exist yet, create a default one.
      _cachedProfile = Profile(id: user.id, role: 'mechanic');
      return _cachedProfile;
    }
  }

  void clearCache() {
    _cachedProfile = null;
  }
}
