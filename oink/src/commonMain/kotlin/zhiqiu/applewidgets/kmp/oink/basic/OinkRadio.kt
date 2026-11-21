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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

/** Oink 单选框：22 圆形、1.5px 描边，选中强调色填充 + 8px 圆点弹入。 */
@Composable
fun OinkRadio(
    selected: Boolean,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: OinkRadioColors = OinkRadioDefaults.colors(),
    interactionSource: MutableInteractionSource? = null,
) {
    val source = interactionSource ?: remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()
    val hovered by source.collectIsHoveredAsState()

    val scale by animateFloatAsState(
        if (pressed && enabled) 0.84f else 1f,
        if (pressed && enabled) tween(OinkMotion.Dur1, easing = OinkMotion.EaseOut) else OinkMotion.bouncy(),
        label = "scale",
    )
    val bg by animateColorAsState(
        when {
            selected -> colors.containerSelected
            !enabled -> colors.containerDisabled
            else -> colors.container
        },
        tween(OinkMotion.Dur2, easing = OinkMotion.EaseOut),
        label = "bg",
    )
    val borderColor by animateColorAsState(
        when {
            selected -> Color.Transparent
            hovered && enabled -> colors.borderHover
            else -> colors.border
        },
        tween(OinkMotion.Dur2, easing = OinkMotion.EaseOut),
        label = "border",
    )
    val dotScale by animateFloatAsState(
        if (selected) 1f else 0f,
        if (selected) OinkMotion.bouncy() else tween(OinkMotion.Dur1, easing = OinkMotion.EaseIn),
        label = "dot",
    )

    val clickable = if (onClick != null) {
        Modifier.clickable(
            interactionSource = source,
            indication = null,
            enabled = enabled,
            role = Role.RadioButton,
            onClick = onClick,
        )
    } else {
        Modifier
    }
    Box(
        modifier = modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .alpha(if (!enabled && selected) 0.5f else 1f)
            .size(OinkRadioDefaults.Size)
            .clip(CircleShape)
            .background(bg)
            .border(OinkMetrics.ControlStroke, borderColor, CircleShape)
            .then(clickable),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            Modifier
                .graphicsLayer { scaleX = dotScale; scaleY = dotScale }
                .size(OinkRadioDefaults.DotSize)
                .clip(CircleShape)
                .background(colors.dot),
        )
    }
}

@Composable
fun OinkRadioLabel(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    description: String? = null,
    colors: OinkRadioColors = OinkRadioDefaults.colors(),
) {
    val c = OinkTheme.colors
    Row(
        modifier = modifier
            .clickable(enabled = enabled, role = Role.RadioButton, onClick = onClick)
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = if (description != null) Alignment.Top else Alignment.CenterVertically,
    ) {
        OinkRadio(selected, onClick, enabled = enabled, colors = colors)
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            OinkText(label, style = OinkTheme.textStyles.label, color = if (enabled) c.text else c.text3)
            if (description != null) {
                OinkText(description, style = OinkTheme.textStyles.caption, color = c.text2)
            }
        }
    }
}

object OinkRadioDefaults {
    val Size = 22.dp
    val DotSize = 8.dp

    @Composable
    fun colors(): OinkRadioColors {
        val c = OinkTheme.colors
        return OinkRadioColors(
            containerSelected = c.accent,
            container = c.raised,
            containerDisabled = c.well,
            border = c.line2,
            borderHover = c.accent,
            dot = c.onAccent,
        )
    }
}

@Immutable
data class OinkRadioColors(
    val containerSelected: Color,
    val container: Color,
    val containerDisabled: Color,
    val border: Color,
    val borderHover: Color,
    val dot: Color,
)