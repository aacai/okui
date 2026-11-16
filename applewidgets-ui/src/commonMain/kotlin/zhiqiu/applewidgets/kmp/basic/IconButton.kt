package zhiqiu.applewidgets.kmp.basic

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme
import zhiqiu.applewidgets.kmp.theme.LocalContentColor
import zhiqiu.applewidgets.kmp.theme.Motion

/**
 * 图标按钮：按下回弹缩放，支持 [ButtonState] 状态字形（加载 / 成功 / 失败）。
 */
@Composable
fun IconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    state: ButtonState = ButtonState.Idle,
    size: Dp = IconButtonDefaults.Size,
    content: @Composable () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val busy = state != ButtonState.Idle
    val scale by animateFloatAsState(
        targetValue = if (pressed && enabled && !busy) 0.92f else 1f,
        animationSpec = Motion.bouncy(),
        label = "iconButtonScale",
    )
    val contentColor = if (enabled) {
        AppleWidgetsTheme.colorScheme.accent
    } else {
        AppleWidgetsTheme.colorScheme.tertiaryLabel
    }

    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .size(size)
            .clip(CircleShape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled && !busy,
                role = Role.Button,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            if (busy) {
                Box(contentAlignment = Alignment.Center) {
                    ButtonStateGlyph(state = state, color = contentColor, size = size * 0.45f)
                }
            } else {
                content()
            }
        }
    }
}

object IconButtonDefaults {
    val Size: Dp = 44.dp
}
