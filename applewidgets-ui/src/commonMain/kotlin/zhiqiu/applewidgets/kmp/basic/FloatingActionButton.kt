package zhiqiu.applewidgets.kmp.basic

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme
import zhiqiu.applewidgets.kmp.theme.LocalContentColor

@Composable
fun FloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = FloatingActionButtonDefaults.Size,
    colors: FloatingActionButtonColors = FloatingActionButtonDefaults.colors(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalContentColor provides colors.contentColor) {
        Box(
            modifier = modifier
                .size(size)
                .shadow(6.dp, CircleShape)
                .clip(CircleShape)
                .background(colors.containerColor)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center,
        ) {
            content()
        }
    }
}

object FloatingActionButtonDefaults {
    val Size = 56.dp

    @Composable
    fun colors(
        containerColor: Color = AppleWidgetsTheme.colorScheme.accent,
        contentColor: Color = AppleWidgetsTheme.colorScheme.onAccent,
    ): FloatingActionButtonColors = FloatingActionButtonColors(containerColor, contentColor)
}

@Immutable
data class FloatingActionButtonColors(
    val containerColor: Color,
    val contentColor: Color,
)
