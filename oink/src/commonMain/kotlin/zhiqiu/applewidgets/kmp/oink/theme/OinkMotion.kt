package zhiqiu.applewidgets.kmp.oink.theme

import zhiqiu.applewidgets.kmp.oink.basic.*
import zhiqiu.applewidgets.kmp.oink.data.*
import zhiqiu.applewidgets.kmp.oink.form.*
import zhiqiu.applewidgets.kmp.oink.layout.*
import zhiqiu.applewidgets.kmp.oink.nav.*
import zhiqiu.applewidgets.kmp.oink.overlay.*
import zhiqiu.applewidgets.kmp.oink.primitives.*
import zhiqiu.applewidgets.kmp.oink.responsive.*

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Oink 动效令牌。弹簧参数直接对应 oink-ui 的 linear() 弹簧
 * （stiffness / damping 换算成 Compose 的 stiffness / dampingRatio）。
 */
object OinkMotion {
    const val Dur1 = 90
    const val Dur2 = 150
    const val Dur3 = 240
    const val Dur4 = 360
    const val Dur5 = 520
    const val FloatIn = 303
    const val FloatFade = 100
    const val FloatOut = 160

    val EaseOut = CubicBezierEasing(0.22f, 1f, 0.36f, 1f)
    val EaseIn = CubicBezierEasing(0.55f, 0f, 1f, 0.45f)
    val EaseInOut = CubicBezierEasing(0.65f, 0f, 0.35f, 1f)
    val EaseStandard = CubicBezierEasing(0.2f, 0f, 0f, 1f)

    /** stiffness 170 · damping 26 — 无过冲，落位。 */
    fun <T> soft(threshold: T? = null): SpringSpec<T> =
        spring(dampingRatio = 1f, stiffness = 170f, visibilityThreshold = threshold)

    /** stiffness 260 · damping 24 — 过冲 3%，滑块 / 位移基准。 */
    fun <T> spring(threshold: T? = null): SpringSpec<T> =
        spring(dampingRatio = 0.744f, stiffness = 260f, visibilityThreshold = threshold)

    /** stiffness 520 · damping 34 — 过冲 3%，更快。 */
    fun <T> snappy(threshold: T? = null): SpringSpec<T> =
        spring(dampingRatio = 0.745f, stiffness = 520f, visibilityThreshold = threshold)

    /** stiffness 300 · damping 18 — 过冲 15%，按压回弹 / 图标弹出。 */
    fun <T> bouncy(threshold: T? = null): SpringSpec<T> =
        spring(dampingRatio = 0.52f, stiffness = 300f, visibilityThreshold = threshold)

    /** stiffness 180 · damping 9 — 过冲 33%，仅品牌时刻。 */
    fun <T> wobbly(threshold: T? = null): SpringSpec<T> =
        spring(dampingRatio = 0.335f, stiffness = 180f, visibilityThreshold = threshold)

    val DpThreshold: Dp = 0.5.dp
}