package zhiqiu.applewidgets.kmp.overlay

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
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
import zhiqiu.applewidgets.kmp.basic.Button
import zhiqiu.applewidgets.kmp.basic.ButtonStyle
import zhiqiu.applewidgets.kmp.basic.IconButton
import zhiqiu.applewidgets.kmp.basic.Separator
import zhiqiu.applewidgets.kmp.basic.Text
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme
import zhiqiu.applewidgets.kmp.theme.LocalContentColor

data class DropdownMenuItem(
    val title: String,
    val subtitle: String? = null,
    val leading: String? = null,
    val trailing: String? = null,
    val enabled: Boolean = true,
    val destructive: Boolean = false,
    val checked: Boolean = false,
    val children: List<DropdownMenuItem> = emptyList(),
    val onClick: () -> Unit = {},
)

@Composable
fun DropdownMenuPanel(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = AppleWidgetsTheme.colorScheme
    val shape = AppleWidgetsTheme.componentStyles.overlayShape
    Column(
        modifier = modifier
            .widthIn(min = 200.dp, max = 320.dp)
            .shadow(16.dp, shape)
            .clip(shape)
            .background(colors.secondarySystemGroupedBackground)
            .padding(vertical = 6.dp),
        content = content,
    )
}

@Composable
fun DropdownMenuItemRow(
    item: DropdownMenuItem,
    onDismiss: () -> Unit,
    onOpenSubmenu: ((DropdownMenuItem) -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    val colors = AppleWidgetsTheme.colorScheme
    val fg = when {
        !item.enabled -> colors.tertiaryLabel
        item.destructive -> colors.destructive
        else -> colors.label
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = item.enabled) {
                if (item.children.isNotEmpty()) {
                    onOpenSubmenu?.invoke(item)
                } else {
                    item.onClick()
                    onDismiss()
                }
            }
            .padding(horizontal = 14.dp, vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (item.leading != null) {
            Text(item.leading, color = fg, style = AppleWidgetsTheme.textStyles.body)
            Spacer(Modifier.width(10.dp))
        }
        Column(Modifier.weight(1f)) {
            Text(item.title, color = fg, style = AppleWidgetsTheme.textStyles.body)
            if (item.subtitle != null) {
                Text(item.subtitle, color = colors.secondaryLabel, style = AppleWidgetsTheme.textStyles.footnote)
            }
        }
        when {
            item.children.isNotEmpty() -> Text("›", color = colors.tertiaryLabel, style = AppleWidgetsTheme.textStyles.title3)
            item.checked -> Text("✓", color = colors.accent, style = AppleWidgetsTheme.textStyles.body)
            item.trailing != null -> Text(item.trailing, color = colors.secondaryLabel, style = AppleWidgetsTheme.textStyles.footnote)
        }
    }
}

@Composable
fun DropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    items: List<DropdownMenuItem>,
    modifier: Modifier = Modifier,
) {
    if (!expanded) return
    var submenu by remember(expanded) { mutableStateOf<DropdownMenuItem?>(null) }

    PlatformDialogOverlay(onDismissRequest = onDismissRequest) {
        Box {
            DropdownMenuPanel(modifier = modifier) {
                items.forEachIndexed { index, item ->
                    DropdownMenuItemRow(
                        item = item,
                        onDismiss = onDismissRequest,
                        onOpenSubmenu = { submenu = it },
                    )
                    if (index != items.lastIndex) {
                        Separator(startIndent = 14.dp)
                    }
                }
            }
            submenu?.let { sub ->
                DropdownMenuPanel(modifier = Modifier.padding(start = 24.dp, top = 24.dp)) {
                    Text(
                        text = sub.title,
                        style = AppleWidgetsTheme.textStyles.footnote,
                        color = AppleWidgetsTheme.colorScheme.secondaryLabel,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    )
                    Separator(startIndent = 0.dp)
                    sub.children.forEachIndexed { index, child ->
                        DropdownMenuItemRow(
                            item = child,
                            onDismiss = onDismissRequest,
                            onOpenSubmenu = { submenu = it },
                        )
                        if (index != sub.children.lastIndex) {
                            Separator(startIndent = 14.dp)
                        }
                    }
                }
            }
        }
    }
}

/**
 * 锚定式下拉：有 Popup 就锚定，没有就退化为居中浮层。
 */
@Composable
fun AnchoredDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    items: List<DropdownMenuItem>,
    modifier: Modifier = Modifier,
) {
    if (!expanded) return
    PlatformAnchoredPopup(onDismissRequest = onDismissRequest) {
        DropdownMenuPanel(modifier = modifier.padding(8.dp)) {
            items.forEachIndexed { index, item ->
                DropdownMenuItemRow(item = item, onDismiss = onDismissRequest)
                if (index != items.lastIndex) Separator(startIndent = 14.dp)
            }
        }
    }
}

@Composable
fun DropdownMenuButton(
    label: String,
    items: List<DropdownMenuItem>,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier) {
        Button(
            text = "$label ▾",
            onClick = { expanded = true },
            style = ButtonStyle.Tinted,
        )
        AnchoredDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            items = items,
        )
    }
}

@Composable
fun IconDropdownMenu(
    icon: String = "•••",
    items: List<DropdownMenuItem>,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier) {
        IconButton(onClick = { expanded = true }) {
            Text(icon, color = LocalContentColor.current)
        }
        AnchoredDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            items = items,
        )
    }
}

@Composable
fun ContextMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    items: List<DropdownMenuItem>,
    modifier: Modifier = Modifier,
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        items = items,
        modifier = modifier,
    )
}

@Composable
fun CascadingDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    items: List<DropdownMenuItem>,
    modifier: Modifier = Modifier,
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        items = items,
        modifier = modifier,
    )
}
