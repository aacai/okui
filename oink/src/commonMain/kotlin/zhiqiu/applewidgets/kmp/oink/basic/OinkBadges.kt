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
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.unit.sp

enum class OinkBadgeVariant { Neutral, Accent, Solid, Success, Warning, Danger, Info }

/**
 * Oink 徽标：24 高胶囊，柔和语义底色，可选前置圆点。
 */
@Composable
fun OinkBadge(
    text: String,
    modifier: Modifier = Modifier,
    dot: Boolean = false,
    variant: OinkBadgeVariant = OinkBadgeVariant.Neutral,
    colors: OinkBadgeColors = OinkBadgeDefaults.colors(variant),
) {
    val bg = colors.container
    val fg = colors.content
    Row(
        modifier = modifier
            .height(OinkBadgeDefaults.Height)
            .clip(CircleShape)
            .background(bg)
            .padding(horizontal = OinkBadgeDefaults.HorizontalPadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (dot) {
            Box(Modifier.padding(end = 6.dp).size(6.dp).clip(CircleShape).background(fg))
        }
        OinkText(
            text = text,
            color = fg,
            style = OinkTheme.textStyles.caption.copy(fontSize = 12.sp, fontWeight = FontWeight.SemiBold),
        )
    }
}

/**
 * Oink 标签：28 高、8 圆角、描边纸面，可带移除按钮。
 */
@Composable
fun OinkTag(
    text: String,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    onRemove: (() -> Unit)? = null,
    colors: OinkTagColors = OinkTagDefaults.colors(),
) {
    Row(
        modifier = modifier
            .height(OinkTagDefaults.Height)
            .clip(OinkShape(OinkRadii.r1))
            .background(colors.container)
            .border(OinkMetrics.Hairline, colors.border, OinkShape(OinkRadii.r1))
            .padding(start = 10.dp, end = if (onRemove != null) 4.dp else 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (leadingIcon != null) {
            OinkIcon(leadingIcon, null, tint = colors.muted, size = 14.dp, modifier = Modifier.padding(end = 6.dp))
        }
        OinkText(text = text, color = colors.content, style = OinkTheme.textStyles.caption)
        if (onRemove != null) {
            val interaction = remember { MutableInteractionSource() }
            val hovered by interaction.collectIsHoveredAsState()
            Box(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(if (hovered) colors.hover else Color.Transparent)
                    .hoverable(interaction)
                    .clickable(interactionSource = interaction, indication = null, onClick = onRemove),
                contentAlignment = Alignment.Center,
            ) {
                OinkIcon(OinkIcons.X, "Remove", tint = colors.muted, size = 12.dp)
            }
        }
    }
}

/**
 * Oink 可选 Chip：32 高胶囊，选中切到 accent 柔和底 + 光环描边，按下轻微回弹。
 */
@Composable
fun OinkChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    colors: OinkChipColors = OinkChipDefaults.colors(),
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val hovered by interaction.collectIsHoveredAsState()
    val scale by animateFloatAsState(if (pressed) 0.95f else 1f, OinkMotion.bouncy(), label = "chipScale")
    val bg by animateColorAsState(
        when {
            selected -> colors.selectedContainer
            hovered && enabled -> colors.hoverContainer
            else -> colors.container
        },
        tween(OinkMotion.Dur2, easing = OinkMotion.EaseOut),
        label = "chipBg",
    )
    val border by animateColorAsState(
        if (selected) colors.selectedBorder else colors.border,
        tween(OinkMotion.Dur2, easing = OinkMotion.EaseOut),
        label = "chipBorder",
    )
    val fg by animateColorAsState(
        if (selected) colors.selectedContent else colors.content,
        tween(OinkMotion.Dur2, easing = OinkMotion.EaseOut),
        label = "chipFg",
    )
    Row(
        modifier = modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .height(OinkChipDefaults.Height)
            .clip(CircleShape)
            .background(bg)
            .border(OinkMetrics.Hairline, border, CircleShape)
            .hoverable(interaction)
            .clickable(interactionSource = interaction, indication = null, enabled = enabled, onClick = onClick)
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (leadingIcon != null) {
            OinkIcon(leadingIcon, null, tint = fg, size = 15.dp, modifier = Modifier.padding(end = 7.dp))
        }
        OinkText(text = label, color = fg, style = OinkTheme.textStyles.caption.copy(fontWeight = FontWeight.Medium))
    }
}

object OinkBadgeDefaults {
    val Height = 24.dp
    val HorizontalPadding = 9.dp
    val DotSize = 6.dp

    /** 语义色随 [OinkBadgeVariant] 变化；Neutral 走中性填充。 */
    @Composable
    fun colors(variant: OinkBadgeVariant = OinkBadgeVariant.Neutral): OinkBadgeColors {
        val c = OinkTheme.colors
        val (bg, fg) = when (variant) {
            OinkBadgeVariant.Neutral -> c.fill to c.text2
            OinkBadgeVariant.Accent -> c.accentSoft to c.accentText
            OinkBadgeVariant.Solid -> c.accent to c.onAccent
            OinkBadgeVariant.Success -> c.successSoft to c.successText
            OinkBadgeVariant.Warning -> c.warningSoft to c.warningText
            OinkBadgeVariant.Danger -> c.dangerSoft to c.dangerText
            OinkBadgeVariant.Info -> c.infoSoft to c.infoText
        }
        return OinkBadgeColors(container = bg, content = fg)
    }
}

@Immutable
data class OinkBadgeColors(
    val container: Color,
    val content: Color,
)

object OinkTagDefaults {
    val Height = 28.dp

    @Composable
    fun colors(): OinkTagColors {
        val c = OinkTheme.colors
        return OinkTagColors(
            container = c.surface,
            border = c.line,
            content = c.text2,
            muted = c.text3,
            hover = c.fillHover,
        )
    }
}

@Immutable
data class OinkTagColors(
    val container: Color,
    val border: Color,
    val content: Color,
    val muted: Color,
    val hover: Color,
)

object OinkChipDefaults {
    val Height = 32.dp
    val HorizontalPadding = 14.dp

    @Composable
    fun colors(): OinkChipColors {
        val c = OinkTheme.colors
        return OinkChipColors(
            container = c.fill,
            hoverContainer = c.fillHover,
            selectedContainer = c.accentSoft,
            border = c.line,
            selectedBorder = c.accentRing,
            content = c.text2,
            selectedContent = c.accentText,
        )
    }
}

@Immutable
data class OinkChipColors(
    val container: Color,
    val hoverContainer: Color,
    val selectedContainer: Color,
    val border: Color,
    val selectedBorder: Color,
    val content: Color,
    val selectedContent: Color,
)