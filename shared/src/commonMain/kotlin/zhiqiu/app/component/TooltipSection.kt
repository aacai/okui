package zhiqiu.app.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.basic.Card
import zhiqiu.applewidgets.kmp.basic.SmallTitle
import zhiqiu.applewidgets.kmp.basic.Tooltip

fun LazyListScope.tooltipSection() {
    item(key = "tooltip") {
        SmallTitle(text = "Tooltip")
        Card(modifier = Modifier.demoCard()) {
            Tooltip(
                text = "Tooltip 示意",
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}
