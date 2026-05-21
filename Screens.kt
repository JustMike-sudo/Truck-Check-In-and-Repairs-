package com.valentinesgarage.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.animation.core.*
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.valentinesgarage.data.local.entity.ChecklistTaskEntity
import com.valentinesgarage.data.local.entity.EmployeeReportEntity
import com.valentinesgarage.data.local.entity.MechanicNoteEntity
import com.valentinesgarage.data.local.entity.TruckEntity
import com.valentinesgarage.ui.theme.*
import com.valentinesgarage.ui.viewmodel.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date

// --- Sleek Liquid Glassmorphism Core Components ---

@Composable
fun LiquidBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightObsidian) // Deep Cosmic Obsidian background
    ) {
        // Subtle Engineering drafting grid of intersecting lines
        val density = androidx.compose.ui.platform.LocalDensity.current
        val gridSpacingPx = with(density) { 24.dp.toPx() }
        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val lineColor = Color(0x06FFFFFF)
            val strokeWidth = 1f

            // Vertical lines
            var x = 0f
            while (x < width) {
                drawLine(
                    color = lineColor,
                    start = androidx.compose.ui.geometry.Offset(x, 0f),
                    end = androidx.compose.ui.geometry.Offset(x, height),
                    strokeWidth = strokeWidth
                )
                x += gridSpacingPx
            }

            // Horizontal lines
            var y = 0f
            while (y < height) {
                drawLine(
                    color = lineColor,
                    start = androidx.compose.ui.geometry.Offset(0f, y),
                    end = androidx.compose.ui.geometry.Offset(width, y),
                    strokeWidth = strokeWidth
                )
                y += gridSpacingPx
            }
        }

        // Glowing Blob 1: Laser Cyan (Top-Right)
        Box(
            modifier = Modifier
                .size(450.dp)
                .align(Alignment.TopEnd)
                .offset(x = 120.dp, y = (-120).dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0x3B00E5FF), // 23% Laser Cyan
                            Color(0x0000E5FF)
                        )
                    ),
                    shape = RoundedCornerShape(225.dp)
                )
        )
        // Glowing Blob 2: Electric Violet (Center-Left)
        Box(
            modifier = Modifier
                .size(400.dp)
                .align(Alignment.CenterStart)
                .offset(x = (-150).dp, y = (-80).dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0x288A2BE2), // 16% Electric Violet
                            Color(0x008A2BE2)
                        )
                    ),
                    shape = RoundedCornerShape(200.dp)
                )
        )
        // Glowing Blob 3: Neon Tangerine (Bottom-Right)
        Box(
            modifier = Modifier
                .size(500.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 180.dp, y = 180.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0x20FF6D00), // 12% Neon Tangerine
                            Color(0x00FF6D00)
                        )
                    ),
                    shape = RoundedCornerShape(250.dp)
                )
        )
        
        content()
    }
}

@Composable
fun CornerHUDBrackets(
    modifier: Modifier = Modifier,
    color: Color = GlacierBlue.copy(alpha = 0.4f),
    sizeDp: androidx.compose.ui.unit.Dp = 8.dp,
    strokeWidthDp: androidx.compose.ui.unit.Dp = 1.dp
) {
    androidx.compose.foundation.Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val len = sizeDp.toPx()
        val sw = strokeWidthDp.toPx()

        // Top-left corner
        drawLine(color, androidx.compose.ui.geometry.Offset(0f, 0f), androidx.compose.ui.geometry.Offset(len, 0f), sw)
        drawLine(color, androidx.compose.ui.geometry.Offset(0f, 0f), androidx.compose.ui.geometry.Offset(0f, len), sw)

        // Top-right corner
        drawLine(color, androidx.compose.ui.geometry.Offset(w - len, 0f), androidx.compose.ui.geometry.Offset(w, 0f), sw)
        drawLine(color, androidx.compose.ui.geometry.Offset(w, 0f), androidx.compose.ui.geometry.Offset(w, len), sw)

        // Bottom-left corner
        drawLine(color, androidx.compose.ui.geometry.Offset(0f, h - len), androidx.compose.ui.geometry.Offset(0f, h), sw)
        drawLine(color, androidx.compose.ui.geometry.Offset(0f, h), androidx.compose.ui.geometry.Offset(len, h), sw)

        // Bottom-right corner
        drawLine(color, androidx.compose.ui.geometry.Offset(w - len, h), androidx.compose.ui.geometry.Offset(w, h), sw)
        drawLine(color, androidx.compose.ui.geometry.Offset(w, h - len), androidx.compose.ui.geometry.Offset(w, h), sw)
    }
}

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(24.dp),
    borderStroke: BorderStroke = BorderStroke(
        1.dp,
        Brush.linearGradient(
            colors = listOf(
                Color(0x33FFFFFF), // Highly reflective top-left bevel
                Color(0x05FFFFFF)  // Low-intensity bottom-right bevel
            )
        )
    ),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = Color(0x13FFFFFF) // 7% Translucent White Glass
        ),
        border = borderStroke,
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            CornerHUDBrackets()
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = horizontalAlignment,
                content = content
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlassTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: androidx.compose.ui.text.input.VisualTransformation = androidx.compose.ui.text.input.VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = true,
    maxLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        singleLine = singleLine,
        maxLines = maxLines,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = LaserCyan,
            unfocusedBorderColor = Color(0x1AFFFFFF),
            focusedLabelColor = LaserCyan,
            unfocusedLabelColor = TextSecondary,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary,
            focusedContainerColor = Color(0x20FFFFFF),
            unfocusedContainerColor = Color(0x0AFFFFFF)
        )
    )
}

@Composable
fun GlassButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = LaserCyan,
    contentColor: Color = CosmicDark,
    shape: Shape = RoundedCornerShape(16.dp),
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = containerColor.copy(alpha = 0.25f),
            disabledContentColor = contentColor.copy(alpha = 0.5f)
        ),
        content = content
    )
}

// --- ROUTES ---
object Routes {
    const val LOGIN = "login"
    const val MAIN_SHELL = "main_shell"
    const val ACTIVE_JOBS = "active_jobs"
    const val CHECK_IN = "check_in"
    const val CHECKLIST = "checklist/{truckId}"
    const val TRUCK_DETAILS = "truck_details/{truckId}"
    const val REPORTS = "reports"
    const val ADMIN = "admin"

    fun checklist(truckId: Int) = "checklist/$truckId"
    fun truckDetails(truckId: Int) = "truck_details/$truckId"
}

// --- MAIN NAV HOST ---
@Composable
fun GarageNavHost(
    authViewModel: AuthViewModel,
    checkInViewModel: CheckInViewModel,
    activeJobsViewModel: ActiveJobsViewModel,
    checklistViewModel: ChecklistViewModel,
    reportsViewModel: ReportsViewModel,
    adminViewModel: AdminViewModel
) {
    val navController = rememberNavController()
    val currentUser by authViewModel.currentUser.collectAsState()

    val startDestination = if (currentUser == null) Routes.LOGIN else Routes.MAIN_SHELL

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Routes.LOGIN) {
            LoginScreen(authViewModel) {
                navController.navigate(Routes.MAIN_SHELL) {
                    popUpTo(Routes.LOGIN) { inclusive = true }
                }
            }
        }
        composable(Routes.MAIN_SHELL) {
            MainShellContainer(
                navController = navController,
                authViewModel = authViewModel,
                checkInViewModel = checkInViewModel,
                activeJobsViewModel = activeJobsViewModel,
                checklistViewModel = checklistViewModel,
                reportsViewModel = reportsViewModel,
                adminViewModel = adminViewModel
            )
        }
    }
}

// --- SCREEN 1: LOGIN ---
@Composable
fun LoginScreen(viewModel: AuthViewModel, onLoginSuccess: () -> Unit) {
    var activeTab by remember { mutableStateOf(0) } // 0 = Sign In, 1 = Sign Up

    // Sign In states
    val registeredUsers by viewModel.registeredUsers.collectAsState()
    var selectedName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    // Sign Up states
    var signUpName by remember { mutableStateOf("") }
    var signUpCode by remember { mutableStateOf("") }

    LaunchedEffect(registeredUsers) {
        if (selectedName.isEmpty() && registeredUsers.isNotEmpty()) {
            selectedName = registeredUsers.first().name
        }
    }

    val context = LocalContext.current

    LiquidBackground(
        modifier = Modifier.fillMaxSize()
    ) {
        GlassCard(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .align(Alignment.Center)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Logo
            Icon(
                imageVector = Icons.Default.Build,
                contentDescription = "Garage Logo",
                tint = LaserCyan,
                modifier = Modifier.size(56.dp)
            )

            Text(
                text = "VALENTINE'S GARAGE",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = LaserCyan,
                    letterSpacing = 2.sp
                ),
                textAlign = TextAlign.Center
            )

            Text(
                text = "Truck Maintenance Portal",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Custom Segmented Tabs (Sign In / Sign Up)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0x0AFFFFFF), RoundedCornerShape(12.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                TabButton(
                    text = "SIGN IN",
                    isSelected = activeTab == 0,
                    modifier = Modifier.weight(1f)
                ) {
                    activeTab = 0
                }
                TabButton(
                    text = "SIGN UP",
                    isSelected = activeTab == 1,
                    modifier = Modifier.weight(1f)
                ) {
                    activeTab = 1
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (activeTab == 0) {
                // --- SIGN IN FLOW ---
                // Username Dropdown
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { expanded = true },
                        modifier = Modifier.fillMaxWidth(),
                        border = BorderStroke(1.dp, Color(0x1AFFFFFF)),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color(0x0AFFFFFF),
                            contentColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (selectedName.isEmpty()) "Select User..." else selectedName,
                                color = TextPrimary
                            )
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = LaserCyan
                            )
                        }
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .background(Color(0xFF0F111A)) // Dark Cosmic solid background for menu readibility
                    ) {
                        registeredUsers.forEach { user ->
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(user.name, color = TextPrimary)
                                        if (user.isAdmin) {
                                            Surface(
                                                color = LaserCyan.copy(alpha = 0.2f),
                                                border = BorderStroke(1.dp, LaserCyan),
                                                shape = RoundedCornerShape(4.dp)
                                            ) {
                                                Text(
                                                    text = "Admin",
                                                    color = LaserCyan,
                                                    fontSize = 10.sp,
                                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                                )
                                            }
                                        }
                                    }
                                },
                                onClick = {
                                    selectedName = user.name
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                // Password/Access Code Field
                GlassTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Access Code", color = TextSecondary) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Sign In Button
                GlassButton(
                    onClick = {
                        val success = viewModel.login(selectedName, password)
                        if (success) {
                            Toast.makeText(context, "Logged in as $selectedName", Toast.LENGTH_SHORT).show()
                            onLoginSuccess()
                        } else {
                            Toast.makeText(context, "Incorrect Access Code", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    containerColor = LaserCyan,
                    contentColor = CosmicDark
                ) {
                    Text(
                        text = "SIGN IN",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }

            } else {
                // --- SIGN UP FLOW ---
                // Mechanic/Admin Name Input
                GlassTextField(
                    value = signUpName,
                    onValueChange = { signUpName = it },
                    label = { Text("Mechanic / Employee Name", color = TextSecondary) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Mechanic/Admin Access Code Input
                GlassTextField(
                    value = signUpCode,
                    onValueChange = { if (it.length <= 8) signUpCode = it },
                    label = { Text("Access Code (min 4 digits)", color = TextSecondary) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Sign Up Button
                GlassButton(
                    onClick = {
                        val (success, msg) = viewModel.signUp(signUpName, signUpCode, false)
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        if (success) {
                            selectedName = signUpName.trim()
                            password = signUpCode.trim()
                            activeTab = 0 // Auto-switch to Sign In tab!
                            signUpName = ""
                            signUpCode = ""
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    containerColor = LaserCyan,
                    contentColor = CosmicDark
                ) {
                    Text(
                        text = "REGISTER MECHANIC",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

@Composable
fun TabButton(
    text: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) LaserCyan else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) CosmicDark else TextSecondary,
            fontSize = 13.sp
        )
    }
}

// --- MAIN CONTAINER SHELL ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainShellContainer(
    navController: NavController,
    authViewModel: AuthViewModel,
    checkInViewModel: CheckInViewModel,
    activeJobsViewModel: ActiveJobsViewModel,
    checklistViewModel: ChecklistViewModel,
    reportsViewModel: ReportsViewModel,
    adminViewModel: AdminViewModel
) {
    val shellNavController = rememberNavController()
    val currentUser by authViewModel.currentUser.collectAsState()
    val isAdmin by authViewModel.isAdmin.collectAsState()
    val context = LocalContext.current

    val navBackStackEntry by shellNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Build,
                            contentDescription = null,
                            tint = SafetyOrange,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "Valentine's Garage",
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        authViewModel.logout()
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.MAIN_SHELL) { inclusive = true }
                        }
                        Toast.makeText(context, "Signed out", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Log Out",
                            tint = CautionRed
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CarbonDark,
                    titleContentColor = TextPrimary
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = CarbonDark,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.List, contentDescription = "Active Jobs") },
                    label = { Text("Active Jobs") },
                    selected = currentRoute == Routes.ACTIVE_JOBS || currentRoute?.startsWith("checklist") == true || currentRoute?.startsWith("truck_details") == true,
                    onClick = { shellNavController.navigate(Routes.ACTIVE_JOBS) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SafetyOrange,
                        selectedTextColor = SafetyOrange,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary,
                        indicatorColor = HeavySteel
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Add, contentDescription = "Check-In") },
                    label = { Text("Check-In") },
                    selected = currentRoute == Routes.CHECK_IN,
                    onClick = { shellNavController.navigate(Routes.CHECK_IN) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SafetyOrange,
                        selectedTextColor = SafetyOrange,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary,
                        indicatorColor = HeavySteel
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Info, contentDescription = "Reports") },
                    label = { Text("Reports") },
                    selected = currentRoute == Routes.REPORTS,
                    onClick = { shellNavController.navigate(Routes.REPORTS) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SafetyOrange,
                        selectedTextColor = SafetyOrange,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary,
                        indicatorColor = HeavySteel
                    )
                )
                if (isAdmin) {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Settings, contentDescription = "Admin Console") },
                        label = { Text("Admin Console") },
                        selected = currentRoute == Routes.ADMIN,
                        onClick = { shellNavController.navigate(Routes.ADMIN) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = SafetyOrange,
                            selectedTextColor = SafetyOrange,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary,
                            indicatorColor = HeavySteel
                        )
                    )
                }
            }
        },
        floatingActionButton = {
            if (currentRoute == Routes.ACTIVE_JOBS) {
                FloatingActionButton(
                    onClick = { shellNavController.navigate(Routes.CHECK_IN) },
                    containerColor = SafetyOrange,
                    contentColor = CharcoalGray
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "New Check-In")
                }
            }
        }
    ) { paddingValues ->
        LiquidBackground(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            NavHost(
                navController = shellNavController,
                startDestination = Routes.ACTIVE_JOBS
            ) {
                composable(Routes.ACTIVE_JOBS) {
                    ActiveJobsScreen(
                        viewModel = activeJobsViewModel,
                        currentUser = currentUser ?: "Mechanic",
                        isAdmin = isAdmin,
                        onTruckClick = { truckId ->
                            shellNavController.navigate(Routes.truckDetails(truckId))
                        }
                    )
                }
                composable(Routes.CHECK_IN) {
                    CheckInScreen(
                        viewModel = checkInViewModel,
                        currentUser = currentUser ?: "Mechanic",
                        onCheckInSuccess = {
                            shellNavController.navigate(Routes.ACTIVE_JOBS) {
                                popUpTo(Routes.CHECK_IN) { inclusive = true }
                            }
                        }
                    )
                }
                composable(Routes.TRUCK_DETAILS) { backStackEntry ->
                    val truckId = backStackEntry.arguments?.getString("truckId")?.toIntOrNull() ?: 0
                    LaunchedEffect(truckId) {
                        checklistViewModel.selectTruck(truckId)
                    }
                    TruckDetailsScreen(
                        viewModel = checklistViewModel,
                        currentUser = currentUser ?: "Mechanic",
                        onNavigateToChecklist = { shellNavController.navigate(Routes.checklist(truckId)) },
                        onBack = { shellNavController.popBackStack() }
                    )
                }
                composable(Routes.CHECKLIST) { backStackEntry ->
                    val truckId = backStackEntry.arguments?.getString("truckId")?.toIntOrNull() ?: 0
                    LaunchedEffect(truckId) {
                        checklistViewModel.selectTruck(truckId)
                    }
                    ChecklistScreen(
                        viewModel = checklistViewModel,
                        currentUser = currentUser ?: "Mechanic",
                        onFinalized = {
                            shellNavController.navigate(Routes.ACTIVE_JOBS) {
                                popUpTo(Routes.ACTIVE_JOBS) { inclusive = false }
                            }
                        },
                        onBack = { shellNavController.popBackStack() }
                    )
                }
                composable(Routes.REPORTS) {
                    ReportScreen(reportsViewModel)
                }
                if (isAdmin) {
                    composable(Routes.ADMIN) {
                        AdminDashboardScreen(adminViewModel)
                    }
                }
            }
        }
    }
}

// --- SCREEN 2: ACTIVE JOBS ---
@Composable
fun MetricChip(label: String, count: Int, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .width(90.dp)
                .height(54.dp)
                .background(Color(0x0CFFFFFF), RoundedCornerShape(12.dp))
                .border(BorderStroke(1.dp, color.copy(alpha = 0.3f)), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            )
        }
        Text(
            text = label.uppercase(),
            fontSize = 10.sp,
            color = TextSecondary,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
    }
}

@Composable
fun ActiveJobsScreen(
    viewModel: ActiveJobsViewModel,
    currentUser: String,
    isAdmin: Boolean,
    onTruckClick: (Int) -> Unit
) {
    val trucks by viewModel.trucks.collectAsState()
    val filter by viewModel.statusFilter.collectAsState()
    val query by viewModel.searchQuery.collectAsState()
    val context = LocalContext.current

    val filterOptions = listOf("All", "Pending", "In Progress", "Complete")

    // Operations statistics from database
    val jobStats by viewModel.jobStats.collectAsState()
    val waitingCount = jobStats["Pending"] ?: 0
    val inProgressCount = jobStats["In Progress"] ?: 0
    val completedCount = jobStats["Complete"] ?: 0

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // OPERATIONAL HERO PANEL
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "VALENTINE'S OPERATIONS",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = LaserCyan,
                        letterSpacing = 2.sp
                    )
                )
                Text(
                    text = "Live Active Dispatches",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MetricChip(label = "Pending", count = waitingCount, color = AlertPink)
                    MetricChip(label = "In Progress", count = inProgressCount, color = NeonTangerine)
                    MetricChip(label = "Complete", count = completedCount, color = NeonGreen)
                }
            }
        }

        // Search Bar
        item {
            GlassTextField(
                value = query,
                onValueChange = { viewModel.setSearchQuery(it) },
                placeholder = { Text("Search by Reg or Driver...", color = TextSecondary) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = LaserCyan) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Filter Buttons Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                filterOptions.forEach { option ->
                    val isSelected = filter == option
                    val color = when (option) {
                        "Pending" -> AlertPink
                        "In Progress" -> NeonTangerine
                        "Complete" -> NeonGreen
                        else -> LaserCyan
                    }
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.setFilter(option) },
                        label = {
                            Text(
                                text = option,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) CosmicDark else color
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = color,
                            containerColor = Color(0x0AFFFFFF),
                            labelColor = color
                        ),
                        border = BorderStroke(1.dp, if (isSelected) Color.Transparent else Color(0x1AFFFFFF))
                    )
                }
            }
        }

        // Truck list or empty state
        if (trucks.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No jobs matching filter criteria.",
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            items(trucks, key = { it.id }) { truck ->
                val canModify = isAdmin || truck.mechanicName == currentUser
                ActiveJobCard(
                    truck = truck,
                    canModify = canModify,
                    onClick = { onTruckClick(truck.id) }
                )
                // Add spacing between items
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun ActiveJobCard(truck: TruckEntity, canModify: Boolean, onClick: () -> Unit) {
    val statusColor = when (truck.status) {
        "Pending" -> AlertPink
        "In Progress" -> NeonTangerine
        "Complete" -> NeonGreen
        else -> TextSecondary
    }

    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (!canModify) {
                    Icon(Icons.Default.Lock, contentDescription = "Locked", tint = TextSecondary, modifier = Modifier.size(16.dp))
                }
                Text(
                    text = truck.registrationNumber,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = LaserCyan
                    )
                )
            }

            // Status Badge
            Surface(
                color = statusColor.copy(alpha = 0.15f),
                border = BorderStroke(1.dp, statusColor.copy(alpha = 0.8f)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = truck.status.uppercase(),
                    color = statusColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }

        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0x1AFFFFFF)))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = "DRIVER", color = TextSecondary, fontSize = 10.sp, letterSpacing = 1.sp)
                Text(text = truck.driverName, color = TextPrimary, fontWeight = FontWeight.Bold)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(text = "MILEAGE", color = TextSecondary, fontSize = 10.sp, letterSpacing = 1.sp)
                Text(text = "${truck.kilometers} km", color = TextPrimary, fontWeight = FontWeight.Bold)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "IN: ${truck.checkInDateTime}",
                color = TextSecondary,
                fontSize = 11.sp
            )

            // Quick condition indicators
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                if (truck.damageCoordinates.isNotBlank()) Icon(Icons.Default.Warning, "Damage", tint = AlertPink, modifier = Modifier.size(16.dp))
            }
        }
    }
}

// --- SCREEN 3: CHECK-IN SCREEN ---
@Composable
fun CheckInScreen(
    viewModel: CheckInViewModel,
    currentUser: String,
    onCheckInSuccess: () -> Unit
) {
    val reg by viewModel.regNumber.collectAsState()
    val driver by viewModel.driverName.collectAsState()
    val kms by viewModel.kilometers.collectAsState()
    val damageCoordinates by viewModel.damageCoordinates.collectAsState()
    val notes by viewModel.additionalNotes.collectAsState()

    val hasScratches by viewModel.hasScratches.collectAsState()
    val hasBrokenLights by viewModel.hasBrokenLights.collectAsState()
    val hasTireDamage by viewModel.hasTireDamage.collectAsState()
    val hasOilLeak by viewModel.hasOilLeak.collectAsState()

    val isSubmitting by viewModel.isSubmitting.collectAsState()
    val errorMsg by viewModel.errorMsg.collectAsState()

    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "TRUCK CHECK-IN FORM",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = LaserCyan,
                letterSpacing = 2.sp
            )
        )

        // General Info Cards
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Text(text = "General Information", fontWeight = FontWeight.Bold, color = LaserCyan)

            GlassTextField(
                value = reg,
                onValueChange = { viewModel.regNumber.value = it },
                label = { Text("Registration Number (e.g. CA-123-456)", color = TextSecondary) },
                modifier = Modifier.fillMaxWidth()
            )

            GlassTextField(
                value = driver,
                onValueChange = { viewModel.driverName.value = it },
                label = { Text("Driver Name", color = TextSecondary) },
                modifier = Modifier.fillMaxWidth()
            )

            GlassTextField(
                value = kms,
                onValueChange = { viewModel.kilometers.value = it },
                label = { Text("Kilometers Driven", color = TextSecondary) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Vehicle Condition Card
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Capture Vehicle Condition", fontWeight = FontWeight.Bold, color = LaserCyan)
            Text(text = "Tap on the vehicle blueprint to mark damage locations.", color = TextSecondary, fontSize = 12.sp)

            InteractiveDamageMap(
                damageCoordinates = damageCoordinates,
                onDamageUpdate = { viewModel.damageCoordinates.value = it }
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Quick Conditions", color = LaserCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)

            ConditionCheckboxRow("Scratches / Body Damage", hasScratches) { viewModel.hasScratches.value = it }
            ConditionCheckboxRow("Broken Lights", hasBrokenLights) { viewModel.hasBrokenLights.value = it }
            ConditionCheckboxRow("Tire / Wheel Damage", hasTireDamage) { viewModel.hasTireDamage.value = it }
            ConditionCheckboxRow("Oil or Fluid Leak", hasOilLeak) { viewModel.hasOilLeak.value = it }
        }

        // Additional Notes Card
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Additional Mechanic Notes", fontWeight = FontWeight.Bold, color = LaserCyan)

            GlassTextField(
                value = notes,
                onValueChange = { viewModel.additionalNotes.value = it },
                label = { Text("Describe issues, sounds, requests...", color = TextSecondary) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                singleLine = false,
                maxLines = 4
            )
        }

        // Error message view
        AnimatedVisibility(
            visible = errorMsg != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Text(
                text = errorMsg ?: "",
                color = AlertPink,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        // Submit Button
        GlassButton(
            onClick = {
                viewModel.checkInTruck(currentUser) {
                    Toast.makeText(context, "Truck successfully registered!", Toast.LENGTH_SHORT).show()
                    onCheckInSuccess()
                }
            },
            enabled = !isSubmitting,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            containerColor = LaserCyan,
            contentColor = CosmicDark
        ) {
            if (isSubmitting) {
                CircularProgressIndicator(color = CosmicDark, modifier = Modifier.size(24.dp))
            } else {
                Text(
                    text = "CHECK IN VEHICLE",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Composable
fun InteractiveDamageMap(
    damageCoordinates: String,
    onDamageUpdate: (String) -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val parsedCoords = remember(damageCoordinates) {
        if (damageCoordinates.isBlank()) emptyList()
        else damageCoordinates.split(";").mapNotNull {
            val parts = it.split(",")
            if (parts.size == 2) {
                Pair(parts[0].toFloatOrNull() ?: 0f, parts[1].toFloatOrNull() ?: 0f)
            } else null
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0x0AFFFFFF))
            .border(1.dp, Color(0x1AFFFFFF), RoundedCornerShape(12.dp))
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    val x = offset.x / size.width
                    val y = offset.y / size.height
                    val newCoords = parsedCoords + Pair(x, y)
                    onDamageUpdate(newCoords.joinToString(";") { "${it.first},${it.second}" })
                }
            }
    ) {
        // Truck Blueprint Outline
        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            drawRoundRect(
                color = LaserCyan.copy(alpha = 0.3f),
                topLeft = Offset(w * 0.2f, h * 0.3f),
                size = Size(w * 0.6f, h * 0.4f),
                cornerRadius = CornerRadius(8.dp.toPx()),
                style = Stroke(width = 2.dp.toPx())
            )
            drawRoundRect(
                color = LaserCyan.copy(alpha = 0.3f),
                topLeft = Offset(w * 0.7f, h * 0.4f),
                size = Size(w * 0.15f, h * 0.3f),
                cornerRadius = CornerRadius(4.dp.toPx()),
                style = Stroke(width = 2.dp.toPx())
            )
            drawCircle(
                color = LaserCyan.copy(alpha = 0.5f),
                radius = h * 0.1f,
                center = Offset(w * 0.3f, h * 0.7f),
                style = Stroke(width = 2.dp.toPx())
            )
            drawCircle(
                color = LaserCyan.copy(alpha = 0.5f),
                radius = h * 0.1f,
                center = Offset(w * 0.6f, h * 0.7f),
                style = Stroke(width = 2.dp.toPx())
            )
            drawCircle(
                color = LaserCyan.copy(alpha = 0.5f),
                radius = h * 0.1f,
                center = Offset(w * 0.8f, h * 0.7f),
                style = Stroke(width = 2.dp.toPx())
            )
        }

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val w = maxWidth
            val h = maxHeight
            parsedCoords.forEach { coord ->
                DamageMarker(
                    xOffset = w * coord.first,
                    yOffset = h * coord.second,
                    onRemove = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        val newCoords = parsedCoords - coord
                        onDamageUpdate(newCoords.joinToString(";") { "${it.first},${it.second}" })
                    }
                )
            }
        }
    }
}

@Composable
fun ConditionCheckboxRow(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = TextPrimary, fontSize = 14.sp)
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = LaserCyan,
                uncheckedColor = Color(0x33FFFFFF)
            )
        )
    }
}

@Composable
fun DamageMarker(xOffset: androidx.compose.ui.unit.Dp, yOffset: androidx.compose.ui.unit.Dp, onRemove: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition()
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Box(
        modifier = Modifier
            .offset(x = xOffset - 12.dp, y = yOffset - 12.dp)
            .size(24.dp)
            .clip(androidx.compose.foundation.shape.CircleShape)
            .clickable(onClick = onRemove)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AlertPink.copy(alpha = pulseAlpha), androidx.compose.foundation.shape.CircleShape)
        )
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(10.dp)
                .background(AlertPink, androidx.compose.foundation.shape.CircleShape)
        )
    }
}

// --- SCREEN 4: TRUCK DETAILS ---
@Composable
fun TruckDetailsScreen(
    viewModel: ChecklistViewModel,
    currentUser: String,
    onNavigateToChecklist: () -> Unit,
    onBack: () -> Unit
) {
    val truck by viewModel.selectedTruck.collectAsState()
    val tasks by viewModel.tasks.collectAsState()
    val notes by viewModel.notes.collectAsState()

    var newNoteText by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    var showDeleteConfirm by remember { mutableStateOf(false) }

    if (truck == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = LaserCyan)
        }
        return
    }

    val activeTruck = truck!!
    val completedCount = tasks.count { it.isCompleted }
    val totalCount = tasks.size

    val progressValue = if (totalCount > 0) completedCount.toFloat() / totalCount else 0f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Back Button
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .background(Color(0x0AFFFFFF), RoundedCornerShape(12.dp))
                .border(BorderStroke(1.dp, Color(0x1AFFFFFF)), RoundedCornerShape(12.dp))
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = LaserCyan)
        }

        // --- HIGH-IMPACT VEHICLE PLATE HERO SECTION ---
        val statusColor = when (activeTruck.status) {
            "Pending" -> AlertPink
            "In Progress" -> NeonTangerine
            "Complete" -> NeonGreen
            else -> TextSecondary
        }

        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            borderStroke = BorderStroke(1.dp, Brush.linearGradient(listOf(LaserCyan.copy(alpha = 0.4f), Color(0x05FFFFFF)))),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Mechanical Tech Badge
            Surface(
                color = Color(0x0CFFFFFF),
                border = BorderStroke(1.dp, Color(0x1AFFFFFF)),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Text(
                    text = "UNIT CHASSIS PROFILE",
                    color = LaserCyan,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    letterSpacing = 2.sp,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp)
                )
            }

            Text(
                text = activeTruck.registrationNumber.uppercase(),
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    letterSpacing = 1.sp
                ),
                textAlign = TextAlign.Center
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "ASSIGNED DRIVER", color = TextSecondary, fontSize = 9.sp, letterSpacing = 1.sp)
                    Text(text = activeTruck.driverName, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
                Box(modifier = Modifier.width(1.dp).height(24.dp).background(Color(0x1AFFFFFF)))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "CURRENT STATUS", color = TextSecondary, fontSize = 9.sp, letterSpacing = 1.sp)
                    Text(
                        text = activeTruck.status.uppercase(),
                        color = statusColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }

        // --- NEON DIAGNOSTIC SCAN BOARD ---
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "INSPECTION FINDINGS SCAN",
                fontWeight = FontWeight.Bold,
                color = LaserCyan,
                fontSize = 12.sp,
                letterSpacing = 1.sp
            )

            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0x1AFFFFFF)))

            ConditionStatusLine(label = "Scratches / Body Damage", present = activeTruck.hasScratches)
            ConditionStatusLine(label = "Broken Lights", present = activeTruck.hasBrokenLights)
            ConditionStatusLine(label = "Tire / Wheel Damage", present = activeTruck.hasTireDamage)
            ConditionStatusLine(label = "Oil or Fluid Leak", present = activeTruck.hasOilLeak)

            if (activeTruck.additionalNotes.isNotEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0x1AFFFFFF)))
                Text(text = "DRIVER DISPATCH REMARKS:", color = TextSecondary, fontSize = 10.sp, letterSpacing = 1.sp)
                Text(
                    text = activeTruck.additionalNotes,
                    color = TextPrimary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // --- CHECKLIST PROGRESSION & NEON BEAM ---
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "REPAIR PROGRESS",
                    fontWeight = FontWeight.Bold,
                    color = LaserCyan,
                    fontSize = 12.sp,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "$completedCount / $totalCount COMPLETED",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }

            // Laser Neon Progress Beam
            LinearProgressIndicator(
                progress = progressValue,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = NeonGreen,
                trackColor = Color(0x0EFFFFFF)
            )

            GlassButton(
                onClick = onNavigateToChecklist,
                modifier = Modifier.fillMaxWidth(),
                containerColor = LaserCyan,
                contentColor = CosmicDark
            ) {
                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("OPEN COLLABORATIVE CHECKLIST", fontWeight = FontWeight.Bold)
            }
        }

        // --- MECHANIC DIAGNOSTIC NOTES CARD ---
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "SYSTEM MECHANIC NOTES",
                fontWeight = FontWeight.Bold,
                color = LaserCyan,
                fontSize = 12.sp,
                letterSpacing = 1.sp
            )

            // Glass TextField diagnostics input
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                GlassTextField(
                    value = newNoteText,
                    onValueChange = { newNoteText = it },
                    placeholder = { Text("Log part changes or progress...", color = TextSecondary) },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                IconButton(
                    onClick = {
                        if (newNoteText.isNotBlank()) {
                            viewModel.addNote(currentUser, newNoteText)
                            newNoteText = ""
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .background(LaserCyan, RoundedCornerShape(12.dp))
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Note", tint = CosmicDark)
                }
            }

            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0x1AFFFFFF)))

            // Notes List
            if (notes.isEmpty()) {
                Text(
                    text = "No mechanic entries logged.",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    notes.forEach { note ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0x0AFFFFFF), RoundedCornerShape(12.dp))
                                .border(BorderStroke(1.dp, Color(0x0FFFFFFF)), RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = note.mechanicName.uppercase(),
                                    color = LaserCyan,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    letterSpacing = 1.sp
                                )
                                Text(text = note.createdAt, color = TextSecondary, fontSize = 9.sp)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = note.noteContent, color = TextPrimary, fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        // --- GLASS ACTION BUTTONS ---
        val context = LocalContext.current
        GlassButton(
            onClick = {
                // PDF Export Simulation: Write a detailed file to storage and show path
                try {
                    val reportDir = File(context.getExternalFilesDir(null), "Reports")
                    if (!reportDir.exists()) reportDir.mkdirs()

                    val file = File(reportDir, "GarageReport_${activeTruck.registrationNumber.replace("-", "_")}.pdf")
                    file.writeText(
                        """
                        =========================================
                        VALENTINE'S GARAGE VEHICLE JOB REPORT
                        =========================================
                        Date Exported: ${SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())}
                        Truck Registration: ${activeTruck.registrationNumber}
                        Driver: ${activeTruck.driverName}
                        Kilometers: ${activeTruck.kilometers}
                        Status: ${activeTruck.status}
                        
                        --- INSPECTION CONDITIONS ---
                        Scratches/Body Damage: ${if (activeTruck.hasScratches) "YES" else "NO"}
                        Broken Lights: ${if (activeTruck.hasBrokenLights) "YES" else "NO"}
                        Tire Damage: ${if (activeTruck.hasTireDamage) "YES" else "NO"}
                        Oil Leaks: ${if (activeTruck.hasOilLeak) "YES" else "NO"}
                        Notes: ${activeTruck.additionalNotes}
                        
                        --- COMPLETED REPAIR TASKS ---
                        ${tasks.joinToString("\n") { "- ${it.taskName}: ${if (it.isCompleted) "COMPLETED [${it.updatedAt}]" else "PENDING"}" }}
                        
                        --- MECHANIC DIAGNOSTICS & LOG ---
                        ${notes.joinToString("\n") { "- [${it.createdAt}] ${it.mechanicName}: ${it.noteContent}" }}
                        =========================================
                        """.trimIndent()
                    )

                    Toast.makeText(context, "Report exported to: ${file.name} successfully!", Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                    Toast.makeText(context, "Export failed: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            containerColor = Color(0x0AFFFFFF),
            contentColor = LaserCyan,
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("GENERATE DIAGNOSTIC PDF REPORT", fontWeight = FontWeight.Bold)
        }

        // Sleek Purge Button
        OutlinedButton(
            onClick = { showDeleteConfirm = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = AlertPink),
            border = BorderStroke(1.dp, AlertPink.copy(alpha = 0.5f)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("DELETE DISPATCH / PURGE DATABASE", fontWeight = FontWeight.Bold)
        }

        if (showDeleteConfirm) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirm = false },
                title = {
                    Text(
                        text = "PURGE VEHICLE DISPATCH RECORD?",
                        fontWeight = FontWeight.Bold,
                        color = AlertPink
                    )
                },
                text = {
                    Text(
                        text = "You are about to delete ${activeTruck.registrationNumber} permanently.\n\nThis will instantly clear all checklist progress, mechanic notes, and logs from the garage database. This action is terminal.",
                        color = TextPrimary
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDeleteConfirm = false
                            viewModel.deleteSelectedTruck {
                                Toast.makeText(context, "Job successfully deleted and purged", Toast.LENGTH_SHORT).show()
                                onBack()
                            }
                        }
                    ) {
                        Text("CONFIRM PURGE", color = AlertPink, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteConfirm = false }) {
                        Text("CANCEL", color = TextSecondary)
                    }
                },
                containerColor = Color(0xFF0F111A), // Dark obsidian
                textContentColor = TextPrimary,
                titleContentColor = AlertPink
            )
        }
    }
}

@Composable
fun ConditionStatusLine(label: String, present: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = TextPrimary)
        if (present) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Warning, contentDescription = null, tint = AlertPink, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "EXPOSED FAULT", color = AlertPink, fontWeight = FontWeight.Bold, fontSize = 11.sp, letterSpacing = 1.sp)
            }
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Check, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "SYSTEM PASS", color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 11.sp, letterSpacing = 1.sp)
            }
        }
    }
}

// --- SCREEN 5: CHECKLIST / TASK LIVE UPDATE ---
@Composable
fun ChecklistScreen(
    viewModel: ChecklistViewModel,
    currentUser: String,
    onFinalized: () -> Unit,
    onBack: () -> Unit
) {
    val truck by viewModel.selectedTruck.collectAsState()
    val tasks by viewModel.tasks.collectAsState()
    val notes by viewModel.notes.collectAsState()

    var noteInput by remember { mutableStateOf("") }
    val context = LocalContext.current

    if (truck == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = LaserCyan)
        }
        return
    }

    val activeTruck = truck!!
    val allCompleted = tasks.isNotEmpty() && tasks.all { it.isCompleted }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Back Navigation Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .background(Color(0x0AFFFFFF), RoundedCornerShape(12.dp))
                    .border(BorderStroke(1.dp, Color(0x1AFFFFFF)), RoundedCornerShape(12.dp))
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = LaserCyan)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "COLLABORATIVE CHECKLIST",
                    fontWeight = FontWeight.ExtraBold,
                    color = LaserCyan,
                    fontSize = 18.sp,
                    letterSpacing = 1.sp
                )
                Text(
                    text = activeTruck.registrationNumber.uppercase(),
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }

        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0x1AFFFFFF)))

        // Mechanic Active User Badge
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0x0AFFFFFF), RoundedCornerShape(16.dp))
                .border(BorderStroke(1.dp, Color(0x1AFFFFFF)), RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Default.Person, contentDescription = null, tint = LaserCyan)
                Text(
                    text = "Working Mechanic: ",
                    color = TextSecondary,
                    fontSize = 14.sp
                )
                Text(
                    text = currentUser,
                    color = LaserCyan,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }

        // Checklist List Header
        Text(
            text = "Repair Check-List Tasks",
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            fontSize = 16.sp
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(tasks, key = { it.id }) { task ->
                ChecklistTaskItem(task = task, onCheckedChange = { viewModel.toggleTask(task) })
            }
        }

        // Add Quick Notes
        GlassCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "ADD MECHANIC LOG ENTRY",
                fontWeight = FontWeight.Bold,
                color = LaserCyan,
                fontSize = 12.sp,
                letterSpacing = 1.sp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                GlassTextField(
                    value = noteInput,
                    onValueChange = { noteInput = it },
                    placeholder = { Text("Log part changes or progress...", color = TextSecondary) },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                GlassButton(
                    onClick = {
                        if (noteInput.isNotBlank()) {
                            viewModel.addNote(currentUser, noteInput)
                            noteInput = ""
                        }
                    },
                    containerColor = LaserCyan,
                    contentColor = CosmicDark,
                    modifier = Modifier.height(56.dp)
                ) {
                    Text("ADD", fontWeight = FontWeight.Bold)
                }
            }
        }

        // Finalize / Sign Off Button
        GlassButton(
            onClick = {
                if (allCompleted) {
                    viewModel.finalizeJob(currentUser) {
                        Toast.makeText(context, "Job successfully signed off & archived!", Toast.LENGTH_SHORT).show()
                        onFinalized()
                    }
                } else {
                    Toast.makeText(context, "Progress saved successfully", Toast.LENGTH_SHORT).show()
                    onBack()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            containerColor = if (allCompleted) NeonGreen else LaserCyan,
            contentColor = CosmicDark
        ) {
            Icon(Icons.Default.Done, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (allCompleted) "SIGN OFF & CLOSE JOB" else "SAVE WORK PROGRESS",
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ChecklistTaskItem(task: ChecklistTaskEntity, onCheckedChange: (Boolean) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (task.isCompleted) Color(0x1100E676) else Color(0x0AFFFFFF))
            .border(
                BorderStroke(
                    1.dp,
                    if (task.isCompleted) NeonGreen.copy(alpha = 0.4f) else Color(0x1AFFFFFF)
                ),
                RoundedCornerShape(16.dp)
            )
            .clickable { onCheckedChange(!task.isCompleted) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = if (task.isCompleted) Icons.Default.CheckCircle else Icons.Default.Build,
                    contentDescription = null,
                    tint = if (task.isCompleted) NeonGreen else TextSecondary,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = task.taskName,
                    color = if (task.isCompleted) TextPrimary else TextSecondary,
                    fontWeight = if (task.isCompleted) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 15.sp
                )
            }

            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = NeonGreen,
                    uncheckedColor = Color(0x33FFFFFF)
                )
            )
        }
    }
}

// --- SCREEN 6: REPORTS SCREEN ---
@Composable
fun ReportScreen(viewModel: ReportsViewModel) {
    val reports by viewModel.reports.collectAsState()
    val query by viewModel.searchQuery.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "COMPLETED JOB ARCHIVE",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                color = LaserCyan,
                letterSpacing = 1.sp
            )
        )

        // Search Bar
        GlassTextField(
            value = query,
            onValueChange = { viewModel.setSearchQuery(it) },
            placeholder = { Text("Filter reports by Employee, Reg...", color = TextSecondary) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = LaserCyan) },
            modifier = Modifier.fillMaxWidth()
        )

        if (reports.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No completed job logs archived.",
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(reports, key = { it.id }) { report ->
                    ReportCard(report = report)
                }
            }
        }
    }
}

@Composable
fun ReportCard(report: EmployeeReportEntity) {
    GlassCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = report.truckRegNo.uppercase(),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = LaserCyan
                )
            )

            Text(
                text = report.dateTime,
                color = TextSecondary,
                fontSize = 11.sp
            )
        }

        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0x1AFFFFFF)))

        Row(
            verticalAlignment = Alignment.CenterVertically, 
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(Icons.Default.Person, contentDescription = null, tint = LaserCyan, modifier = Modifier.size(16.dp))
            Text(text = "Serviced by: ", color = TextSecondary, fontSize = 13.sp)
            Text(text = report.employeeName, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = "Completed Tasks:", 
            color = LaserCyan, 
            fontWeight = FontWeight.Bold, 
            fontSize = 11.sp,
            letterSpacing = 1.sp
        )
        Text(text = report.tasksCompleted, color = TextPrimary, fontSize = 13.sp)

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = "Mechanic Log:", 
            color = LaserCyan, 
            fontWeight = FontWeight.Bold, 
            fontSize = 11.sp,
            letterSpacing = 1.sp
        )
        Text(text = report.notesWritten, color = TextSecondary, fontSize = 13.sp)
    }
}

// --- SCREEN 7: ADMIN DASHBOARD ---
@Composable
fun AdminDashboardScreen(viewModel: AdminViewModel) {
    var activeSubTab by remember { mutableStateOf(0) } // 0 = Fleet Inventory, 1 = Client Register, 2 = Mechanic Logs

    val trucks by viewModel.trucks.collectAsState()
    val allTasks by viewModel.allTasks.collectAsState()
    val clients = trucks // Clients list is derived from checked-in trucks as they have driver/dispatch details
    val logs by viewModel.logs.collectAsState()
    val jobStats by viewModel.jobStats.collectAsState()
    val mechanicLeaderboard by viewModel.mechanicLeaderboard.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "ADMIN CONSOLE",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                color = GlacierBlue,
                letterSpacing = 2.sp
            )
        )

        // Custom premium segmented sub-tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0x0AFFFFFF), RoundedCornerShape(12.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            TabButton(
                text = "FLEET INVENTORY",
                isSelected = activeSubTab == 0,
                modifier = Modifier.weight(1f)
            ) {
                activeSubTab = 0
            }
            TabButton(
                text = "CLIENT REGISTER",
                isSelected = activeSubTab == 1,
                modifier = Modifier.weight(1f)
            ) {
                activeSubTab = 1
            }
            TabButton(
                text = "MECHANIC LOGS",
                isSelected = activeSubTab == 2,
                modifier = Modifier.weight(1f)
            ) {
                activeSubTab = 2
            }
            TabButton(
                text = "ANALYTICS",
                isSelected = activeSubTab == 3,
                modifier = Modifier.weight(1f)
            ) {
                activeSubTab = 3
            }
        }

        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0x1AFFFFFF)))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            when (activeSubTab) {
                0 -> AdminFleetInventoryTab(trucks, allTasks)
                1 -> AdminClientRegisterTab(clients)
                2 -> AdminMechanicLogsTab(logs)
                3 -> AdminAnalyticsTab(jobStats, mechanicLeaderboard)
            }
        }
    }
}

@Composable
fun AdminFleetInventoryTab(trucks: List<TruckEntity>, allTasks: List<ChecklistTaskEntity>) {
    if (trucks.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No checked-in vehicles in fleet.", color = TextSecondary)
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(trucks, key = { it.id }) { truck ->
                val statusColor = when (truck.status) {
                    "Pending" -> DeepCrimson
                    "In Progress" -> ChampagneGold
                    "Complete" -> MintSage
                    else -> TextSecondary
                }

                val truckTasks = allTasks.filter { it.truckId == truck.id && it.isCompleted }

                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = truck.registrationNumber.uppercase(),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = GlacierBlue
                            )
                        )

                        Surface(
                            color = statusColor.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, statusColor.copy(alpha = 0.8f)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = truck.status.uppercase(),
                                color = statusColor,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                letterSpacing = 1.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0x1AFFFFFF)))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("ASSIGNED DRIVER", color = TextSecondary, fontSize = 9.sp, letterSpacing = 1.sp)
                            Text(truck.driverName, color = TextPrimary, fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("CHECK-IN MECHANIC", color = TextSecondary, fontSize = 9.sp, letterSpacing = 1.sp)
                            Text(truck.mechanicName, color = ChampagneGold, fontWeight = FontWeight.Bold)
                        }
                    }
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(horizontalAlignment = Alignment.Start) {
                            Text("MILESTONES", color = TextSecondary, fontSize = 9.sp, letterSpacing = 1.sp)
                            Text("${truck.kilometers} km", color = TextPrimary, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Condition Flags Section
                    Text("CONDITIONS", color = TextSecondary, fontSize = 9.sp, letterSpacing = 1.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ConditionSmallBadge("Scratch", truck.hasScratches)
                        ConditionSmallBadge("Lights", truck.hasBrokenLights)
                        ConditionSmallBadge("Tire", truck.hasTireDamage)
                        ConditionSmallBadge("Leak", truck.hasOilLeak)
                    }

                    // Tasks Completed Section
                    if (truckTasks.isNotEmpty()) {
                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0x1AFFFFFF)))
                        Text("WHAT HAS BEEN DONE", color = TextSecondary, fontSize = 9.sp, letterSpacing = 1.sp)
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            truckTasks.forEach { task ->
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = MintSage, modifier = Modifier.size(12.dp))
                                    Text(task.taskName, color = TextPrimary, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ConditionSmallBadge(label: String, isFault: Boolean) {
    val color = if (isFault) DeepCrimson else MintSage
    val text = if (isFault) "FAULT: $label" else "PASS: $label"
    Surface(
        color = color.copy(alpha = 0.1f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.4f)),
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            text = text.uppercase(),
            color = color,
            fontWeight = FontWeight.Bold,
            fontSize = 9.sp,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

@Composable
fun AdminClientRegisterTab(clients: List<TruckEntity>) {
    if (clients.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No active clients registered.", color = TextSecondary)
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(clients, key = { it.id }) { client ->
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = GlacierBlue,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = client.driverName,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            )
                        }

                        Text(
                            text = client.registrationNumber.uppercase(),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                color = GlacierBlue,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }

                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0x1AFFFFFF)))

                    Text(
                        text = "CHECK-IN TIME: ${client.checkInDateTime}",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )

                    if (client.additionalNotes.isNotEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0x0AFFFFFF), RoundedCornerShape(8.dp))
                                .padding(8.dp)
                        ) {
                            Text(
                                text = "SPECIAL DISPATCH REMARKS",
                                color = ChampagneGold,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = client.additionalNotes,
                                color = TextPrimary,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminMechanicLogsTab(logs: List<AdminLogEntry>) {
    if (logs.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No operations log entries found.", color = TextSecondary)
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(logs) { entry ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Vertical timeline bullet & line indicator
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(20.dp)
                    ) {
                        val bulletColor = when (entry) {
                            is AdminLogEntry.LiveNote -> ChampagneGold
                            is AdminLogEntry.SignOffReport -> MintSage
                        }
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(bulletColor, RoundedCornerShape(5.dp))
                        )
                        Box(
                            modifier = Modifier
                                .width(2.dp)
                                .height(80.dp)
                                .background(Color(0x1AFFFFFF))
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        when (entry) {
                            is AdminLogEntry.LiveNote -> {
                                GlassCard(
                                    modifier = Modifier.fillMaxWidth(),
                                    borderStroke = BorderStroke(1.dp, ChampagneGold.copy(alpha = 0.2f))
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "LIVE DIAGNOSTIC NOTE",
                                            color = ChampagneGold,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp,
                                            letterSpacing = 1.sp
                                        )
                                        Text(
                                            text = entry.timestamp,
                                            color = TextSecondary,
                                            fontSize = 10.sp
                                        )
                                    }

                                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0x1AFFFFFF)))

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text("Mechanic:", color = TextSecondary, fontSize = 12.sp)
                                        Text(entry.mechanicName, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        Text("on Unit:", color = TextSecondary, fontSize = 12.sp)
                                        Text(
                                            entry.registrationNumber.uppercase(),
                                            color = GlacierBlue,
                                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                    }

                                    Text(
                                        text = entry.noteContent,
                                        color = TextPrimary,
                                        fontSize = 13.sp,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                            }
                            is AdminLogEntry.SignOffReport -> {
                                GlassCard(
                                    modifier = Modifier.fillMaxWidth(),
                                    borderStroke = BorderStroke(1.dp, MintSage.copy(alpha = 0.2f))
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "JOB SIGNED OFF & ARCHIVED",
                                            color = MintSage,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp,
                                            letterSpacing = 1.sp
                                        )
                                        Text(
                                            text = entry.timestamp,
                                            color = TextSecondary,
                                            fontSize = 10.sp
                                        )
                                    }

                                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0x1AFFFFFF)))

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text("Admin/Mechanic:", color = TextSecondary, fontSize = 12.sp)
                                        Text(entry.mechanicName, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        Text("on Unit:", color = TextSecondary, fontSize = 12.sp)
                                        Text(
                                            entry.registrationNumber.uppercase(),
                                            color = GlacierBlue,
                                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "COMPLETED TASKS:",
                                        color = GlacierBlue,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                    Text(
                                        text = entry.tasksCompleted,
                                        color = TextPrimary,
                                        fontSize = 12.sp
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "FINAL COMMENTS:",
                                        color = GlacierBlue,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                    Text(
                                        text = entry.notesWritten,
                                        color = TextSecondary,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminAnalyticsTab(jobStats: Map<String, Int>, mechanicLeaderboard: List<Pair<String, Int>>) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Text("JOB STATUS BREAKDOWN", fontWeight = FontWeight.Bold, color = LaserCyan)
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MetricChip("Pending", jobStats["Pending"] ?: 0, AlertPink)
                MetricChip("In Progress", jobStats["In Progress"] ?: 0, NeonTangerine)
                MetricChip("Complete", jobStats["Complete"] ?: 0, NeonGreen)
            }
        }

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Text("MECHANIC LEADERBOARD", fontWeight = FontWeight.Bold, color = LaserCyan)
            if (mechanicLeaderboard.isEmpty()) {
                Text("No data available.", color = TextSecondary, modifier = Modifier.padding(top = 8.dp))
            } else {
                Column(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    mechanicLeaderboard.forEachIndexed { index, (name, count) ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("#${index + 1}", color = if (index == 0) ChampagneGold else TextSecondary, fontWeight = FontWeight.Bold)
                                Text(name, color = TextPrimary)
                            }
                            Text("$count Jobs", color = LaserCyan, fontWeight = FontWeight.Bold)
                        }
                        if (index < mechanicLeaderboard.lastIndex) {
                            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0x1AFFFFFF)))
                        }
                    }
                }
            }
        }
    }
}
