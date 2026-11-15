package zhiqiu.applewidgets.kmp.oink.form

import zhiqiu.applewidgets.kmp.oink.basic.*
import zhiqiu.applewidgets.kmp.oink.data.*
import zhiqiu.applewidgets.kmp.oink.layout.*
import zhiqiu.applewidgets.kmp.oink.nav.*
import zhiqiu.applewidgets.kmp.oink.overlay.*
import zhiqiu.applewidgets.kmp.oink.primitives.*
import zhiqiu.applewidgets.kmp.oink.responsive.*
import zhiqiu.applewidgets.kmp.oink.theme.*

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Oink 步进器：凹槽容器内嵌减 / 加按钮与居中数值，按钮 hover 升起、按下回弹。
 */
@Composable
fun OinkStepper(
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    range: IntRange = 0..99,
    step: Int = 1,
    colors: OinkStepperColors = OinkStepperDefaults.colors(),
) {
    Row(
        modifier = modifier
            .height(OinkStepperDefaults.Height)
            .clip(OinkShape(OinkRadii.r3))
            .background(colors.container)
            .border(OinkMetrics.Hairline, colors.border, OinkShape(OinkRadii.r3))
            .padding(3.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        StepperButton(
            icon = OinkIcons.Minus,
            enabled = enabled && value > range.first,
            colors = colors,
            onClick = { onValueChange((value - step).coerceAtLeast(range.first)) },
        )
        Box(
            Modifier.widthIn(min = OinkStepperDefaults.MinValueWidth).height(OinkStepperDefaults.ButtonSize),
            contentAlignment = Alignment.Center,
        ) {
            OinkText(
                text = value.toString(),
                color = if (enabled) colors.content else colors.disabledContent,
                style = OinkTheme.textStyles.body.copy(fontWeight = FontWeight.Medium),
            )
        }
        StepperButton(
            icon = OinkIcons.Plus,
            enabled = enabled && value < range.last,
            colors = colors,
            onClick = { onValueChange((value + step).coerceAtMost(range.last)) },
        )
    }
}

@Composable
private fun StepperButton(
    icon: ImageVector,
    enabled: Boolean,
    colors: OinkStepperColors,
    onClick: () -> Unit,
) {
    val interaction = remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(if (pressed) 0.88f else 1f, OinkMotion.bouncy(), label = "stepScale")
    Box(
        modifier = Modifier
            .size(OinkStepperDefaults.ButtonSize)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .clip(OinkShape(OinkRadii.r2))
            .background(if (hovered && enabled) colors.buttonHover else Color.Transparent)
            .hoverable(interaction)
            .clickable(interactionSource = interaction, indication = null, enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        OinkIcon(
            imageVector = icon,
            contentDescription = null,
            tint = if (enabled) colors.content else colors.disabledContent,
            modifier = Modifier.size(16.dp),
        )
    }
}

object OinkStepperDefaults {
    val Height = 40.dp
    val ButtonSize = 34.dp
    val MinValueWidth = 44.dp

    @Composable
    fun colors(): OinkStepperColors {
        val c = OinkTheme.colors
        return OinkStepperColors(
            container = c.well,
            border = c.line,
            content = c.text,
            disabledContent = c.text3,
            buttonHover = c.fillHover,
        )
    }
}

@Immutable
data class OinkStepperColors(
    val container: Color,
    val border: Color,
    val content: Color,
    val disabledContent: Color,
    val buttonHover: Color,
)