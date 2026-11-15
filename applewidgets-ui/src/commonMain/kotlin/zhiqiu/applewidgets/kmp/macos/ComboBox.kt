package zhiqiu.applewidgets.kmp.macos

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.basic.Text
import zhiqiu.applewidgets.kmp.overlay.PlatformAnchoredPopup
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

@Composable
fun ComboBox(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    var expanded by remember { mutableStateOf(false) }
    val colors = AppleWidgetsTheme.colorScheme
    val shape = RoundedCornerShape(6.dp)
    val label = options.getOrNull(selectedIndex) ?: ""
    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .defaultMinSize(minHeight = 28.dp)
                .clip(shape)
                .background(if (AppleWidgetsTheme.isDark) colors.tertiaryFill else colors.secondarySystemBackground)
                .border(0.5.dp, colors.separator, shape)
                .clickable(enabled = enabled) { expanded = true }
                .padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                style = AppleWidgetsTheme.textStyles.callout,
                color = if (enabled) colors.label else colors.tertiaryLabel,
                modifier = Modifier.weight(1f, fill = false).padding(end = 8.dp),
            )
            Text("▾", style = AppleWidgetsTheme.textStyles.caption1, color = colors.secondaryLabel)
        }
        if (expanded) {
            PlatformAnchoredPopup(onDismissRequest = { expanded = false }) {
                Column(
                    Modifier
                        .fillMaxWidth(0.5f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(colors.secondarySystemBackground)
                        .border(0.5.dp, colors.separator, RoundedCornerShape(8.dp)),
                ) {
                    options.forEachIndexed { index, option ->
                        Text(
                            text = option,
                            style = AppleWidgetsTheme.textStyles.callout,
                            color = if (index == selectedIndex) colors.accent else colors.label,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSelect(index)
                                    expanded = false
                                }
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                        )
                    }
                }
            }
        }
    }
}
