package zhiqiu.app.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.basic.AppleList
import zhiqiu.applewidgets.kmp.basic.Avatar
import zhiqiu.applewidgets.kmp.basic.BasicComponent
import zhiqiu.applewidgets.kmp.basic.Button
import zhiqiu.applewidgets.kmp.basic.ButtonStyle
import zhiqiu.applewidgets.kmp.basic.CalendarDatePicker
import zhiqiu.applewidgets.kmp.basic.Card
import zhiqiu.applewidgets.kmp.basic.DateComponents
import zhiqiu.applewidgets.kmp.basic.FloatingActionButton
import zhiqiu.applewidgets.kmp.basic.FloatingToolbar
import zhiqiu.applewidgets.kmp.basic.IconButton
import zhiqiu.applewidgets.kmp.basic.Label
import zhiqiu.applewidgets.kmp.basic.ListRowDivider
import zhiqiu.applewidgets.kmp.basic.ListStyle
import zhiqiu.applewidgets.kmp.basic.MapPlaceholder
import zhiqiu.applewidgets.kmp.basic.PageControl
import zhiqiu.applewidgets.kmp.basic.PhotosPickerPlaceholder
import zhiqiu.applewidgets.kmp.basic.ShareSheetPlaceholder
import zhiqiu.applewidgets.kmp.basic.SmallTitle
import zhiqiu.applewidgets.kmp.basic.Stepper
import zhiqiu.applewidgets.kmp.basic.SwipeAction
import zhiqiu.applewidgets.kmp.basic.SwipeableListRow
import zhiqiu.applewidgets.kmp.basic.Text
import zhiqiu.applewidgets.kmp.basic.ToggleButton
import zhiqiu.applewidgets.kmp.layout.FormSection
import zhiqiu.applewidgets.kmp.layout.NavigationStack
import zhiqiu.applewidgets.kmp.layout.rememberNavigationController
import zhiqiu.applewidgets.kmp.macos.ComboBox
import zhiqiu.applewidgets.kmp.macos.TokenField
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme
import zhiqiu.applewidgets.kmp.theme.LocalContentColor

fun LazyListScope.otherSection(
    onShowToast: (String) -> Unit,
) {
    item(key = "other") {
        var editing by remember { mutableStateOf(false) }
        var qty by remember { mutableIntStateOf(3) }
        var page by remember { mutableIntStateOf(1) }
        var toggle by remember { mutableStateOf(true) }
        var date by remember { mutableStateOf(DateComponents(2026, 11, 13)) }
        var combo by remember { mutableIntStateOf(1) }
        var tokens by remember { mutableStateOf(listOf("设计", "CMP")) }
        val nav = rememberNavigationController("首页")

        SmallTitle(text = "Other")
        Card(modifier = Modifier.demoCard()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Label(title = "账户", subtitle = "zhiqiu", systemImage = "☺")
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Avatar(initials = "AW")
                    ToggleButton(text = "Toggle", checked = toggle, onCheckedChange = { toggle = it })
                    PageControl(pageCount = 5, currentPage = page)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        text = if (editing) "完成" else "编辑",
                        onClick = { editing = !editing },
                        style = ButtonStyle.Tinted,
                    )
                    Stepper(value = qty, onValueChange = { qty = it }, valueRange = 0..20)
                }
                AppleList(style = ListStyle.InsetGrouped) {
                    SwipeableListRow(
                        editing = editing,
                        onDelete = { onShowToast("删除行") },
                        trailingActions = listOf(
                            SwipeAction(
                                title = "删除",
                                containerColor = AppleWidgetsTheme.colorScheme.destructive,
                                onClick = { onShowToast("滑动删除") },
                            ),
                        ),
                        modifier = Modifier.height(52.dp),
                    ) {
                        BasicComponent(
                            title = "滑动显示删除",
                            summary = "左滑试试",
                            modifier = Modifier.weight(1f),
                        )
                    }
                    ListRowDivider()
                    BasicComponent(title = "普通行", summary = "InsetGrouped")
                }
                FormSection(header = "表单区") {
                    BasicComponent(title = "名称", summary = "FormSection")
                }
                CalendarDatePicker(value = date, onValueChange = { date = it })
                ComboBox(
                    options = listOf("自动", "浅色", "深色"),
                    selectedIndex = combo,
                    onSelect = { combo = it },
                    modifier = Modifier.fillMaxWidth(),
                )
                TokenField(tokens = tokens, onTokensChange = { tokens = it }, placeholder = "回车添加标签")
                NavigationStack(
                    controller = nav,
                    modifier = Modifier.fillMaxWidth().height(160.dp),
                    titleOf = { it },
                ) { route ->
                    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("当前：$route", style = AppleWidgetsTheme.textStyles.body)
                        if (route == "首页") {
                            Button(text = "Push 详情", onClick = { nav.push("详情") }, style = ButtonStyle.Filled)
                        }
                    }
                }
                FloatingToolbar {
                    IconButton(onClick = {}) {
                        Text("⇠", color = LocalContentColor.current)
                    }
                    IconButton(onClick = {}) {
                        Text("⇢", color = LocalContentColor.current)
                    }
                    Text("FloatingToolbar", color = AppleWidgetsTheme.colorScheme.label)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    FloatingActionButton(onClick = {}) {
                        Text("+", color = LocalContentColor.current, style = AppleWidgetsTheme.textStyles.title1)
                    }
                }
                ShareSheetPlaceholder(onUnavailable = { onShowToast("ShareSheet 未接") })
                PhotosPickerPlaceholder(onUnavailable = { onShowToast("PhotosPicker 未接") })
                MapPlaceholder(onUnavailable = { onShowToast("Map 未接") })
            }
        }
    }
}
