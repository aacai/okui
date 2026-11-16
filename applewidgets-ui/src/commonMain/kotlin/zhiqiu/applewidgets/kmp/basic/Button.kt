package zhiqiu.applewidgets.kmp.basic

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme
import zhiqiu.applewidgets.kmp.theme.LocalContentColor
import zhiqiu.applewidgets.kmp.theme.Motion

/** 按钮视觉变体。 */
enum class ButtonStyle {
    /** 中性底（fill）。 */
    Neutral,
    /** 强调色实心。 */
    Filled,
    /** 强调色淡底。 */
    Tinted,
    /** 透明底 + 强调色文字。 */
    Bordered,
    /** 纯文字。 */
    Plain,
    /** 反色底：当前前景色作底，背景色作字。 */
    Inverted,
    /** 危险色实心。 */
    Destructive,
}

/** 按钮状态机：空闲 / 加载 / 成功 / 失败。 */
enum class ButtonState {
    Idle,
    Loading,
    Success,
    Error,
}

/**
 * 按钮。
 *
 * 形状与高度来自 [zhiqiu.applewidgets.kmp.theme.ComponentStyles]（换皮肤即换外观），
 * 颜色来自 [ButtonColors]（由 [ButtonDefaults.colors] 从主题语义色派生），
 * 动效来自 [Motion]：按下回弹缩放、悬浮变色、加载 / 成功 / 失败状态字形。
 */
@Composable
fun Button(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    style: ButtonStyle = ButtonStyle.Filled,
    state: ButtonState = ButtonState.Idle,
    shape: Shape? = null,
    minWidth: Dp? = null,
    minHeight: Dp? = null,
    colors: ButtonColors = ButtonDefaults.colors(style),
    insideMargin: PaddingValues = ButtonDefaults.InsideMargin,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable RowScope.() -> Unit,
) {
    @Suppress("NAME_SHADOWING")
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val hovered by interactionSource.collectIsHoveredAsState()
    val busy = state != ButtonState.Idle
    // 仅加载态禁点击：成功 / 失败是瞬时反馈，仍需允许继续操作。
    val active = enabled && state != ButtonState.Loading

    val styles = AppleWidgetsTheme.componentStyles
    val buttonShape = shape ?: styles.buttonShape
    val buttonMinHeight = minHeight ?: styles.buttonMinHeight

    val scale by animateFloatAsState(
        targetValue = if (pressed && active) 0.955f else 1f,
        animationSpec = Motion.bouncy(),
        label = "buttonScale",
    )

    val shake = remember { Animatable(0f) }
    LaunchedEffect(state) {
        if (state == ButtonState.Error) {
            shake.snapTo(0f)
            shake.animateTo(0f, keyframes {
                durationMillis = Motion.Dur5
                -7f at (Motion.Dur5 * 0.15f).toInt()
                6f at (Motion.Dur5 * 0.35f).toInt()
                -4f at (Motion.Dur5 * 0.55f).toInt()
                2f at (Motion.Dur5 * 0.75f).toInt()
                0f at Motion.Dur5
            })
        }
    }

    val container = when {
        !enabled -> colors.disabledContainerColor
        hovered -> colors.hoveredContainerColor
        else -> colors.containerColor
    }
    val contentColor = if (enabled) colors.contentColor else colors.disabledContentColor

    val labelAlpha by animateFloatAsState(
        if (busy) 0f else 1f,
        tween(Motion.Dur2, easing = Motion.EaseOut),
        label = "labelAlpha",
    )
    val labelShift by animateDpAsState(
        if (busy) (-12).dp else 0.dp,
        tween(Motion.Dur3, easing = Motion.EaseOut),
        label = "labelShift",
    )
    val statusAlpha by animateFloatAsState(
        if (busy) 1f else 0f,
        tween(Motion.Dur2, easing = Motion.EaseOut),
        label = "statusAlpha",
    )
    val statusShift by animateDpAsState(
        if (busy) 0.dp else 12.dp,
        if (busy) Motion.bouncy(Motion.DpThreshold) else tween(Motion.Dur2, easing = Motion.EaseIn),
        label = "statusShift",
    )

    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Box(
            modifier = modifier
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    translationX = shake.value
                }
                .defaultMinSize(minWidth = minWidth ?: Dp.Unspecified, minHeight = buttonMinHeight)
                .clip(buttonShape)
                .background(container)
                .then(
                    if (colors.borderColor != Color.Transparent) {
                        Modifier.border(colors.borderWidth, colors.borderColor, buttonShape)
                    } else {
                        Modifier
                    },
                )
                .clickable(
                    interactionSource = interactionSource,
                    indication = LocalIndication.current,
                    enabled = active,
                    role = Role.Button,
                    onClick = onClick,
                )
                .padding(insideMargin),
            contentAlignment = Alignment.Center,
        ) {
            Row(
                modifier = Modifier.graphicsLayer {
                    alpha = labelAlpha
                    translationY = labelShift.toPx()
                },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                content = content,
            )
            if (busy) {
                Box(
                    modifier = Modifier.graphicsLayer {
                        alpha = statusAlpha
                        translationY = statusShift.toPx()
                    },
                    contentAlignment = Alignment.Center,
                ) {
                    ButtonStateGlyph(
                        state = state,
                        color = contentColor,
                        size = buttonMinHeight * 0.45f,
                    )
                }
            }
        }
    }
}

@Composable
fun Button(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    style: ButtonStyle = ButtonStyle.Filled,
    state: ButtonState = ButtonState.Idle,
    colors: ButtonColors = ButtonDefaults.colors(style),
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        style = style,
        state = state,
        colors = colors,
    ) {
        Text(
            text = text,
            style = AppleWidgetsTheme.textStyles.headline,
            color = LocalContentColor.current,
        )
    }
}

object ButtonDefaults {
    val InsideMargin = PaddingValues(horizontal = 20.dp, vertical = 12.dp)

    @Composable
    fun buttonColors(
        containerColor: Color = AppleWidgetsTheme.colorScheme.accent,
        contentColor: Color = AppleWidgetsTheme.colorScheme.onAccent,
        disabledContainerColor: Color = AppleWidgetsTheme.colorScheme.fill,
        disabledContentColor: Color = AppleWidgetsTheme.colorScheme.tertiaryLabel,
        hoveredContainerColor: Color = containerColor,
        borderColor: Color = Color.Transparent,
        borderWidth: Dp = 0.dp,
    ): ButtonColors = ButtonColors(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColor,
        hoveredContainerColor = hoveredContainerColor,
        borderColor = borderColor,
        borderWidth = borderWidth,
    )

    @Composable
    fun colors(style: ButtonStyle): ButtonColors {
        val c = AppleWidgetsTheme.colorScheme
        return when (style) {
            ButtonStyle.Neutral -> buttonColors(containerColor = c.fill, contentColor = c.label)
            ButtonStyle.Filled -> buttonColors()
            ButtonStyle.Tinted -> buttonColors(
                containerColor = c.accent.copy(alpha = 0.15f),
                contentColor = c.accent,
            )
            ButtonStyle.Bordered -> buttonColors(
                containerColor = Color.Transparent,
                contentColor = c.accent,
                disabledContainerColor = Color.Transparent,
            )
            ButtonStyle.Plain -> buttonColors(
                containerColor = Color.Transparent,
                contentColor = c.accent,
                disabledContainerColor = Color.Transparent,
            )
            ButtonStyle.Inverted -> buttonColors(
                containerColor = c.label,
                contentColor = c.systemBackground,
            )
            ButtonStyle.Destructive -> buttonColors(
                containerColor = c.destructive,
                contentColor = Color.White,
            )
        }
    }
}

@Immutable
data class ButtonColors(
    val containerColor: Color,
    val contentColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color,
    val hoveredContainerColor: Color = containerColor,
    val borderColor: Color = Color.Transparent,
    val borderWidth: Dp = 0.dp,
)

