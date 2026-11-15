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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun InputsSection() {
    SectionHeader("Forms", "Inputs", "Focus lifts the field to a raised surface and lights an accent ring.")
    DemoCard {
        var name by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("piglet@oink.dev") }
        var query by remember { mutableStateOf("") }
        var select by remember { mutableStateOf(1) }
        var menuOpen by remember { mutableStateOf(false) }
        Label("Text field")
        OinkTextField(value = name, onValueChange = { name = it }, placeholder = "Your name", leadingIcon = OinkIcons.User, showClear = true)
        Spacer(Modifier.height(12.dp))
        OinkTextField(value = email, onValueChange = { email = it }, placeholder = "Email", leadingIcon = OinkIcons.Mail, showClear = true)
        Spacer(Modifier.height(12.dp))
        Label("Search")
        OinkSearchInput(value = query, onValueChange = { query = it }, placeholder = "Search components…")
        Spacer(Modifier.height(16.dp))
        Label("Select & menu")
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.width(220.dp)) {
                OinkSelect(options = listOf("Pink", "Orange", "Amber", "Blue", "Violet"), selectedIndex = select, onSelectedChange = { select = it })
            }
            Box {
                OinkButton(text = "Open menu", onClick = { menuOpen = true }, variant = OinkButtonVariant.Soft, leadingIcon = OinkIcons.Menu)
                OinkMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
                    OinkMenuLabel("Actions")
                    OinkMenuItem(text = "Edit", leadingIcon = OinkIcons.Edit, onClick = { menuOpen = false })
                    OinkMenuItem(text = "Duplicate", leadingIcon = OinkIcons.Copy, trailing = "⌘D", onClick = { menuOpen = false })
                    OinkMenuItem(text = "Share", leadingIcon = OinkIcons.Share, onClick = { menuOpen = false })
                    OinkMenuSeparator()
                    OinkMenuItem(text = "Delete", leadingIcon = OinkIcons.Trash, destructive = true, onClick = { menuOpen = false })
                }
            }
        }
    }
}
