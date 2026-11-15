package zhiqiu.applewidgets.kmp.macos

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.basic.Separator
import zhiqiu.applewidgets.kmp.basic.Text
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

@Composable
fun Stepper(
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: IntRange = 0..100,
    enabled: Boolean = true,
    showValue: Boolean = true,
) {
    val colors = AppleWidgetsTheme.colorScheme
    val shape = RoundedCornerShape(6.dp)
    val bg = if (AppleWidgetsTheme.isDark) androidx.compose.ui.graphics.Color(0xFF3A3A3C) else androidx.compose.ui.graphics.Color(0xFFF6F6F6)
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        if (showValue) {
            Text(
                text = value.toString(),
                style = AppleWidgetsTheme.textStyles.body,
                color = colors.label,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .width(40.dp)
                    .padding(end = 8.dp),
            )
        }
        Column(
            modifier = Modifier
                .defaultMinSize(minWidth = 22.dp)
                .clip(shape)
                .background(bg)
                .border(0.5.dp, colors.opaqueSeparator, shape),
        ) {
            Text(
                text = "▴",
                style = AppleWidgetsTheme.textStyles.caption2,
                color = if (enabled && value < valueRange.last) colors.label else colors.tertiaryLabel,
                modifier = Modifier
                    .clickable(enabled = enabled && value < valueRange.last) {
                        onValueChange((value + 1).coerceAtMost(valueRange.last))
                    }
                    .padding(horizontal = 6.dp, vertical = 2.dp),
            )
            Separator(startIndent = 0.dp)
            Text(
                text = "▾",
                style = AppleWidgetsTheme.textStyles.caption2,
                color = if (enabled && value > valueRange.first) colors.label else colors.tertiaryLabel,
                modifier = Modifier
                    .clickable(enabled = enabled && value > valueRange.first) {
                        onValueChange((value - 1).coerceAtLeast(valueRange.first))
                    }
                    .padding(horizontal = 6.dp, vertical = 2.dp),
            )
        }
    }
}
