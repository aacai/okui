package zhiqiu.applewidgets.kmp.oink.basic

import zhiqiu.applewidgets.kmp.oink.data.*
import zhiqiu.applewidgets.kmp.oink.form.*
import zhiqiu.applewidgets.kmp.oink.layout.*
import zhiqiu.applewidgets.kmp.oink.nav.*
import zhiqiu.applewidgets.kmp.oink.overlay.*
import zhiqiu.applewidgets.kmp.oink.primitives.*
import zhiqiu.applewidgets.kmp.oink.responsive.*
import zhiqiu.applewidgets.kmp.oink.theme.*

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

enum class OinkAlertVariant { Info, Success, Warning, Danger }

/**
 * Oink 提示条：语义柔和底 + 同色图标与标题，16 圆角。
 */
@Composable
fun OinkAlert(
    title: String,
    modifier: Modifier = Modifier,
    variant: OinkAlertVariant = OinkAlertVariant.Info,
    text: String? = null,
    icon: ImageVector? = null,
    colors: OinkAlertColors = OinkAlertDefaults.colors(variant),
) {
    val defaultIcon = when (variant) {
        OinkAlertVariant.Info -> OinkIcons.Info
        OinkAlertVariant.Success -> OinkIcons.CheckCircle
        OinkAlertVariant.Warning -> OinkIcons.Alert
        OinkAlertVariant.Danger -> OinkIcons.XCircle
    }
    val shape = OinkShape(OinkRadii.r3)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(colors.container)
            .border(OinkMetrics.Hairline, colors.border, shape)
            .padding(OinkAlertDefaults.Padding),
        verticalAlignment = Alignment.Top,
    ) {
        OinkIcon(icon ?: defaultIcon, null, tint = colors.content, size = 18.dp, modifier = Modifier.padding(end = 11.dp, top = 1.dp))
        Column(Modifier.weight(1f)) {
            OinkText(text = title, color = colors.content, style = OinkTheme.textStyles.label)
            if (text != null) {
                OinkText(
                    text = text,
                    color = colors.description,
                    style = OinkTheme.textStyles.caption,
                    modifier = Modifier.padding(top = 3.dp),
                )
            }
        }
    }
}

/**
 * Oink 空状态：圆形图标托底 + 标题 + 说明 + 可选操作。
 */
@Composable
fun OinkEmptyState(
    title: String,
    modifier: Modifier = Modifier,
    text: String? = null,
    icon: ImageVector = OinkIcons.Inbox,
    action: (@Composable () -> Unit)? = null,
    colors: OinkEmptyStateColors = OinkEmptyStateDefaults.colors(),
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(OinkEmptyStateDefaults.Padding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            Modifier.size(OinkEmptyStateDefaults.IconContainerSize).clip(CircleShape).background(colors.iconContainer),
            contentAlignment = Alignment.Center,
        ) {
            OinkIcon(icon, null, tint = colors.icon, size = 24.dp)
        }
        OinkText(
            text = title,
            color = colors.title,
            style = OinkTheme.textStyles.h4,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp),
        )
        if (text != null) {
            OinkText(
                text = text,
                color = colors.description,
                style = OinkTheme.textStyles.caption,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 6.dp),
            )
        }
        if (action != null) {
            Box(Modifier.padding(top = 18.dp)) { action() }
        }
    }
}

/**
 * Oink 骨架屏：well 底 + 一道横向扫过的高光，用于加载占位。
 */
@Composable
fun OinkSkeleton(
    modifier: Modifier = Modifier,
    shape: Shape = OinkShape(OinkRadii.r2),
    colors: OinkSkeletonColors = OinkSkeletonDefaults.colors(),
) {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val t by transition.animateFloat(
        initialValue = -1f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(tween(1300, easing = LinearEasing)),
        label = "sweep",
    )
    val base = colors.base
    val highlight = colors.highlight
    Box(
        modifier = modifier
            .clip(shape)
            .background(base)
            .drawBehind {
                val w = size.width
                val start = t * w
                drawRect(
                    brush = Brush.linearGradient(
                        colors = listOf(Color.Transparent, highlight, Color.Transparent),
                        start = Offset(start, 0f),
                        end = Offset(start + w, size.height),
                    ),
                )
            },
    )
}

/**
 * Oink 键帽：24 高、7 圆角、描边 + 底部内阴影，等宽字体。
 */
@Composable
fun OinkKbd(text: String, modifier: Modifier = Modifier, colors: OinkKbdColors = OinkKbdDefaults.colors()) {
    val shape = OinkShape(OinkKbdDefaults.CornerRadius)
    Box(
        modifier = modifier
            .height(OinkKbdDefaults.Height)
            .clip(shape)
            .background(colors.container)
            .drawBehind {
                drawLine(
                    color = colors.border,
                    start = Offset(0f, size.height - 0.75.dp.toPx()),
                    end = Offset(size.width, size.height - 0.75.dp.toPx()),
                    strokeWidth = 1.5.dp.toPx(),
                )
            }
            .border(OinkMetrics.Hairline, colors.border, shape)
            .padding(horizontal = OinkKbdDefaults.HorizontalPadding),
        contentAlignment = Alignment.Center,
    ) {
        OinkText(text = text, color = colors.content, style = OinkTheme.textStyles.mono)
    }
}

object OinkAlertDefaults {
    val Padding = 14.dp

    /** 不同语义变体取对应柔和底与同色前景。 */
    @Composable
    fun colors(variant: OinkAlertVariant = OinkAlertVariant.Info): OinkAlertColors {
        val c = OinkTheme.colors
        val (bg, fg) = when (variant) {
            OinkAlertVariant.Info -> c.infoSoft to c.infoText
            OinkAlertVariant.Success -> c.successSoft to c.successText
            OinkAlertVariant.Warning -> c.warningSoft to c.warningText
            OinkAlertVariant.Danger -> c.dangerSoft to c.dangerText
        }
        return OinkAlertColors(
            container = bg,
            border = fg.copy(alpha = 0.18f),
            content = fg,
            description = c.text2,
        )
    }
}

@Immutable
data class OinkAlertColors(
    val container: Color,
    val border: Color,
    val content: Color,
    val description: Color,
)

object OinkEmptyStateDefaults {
    val Padding = 32.dp
    val IconContainerSize = 56.dp

    @Composable
    fun colors(): OinkEmptyStateColors {
        val c = OinkTheme.colors
        return OinkEmptyStateColors(
            iconContainer = c.well,
            icon = c.text3,
            title = c.text,
            description = c.text3,
        )
    }
}

@Immutable
data class OinkEmptyStateColors(
    val iconContainer: Color,
    val icon: Color,
    val title: Color,
    val description: Color,
)

object OinkSkeletonDefaults {
    @Composable
    fun colors(): OinkSkeletonColors {
        val c = OinkTheme.colors
        return OinkSkeletonColors(base = c.well, highlight = c.wellHi)
    }
}

@Immutable
data class OinkSkeletonColors(
    val base: Color,
    val highlight: Color,
)

object OinkKbdDefaults {
    val Height = 24.dp
    val CornerRadius = 7.dp
    val HorizontalPadding = 7.dp

    @Composable
    fun colors(): OinkKbdColors {
        val c = OinkTheme.colors
        return OinkKbdColors(container = c.surface, border = c.line2, content = c.text2)
    }
}

@Immutable
data class OinkKbdColors(
    val container: Color,
    val border: Color,
    val content: Color,
)