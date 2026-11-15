package zhiqiu.applewidgets.kmp.basic

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

/**
 * 分组小标题（Gallery Section 上方）。
 */
@Composable
fun SmallTitle(
    text: String,
    modifier: Modifier = Modifier,
    colors: SmallTitleColors = SmallTitleDefaults.colors(),
) {
    Text(
        text = text,
        style = AppleWidgetsTheme.textStyles.footnote,
        color = colors.color,
        modifier = modifier.padding(horizontal = 32.dp, vertical = 8.dp),
    )
}

object SmallTitleDefaults {
    @Composable
    fun colors(
        color: Color = AppleWidgetsTheme.colorScheme.secondaryLabel,
    ): SmallTitleColors = SmallTitleColors(color = color)
}

@Immutable
data class SmallTitleColors(
    val color: Color,
) {
    @Stable
    fun color(): Color = color
}
