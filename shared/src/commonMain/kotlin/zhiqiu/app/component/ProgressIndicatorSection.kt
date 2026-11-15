package zhiqiu.app.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.basic.Button
import zhiqiu.applewidgets.kmp.basic.ButtonStyle
import zhiqiu.applewidgets.kmp.basic.Card
import zhiqiu.applewidgets.kmp.basic.Gauge
import zhiqiu.applewidgets.kmp.basic.ProgressView
import zhiqiu.applewidgets.kmp.basic.SmallTitle

fun LazyListScope.progressIndicatorSection() {
    item(key = "progressIndicator") {
        var gauge by remember { mutableStateOf(0.62f) }

        SmallTitle(text = "ProgressIndicator")
        Card(modifier = Modifier.demoCard()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                ProgressView()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Gauge(value = gauge)
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            text = "进度 +",
                            onClick = { gauge = (gauge + 0.1f).coerceAtMost(1f) },
                            style = ButtonStyle.Tinted,
                        )
                        Button(
                            text = "进度 −",
                            onClick = { gauge = (gauge - 0.1f).coerceAtLeast(0f) },
                            style = ButtonStyle.Bordered,
                        )
                    }
                }
            }
        }
    }
}
