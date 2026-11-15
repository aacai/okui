package zhiqiu.app.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.basic.Card
import zhiqiu.applewidgets.kmp.basic.RangeSlider
import zhiqiu.applewidgets.kmp.basic.SmallTitle
import zhiqiu.applewidgets.kmp.basic.Text
import zhiqiu.applewidgets.kmp.preference.SliderPreference
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

fun LazyListScope.sliderSection() {
    item(key = "slider") {
        var slider by remember { mutableFloatStateOf(0.3f) }
        var disabled = 0.7f
        var range by remember { mutableStateOf(0.2f..0.8f) }

        SmallTitle(text = "Slider")
        Card(modifier = Modifier.demoCard()) {
            SliderPreference(
                title = "Normal",
                summary = "${(slider * 100).toInt()}%",
                value = slider,
                onValueChange = { slider = it },
            )
            SliderPreference(
                title = "Disabled",
                summary = "${(disabled * 100).toInt()}%",
                value = disabled,
                onValueChange = {},
                enabled = false,
            )
        }

        SmallTitle(text = "RangeSlider")
        Card(modifier = Modifier.demoCard()) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    text = "${(range.start * 100).toInt()}% - ${(range.endInclusive * 100).toInt()}%",
                    color = AppleWidgetsTheme.colorScheme.secondaryLabel,
                    style = AppleWidgetsTheme.textStyles.footnote,
                )
                RangeSlider(
                    value = range,
                    onValueChange = { range = it },
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
        }
    }
}
