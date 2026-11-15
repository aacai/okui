package zhiqiu.app.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.basic.Card
import zhiqiu.applewidgets.kmp.basic.SegmentedControl
import zhiqiu.applewidgets.kmp.basic.SmallTitle
import zhiqiu.applewidgets.kmp.basic.TabRow
import zhiqiu.applewidgets.kmp.basic.Text
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

fun LazyListScope.tabRowSection() {
    item(key = "tabRow") {
        var tab by remember { mutableIntStateOf(0) }
        var segment by remember { mutableIntStateOf(0) }
        val tabs = remember { listOf("Tab 1", "Tab 2", "Tab 3") }

        SmallTitle(text = "TabRow")
        TabRow(
            tabs = tabs,
            selectedIndex = tab,
            onSelect = { tab = it },
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .padding(bottom = 12.dp),
        )
        Card(modifier = Modifier.demoCard()) {
            Column(Modifier.padding(16.dp)) {
                SegmentedControl(
                    options = listOf("日", "周", "月"),
                    selectedIndex = segment,
                    onSelect = { segment = it },
                )
                Text(
                    text = "Content of ${tabs.getOrElse(tab) { "Tab" }}",
                    modifier = Modifier.padding(top = 12.dp),
                    color = AppleWidgetsTheme.colorScheme.label,
                )
            }
        }
    }
}
