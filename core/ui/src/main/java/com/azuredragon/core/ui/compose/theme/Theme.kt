package com.azuredragon.core.ui.compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val LightColorPalette = ThemeColors(
    brand = NeonBlue,
    brandSecondary = NeonBlue,
    uiBackground = White,
    uiBorder = Transparent,
    textPrimary = Biscay,
    textSecondary = RockBlue,
    iconPrimary = NeonBlue,
    iconSecondary = NeonBlue,
    iconInteractive = Gainsboro,
    iconInteractiveInactive = Gainsboro,
    error = VenetianRed,
    isDark = false,
)

private val DarkColorPalette = ThemeColors(
    brand = NeonBlue,
    brandSecondary = NeonBlue,
    uiBackground = White,
    uiBorder = Transparent,
    textPrimary = Biscay,
    textSecondary = RockBlue,
    iconPrimary = NeonBlue,
    iconSecondary = NeonBlue,
    iconInteractive = Gainsboro,
    iconInteractiveInactive = Gainsboro,
    error = VenetianRed,
    isDark = false,
)

/**
 * Custom Color Palette
 */
@Stable
class ThemeColors(
    brand: Color,
    brandSecondary: Color,
    uiBackground: Color,
    uiBorder: Color,
    textPrimary: Color,
    textSecondary: Color,
    iconPrimary: Color = brand,
    iconSecondary: Color,
    iconInteractive: Color,
    iconInteractiveInactive: Color,
    error: Color,
    notificationBadge: Color = error,
    isDark: Boolean
) {
    var brand by mutableStateOf(brand)
        private set
    var brandSecondary by mutableStateOf(brandSecondary)
        private set
    var uiBackground by mutableStateOf(uiBackground)
        private set
    var uiBorder by mutableStateOf(uiBorder)
        private set
    var textPrimary by mutableStateOf(textPrimary)
        private set
    var textSecondary by mutableStateOf(textSecondary)
        private set
    var iconPrimary by mutableStateOf(iconPrimary)
        private set
    var iconSecondary by mutableStateOf(iconSecondary)
        private set
    var iconInteractive by mutableStateOf(iconInteractive)
        private set
    var iconInteractiveInactive by mutableStateOf(iconInteractiveInactive)
        private set
    var error by mutableStateOf(error)
        private set
    var notificationBadge by mutableStateOf(notificationBadge)
        private set
    var isDark by mutableStateOf(isDark)
        private set

    fun update(other: ThemeColors) {
        brand = other.brand
        brandSecondary = other.brandSecondary
        uiBackground = other.uiBackground
        uiBorder = other.uiBorder
        textPrimary = other.textPrimary
        textSecondary = other.textSecondary
        iconPrimary = other.iconPrimary
        iconSecondary = other.iconSecondary
        iconInteractive = other.iconInteractive
        iconInteractiveInactive = other.iconInteractiveInactive
        error = other.error
        notificationBadge = other.notificationBadge
        isDark = other.isDark
    }

    fun copy(): ThemeColors = ThemeColors(
        brand = brand,
        brandSecondary = brandSecondary,
        uiBackground = uiBackground,
        uiBorder = uiBorder,
        textPrimary = textPrimary,
        textSecondary = textSecondary,
        iconPrimary = iconPrimary,
        iconSecondary = iconSecondary,
        iconInteractive = iconInteractive,
        iconInteractiveInactive = iconInteractiveInactive,
        error = error,
        notificationBadge = notificationBadge,
        isDark = isDark,
    )
}

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    systemBarColor: Color = Color.Unspecified,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette

    val sysUiController = rememberSystemUiController()
    SideEffect {
        val finalizedSystemBarColor = if (systemBarColor == Color.Unspecified) {
            colors.uiBackground.copy(alpha = AlphaNearOpaque)
        } else {
            systemBarColor
        }

        sysUiController.setSystemBarsColor(color = finalizedSystemBarColor)
    }

    ProvideThemeColors(colors) {
        MaterialTheme(
            colors = debugColors(darkTheme),
            typography = MaterialTypography,
            shapes = Shapes,
            content = content
        )
    }
}

object AppTheme {
    val colors: ThemeColors
        @Composable
        get() = LocalThemeColors.current
}

@Composable
fun ProvideThemeColors(
    colors: ThemeColors,
    content: @Composable () -> Unit
) {
    val colorPalette = remember {
        // Explicitly creating a new object here so we don't mutate the initial [colors]
        // provided, and overwrite the values set in it.
        colors.copy()
    }
    colorPalette.update(colors)
    CompositionLocalProvider(LocalThemeColors provides colorPalette, content = content)
}

private val LocalThemeColors = staticCompositionLocalOf<ThemeColors> {
    error("No ThemeColorPalette provided")
}

/**
 * A Material [Colors] implementation which sets all colors to [debugColor] to discourage usage of
 * [MaterialTheme.colors] in preference to [AppTheme.colors].
 */
fun debugColors(
    darkTheme: Boolean,
    debugColor: Color = Color.Red
) = Colors(
    primary = debugColor,
    primaryVariant = debugColor,
    secondary = debugColor,
    secondaryVariant = debugColor,
    background = debugColor,
    surface = debugColor,
    error = debugColor,
    onPrimary = debugColor,
    onSecondary = debugColor,
    onBackground = debugColor,
    onSurface = debugColor,
    onError = debugColor,
    isLight = !darkTheme
)
