package zhiqiu.app.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import zhiqiu.applewidgets.kmp.basic.SmallTitle
import zhiqiu.applewidgets.kmp.basic.Switch
import zhiqiu.applewidgets.kmp.basic.Text
import zhiqiu.applewidgets.kmp.preference.SwitchPreference
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

fun LazyListScope.switchSection() {
    item(key = "switch") {
        var switch by remember { mutableStateOf(false) }
        var switchTrue by remember { mutableStateOf(true) }
        var expand by remember { mutableStateOf(false) }
        var nested by remember { mutableStateOf(false) }

        SmallTitle(text = "Switch")
        Card(modifier = Modifier.demoCard()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Switch(checked = switch, onCheckedChange = { switch = it })
                Switch(checked = switchTrue, onCheckedChange = { switchTrue = it })
                Switch(checked = false, onCheckedChange = {}, enabled = false)
                Switch(checked = true, onCheckedChange = {}, enabled = false)
            }
            SwitchPreference(
                title = "Switch",
                summary = "Click to expand a Switch",
                checked = expand,
                onCheckedChange = { expand = it },
            )
            AnimatedVisibility(
                visible = expand,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically(),
            ) {
                SwitchPreference(
                    title = "Switch",
                    checked = nested,
                    onCheckedChange = { nested = it },
                    endActions = {
                        Text(
                            text = "$nested",
                            color = AppleWidgetsTheme.colorScheme.secondaryLabel,
                        )
                    },
                )
            }
            SwitchPreference(
                title = "Disabled Switch",
                checked = true,
                enabled = false,
                onCheckedChange = {},
            )
        }
    }
}
