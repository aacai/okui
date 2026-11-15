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
internal fun SlidersSection() {
    SectionHeader("Range", "Sliders & steppers", "Drag the thumb to see the value bubble; discrete sliders snap to ticks.")
    DemoCard {
        var v by remember { mutableStateOf(0.6f) }
        var steps by remember { mutableStateOf(40f) }
        var count by remember { mutableStateOf(3) }
        Label("Continuous — ${(v * 100).toInt()}%")
        OinkSlider(value = v, onValueChange = { v = it }, valueLabel = { "${(it * 100).toInt()}%" })
        Spacer(Modifier.height(24.dp))
        Label("Stepped with ticks")
        OinkSlider(value = steps, onValueChange = { steps = it }, valueRange = 0f..100f, steps = 10, ticks = 11)
        Spacer(Modifier.height(24.dp))
        Label("Stepper")
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            OinkStepper(value = count, onValueChange = { count = it }, range = 0..12)
            OinkText(text = "Selected: $count", style = OinkTheme.textStyles.caption, color = OinkTheme.colors.text2)
        }
    }
}
