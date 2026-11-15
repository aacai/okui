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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun ButtonsSection() {
    val toast = rememberOinkToastState()
    SectionHeader("Actions", "Buttons", "Six variants, three sizes, and live loading / success / error states with a spring press.")
    DemoCard {
        Label("Variants")
        FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            OinkButton(text = "Soft", onClick = {}, variant = OinkButtonVariant.Soft)
            OinkButton(text = "Primary", onClick = {}, variant = OinkButtonVariant.Primary)
            OinkButton(text = "Accent", onClick = {}, variant = OinkButtonVariant.Accent)
            OinkButton(text = "Ghost", onClick = {}, variant = OinkButtonVariant.Ghost)
            OinkButton(text = "Outline", onClick = {}, variant = OinkButtonVariant.Outline)
            OinkButton(text = "Danger", onClick = {}, variant = OinkButtonVariant.Danger)
        }
        Spacer(Modifier.height(20.dp))
        Label("Sizes & icons")
        FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            OinkButton(text = "Small", onClick = {}, size = OinkButtonSize.Sm, variant = OinkButtonVariant.Soft)
            OinkButton(text = "Medium", onClick = {}, size = OinkButtonSize.Md, variant = OinkButtonVariant.Soft)
            OinkButton(text = "Large", onClick = {}, size = OinkButtonSize.Lg, variant = OinkButtonVariant.Soft)
            OinkButton(text = "Download", onClick = {}, variant = OinkButtonVariant.Accent, leadingIcon = OinkIcons.Download)
            OinkIconButton(icon = OinkIcons.Heart, onClick = {}, contentDescription = "Like", variant = OinkButtonVariant.Soft)
            OinkIconButton(icon = OinkIcons.Share, onClick = {}, contentDescription = "Share", variant = OinkButtonVariant.Outline)
            OinkButton(text = "Disabled", onClick = {}, enabled = false, variant = OinkButtonVariant.Accent)
        }
        Spacer(Modifier.height(20.dp))
        Label("Async states — click to run")
        FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            AsyncButton("Save changes", OinkButtonVariant.Accent)
            AsyncButton("Deploy", OinkButtonVariant.Primary)
            AsyncButton("Delete", OinkButtonVariant.Danger, fail = true)
        }
    }
}

@Composable
private fun AsyncButton(label: String, variant: OinkButtonVariant, fail: Boolean = false) {
    var state by remember { mutableStateOf(OinkButtonState.Idle) }
    val scope = rememberCoroutineScope()
    OinkButton(
        text = label,
        variant = variant,
        state = state,
        onClick = {
            if (state != OinkButtonState.Idle) return@OinkButton
            state = OinkButtonState.Loading
            scope.launch {
                delay(1100)
                state = if (fail) OinkButtonState.Error else OinkButtonState.Success
                delay(1200)
                state = OinkButtonState.Idle
            }
        },
    )
}
