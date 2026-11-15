package zhiqiu.app.oink

import zhiqiu.applewidgets.kmp.oink.basic.*
import zhiqiu.applewidgets.kmp.oink.data.*
import zhiqiu.applewidgets.kmp.oink.form.*
import zhiqiu.applewidgets.kmp.oink.layout.*
import zhiqiu.applewidgets.kmp.oink.nav.*
import zhiqiu.applewidgets.kmp.oink.overlay.*
import zhiqiu.applewidgets.kmp.oink.primitives.*
import zhiqiu.applewidgets.kmp.oink.responsive.*
import zhiqiu.applewidgets.kmp.oink.theme.*

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.basic.DateComponents

private var popover by mutableStateOf(false)
private var context by mutableStateOf(false)
private var drawer by mutableStateOf(false)
private var sheet by mutableStateOf(false)
private var palette by mutableStateOf(false)
private var commandQuery by mutableStateOf("")
private var date by mutableStateOf<DateComponents?>(DateComponents(2026, 11, 15))
private var calYear by mutableStateOf(2026)
private var calMonth by mutableStateOf(11)
private var day by mutableStateOf<Int?>(15)

/**
 * 浮层补件：气泡、右键菜单、抽屉、底部面板、侧栏与命令面板；附带日历与日期选择。
 */
@Composable
internal fun OverlaysExtraSection() {
    Column {
        SectionHeader(
            kicker = "Overlays",
            title = "Floating surfaces",
            desc = "Popover, context menu, drawer, sheet, inline panel and command palette, plus calendar and date picker.",
        )
        DemoCard {
            OinkInline(spacing = 12.dp) {
                Box {
                    OinkButton(text = "Popover", onClick = { popover = true }, variant = OinkButtonVariant.Soft)
                    OinkPopover(expanded = popover, onDismissRequest = { popover = false }) {
                        OinkMenuItem(text = "Duplicate", leadingIcon = OinkIcons.Copy, onClick = { popover = false })
                        OinkMenuItem(text = "Delete", leadingIcon = OinkIcons.Trash, destructive = true, onClick = { popover = false })
                    }
                }
                Box {
                    OinkButton(text = "Context menu", onClick = { context = true }, variant = OinkButtonVariant.Soft)
                    OinkContextMenu(expanded = context, onDismissRequest = { context = false }) {
                        OinkMenuItem(text = "Rename", leadingIcon = OinkIcons.Edit, onClick = { context = false })
                        OinkMenuItem(text = "Share", leadingIcon = OinkIcons.Share, onClick = { context = false })
                    }
                }
                OinkButton(text = "Drawer", onClick = { drawer = true }, variant = OinkButtonVariant.Outline)
                OinkButton(text = "Sheet", onClick = { sheet = true }, variant = OinkButtonVariant.Outline)
                OinkButton(text = "⌘K Palette", onClick = { palette = true }, variant = OinkButtonVariant.Accent, leadingIcon = OinkIcons.Command)
            }
            OinkInline(modifier = Modifier.padding(top = 20.dp), spacing = 20.dp) {
                OinkDatePicker(value = date, onValueChange = { date = it }, modifier = Modifier.width(200.dp))
                OinkCalendar(
                    year = calYear,
                    month = calMonth,
                    selectedDay = day,
                    onSelectDay = { day = it },
                    onMonthChange = { y, m -> calYear = y; calMonth = m },
                )
            }
            Label("Panel")
            OinkPanel(title = "Inspector", onClose = {}, modifier = Modifier.padding(top = 4.dp)) {
                OinkText("Inline side panel, not modal.", style = OinkTheme.textStyles.body, color = OinkTheme.colors.text2)
            }
        }
        OinkDrawer(open = drawer, onDismissRequest = { drawer = false }, title = "Filters") {
            OinkText("Drawer content", style = OinkTheme.textStyles.body)
        }
        OinkSheet(open = sheet, onDismissRequest = { sheet = false }, title = "Share") {
            OinkText("Sheet content", style = OinkTheme.textStyles.body)
        }
        OinkCommandPalette(
            open = palette,
            onDismissRequest = { palette = false },
            items = listOf(
                OinkCommandItem("New file", hint = "⌘N", icon = OinkIcons.Plus, group = "File"),
                OinkCommandItem("Search", hint = "⌘K", icon = OinkIcons.Search, group = "Edit"),
                OinkCommandItem("Settings", icon = OinkIcons.Settings, group = "Edit"),
            ),
            onSelect = { palette = false },
            query = commandQuery,
            onQueryChange = { commandQuery = it },
        )
    }
}
