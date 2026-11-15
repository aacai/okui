package zhiqiu.applewidgets.kmp.basic

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

/**
 * 简化 ColorPicker：色板 + RGBA 滑块。
 */
@Composable
fun ColorPicker(
    color: Color,
    onColorChange: (Color) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(color)
                    .border(1.dp, AppleWidgetsTheme.colorScheme.separator, CircleShape),
            )
            Text(
                text = "#${color.toHex()}",
                style = AppleWidgetsTheme.textStyles.callout,
                color = AppleWidgetsTheme.colorScheme.label,
            )
        }
        ColorPalette(
            colors = ColorPaletteDefaults.SystemSwatches,
            selected = color,
            onSelect = onColorChange,
        )
        ChannelSlider("R", color.red) { onColorChange(color.copy(red = it)) }
        ChannelSlider("G", color.green) { onColorChange(color.copy(green = it)) }
        ChannelSlider("B", color.blue) { onColorChange(color.copy(blue = it)) }
        ChannelSlider("A", color.alpha) { onColorChange(color.copy(alpha = it)) }
    }
}

@Composable
fun ColorWell(
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(28.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(color)
            .border(1.dp, AppleWidgetsTheme.colorScheme.opaqueSeparator, RoundedCornerShape(6.dp))
            .clickable(onClick = onClick),
    )
}

@Composable
private fun ChannelSlider(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(label, style = AppleWidgetsTheme.textStyles.footnote, color = AppleWidgetsTheme.colorScheme.secondaryLabel)
        Slider(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = (value * 255).toInt().toString(),
            style = AppleWidgetsTheme.textStyles.caption1,
            color = AppleWidgetsTheme.colorScheme.secondaryLabel,
            modifier = Modifier.padding(start = 4.dp),
        )
    }
}

private fun Color.toHex(): String {
    fun ch(v: Float) = ((v * 255).toInt() and 0xFF).toString(16).padStart(2, '0')
    return (ch(red) + ch(green) + ch(blue)).uppercase()
}
