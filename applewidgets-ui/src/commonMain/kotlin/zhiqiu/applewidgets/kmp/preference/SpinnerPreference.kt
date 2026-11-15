package zhiqiu.applewidgets.kmp.preference

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import zhiqiu.applewidgets.kmp.basic.BasicComponentColors
import zhiqiu.applewidgets.kmp.basic.BasicComponentDefaults
import zhiqiu.applewidgets.kmp.overlay.DropdownMenu
import zhiqiu.applewidgets.kmp.overlay.DropdownMenuItem

/**
 * 高级选择器设置行：点击弹出 [DropdownMenu]（支持勾选态）。
 */
@Composable
fun SpinnerPreference(
    title: String,
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    titleColor: BasicComponentColors = BasicComponentDefaults.titleColor(),
    summary: String? = null,
    summaryColor: BasicComponentColors = BasicComponentDefaults.summaryColor(),
    insideMargin: PaddingValues = BasicComponentDefaults.InsideMargin,
    enabled: Boolean = true,
) {
    var expanded by remember { mutableStateOf(false) }
    ArrowPreference(
        title = title,
        onClick = { if (enabled) expanded = true },
        modifier = modifier,
        titleColor = titleColor,
        summary = summary,
        summaryColor = summaryColor,
        value = options.getOrNull(selectedIndex),
        insideMargin = insideMargin,
        enabled = enabled,
    )
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        items = options.mapIndexed { index, option ->
            DropdownMenuItem(
                title = option,
                checked = index == selectedIndex,
                onClick = { onSelect(index) },
            )
        },
    )
}
