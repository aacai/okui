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
internal fun ProgressSection() {
    SectionHeader("Feedback", "Progress", "Determinate bars and rings spring to their value; indeterminate bars sweep.")
    DemoCard {
        var p by remember { mutableStateOf(0.68f) }
        Label("Determinate — ${(p * 100).toInt()}%")
        OinkProgress(progress = p)
        Spacer(Modifier.height(12.dp))
        OinkSlider(value = p, onValueChange = { p = it }, showBubble = false)
        Spacer(Modifier.height(20.dp))
        Label("Indeterminate")
        OinkProgress()
        Spacer(Modifier.height(24.dp))
        Label("Rings")
        Row(horizontalArrangement = Arrangement.spacedBy(24.dp), verticalAlignment = Alignment.CenterVertically) {
            OinkRing(progress = p, size = 72.dp) { OinkText("${(p * 100).toInt()}%", style = OinkTheme.textStyles.caption, color = OinkTheme.colors.text) }
            OinkRing(progress = 0.4f, size = 56.dp, strokeWidth = 5.dp, color = OinkTheme.colors.success)
            OinkRing(progress = 0.85f, size = 56.dp, strokeWidth = 5.dp, color = OinkTheme.colors.warning)
        }
    }
}
