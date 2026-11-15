package zhiqiu.applewidgets.kmp.oink.overlay

import zhiqiu.applewidgets.kmp.oink.basic.*
import zhiqiu.applewidgets.kmp.oink.data.*
import zhiqiu.applewidgets.kmp.oink.form.*
import zhiqiu.applewidgets.kmp.oink.layout.*
import zhiqiu.applewidgets.kmp.oink.nav.*
import zhiqiu.applewidgets.kmp.oink.primitives.*
import zhiqiu.applewidgets.kmp.oink.responsive.*
import zhiqiu.applewidgets.kmp.oink.theme.*

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

enum class OinkToastVariant { Neutral, Success, Danger, Info }

/**
 * Oink 吐司：20 圆角 pop 浮层，图标 + 文案 + 可选操作 / 关闭。
 */
@Composable
fun OinkToast(
    message: String,
    modifier: Modifier = Modifier,
    variant: OinkToastVariant = OinkToastVariant.Neutral,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null,
    colors: OinkToastColors = OinkToastDefaults.colors(variant),
) {
    val icon = when (variant) {
        OinkToastVariant.Neutral -> OinkIcons.Info
        OinkToastVariant.Success -> OinkIcons.CheckCircle
        OinkToastVariant.Danger -> OinkIcons.XCircle
        OinkToastVariant.Info -> OinkIcons.Info
    }
    val shape = OinkShape(OinkToastDefaults.CornerRadius)
    Row(
        modifier = modifier
            .widthIn(min = OinkToastDefaults.MinWidth, max = OinkToastDefaults.MaxWidth)
            .clip(shape)
            .background(colors.container)
            .border(OinkMetrics.Hairline, colors.border, shape)
            .padding(start = 14.dp, end = 8.dp, top = 10.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OinkIcon(icon, null, tint = colors.icon, size = 18.dp, modifier = Modifier.padding(end = 10.dp))
        OinkText(text = message, color = colors.content, style = OinkTheme.textStyles.caption, modifier = Modifier.weight(1f, fill = false))
        if (actionLabel != null && onAction != null) {
            Box(
                Modifier
                    .padding(start = 10.dp)
                    .clip(OinkShape(OinkRadii.r1))
                    .clickable(onClick = onAction)
                    .padding(horizontal = 10.dp, vertical = 6.dp),
            ) {
                OinkText(text = actionLabel, color = colors.action, style = OinkTheme.textStyles.caption.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold))
            }
        }
        if (onDismiss != null) {
            Box(
                Modifier
                    .padding(start = 4.dp)
                    .size(26.dp)
                    .clip(OinkShape(OinkRadii.r1))
                    .clickable(onClick = onDismiss),
                contentAlignment = Alignment.Center,
            ) {
                OinkIcon(OinkIcons.X, "Dismiss", tint = colors.dismiss, size = 14.dp)
            }
        }
    }
}

/** 单条吐司状态：show() 触发，3 秒后自动收起。 */
class OinkToastState {
    var visible by mutableStateOf(false)
        internal set
    var message by mutableStateOf("")
        internal set
    var variant by mutableStateOf(OinkToastVariant.Neutral)
        internal set

    fun show(message: String, variant: OinkToastVariant = OinkToastVariant.Success) {
        this.message = message
        this.variant = variant
        this.visible = true
    }

    fun dismiss() {
        visible = false
    }
}

@Composable
fun rememberOinkToastState(): OinkToastState = remember { OinkToastState() }

/**
 * 吐司宿主：放在页面根部，吐司从底部滑入。
 */
@Composable
fun OinkToastHost(
    state: OinkToastState,
    modifier: Modifier = Modifier,
    durationMillis: Long = 3000L,
) {
    LaunchedEffect(state.visible, state.message) {
        if (state.visible) {
            delay(durationMillis)
            state.dismiss()
        }
    }
    Box(modifier = modifier, contentAlignment = Alignment.BottomCenter) {
        AnimatedVisibility(
            visible = state.visible,
            enter = fadeIn() + slideInVertically { it / 2 + 24 },
            exit = fadeOut() + slideOutVertically { it / 2 + 24 },
        ) {
            OinkToast(
                message = state.message,
                variant = state.variant,
                onDismiss = { state.dismiss() },
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}

object OinkToastDefaults {
    val MinWidth = 220.dp
    val MaxWidth = 420.dp
    val CornerRadius = 20.dp

    /** 不同语义变体取对应前景图标色，容器始终为 pop 浮层。 */
    @Composable
    fun colors(variant: OinkToastVariant = OinkToastVariant.Neutral): OinkToastColors {
        val c = OinkTheme.colors
        val icon = when (variant) {
            OinkToastVariant.Neutral -> c.text2
            OinkToastVariant.Success -> c.successText
            OinkToastVariant.Danger -> c.dangerText
            OinkToastVariant.Info -> c.infoText
        }
        return OinkToastColors(
            container = c.pop,
            border = c.line,
            content = c.text,
            icon = icon,
            action = c.accentText,
            dismiss = c.text3,
        )
    }
}

@Immutable
data class OinkToastColors(
    val container: Color,
    val border: Color,
    val content: Color,
    val icon: Color,
    val action: Color,
    val dismiss: Color,
)