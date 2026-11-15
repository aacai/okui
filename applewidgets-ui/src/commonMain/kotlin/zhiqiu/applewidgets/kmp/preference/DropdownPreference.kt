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
import zhiqiu.applewidgets.kmp.overlay.ListPopup
import zhiqiu.applewidgets.kmp.overlay.ListPopupItem

/**
 * 下拉选择设置行：点击弹出 [ListPopup]。
 */
@Composable
fun DropdownPreference(
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
    var showPopup by remember { mutableStateOf(false) }
    val value = options.getOrNull(selectedIndex)
    ArrowPreference(
        title = title,
        onClick = { if (enabled) showPopup = true },
        modifier = modifier,
        titleColor = titleColor,
        summary = summary,
        summaryColor = summaryColor,
        value = value,
        insideMargin = insideMargin,
        enabled = enabled,
    )
    if (showPopup) {
        ListPopup(
            items = options.mapIndexed { index, option ->
                ListPopupItem(title = option) { onSelect(index) }
            },
            onDismissRequest = { showPopup = false },
        )
    }
}
