package zhiqiu.applewidgets.kmp.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 组件级样式令牌：形状 + 尺寸。
 *
 * 与 [Colors] / [TextStyles] 同属主题令牌，由主题（皮肤）注入，组件只读取不写死。
 * 默认值即 iOS 观感；换皮肤（例如 Oink）时由皮肤提供超椭圆 / 胶囊形状与紧凑尺寸，
 * 组件无需替换即可呈现新风格。
 */
@Immutable
data class ComponentStyles(
    val buttonShape: Shape = RoundedCornerShape(12.dp),
    val buttonMinHeight: Dp = 50.dp,
    val cardShape: Shape = RoundedCornerShape(10.dp),
    val controlShape: Shape = RoundedCornerShape(10.dp),
    val controlMinHeight: Dp = 36.dp,
    val chipShape: Shape = RoundedCornerShape(16.dp),
    val chipMinHeight: Dp = 32.dp,
    val overlayShape: Shape = RoundedCornerShape(14.dp),
    val sheetShape: Shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
)

internal val LocalComponentStyles = staticCompositionLocalOf { ComponentStyles() }
