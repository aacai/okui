package zhiqiu.app.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.basic.Card
import zhiqiu.applewidgets.kmp.basic.SmallTitle
import zhiqiu.applewidgets.kmp.overlay.CascadingDropdownMenu
import zhiqiu.applewidgets.kmp.overlay.ContextMenu
import zhiqiu.applewidgets.kmp.overlay.DropdownMenuButton
import zhiqiu.applewidgets.kmp.overlay.DropdownMenuItem
import zhiqiu.applewidgets.kmp.overlay.IconCascadingDropdownMenu
import zhiqiu.applewidgets.kmp.overlay.IconDropdownMenu
import zhiqiu.applewidgets.kmp.preference.ArrowPreference
import zhiqiu.applewidgets.kmp.preference.DropdownPreference

fun LazyListScope.dropdownSection() {
    item(key = "dropdown") {
        var dropdown by remember { mutableIntStateOf(1) }
        var showContext by remember { mutableStateOf(false) }
        var showCascade by remember { mutableStateOf(false) }

        SmallTitle(text = "Dropdown")
        Card(modifier = Modifier.demoCard()) {
            DropdownPreference(
                title = "DropdownPreference",
                options = listOf("低", "中", "高"),
                selectedIndex = dropdown,
                onSelect = { dropdown = it },
            )
            ArrowPreference(
                title = "ContextMenu",
                summary = "Click to show a ContextMenu",
                onClick = { showContext = true },
            )
            ArrowPreference(
                title = "Cascading Menu",
                summary = "Click to show a CascadingDropdownMenu",
                onClick = { showCascade = true },
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                DropdownMenuButton(
                    label = "操作",
                    items = listOf(
                        DropdownMenuItem("拷贝", trailing = "⌘C"),
                        DropdownMenuItem("粘贴", trailing = "⌘V"),
                        DropdownMenuItem("删除", destructive = true),
                    ),
                )
                IconDropdownMenu(
                    items = listOf(
                        DropdownMenuItem("分享", leading = "↗"),
                        DropdownMenuItem("收藏", leading = "★"),
                        DropdownMenuItem("举报", destructive = true),
                    ),
                )
                IconCascadingDropdownMenu(
                    items = listOf(
                        DropdownMenuItem(
                            title = "整理",
                            children = listOf(
                                DropdownMenuItem("按名称"),
                                DropdownMenuItem("按日期"),
                            ),
                        ),
                        DropdownMenuItem("刷新"),
                    ),
                )
            }
        }

        ContextMenu(
            expanded = showContext,
            onDismissRequest = { showContext = false },
            items = listOf(
                DropdownMenuItem("全选"),
                DropdownMenuItem("查找"),
                DropdownMenuItem("删除", destructive = true),
            ),
        )
        CascadingDropdownMenu(
            expanded = showCascade,
            onDismissRequest = { showCascade = false },
            items = listOf(
                DropdownMenuItem(
                    title = "移动到",
                    children = listOf(
                        DropdownMenuItem("文档"),
                        DropdownMenuItem("下载"),
                        DropdownMenuItem("桌面"),
                    ),
                ),
                DropdownMenuItem(
                    title = "标签",
                    children = listOf(
                        DropdownMenuItem("红色", leading = "●"),
                        DropdownMenuItem("蓝色", leading = "●"),
                    ),
                ),
                DropdownMenuItem("导出…"),
            ),
        )
    }
}
