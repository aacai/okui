package zhiqiu.app.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.basic.Card
import zhiqiu.applewidgets.kmp.basic.Checkbox
import zhiqiu.applewidgets.kmp.basic.SmallTitle
import zhiqiu.applewidgets.kmp.preference.CheckboxPreference

fun LazyListScope.checkboxSection() {
    item(key = "checkbox") {
        var a by remember { mutableStateOf(false) }
        var b by remember { mutableStateOf(true) }
        var pref by remember { mutableStateOf(false) }
        var prefEnd by remember { mutableStateOf(true) }

        SmallTitle(text = "Checkbox")
        Card(modifier = Modifier.demoCard()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Checkbox(checked = a, onCheckedChange = { a = it })
                Checkbox(checked = b, onCheckedChange = { b = it })
                Checkbox(checked = false, onCheckedChange = {}, enabled = false)
                Checkbox(checked = true, onCheckedChange = {}, enabled = false)
            }
            CheckboxPreference(
                title = "Checkbox",
                summary = "State: $pref",
                checked = pref,
                onCheckedChange = { pref = it },
            )
            CheckboxPreference(
                title = "Checkbox End",
                summary = "State: $prefEnd",
                checked = prefEnd,
                onCheckedChange = { prefEnd = it },
            )
            CheckboxPreference(
                title = "Disabled Checkbox",
                checked = true,
                enabled = false,
                onCheckedChange = {},
            )
        }
    }
}
