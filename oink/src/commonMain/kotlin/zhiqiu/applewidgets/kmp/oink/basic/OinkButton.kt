package zhiqiu.applewidgets.kmp.oink.basic

import zhiqiu.applewidgets.kmp.oink.data.*
import zhiqiu.applewidgets.kmp.oink.form.*
import zhiqiu.applewidgets.kmp.oink.layout.*
import zhiqiu.applewidgets.kmp.oink.nav.*
import zhiqiu.applewidgets.kmp.oink.overlay.*
import zhiqiu.applewidgets.kmp.oink.primitives.*
import zhiqiu.applewidgets.kmp.oink.responsive.*
import zhiqiu.applewidgets.kmp.oink.theme.*

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import zhiqiu.applewidgets.kmp.basic.Button as AppleButton
import zhiqiu.applewidgets.kmp.basic.ButtonColors
import zhiqiu.applewidgets.kmp.basic.ButtonState

enum class OinkButtonVariant { Soft, Primary, Accent, Ghost, Outline, Danger }
enum class OinkButtonSize { Sm, Md, Lg }
enum class OinkButtonState { Idle, Loading, Success, Error }

/**
 * Oink 按钮：委托组件库的 [AppleButton]，只保留 Oink 语义色与尺寸档位。
 * 形状 / 高度 / 动效 / 状态机全部复用组件库实现，Oink 只负责"长什么样"。
 */
@Composable
fun OinkButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    variant: OinkButtonVariant = OinkButtonVariant.Soft,
    size: OinkButtonSize = OinkButtonSize.Md,
    state: OinkButtonState = OinkButtonState.Idle,
    colors: OinkButtonColors = OinkButtonDefaults.colors(variant),
    interactionSource: MutableInteractionSource? = null,
    insideMargin: PaddingValues? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val height = size.height
    AppleButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        state = state.toButtonState(),
        shape = CircleShape,
        minWidth = height,
        minHeight = height,
        colors = colors.toButtonColors(),
        insideMargin = insideMargin ?: PaddingValues(horizontal = height * 0.48f),
        interactionSource = interactionSource,
        content = content,
    )
}

@Composable
fun OinkButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    variant: OinkButtonVariant = OinkButtonVariant.Soft,
    size: OinkButtonSize = OinkButtonSize.Md,
    state: OinkButtonState = OinkButtonState.Idle,
    leadingIcon: ImageVector? = null,
) {
    val fontSize = when (size) {
        OinkButtonSize.Sm -> 13
        OinkButtonSize.Md -> 15
        OinkButtonSize.Lg -> 16
    }
    val iconSize = if (size == OinkButtonSize.Sm) 16.dp else 18.dp
    OinkButton(onClick, modifier, enabled, variant, size, state) {
        if (leadingIcon != null) {
            OinkIcon(leadingIcon, null, tint = OinkButtonDefaults.colors(variant).content, size = iconSize)
        }
        OinkText(
            text = text,
            style = OinkTheme.textStyles.button.copy(fontSize = fontSize.sp),
            color = OinkButtonDefaults.colors(variant).content,
        )
    }
}

@Composable
fun OinkIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    enabled: Boolean = true,
    variant: OinkButtonVariant = OinkButtonVariant.Ghost,
    size: OinkButtonSize = OinkButtonSize.Md,
) {
    OinkButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        variant = variant,
        size = size,
        insideMargin = PaddingValues(0.dp),
    ) {
        OinkIcon(
            icon,
            contentDescription,
            tint = OinkButtonDefaults.colors(variant).content,
            size = if (size == OinkButtonSize.Sm) 16.dp else 18.dp,
        )
    }
}

object OinkButtonDefaults {
    @Composable
    fun colors(variant: OinkButtonVariant): OinkButtonColors {
        val c = OinkTheme.colors
        return when (variant) {
            OinkButtonVariant.Soft -> OinkButtonColors(c.fill, c.fillHover, c.text)
            OinkButtonVariant.Primary -> OinkButtonColors(c.inverse, c.inverseHover, c.onInverse)
            OinkButtonVariant.Accent -> OinkButtonColors(c.accentFill, c.accentFillHover, c.onAccent)
            OinkButtonVariant.Ghost -> OinkButtonColors(Color.Transparent, c.fill, c.text)
            OinkButtonVariant.Outline -> OinkButtonColors(Color.Transparent, c.fill, c.text, c.line2)
            OinkButtonVariant.Danger -> OinkButtonColors(c.dangerSoft, c.dangerSoft2, c.dangerText)
        }
    }
}

@Immutable
data class OinkButtonColors(
    val container: Color,
    val hoveredContainer: Color,
    val content: Color,
    val border: Color = Color.Transparent,
)

private val OinkButtonSize.height: Dp
    get() = when (this) {
        OinkButtonSize.Sm -> OinkMetrics.HeightSm
        OinkButtonSize.Md -> OinkMetrics.HeightMd
        OinkButtonSize.Lg -> OinkMetrics.HeightLg
    }

private fun OinkButtonState.toButtonState(): ButtonState = when (this) {
    OinkButtonState.Idle -> ButtonState.Idle
    OinkButtonState.Loading -> ButtonState.Loading
    OinkButtonState.Success -> ButtonState.Success
    OinkButtonState.Error -> ButtonState.Error
}

private fun OinkButtonColors.toButtonColors(): ButtonColors = ButtonColors(
    containerColor = container,
    contentColor = content,
    disabledContainerColor = container.copy(alpha = 0.42f),
    disabledContentColor = content.copy(alpha = 0.42f),
    hoveredContainerColor = hoveredContainer,
    borderColor = border,
    borderWidth = if (border == Color.Transparent) 0.dp else OinkMetrics.ControlStroke,
)
