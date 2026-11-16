package zhiqiu.applewidgets.kmp.oink.theme

import zhiqiu.applewidgets.kmp.oink.basic.*
import zhiqiu.applewidgets.kmp.oink.data.*
import zhiqiu.applewidgets.kmp.oink.form.*
import zhiqiu.applewidgets.kmp.oink.layout.*
import zhiqiu.applewidgets.kmp.oink.nav.*
import zhiqiu.applewidgets.kmp.oink.overlay.*
import zhiqiu.applewidgets.kmp.oink.primitives.*
import zhiqiu.applewidgets.kmp.oink.responsive.*

import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring
import zhiqiu.applewidgets.kmp.theme.Motion

/**
 * Oink 动效别名：转发到组件库的 [Motion] 基线，避免两套动效常量各写一份。
 * 只保留 Oink 品牌专属的 [wobbly]。
 */
object OinkMotion {
    const val Dur1 = Motion.Dur1
    const val Dur2 = Motion.Dur2
    const val Dur3 = Motion.Dur3
    const val Dur4 = Motion.Dur4
    const val Dur5 = Motion.Dur5
    const val FloatIn = 303
    const val FloatFade = 100
    const val FloatOut = 160

    val EaseOut = Motion.EaseOut
    val EaseIn = Motion.EaseIn
    val EaseInOut = Motion.EaseInOut
    val EaseStandard = Motion.EaseStandard

    /** stiffness 170 · damping 26 — 无过冲，落位。 */
    fun <T> soft(threshold: T? = null): SpringSpec<T> = Motion.soft(threshold)

    /** stiffness 260 · damping 24 — 过冲 3%，滑块 / 位移基准。 */
    fun <T> spring(threshold: T? = null): SpringSpec<T> = Motion.spring(threshold)

    /** stiffness 520 · damping 34 — 过冲 3%，更快。 */
    fun <T> snappy(threshold: T? = null): SpringSpec<T> = Motion.snappy(threshold)

    /** stiffness 300 · damping 18 — 过冲 15%，按压回弹 / 图标弹出。 */
    fun <T> bouncy(threshold: T? = null): SpringSpec<T> = Motion.bouncy(threshold)

    /** stiffness 180 · damping 9 — 过冲 33%，仅品牌时刻。 */
    fun <T> wobbly(threshold: T? = null): SpringSpec<T> =
        spring(dampingRatio = 0.335f, stiffness = 180f, visibilityThreshold = threshold)

    val DpThreshold = Motion.DpThreshold
}