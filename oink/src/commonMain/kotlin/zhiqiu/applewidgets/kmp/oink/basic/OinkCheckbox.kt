package zhiqiu.applewidgets.kmp.oink.basic

import zhiqiu.applewidgets.kmp.oink.data.*
import zhiqiu.applewidgets.kmp.oink.form.*
import zhiqiu.applewidgets.kmp.oink.layout.*
import zhiqiu.applewidgets.kmp.oink.nav.*
import zhiqiu.applewidgets.kmp.oink.overlay.*
import zhiqiu.applewidgets.kmp.oink.primitives.*
import zhiqiu.applewidgets.kmp.oink.responsive.*
import zhiqiu.applewidgets.kmp.oink.theme.*

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.platform.performToggleHaptic

/**
 * Oink 复选框：22×22 超椭圆（标称 7）、1.5px 描边，选中强调色填充 + 对勾描线，
 * 按压缩到 0.84，半选画横线。
 */
@Composable
fun OinkCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    indeterminate: Boolean = false,
    colors: OinkCheckboxColors = OinkCheckboxDefaults.colors(),
    interactionSource: MutableInteractionSource? = null,
) {
    val source = interactionSource ?: remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()
    val hovered by source.collectIsHoveredAsState()
    val haptic: HapticFeedback = LocalHapticFeedback.current
    val filled = checked || indeterminate
    val shape = OinkShape(OinkCheckboxDefaults.CornerRadius)

    val scale by animateFloatAsState(
        if (pressed && enabled) 0.84f else 1f,
        if (pressed && enabled) tween(OinkMotion.Dur1, easing = OinkMotion.EaseOut) else OinkMotion.bouncy(),
        label = "scale",
    )
    val bg by animateColorAsState(
        when {
            filled -> colors.containerChecked
            !enabled -> colors.containerDisabled
            else -> colors.container
        },
        tween(OinkMotion.Dur2, easing = OinkMotion.EaseOut),
        label = "bg",
    )
    val borderColor by animateColorAsState(
        when {
            filled -> Color.Transparent
            hovered && enabled -> colors.borderHover
            else -> colors.border
        },
        tween(OinkMotion.Dur2, easing = OinkMotion.EaseOut),
        label = "border",
    )
    val progress by animateFloatAsState(
        if (filled) 1f else 0f,
        if (filled) tween(OinkMotion.Dur3, delayMillis = 50, easing = OinkMotion.EaseOut) else tween(OinkMotion.Dur1, easing = OinkMotion.EaseIn),
        label = "progress",
    )

    Box(
        modifier = modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .alpha(if (!enabled && filled) 0.5f else 1f)
            .size(OinkCheckboxDefaults.Size)
            .clip(shape)
            .background(bg)
            .border(OinkMetrics.ControlStroke, borderColor, shape)
            .clickable(
                interactionSource = source,
                indication = null,
                enabled = enabled,
                role = Role.Checkbox,
                onClick = {
                    haptic.performToggleHaptic(!checked)
                    onCheckedChange(!checked)
                },
            ),
        contentAlignment = Alignment.Center,
    ) {
        if (indeterminate) {
            OinkDrawnPath(progress, colors.mark, OinkCheckboxDefaults.MarkSize, OinkDashPath)
        } else {
            OinkDrawnPath(progress, colors.mark, OinkCheckboxDefaults.MarkSize, OinkCheckPath)
        }
    }
}

@Composable
fun OinkCheckboxLabel(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    description: String? = null,
    indeterminate: Boolean = false,
    colors: OinkCheckboxColors = OinkCheckboxDefaults.colors(),
) {
    val c = OinkTheme.colors
    val haptic: HapticFeedback = LocalHapticFeedback.current
    Row(
        modifier = modifier
            .clickable(
                enabled = enabled,
                role = Role.Checkbox,
                onClick = {
                    haptic.performToggleHaptic(!checked)
                    onCheckedChange(!checked)
                },
            )
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = if (description != null) Alignment.Top else Alignment.CenterVertically,
    ) {
        OinkCheckbox(checked, onCheckedChange, enabled = enabled, indeterminate = indeterminate, colors = colors)
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            OinkText(label, style = OinkTheme.textStyles.label, color = if (enabled) c.text else c.text3)
            if (description != null) {
                OinkText(description, style = OinkTheme.textStyles.caption, color = c.text2)
            }
        }
    }
}

object OinkCheckboxDefaults {
    val Size = 22.dp
    val CornerRadius = 7.dp
    val MarkSize = 16.dp

    @Composable
    fun colors(): OinkCheckboxColors {
        val c = OinkTheme.colors
        return OinkCheckboxColors(
            containerChecked = c.accent,
            container = c.raised,
            containerDisabled = c.well,
            border = c.line2,
            borderHover = c.accent,
            mark = c.onAccent,
        )
    }
}

@Immutable
data class OinkCheckboxColors(
    val containerChecked: Color,
    val container: Color,
    val containerDisabled: Color,
    val border: Color,
    val borderHover: Color,
    val mark: Color,
)