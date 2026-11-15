package zhiqiu.app.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.basic.Card
import zhiqiu.applewidgets.kmp.basic.ColorPalette
import zhiqiu.applewidgets.kmp.basic.ColorPaletteDefaults
import zhiqiu.applewidgets.kmp.basic.ColorPicker
import zhiqiu.applewidgets.kmp.basic.ColorWell
import zhiqiu.applewidgets.kmp.basic.SmallTitle

fun LazyListScope.colorPickerSection() {
    item(key = "colorPicker") {
        var color by remember { mutableStateOf(Color(0xFF007AFF)) }
        var swatch by remember { mutableStateOf<Color?>(ColorPaletteDefaults.SystemSwatches[0]) }

        SmallTitle(text = "ColorPicker")
        Card(modifier = Modifier.demoCard()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                ColorWell(color = color, onClick = {})
                ColorPicker(color = color, onColorChange = { color = it })
                ColorPalette(
                    colors = ColorPaletteDefaults.SystemSwatches,
                    selected = swatch,
                    onSelect = { swatch = it },
                )
            }
        }
    }
}
