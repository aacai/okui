package zhiqiu.applewidgets.kmp.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Stable
enum class ColorSchemeMode {
    System,
    Light,
    Dark,
}

@Stable
class ThemeController(
    colorSchemeMode: ColorSchemeMode = ColorSchemeMode.System,
    lightColors: Colors = lightColorScheme(),
    darkColors: Colors = darkColorScheme(),
    accent: Color? = null,
    isDark: Boolean? = null,
) {
    var colorSchemeMode by mutableStateOf(colorSchemeMode)
    var lightColors by mutableStateOf(if (accent != null) lightColors.copy(accent = accent, link = accent) else lightColors)
    var darkColors by mutableStateOf(if (accent != null) darkColors.copy(accent = accent, link = accent) else darkColors)
    var accent by mutableStateOf(accent)
    var isDarkOverride by mutableStateOf(isDark)

    @Composable
    fun currentColors(): Colors {
        val systemDark = isSystemInDarkTheme()
        val dark = isDarkOverride ?: when (colorSchemeMode) {
            ColorSchemeMode.Light -> false
            ColorSchemeMode.Dark -> true
            ColorSchemeMode.System -> systemDark
        }
        return if (dark) darkColors else lightColors
    }
}

internal val LocalColorSchemeMode = staticCompositionLocalOf<ColorSchemeMode?> { null }
