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

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun SegmentsSection() {
    SectionHeader("Navigation", "Segments & tabs", "A sliding thumb and a gliding ink bar, both driven by the same spring.")
    DemoCard {
        var seg by remember { mutableStateOf(1) }
        var tab by remember { mutableStateOf(0) }
        Label("Segmented")
        OinkSegmented(options = listOf("Overview", "Activity", "Settings"), selectedIndex = seg, onSelectedChange = { seg = it })
        Spacer(Modifier.height(24.dp))
        Label("Tabs")
        OinkTabs(tabs = listOf("For you", "Following", "Trending", "Saved"), selectedIndex = tab, onSelectedChange = { tab = it })
        Spacer(Modifier.height(12.dp))
        OinkText(
            text = "Showing the “${listOf("For you", "Following", "Trending", "Saved")[tab]}” feed.",
            style = OinkTheme.textStyles.caption,
            color = OinkTheme.colors.text2,
        )
    }
}
