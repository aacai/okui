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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.basic.Card
import zhiqiu.applewidgets.kmp.basic.RadioButton
import zhiqiu.applewidgets.kmp.basic.SmallTitle
import zhiqiu.applewidgets.kmp.preference.RadioButtonPreference

fun LazyListScope.radioButtonSection() {
    item(key = "radioButton") {
        var selected by remember { mutableIntStateOf(0) }
        var pref by remember { mutableIntStateOf(0) }

        SmallTitle(text = "RadioButton")
        Card(modifier = Modifier.demoCard()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                RadioButton(selected = selected == 0, onClick = { selected = 0 })
                RadioButton(selected = selected == 1, onClick = { selected = 1 })
                RadioButton(selected = false, onClick = {}, enabled = false)
                RadioButton(selected = true, onClick = {}, enabled = false)
            }
            RadioButtonPreference(
                title = "Option A",
                selected = pref == 0,
                onClick = { pref = 0 },
            )
            RadioButtonPreference(
                title = "Option B",
                summary = "Secondary line",
                selected = pref == 1,
                onClick = { pref = 1 },
            )
            RadioButtonPreference(
                title = "Disabled Radio",
                selected = true,
                enabled = false,
                onClick = {},
            )
        }
    }
}
