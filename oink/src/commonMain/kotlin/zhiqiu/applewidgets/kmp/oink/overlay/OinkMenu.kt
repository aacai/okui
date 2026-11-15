package zhiqiu.applewidgets.kmp.oink.overlay

import zhiqiu.applewidgets.kmp.oink.basic.*
import zhiqiu.applewidgets.kmp.oink.data.*
import zhiqiu.applewidgets.kmp.oink.form.*
import zhiqiu.applewidgets.kmp.oink.layout.*
import zhiqiu.applewidgets.kmp.oink.nav.*
import zhiqiu.applewidgets.kmp.oink.primitives.*
import zhiqiu.applewidgets.kmp.oink.responsive.*
import zhiqiu.applewidgets.kmp.oink.theme.*

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.overlay.PlatformAnchoredPopup

/**
 * Oink 菜单浮层：pop 底色、18 圆角、6 内边距，弹出时轻微缩放回弹。
 */
@Composable
fun OinkMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    colors: OinkMenuColors = OinkMenuDefaults.colors(),
    content: @Composable ColumnScope.() -> Unit,
) {
    if (!expanded) return
    val enter = remember { Animatable(0f) }
    LaunchedEffect(Unit) { enter.animateTo(1f, OinkMotion.spring()) }
    PlatformAnchoredPopup(onDismissRequest = onDismissRequest) {
        Column(
            modifier = modifier
                .graphicsLayer {
                    val s = 0.92f + 0.08f * enter.value
                    scaleX = s
                    scaleY = s
                    alpha = enter.value
                    transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0.5f, 0f)
                }
                .widthIn(min = OinkMenuDefaults.MinWidth)
                .clip(OinkShape(OinkMenuDefaults.CornerRadius))
                .background(colors.container)
                .border(OinkMetrics.Hairline, colors.border, OinkShape(OinkMenuDefaults.CornerRadius))
                .padding(OinkMenuDefaults.Padding),
            content = content,
        )
    }
}

/**
 * Oink 菜单项：36 高、12 圆角，hover 浅填充，选中显示 accent 勾。
 */
@Composable
fun OinkMenuItem(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    trailing: String? = null,
    selected: Boolean = false,
    destructive: Boolean = false,
    colors: OinkMenuItemColors = OinkMenuItemDefaults.colors(),
) {
    val interaction = remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()
    val contentColor = when {
        !enabled -> colors.disabledContent
        destructive -> colors.destructiveContent
        selected -> colors.selectedContent
        else -> colors.content
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(OinkMetrics.ItemHeight)
            .clip(OinkShape(OinkRadii.r2))
            .background(if (hovered && enabled) colors.hover else androidx.compose.ui.graphics.Color.Transparent)
            .hoverable(interaction)
            .clickable(interactionSource = interaction, indication = null, enabled = enabled, onClick = onClick)
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (leadingIcon != null) {
            OinkIcon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = when {
                    destructive -> colors.destructiveContent
                    selected -> colors.selectedIcon
                    else -> colors.icon
                },
                modifier = Modifier.padding(end = 10.dp).size(16.dp),
            )
        }
        OinkText(
            text = text,
            color = contentColor,
            style = OinkTheme.textStyles.menuItem,
            modifier = Modifier.weight(1f),
        )
        if (trailing != null) {
            OinkText(
                text = trailing,
                color = colors.trailing,
                style = OinkTheme.textStyles.caption,
                modifier = Modifier.padding(start = 10.dp),
            )
        }
        if (selected) {
            OinkIcon(
                imageVector = OinkIcons.Check,
                contentDescription = null,
                tint = colors.check,
                modifier = Modifier.padding(start = 10.dp).size(15.dp),
            )
        }
    }
}

@Composable
fun OinkMenuSeparator(modifier: Modifier = Modifier, colors: OinkMenuItemColors = OinkMenuItemDefaults.colors()) {
    Box(
        modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 6.dp)
            .height(OinkMetrics.Hairline)
            .background(colors.separator),
    )
}

@Composable
fun OinkMenuLabel(
    text: String,
    modifier: Modifier = Modifier,
    colors: OinkMenuItemColors = OinkMenuItemDefaults.colors(),
) {
    OinkText(
        text = text,
        color = colors.label,
        style = OinkTheme.textStyles.micro,
        modifier = modifier.padding(start = 10.dp, top = 6.dp, bottom = 4.dp),
    )
}

object OinkMenuDefaults {
    val MinWidth = 180.dp
    val CornerRadius = 18.dp
    val Padding = 6.dp

    @Composable
    fun colors(): OinkMenuColors {
        val c = OinkTheme.colors
        return OinkMenuColors(container = c.pop, border = c.line)
    }
}

@Immutable
data class OinkMenuColors(
    val container: Color,
    val border: Color,
)

object OinkMenuItemDefaults {
    @Composable
    fun colors(): OinkMenuItemColors {
        val c = OinkTheme.colors
        return OinkMenuItemColors(
            content = c.text,
            disabledContent = c.text3,
            destructiveContent = c.dangerText,
            selectedContent = c.accentText,
            selectedIcon = c.accent,
            icon = c.text2,
            trailing = c.text3,
            label = c.text3,
            separator = c.line,
            hover = c.fillHover,
            check = c.accent,
        )
    }
}

@Immutable
data class OinkMenuItemColors(
    val content: Color,
    val disabledContent: Color,
    val destructiveContent: Color,
    val selectedContent: Color,
    val selectedIcon: Color,
    val icon: Color,
    val trailing: Color,
    val label: Color,
    val separator: Color,
    val hover: Color,
    val check: Color,
)