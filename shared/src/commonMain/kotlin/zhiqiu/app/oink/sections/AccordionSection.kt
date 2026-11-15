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

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun AccordionSection() {
    SectionHeader("Disclosure", "Accordion", "Expandable rows with a rotating chevron and a vertical reveal.")
    var open by remember { mutableStateOf(0) }
    OinkCard(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
        OinkAccordionItem(title = "What is oink-ui?", expanded = open == 0, onToggle = { open = if (open == 0) -1 else 0 }) {
            OinkText("A soft, flat, quiet React component library. This is a faithful Compose Multiplatform port of its design language.", style = OinkTheme.textStyles.body, color = OinkTheme.colors.text2)
        }
        OinkRowDivider()
        OinkAccordionItem(title = "How are the corners drawn?", expanded = open == 1, onToggle = { open = if (open == 1) -1 else 1 }) {
            OinkText("Each corner is a superellipse (k = 1.75) built from two cubic Béziers using Chromium-fitted handle lengths, scaled by 1.45.", style = OinkTheme.textStyles.body, color = OinkTheme.colors.text2)
        }
        OinkRowDivider()
        OinkAccordionItem(title = "Does it support theming?", expanded = open == 2, onToggle = { open = if (open == 2) -1 else 2 }) {
            OinkText("Light and dark palettes plus five accents. Every token cross-fades over 240ms when you switch.", style = OinkTheme.textStyles.body, color = OinkTheme.colors.text2)
        }
    }
}
