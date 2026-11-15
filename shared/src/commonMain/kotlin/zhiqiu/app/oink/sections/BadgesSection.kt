@file:OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)

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
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
internal fun BadgesSection() {
    SectionHeader("Status", "Badges, tags & chips", "Quiet semantic pills, removable tags, and selectable chips.")
    DemoCard {
        Label("Badges")
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OinkBadge(text = "Neutral")
            OinkBadge(text = "Accent", variant = OinkBadgeVariant.Accent)
            OinkBadge(text = "Solid", variant = OinkBadgeVariant.Solid)
            OinkBadge(text = "Success", variant = OinkBadgeVariant.Success, dot = true)
            OinkBadge(text = "Warning", variant = OinkBadgeVariant.Warning, dot = true)
            OinkBadge(text = "Danger", variant = OinkBadgeVariant.Danger, dot = true)
            OinkBadge(text = "Info", variant = OinkBadgeVariant.Info, dot = true)
        }
        Spacer(Modifier.height(20.dp))
        Label("Tags")
        var tags by remember { mutableStateOf(listOf("design", "compose", "kotlin", "oink")) }
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            tags.forEach { t -> OinkTag(text = t, leadingIcon = OinkIcons.Tag, onRemove = { tags = tags - t }) }
            if (tags.isEmpty()) OinkText("All tags removed", style = OinkTheme.textStyles.caption, color = OinkTheme.colors.text3)
        }
        Spacer(Modifier.height(20.dp))
        Label("Chips")
        var selected by remember { mutableStateOf(setOf("Music")) }
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Music", "Pods", "Films", "Books", "Games").forEach { label ->
                OinkChip(
                    label = label,
                    selected = label in selected,
                    onClick = { selected = if (label in selected) selected - label else selected + label },
                    leadingIcon = if (label in selected) OinkIcons.Check else null,
                )
            }
        }
    }
}
