package zhiqiu.applewidgets.kmp.theme

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 动效基线：时长、缓动、弹簧。
 *
 * 与颜色 / 形状不同，动效属于组件行为规范而非皮肤外观，所有主题共用一份，
 * 避免"每套皮肤各写一份动效常量"的重复。皮肤若要差异化节奏，只在组件内覆盖。
 */
object Motion {
    const val Dur1 = 90
    const val Dur2 = 150
    const val Dur3 = 240
    const val Dur4 = 360
    const val Dur5 = 520

    val EaseOut = CubicBezierEasing(0.22f, 1f, 0.36f, 1f)
    val EaseIn = CubicBezierEasing(0.55f, 0f, 1f, 0.45f)
    val EaseInOut = CubicBezierEasing(0.65f, 0f, 0.35f, 1f)
    val EaseStandard = CubicBezierEasing(0.2f, 0f, 0f, 1f)

    /** stiffness 170 · dampingRatio 1 — 无过冲，落位。 */
    fun <T> soft(threshold: T? = null): SpringSpec<T> =
        spring(dampingRatio = 1f, stiffness = 170f, visibilityThreshold = threshold)

    /** stiffness 260 · dampingRatio 0.744 — 过冲 3%，位移基准。 */
    fun <T> spring(threshold: T? = null): SpringSpec<T> =
        spring(dampingRatio = 0.744f, stiffness = 260f, visibilityThreshold = threshold)

    /** stiffness 520 · dampingRatio 0.745 — 过冲 3%，更快。 */
    fun <T> snappy(threshold: T? = null): SpringSpec<T> =
        spring(dampingRatio = 0.745f, stiffness = 520f, visibilityThreshold = threshold)

    /** stiffness 300 · dampingRatio 0.52 — 过冲 15%，按压回弹 / 图标弹出。 */
    fun <T> bouncy(threshold: T? = null): SpringSpec<T> =
        spring(dampingRatio = 0.52f, stiffness = 300f, visibilityThreshold = threshold)

    val DpThreshold: Dp = 0.5.dp
}
