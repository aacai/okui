package zhiqiu.applewidgets.kmp.oink.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * 配色模式：跟随系统、强制浅色、强制深色。
 */
@Stable
enum class OinkColorSchemeMode { System, Light, Dark }

/**
 * Oink 主题控制器：持有明暗模式与强调色的可变状态，组件外部（如设置页）可直接修改，
 * 修改后经 [OinkTheme] 的 240ms 交叉淡入生效，无需重建组合树。
 *
 * 示例：
 * ```kotlin
 * val controller = rememberOinkThemeController()
 * OinkTheme(controller) { App() }
 * // 任意位置：
 * controller.accent = OinkAccent.Blue
 * controller.colorSchemeMode = OinkColorSchemeMode.Dark
 * ```
 */
@Stable
class OinkThemeController(
    colorSchemeMode: OinkColorSchemeMode = OinkColorSchemeMode.System,
    accent: OinkAccent = OinkAccent.Pink,
    /** System 模式下的明暗覆写；null 跟随系统。 */
    isDark: Boolean? = null,
) {
    var colorSchemeMode by mutableStateOf(colorSchemeMode)
    var accent by mutableStateOf(accent)
    var isDark by mutableStateOf(isDark)

    @Composable
    fun currentColors(): OinkColors = when (colorSchemeMode) {
        OinkColorSchemeMode.System -> {
            val dark = isDark ?: isSystemInDarkTheme()
            if (dark) oinkDarkColors(accent) else oinkLightColors(accent)
        }
        OinkColorSchemeMode.Light -> oinkLightColors(accent)
        OinkColorSchemeMode.Dark -> oinkDarkColors(accent)
    }
}

/** 创建并记住一个控制器，生命周期内状态跨重组保留。 */
@Composable
fun rememberOinkThemeController(
    colorSchemeMode: OinkColorSchemeMode = OinkColorSchemeMode.System,
    accent: OinkAccent = OinkAccent.Pink,
): OinkThemeController = remember { OinkThemeController(colorSchemeMode, accent) }

/** 当前作用域内的控制器；未包 [OinkTheme] 时为 null，此时读 [OinkTheme.colors] 的默认浅色即可。 */
val LocalOinkThemeController = staticCompositionLocalOf<OinkThemeController?> { null }
