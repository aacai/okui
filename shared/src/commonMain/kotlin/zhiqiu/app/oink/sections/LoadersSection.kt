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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun LoadersSection() {
    SectionHeader("Loading", "Spinners & the tail", "A ring spinner, bouncing dots, and the brand tail that draws and wags.")
    DemoCard {
        Row(horizontalArrangement = Arrangement.spacedBy(40.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                OinkSpinner(size = 36.dp)
                OinkText("Spinner", style = OinkTheme.textStyles.caption, color = OinkTheme.colors.text3, modifier = Modifier.padding(top = 12.dp))
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                OinkDots()
                OinkText("Dots", style = OinkTheme.textStyles.caption, color = OinkTheme.colors.text3, modifier = Modifier.padding(top = 12.dp))
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                OinkTail(size = 44.dp)
                OinkText("Tail", style = OinkTheme.textStyles.caption, color = OinkTheme.colors.text3, modifier = Modifier.padding(top = 12.dp))
            }
        }
    }
}
