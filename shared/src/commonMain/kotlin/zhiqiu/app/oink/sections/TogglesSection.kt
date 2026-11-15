package zhiqiu.app.oink

import zhiqiu.applewidgets.kmp.oink.basic.*
import zhiqiu.applewidgets.kmp.oink.data.*
import zhiqiu.applewidgets.kmp.oink.form.*
import zhiqiu.applewidgets.kmp.oink.layout.*
import zhiqiu.applewidgets.kmp.oink.nav.*
import zhiqiu.applewidgets.kmp.oink.overlay.*
import zhiqiu.applewidgets.kmp.oink.primitives.*
import zhiqiu.applewidgets.kmp.oink.responsive.*
import zhiqiu.applewidgets.kmp.oink.theme.*

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun TogglesSection() {
    SectionHeader("Selection", "Toggles", "Switches stretch on press; checks and radios draw their glyph with a spring.")
    DemoCard {
        var a by remember { mutableStateOf(true) }
        var b by remember { mutableStateOf(false) }
        var c1 by remember { mutableStateOf(true) }
        var c2 by remember { mutableStateOf(false) }
        var radio by remember { mutableStateOf(0) }
        Label("Switch")
        Row(horizontalArrangement = Arrangement.spacedBy(18.dp), verticalAlignment = Alignment.CenterVertically) {
            OinkSwitch(checked = a, onCheckedChange = { a = it }, size = OinkSwitchSize.Lg)
            OinkSwitch(checked = b, onCheckedChange = { b = it })
            OinkSwitch(checked = true, onCheckedChange = {}, enabled = false)
            OinkSwitch(checked = false, onCheckedChange = {}, enabled = false, size = OinkSwitchSize.Sm)
        }
        Spacer(Modifier.height(20.dp))
        Label("Checkbox")
        Row(horizontalArrangement = Arrangement.spacedBy(24.dp), verticalAlignment = Alignment.CenterVertically) {
            OinkCheckboxLabel(label = "Notifications", checked = c1, onCheckedChange = { c1 = it })
            OinkCheckboxLabel(label = "Beta features", description = "Opt in to experiments", checked = c2, onCheckedChange = { c2 = it })
            OinkCheckbox(checked = true, onCheckedChange = {}, indeterminate = true)
            OinkCheckbox(checked = false, onCheckedChange = {}, enabled = false)
        }
        Spacer(Modifier.height(20.dp))
        Label("Radio")
        Row(horizontalArrangement = Arrangement.spacedBy(24.dp), verticalAlignment = Alignment.CenterVertically) {
            listOf("Daily", "Weekly", "Monthly").forEachIndexed { i, l ->
                OinkRadioLabel(label = l, selected = radio == i, onClick = { radio = i })
            }
            OinkRadio(selected = false, onClick = {}, enabled = false)
        }
    }
}
