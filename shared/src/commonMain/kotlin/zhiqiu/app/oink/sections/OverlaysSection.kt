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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp

@Composable
internal fun OverlaysSection(toastState: OinkToastState) {
    SectionHeader("Floating", "Dialogs, toasts & tooltips", "System dialogs with a scrim, auto-dismissing toasts, and hover tooltips.")
    var dialog by remember { mutableStateOf(false) }
    var danger by remember { mutableStateOf(false) }
    DemoCard {
        FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            OinkButton(text = "Open dialog", onClick = { dialog = true }, variant = OinkButtonVariant.Accent, leadingIcon = OinkIcons.Layout)
            OinkButton(text = "Delete dialog", onClick = { danger = true }, variant = OinkButtonVariant.Danger, leadingIcon = OinkIcons.Trash)
            OinkButton(text = "Show toast", onClick = { toastState.show("Profile saved", OinkToastVariant.Success) }, variant = OinkButtonVariant.Soft, leadingIcon = OinkIcons.Bell)
            OinkButton(text = "Error toast", onClick = { toastState.show("Something went wrong", OinkToastVariant.Danger) }, variant = OinkButtonVariant.Soft)
            OinkTooltip(tooltip = "I appear on hover") {
                OinkButton(text = "Hover me", onClick = {}, variant = OinkButtonVariant.Outline, leadingIcon = OinkIcons.Info)
            }
        }
    }
    if (dialog) {
        OinkDialog(
            onDismissRequest = { dialog = false },
            title = "Save changes?",
            text = "Your profile will be updated with the new details. You can revert this at any time.",
            icon = OinkIcons.Sparkle,
            confirmLabel = "Save",
            onConfirm = { dialog = false; toastState.show("Profile saved", OinkToastVariant.Success) },
        )
    }
    if (danger) {
        OinkDialog(
            onDismissRequest = { danger = false },
            title = "Delete project?",
            text = "This permanently removes the project and all of its files. This action cannot be undone.",
            icon = OinkIcons.Trash,
            destructive = true,
            confirmLabel = "Delete",
            onConfirm = { danger = false; toastState.show("Project deleted", OinkToastVariant.Danger) },
        )
    }
}
