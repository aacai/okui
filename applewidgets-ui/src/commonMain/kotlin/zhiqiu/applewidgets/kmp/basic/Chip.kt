package zhiqiu.applewidgets.kmp.basic

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme
import zhiqiu.applewidgets.kmp.theme.LocalContentColor

@Composable
fun Chip(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    enabled: Boolean = true,
    colors: ChipColors = ChipDefaults.colors(selected),
) {
    val styles = AppleWidgetsTheme.componentStyles
    val shape = styles.chipShape
    val container = if (enabled) colors.containerColor else colors.disabledContainerColor
    val content = if (enabled) colors.contentColor else colors.disabledContentColor
    Row(
        modifier = modifier
            .defaultMinSize(minHeight = styles.chipMinHeight)
            .clip(shape)
            .background(container)
            .then(
                if (!selected) Modifier.border(1.dp, colors.borderColor, shape) else Modifier,
            )
            .clickable(enabled = enabled, onClick = onClick)
            .padding(ChipDefaults.InsideMargin),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        androidx.compose.runtime.CompositionLocalProvider(LocalContentColor provides content) {
            Text(text = text, style = AppleWidgetsTheme.textStyles.footnote, color = content)
        }
    }
}

object ChipDefaults {
    val InsideMargin = PaddingValues(horizontal = 12.dp, vertical = 6.dp)

    @Composable
    fun colors(selected: Boolean): ChipColors {
        val c = AppleWidgetsTheme.colorScheme
        return if (selected) {
            ChipColors(
                containerColor = c.accent,
                contentColor = c.onAccent,
                borderColor = Color.Transparent,
                disabledContainerColor = c.fill,
                disabledContentColor = c.tertiaryLabel,
            )
        } else {
            ChipColors(
                containerColor = Color.Transparent,
                contentColor = c.label,
                borderColor = c.opaqueSeparator,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = c.tertiaryLabel,
            )
        }
    }
}

@Immutable
data class ChipColors(
    val containerColor: Color,
    val contentColor: Color,
    val borderColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color,
)
