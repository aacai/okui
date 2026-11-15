package zhiqiu.app.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.basic.BreadcrumbBar
import zhiqiu.applewidgets.kmp.basic.Card
import zhiqiu.applewidgets.kmp.basic.SmallTitle

fun LazyListScope.breadcrumbBarSection() {
    item(key = "breadcrumbBar") {
        var selected by remember { mutableIntStateOf(2) }
        val items = remember { listOf("个人收藏", "项目", "源码") }

        SmallTitle(text = "BreadcrumbBar")
        Card(modifier = Modifier.demoCard()) {
            BreadcrumbBar(
                items = items,
                onSelect = { selected = it },
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}
