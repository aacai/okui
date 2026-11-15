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
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

/**
 * Oink 线性进度：8 高凹槽轨道 + accent 填充。[progress] 为 null 时进入往返滑动不确定态。
 */
@Composable
fun OinkProgress(
    modifier: Modifier = Modifier,
    progress: Float? = null,
    color: Color = Color.Unspecified,
    colors: OinkProgressColors = OinkProgressDefaults.colors(),
) {
    val fill = if (color != Color.Unspecified) color else colors.indicator
    if (progress != null) {
        val animated by animateFloatAsState(progress.coerceIn(0f, 1f), OinkMotion.spring(), label = "progress")
        BoxWithConstraints(
            modifier = modifier
                .fillMaxWidth()
                .height(OinkProgressDefaults.Height)
                .clip(CircleShape)
                .background(colors.track),
        ) {
            val w = constraints.maxWidth.toFloat()
            Box(
                Modifier
                    .align(Alignment.CenterStart)
                    .width(with(LocalDensity.current) { (w * animated).toDp() })
                    .fillMaxHeight()
                    .clip(CircleShape)
                    .background(fill),
            )
        }
    } else {
        val transition = rememberInfiniteTransition(label = "indeterminate")
        val t by transition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(tween(1100, easing = LinearEasing)),
            label = "slide",
        )
        BoxWithConstraints(
            modifier = modifier
                .fillMaxWidth()
                .height(OinkProgressDefaults.Height)
                .clip(CircleShape)
                .background(colors.track),
        ) {
            val w = constraints.maxWidth.toFloat()
            val segW = w * 0.35f
            val x = (t * 1.35f - 0.35f) * w
            Box(
                Modifier
                    .align(Alignment.CenterStart)
                    .offset { IntOffset(x.roundToInt(), 0) }
                    .width(with(LocalDensity.current) { segW.toDp() })
                    .fillMaxHeight()
                    .clip(CircleShape)
                    .background(fill),
            )
        }
    }
}

/**
 * Oink 环形进度：well2 轨道 + accent 圆弧（圆头），中心可放内容。
 */
@Composable
fun OinkRing(
    progress: Float,
    modifier: Modifier = Modifier,
    size: Dp = OinkProgressDefaults.RingSize,
    strokeWidth: Dp = OinkProgressDefaults.RingStrokeWidth,
    color: Color = Color.Unspecified,
    colors: OinkProgressColors = OinkProgressDefaults.colors(),
    content: @Composable () -> Unit = {},
) {
    val fill = if (color != Color.Unspecified) color else colors.indicator
    val animated by animateFloatAsState(progress.coerceIn(0f, 1f), OinkMotion.spring(), label = "ring")
    Box(modifier = modifier.size(size), contentAlignment = Alignment.Center) {
        Canvas(Modifier.size(size)) {
            val stroke = strokeWidth.toPx()
            val inset = stroke / 2f
            val arcSize = Size(this.size.width - stroke, this.size.height - stroke)
            val topLeft = Offset(inset, inset)
            drawArc(
                color = colors.track,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(stroke, cap = StrokeCap.Round),
            )
            drawArc(
                color = fill,
                startAngle = -90f,
                sweepAngle = 360f * animated,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(stroke, cap = StrokeCap.Round),
            )
        }
        content()
    }
}

object OinkProgressDefaults {
    val Height = 8.dp
    val RingSize = 64.dp
    val RingStrokeWidth = 6.dp

    @Composable
    fun colors(): OinkProgressColors {
        val c = OinkTheme.colors
        return OinkProgressColors(track = c.well2, indicator = c.accent)
    }
}

@Immutable
data class OinkProgressColors(
    val track: Color,
    val indicator: Color,
)