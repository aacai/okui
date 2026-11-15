package zhiqiu.app.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.basic.Card
import zhiqiu.applewidgets.kmp.basic.Chip
import zhiqiu.applewidgets.kmp.basic.SmallTitle
import zhiqiu.applewidgets.kmp.basic.Text
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

fun LazyListScope.cardSection() {
    item(key = "card") {
        var chip by remember { mutableIntStateOf(0) }

        SmallTitle(text = "Card")
        Card(modifier = Modifier.demoCard()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Card", style = AppleWidgetsTheme.textStyles.title3)
                Text(
                    "Inset grouped container",
                    color = AppleWidgetsTheme.colorScheme.secondaryLabel,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("设计", "开发", "发布").forEachIndexed { i, label ->
                        Chip(text = label, selected = chip == i, onClick = { chip = i })
                    }
                }
            }
        }
    }
}
