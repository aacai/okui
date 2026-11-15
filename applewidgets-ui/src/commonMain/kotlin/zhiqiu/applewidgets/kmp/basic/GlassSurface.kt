package zhiqiu.applewidgets.kmp.basic

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.platform.AppleCapabilities
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

/**
 * 毛玻璃表面：有真实 blur 能力时再接 RenderEffect；
 * 当前无能力则半透明填充降级（全端可用）。
 */
@Composable
fun GlassSurface(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = GlassSurfaceDefaults.CornerRadius,
    colors: GlassSurfaceColors = GlassSurfaceDefaults.colors(),
    content: @Composable () -> Unit,
) {
    val shape = RoundedCornerShape(cornerRadius)
    // backdropBlur == true 时后续可挂平台 blur；现阶段统一半透明降级
    val container = if (AppleCapabilities.backdropBlur) {
        colors.containerColor
    } else {
        colors.containerColor
    }
    Box(
        modifier = modifier
            .clip(shape)
            .background(container)
            .border(0.5.dp, colors.borderColor, shape),
    ) {
        content()
    }
}

object GlassSurfaceDefaults {
    val CornerRadius = 16.dp

    @Composable
    fun colors(
        containerColor: Color = if (AppleWidgetsTheme.isDark) {
            Color.White.copy(alpha = 0.12f)
        } else {
            Color.White.copy(alpha = 0.72f)
        },
        borderColor: Color = AppleWidgetsTheme.colorScheme.separator,
    ): GlassSurfaceColors = GlassSurfaceColors(containerColor, borderColor)
}

@Immutable
data class GlassSurfaceColors(
    val containerColor: Color,
    val borderColor: Color,
)
