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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.liquid.LiquidGlassScrim
import zhiqiu.applewidgets.kmp.overlay.PlatformDialogOverlay

/**
 * Oink 对话框：32 超椭圆圆角纸面 + 遮罩，弹入缩放回弹。底部为取消 / 确认操作。
 */
@Composable
fun OinkDialog(
    onDismissRequest: () -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    text: String? = null,
    icon: ImageVector? = null,
    destructive: Boolean = false,
    confirmLabel: String = "Confirm",
    onConfirm: (() -> Unit)? = null,
    dismissLabel: String = "Cancel",
    onDismiss: (() -> Unit)? = null,
    colors: OinkDialogColors = OinkDialogDefaults.colors(),
    content: (@Composable () -> Unit)? = null,
) {
    val enter = remember { Animatable(0f) }
    LaunchedEffect(Unit) { enter.animateTo(1f, OinkMotion.spring()) }
    PlatformDialogOverlay(onDismissRequest = onDismissRequest) {
        LiquidGlassScrim(
            color = colors.scrim,
            onClick = onDismissRequest,
            contentAlignment = Alignment.Center,
        ) {
            val shape = OinkShape(OinkRadii.r5)
            Column(
                modifier = modifier
                    .graphicsLayer {
                        val s = 0.92f + 0.08f * enter.value
                        scaleX = s
                        scaleY = s
                        alpha = enter.value
                    }
                    .widthIn(min = OinkDialogDefaults.MinWidth, max = OinkDialogDefaults.MaxWidth)
                    .clip(shape)
                    .background(colors.container)
                    .border(OinkMetrics.Hairline, colors.border, shape)
                    .clickable(enabled = false, onClick = {})
                    .padding(24.dp),
            ) {
                if (icon != null) {
                    val tint = if (destructive) colors.destructiveIcon else colors.icon
                    val bg = if (destructive) colors.destructiveIconContainer else colors.iconContainer
                    Box(
                        Modifier.size(OinkDialogDefaults.IconSize).clip(CircleShape).background(bg),
                        contentAlignment = Alignment.Center,
                    ) {
                        OinkIcon(icon, null, tint = tint, size = 22.dp)
                    }
                }
                OinkText(
                    text = title,
                    color = colors.title,
                    style = OinkTheme.textStyles.h3,
                    modifier = Modifier.padding(top = if (icon != null) 16.dp else 0.dp),
                )
                if (text != null) {
                    OinkText(
                        text = text,
                        color = colors.description,
                        style = OinkTheme.textStyles.body,
                        modifier = Modifier.padding(top = 8.dp),
                    )
                }
                if (content != null) {
                    Box(Modifier.padding(top = 16.dp)) { content() }
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.End),
                ) {
                    OinkButton(
                        text = dismissLabel,
                        onClick = { (onDismiss ?: onDismissRequest).invoke() },
                        variant = OinkButtonVariant.Soft,
                    )
                    OinkButton(
                        text = confirmLabel,
                        onClick = { (onConfirm ?: onDismissRequest).invoke() },
                        variant = if (destructive) OinkButtonVariant.Danger else OinkButtonVariant.Accent,
                    )
                }
            }
        }
    }
}

object OinkDialogDefaults {
    val MinWidth = 280.dp
    val MaxWidth = 400.dp
    val IconSize = 44.dp

    @Composable
    fun colors(): OinkDialogColors {
        val c = OinkTheme.colors
        return OinkDialogColors(
            scrim = c.scrim,
            container = c.surface,
            border = c.line,
            title = c.text,
            description = c.text2,
            iconContainer = c.accentSoft,
            icon = c.accentText,
            destructiveIconContainer = c.dangerSoft,
            destructiveIcon = c.dangerText,
        )
    }
}

@Immutable
data class OinkDialogColors(
    val scrim: Color,
    val container: Color,
    val border: Color,
    val title: Color,
    val description: Color,
    val iconContainer: Color,
    val icon: Color,
    val destructiveIconContainer: Color,
    val destructiveIcon: Color,
)