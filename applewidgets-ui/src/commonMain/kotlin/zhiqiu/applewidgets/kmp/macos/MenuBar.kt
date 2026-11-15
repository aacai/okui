package zhiqiu.applewidgets.kmp.macos

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.basic.Text
import zhiqiu.applewidgets.kmp.overlay.AnchoredDropdownMenu
import zhiqiu.applewidgets.kmp.overlay.DropdownMenuItem
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

data class MenuBarItem(
    val title: String,
    val items: List<DropdownMenuItem>,
)

/**
 * macOS 菜单栏示意（非系统菜单栏）。
 */
@Composable
fun MenuBar(
    items: List<MenuBarItem>,
    modifier: Modifier = Modifier,
) {
    val colors = AppleWidgetsTheme.colorScheme
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(28.dp)
            .background(colors.secondarySystemBackground)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        items.forEach { item ->
            var expanded by remember(item.title) { mutableStateOf(false) }
            Text(
                text = item.title,
                style = AppleWidgetsTheme.textStyles.footnote,
                color = colors.label,
                modifier = Modifier
                    .clickable { expanded = true }
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            )
            AnchoredDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                items = item.items,
            )
        }
    }
}
