package zhiqiu.app.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.basic.PickerView
import zhiqiu.applewidgets.kmp.basic.SmallTitle

fun LazyListScope.pickerViewSection() {
    item(key = "picker-view") {
        SmallTitle(text = "Picker")
        var selected by remember { mutableIntStateOf(1) }
        val options = listOf("Alpha", "Beta", "Gamma", "Delta", "Epsilon", "Zeta", "Eta")
        PickerView(
            options = options,
            selectedIndex = selected,
            onSelect = { selected = it },
            modifier = Modifier.padding(horizontal = 12.dp).padding(bottom = 12.dp),
        )
    }
}
