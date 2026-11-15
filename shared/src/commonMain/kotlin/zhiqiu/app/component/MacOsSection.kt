package zhiqiu.app.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import zhiqiu.applewidgets.kmp.basic.Switch
import zhiqiu.applewidgets.kmp.basic.Text
import zhiqiu.applewidgets.kmp.macos.DisclosureGroup
import zhiqiu.applewidgets.kmp.macos.MenuBar
import zhiqiu.applewidgets.kmp.macos.MenuBarItem
import zhiqiu.applewidgets.kmp.macos.PushButton
import zhiqiu.applewidgets.kmp.macos.PushButtonStyle
import zhiqiu.applewidgets.kmp.macos.RadioGroup
import zhiqiu.applewidgets.kmp.macos.SearchField
import zhiqiu.applewidgets.kmp.macos.SidebarItem
import zhiqiu.applewidgets.kmp.macos.Stepper
import zhiqiu.applewidgets.kmp.overlay.DropdownMenuItem
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

fun LazyListScope.macOsSection() {
    item(key = "macos") {
        var query by remember { mutableStateOf("") }
        var radio by remember { mutableIntStateOf(0) }
        var step by remember { mutableIntStateOf(2) }
        var flag by remember { mutableStateOf(true) }
        var selected by remember { mutableIntStateOf(0) }

        SmallTitle(text = "macOS")
        Card(modifier = Modifier.demoCard()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                MenuBar(
                    items = listOf(
                        MenuBarItem(
                            "文件",
                            listOf(
                                DropdownMenuItem("新建", trailing = "⌘N"),
                                DropdownMenuItem("打开…", trailing = "⌘O"),
                                DropdownMenuItem("关闭", trailing = "⌘W"),
                            ),
                        ),
                        MenuBarItem(
                            "编辑",
                            listOf(
                                DropdownMenuItem("拷贝", trailing = "⌘C"),
                                DropdownMenuItem("粘贴", trailing = "⌘V"),
                            ),
                        ),
                    ),
                )
                SearchField(value = query, onValueChange = { query = it }, modifier = Modifier.fillMaxWidth())
                SidebarItem("项目", selected = selected == 0, onClick = { selected = 0 }, icon = "📁")
                SidebarItem("共享", selected = selected == 1, onClick = { selected = 1 }, icon = "↗")
                RadioGroup(
                    options = listOf("浅色", "深色", "系统"),
                    selectedIndex = radio,
                    onSelect = { radio = it },
                )
                Stepper(value = step, onValueChange = { step = it }, valueRange = 0..10)
                DisclosureGroup(title = "高级选项", initiallyExpanded = true) {
                    Text("实验功能", color = AppleWidgetsTheme.colorScheme.label)
                }
                PushButton(text = "Prominent", onClick = {}, style = PushButtonStyle.Prominent)
                PushButton(text = "Regular", onClick = {})
                Switch(checked = flag, onCheckedChange = { flag = it })
            }
        }
    }
}
