package zhiqiu.app.component

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import zhiqiu.applewidgets.kmp.basic.Card
import zhiqiu.applewidgets.kmp.basic.SmallTitle
import zhiqiu.applewidgets.kmp.preference.SpinnerPreference

fun LazyListScope.spinnerSection() {
    item(key = "spinner") {
        var quality by remember { mutableIntStateOf(0) }
        var language by remember { mutableIntStateOf(1) }

        SmallTitle(text = "Spinner")
        Card(modifier = Modifier.demoCard()) {
            SpinnerPreference(
                title = "质量",
                options = listOf("低", "中", "高", "原始"),
                selectedIndex = quality,
                onSelect = { quality = it },
                summary = "导出编码质量",
            )
            SpinnerPreference(
                title = "语言",
                options = listOf("系统", "中文", "English", "日本語"),
                selectedIndex = language,
                onSelect = { language = it },
            )
            SpinnerPreference(
                title = "Disabled Spinner",
                options = listOf("A", "B"),
                selectedIndex = 0,
                onSelect = {},
                enabled = false,
            )
        }
    }
}
