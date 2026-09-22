package com.monstermind.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * MonsterMind Material 3 Theme
 *
 * Supports:
 * - Light mode
 * - Dark mode
 * - Dynamic theming (Android 12+, uses system colors)
 * - Smooth transitions between modes
 */

/**
 * Light color scheme for MonsterMind.
 */
private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = NeutralWhite,
    primaryContainer = PrimaryLight,
    onPrimaryContainer = PrimaryDark,

    secondary = Secondary,
    onSecondary = NeutralWhite,
    secondaryContainer = SecondaryLight,
    onSecondaryContainer = SecondaryDark,

    tertiary = Tertiary,
    onTertiary = NeutralWhite,
    tertiaryContainer = TertiaryLight,
    onTertiaryContainer = TertiaryDark,

    error = Error,
    onError = ErrorDark,
    errorContainer = ErrorLight,
    onErrorContainer = ErrorDark,

    background = NeutralWhite,
    onBackground = NeutralDark,

    surface = NeutralGray10,
    onSurface = NeutralDark,

    outline = NeutralGray50,
    outlineVariant = NeutralGray70
)

/**
 * Dark color scheme for MonsterMind.
 */
private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    onPrimary = NeutralDark,
    primaryContainer = PrimaryDark,
    onPrimaryContainer = PrimaryLight,

    secondary = Secondary,
    onSecondary = NeutralDark,
    secondaryContainer = SecondaryDark,
    onSecondaryContainer = SecondaryLight,

    tertiary = Tertiary,
    onTertiary = NeutralDark,
    tertiaryContainer = TertiaryDark,
    onTertiaryContainer = TertiaryLight,

    error = Error,
    onError = ErrorDark,
    errorContainer = ErrorDark,
    onErrorContainer = ErrorLight,

    background = NeutralDark,
    onBackground = NeutralWhite,

    surface = NeutralGray90,
    onSurface = NeutralWhite,

    outline = NeutralGray60,
    outlineVariant = NeutralGray50
)

/**
 * MonsterMind Theme Composable.
 *
 * Applies Material 3 theming to all child composables.
 *
 * Features:
 * - Automatic dark/light mode detection
 * - Dynamic theming on Android 12+ (uses system colors)
 * - Fallback to custom color scheme on older devices
 * - Consistent typography
 *
 * Usage:
 * @Composable
 * fun MyApp() {
 *   MonsterMindTheme {
 *     // Your content here
 *   }
 * }
 *
 * @param darkTheme Whether to use dark theme (null = auto-detect from system)
 * @param dynamicColor Whether to use dynamic theming (Android 12+ only)
 * @param content The content to theme
 */
@Composable
fun MonsterMindTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        // Dynamic theming: Android 12+ uses system colors
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) {
                dynamicDarkColorScheme(context)
            } else {
                dynamicLightColorScheme(context)
            }
        }
        // Fallback: Use custom color scheme
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}