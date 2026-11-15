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

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun CardsSection() {
    SectionHeader("Surfaces", "Cards & rows", "Paper surfaces separated by hairlines and a top highlight — no drop shadows.")
    var wifi by remember { mutableStateOf(true) }
    var bluetooth by remember { mutableStateOf(false) }
    var notif by remember { mutableStateOf(true) }
    OinkGroup(title = "Preferences", modifier = Modifier.padding(horizontal = 8.dp)) {
        OinkRow(title = "Wi-Fi", description = "Connected to OinkNet", leadingIcon = OinkIcons.Globe, trailing = {
            OinkSwitch(checked = wifi, onCheckedChange = { wifi = it })
        })
        OinkRowDivider()
        OinkRow(title = "Bluetooth", description = "2 devices paired", leadingIcon = OinkIcons.Zap, trailing = {
            OinkSwitch(checked = bluetooth, onCheckedChange = { bluetooth = it })
        })
        OinkRowDivider()
        OinkRow(title = "Notifications", leadingIcon = OinkIcons.Bell, onClick = {}, trailing = {
            OinkSwitch(checked = notif, onCheckedChange = { notif = it })
        })
        OinkRowDivider()
        OinkRow(title = "Storage", leadingIcon = OinkIcons.Archive, onClick = {}, trailing = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                OinkText("64 GB", style = OinkTheme.textStyles.rowValue, color = OinkTheme.colors.text2)
                OinkIcon(OinkIcons.ChevronRight, null, tint = OinkTheme.colors.text3, size = 16.dp, modifier = Modifier.padding(start = 6.dp))
            }
        })
    }
}
