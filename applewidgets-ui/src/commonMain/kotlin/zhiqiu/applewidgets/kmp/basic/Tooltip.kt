package zhiqiu.applewidgets.kmp.basic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

@Composable
fun Tooltip(
    text: String,
    modifier: Modifier = Modifier,
    colors: TooltipColors = TooltipDefaults.colors(),
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(colors.containerColor)
            .padding(horizontal = 10.dp, vertical = 6.dp),
    ) {
        Text(
            text = text,
            style = AppleWidgetsTheme.textStyles.caption1,
            color = colors.contentColor,
        )
    }
}

object TooltipDefaults {
    @Composable
    fun colors(
        containerColor: Color = if (AppleWidgetsTheme.isDark) Color(0xFF636366) else Color(0xFF3A3A3C),
        contentColor: Color = Color.White,
    ): TooltipColors = TooltipColors(containerColor, contentColor)
}

@Immutable
data class TooltipColors(
    val containerColor: Color,
    val contentColor: Color,
)
