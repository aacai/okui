package zhiqiu.applewidgets.kmp.basic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.draw.clip
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme
import zhiqiu.applewidgets.kmp.theme.LocalContentColor

@Composable
fun Surface(
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    colors: SurfaceColors = SurfaceDefaults.surfaceColors(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalContentColor provides colors.contentColor) {
        Box(
            modifier = modifier
                .clip(shape)
                .background(colors.containerColor),
        ) {
            content()
        }
    }
}

object SurfaceDefaults {
    @Composable
    fun surfaceColors(
        containerColor: Color = AppleWidgetsTheme.colorScheme.systemBackground,
        contentColor: Color = AppleWidgetsTheme.colorScheme.label,
    ): SurfaceColors = SurfaceColors(containerColor, contentColor)
}

@Immutable
data class SurfaceColors(
    val containerColor: Color,
    val contentColor: Color,
)
