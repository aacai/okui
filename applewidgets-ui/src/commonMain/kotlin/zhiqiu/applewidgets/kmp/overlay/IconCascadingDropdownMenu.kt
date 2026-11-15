package zhiqiu.applewidgets.kmp.overlay

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import zhiqiu.applewidgets.kmp.basic.IconButton
import zhiqiu.applewidgets.kmp.basic.Text
import zhiqiu.applewidgets.kmp.theme.LocalContentColor

/**
 * IconButton 触发的级联下拉菜单（对标 miuix OverlayIconCascadingDropdownMenu）。
 */
@Composable
fun IconCascadingDropdownMenu(
    items: List<DropdownMenuItem>,
    modifier: Modifier = Modifier,
    icon: String = "•••",
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier) {
        IconButton(onClick = { expanded = true }) {
            Text(icon, color = LocalContentColor.current)
        }
        CascadingDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            items = items,
        )
    }
}
