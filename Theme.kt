package com.valentinesgarage.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Sophisticated Technical Color System
val MidnightObsidian = Color(0xFF07080D)
val GlacierBlue = Color(0xFF80E5FF)
val ChampagneGold = Color(0xFFDFBA73)
val DeepCrimson = Color(0xFFE57385)
val MintSage = Color(0xFF81C798)

// Map existing aliases to the new color scheme
val CosmicDark = MidnightObsidian
val GlassBody = Color(0x12FFFFFF)
val GlassBodyDark = Color(0x4007080D)
val GlassStroke = Color(0x2BFFFFFF)
val LaserCyan = GlacierBlue
val ElectricViolet = Color(0xFF8A2BE2)
val NeonTangerine = ChampagneGold
val AlertPink = DeepCrimson
val NeonGreen = MintSage

val CarbonDark = Color(0x2A181C26)
val CharcoalGray = CosmicDark
val HeavySteel = Color(0x1D263238)
val LightSteel = Color(0x40FFFFFF)

val SafetyOrange = NeonTangerine
val SafetyAmber = ChampagneGold
val LaserTeal = LaserCyan
val CautionRed = AlertPink

val TextPrimary = Color(0xFFECEFF1)
val TextSecondary = Color(0xFF90A4AE)

private val IndustrialDarkColorScheme = darkColorScheme(
    primary = LaserCyan,
    onPrimary = CosmicDark,
    primaryContainer = HeavySteel,
    onPrimaryContainer = TextPrimary,
    secondary = LaserTeal,
    onSecondary = CosmicDark,
    background = CosmicDark,
    onBackground = TextPrimary,
    surface = CarbonDark,
    onSurface = TextPrimary,
    surfaceVariant = HeavySteel,
    onSurfaceVariant = TextSecondary,
    error = CautionRed,
    onError = TextPrimary
)

val IndustrialTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        letterSpacing = 0.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        letterSpacing = 0.15.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.25.sp
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        letterSpacing = 1.25.sp
    )
)

@Composable
fun ValentineGarageTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = IndustrialDarkColorScheme,
        typography = IndustrialTypography,
        content = content
    )
}
