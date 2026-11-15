package zhiqiu.app.component

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import zhiqiu.applewidgets.kmp.basic.Card
import zhiqiu.applewidgets.kmp.basic.SmallTitle
import zhiqiu.applewidgets.kmp.basic.Text
import zhiqiu.applewidgets.kmp.preference.ArrowPreference
import zhiqiu.applewidgets.kmp.preference.SliderPreference
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

fun LazyListScope.arrowSection() {
    item(key = "arrow") {
        var volume by remember { mutableFloatStateOf(0.5f) }

        SmallTitle(text = "Arrow")
        Card(modifier = Modifier.demoCard()) {
            ArrowPreference(
                title = "Arrow",
                summary = "Navigate",
                value = "详情",
                onClick = {},
                endActions = {
                    Text(
                        text = "End",
                        style = AppleWidgetsTheme.textStyles.footnote,
                        color = AppleWidgetsTheme.colorScheme.secondaryLabel,
                    )
                },
            )
            SliderPreference(
                title = "Volume",
                summary = "${(volume * 100).toInt()}%",
                value = volume,
                onValueChange = { volume = it },
            )
            ArrowPreference(
                title = "Disabled Arrow",
                value = "End",
                enabled = false,
                onClick = {},
            )
        }
    }
}
