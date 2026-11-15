package zhiqiu.applewidgets.kmp.macos

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.basic.Separator
import zhiqiu.applewidgets.kmp.basic.Text
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

data class MenuItemData(
    val title: String,
    val shortcut: String? = null,
    val enabled: Boolean = true,
    val destructive: Boolean = false,
    val onClick: () -> Unit = {},
)

@Composable
fun MenuPanel(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = AppleWidgetsTheme.colorScheme
    val shape = RoundedCornerShape(8.dp)
    Column(
        modifier = modifier
            .widthIn(min = 160.dp, max = 280.dp)
            .shadow(8.dp, shape)
            .clip(shape)
            .background(colors.secondarySystemGroupedBackground)
            .border(0.5.dp, colors.separator, shape)
            .padding(vertical = 4.dp),
        content = content,
    )
}

@Composable
fun MenuItem(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shortcut: String? = null,
    enabled: Boolean = true,
    destructive: Boolean = false,
    selected: Boolean = false,
) {
    val colors = AppleWidgetsTheme.colorScheme
    val fg = when {
        !enabled -> colors.tertiaryLabel
        destructive -> colors.destructive
        selected -> colors.onAccent
        else -> colors.label
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(if (selected) colors.accent else androidx.compose.ui.graphics.Color.Transparent)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = title, style = AppleWidgetsTheme.textStyles.callout, color = fg, modifier = Modifier.weight(1f))
        if (shortcut != null) {
            Spacer(Modifier.width(16.dp))
            Text(text = shortcut, style = AppleWidgetsTheme.textStyles.caption1, color = if (selected) colors.onAccent.copy(0.8f) else colors.secondaryLabel)
        }
    }
}

@Composable
fun MenuDivider(modifier: Modifier = Modifier) {
    Separator(modifier = modifier.padding(vertical = 4.dp), startIndent = 0.dp)
}

@Composable
fun DropdownMenuButton(
    label: String,
    items: List<MenuItemData>,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        PushButton(
            text = "$label ▾",
            onClick = { expanded = !expanded },
            style = PushButtonStyle.Regular,
        )
        if (expanded) {
            Box(modifier = Modifier.padding(top = 32.dp)) {
                MenuPanel {
                    items.forEach { item ->
                        MenuItem(
                            title = item.title,
                            shortcut = item.shortcut,
                            enabled = item.enabled,
                            destructive = item.destructive,
                            onClick = {
                                item.onClick()
                                expanded = false
                            },
                        )
                    }
                }
            }
        }
    }
}
