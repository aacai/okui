package zhiqiu.applewidgets.kmp.shape

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Continuous corner 开关。完整 squircle 路径后续可替换；
 * 当前统一走 [RoundedCornerShape] 作为视觉降级实现。
 */
val LocalContinuousCornerEnabled = staticCompositionLocalOf { true }

fun continuousCornerShape(radius: Dp): Shape = RoundedCornerShape(radius)

object ContinuousCornerDefaults {
    val Card = 10.dp
    val Button = 12.dp
    val Sheet = 12.dp
    val Alert = 14.dp
    val Chip = 16.dp
}

@Composable
fun rememberContinuousCornerShape(radius: Dp): Shape = continuousCornerShape(radius)
