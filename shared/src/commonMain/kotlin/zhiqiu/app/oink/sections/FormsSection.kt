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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val demoSources = listOf("Design", "Frontend", "Docs")

private var pin by mutableStateOf("382910")
private var notes by mutableStateOf("")
private var picker by mutableStateOf(Color(0xFFFF6B8E))

/**
 * 表单补件：字段、多行、数字、验证码、选项卡、快捷键、文件投放、色板取色与高级选择。
 */
@Composable
internal fun FormsSection() {
    Column {
        SectionHeader(
            kicker = "Forms",
            title = "Fields and pickers",
            desc = "Labeled fields, textarea, number input, verification code, option cards, hotkeys, file drop, colors and combobox.",
        )
        DemoCard {
            OinkField(label = "Email", description = "We only use this for receipts.") {
                OinkText("alex@example.com")
            }
            OinkField(
                label = "Verification code",
                required = true,
                error = if (pin.length == 6) null else "Enter all six digits",
                modifier = Modifier.padding(top = 20.dp),
            ) {
                OinkPinInput(length = 6, value = pin, onValueChange = { pin = it.take(6) })
            }
            OinkField(label = "Notes", modifier = Modifier.padding(top = 20.dp)) {
                OinkTextarea(value = notes, onValueChange = { notes = it }, placeholder = "Anything else?")
            }
            OinkInline(modifier = Modifier.padding(top = 20.dp), spacing = 12.dp) {
                var count by remember { mutableStateOf(3) }
                OinkNumberInput(value = count, onValueChange = { count = it }, suffix = "seats", modifier = Modifier.width(160.dp))
                OinkHotkey("⌘+K")
            }
            Label("Option cards")
            var plan by remember { mutableStateOf(0) }
            OinkInline(spacing = 12.dp) {
                OinkOptionCard("Monthly", selected = plan == 0, onClick = { plan = 0 }, description = "$12 / month", modifier = Modifier.weight(1f))
                OinkOptionCard("Yearly", selected = plan == 1, onClick = { plan = 1 }, description = "$120 / year", modifier = Modifier.weight(1f))
            }
            Label("Colors")
            var swatch by remember { mutableStateOf(0) }
            OinkColorSwatches(
                colors = listOf(Color(0xFFFF6B8E), Color(0xFFFF7A2F), Color(0xFFFFBE3B), Color(0xFF2E7BFF), Color(0xFFC77DFF)),
                selectedIndex = swatch,
                onSelectedChange = { swatch = it },
            )
            OinkColorPicker(color = picker, onColorChange = { picker = it }, modifier = Modifier.padding(top = 16.dp))
            Label("Select")
            var selected by remember { mutableStateOf(setOf(0)) }
            OinkMultiSelect(options = demoSources, selectedIndices = selected, onSelectedChange = { selected = it })
            var query by remember { mutableStateOf("") }
            OinkCombobox(
                options = demoSources,
                value = query,
                onValueChange = { query = it },
                onSelect = { query = it },
                modifier = Modifier.padding(top = 12.dp),
            )
            OinkFileDrop(onClick = {}, modifier = Modifier.padding(top = 16.dp))
        }
    }
}
