package zhiqiu.app.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.basic.Card
import zhiqiu.applewidgets.kmp.basic.NumberPicker
import zhiqiu.applewidgets.kmp.basic.SmallTitle
import zhiqiu.applewidgets.kmp.basic.Text
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

fun LazyListScope.numberPickerSection() {
    item(key = "numberPicker") {
        var hour by remember { mutableIntStateOf(16) }
        var minute by remember { mutableIntStateOf(30) }
        val hours = remember { (0..23).map { it.toString().padStart(2, '0') } }
        val minutes = remember { (0..59).map { it.toString().padStart(2, '0') } }

        SmallTitle(text = "NumberPicker")
        Card(modifier = Modifier.demoCard()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                NumberPicker(
                    values = hours,
                    selectedIndex = hour,
                    onSelect = { hour = it },
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = ":",
                    style = AppleWidgetsTheme.textStyles.title2,
                )
                NumberPicker(
                    values = minutes,
                    selectedIndex = minute,
                    onSelect = { minute = it },
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}
