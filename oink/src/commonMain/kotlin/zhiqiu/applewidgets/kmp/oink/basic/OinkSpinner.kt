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
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/** 圆环加载：well2 底环 + 强调色四分之一弧，0.8s 匀速旋转。 */
@Composable
fun OinkSpinner(
    modifier: Modifier = Modifier,
    size: Dp = OinkSpinnerDefaults.Size,
    color: Color = Color.Unspecified,
    trackColor: Color = Color.Unspecified,
    strokeWidth: Dp = OinkSpinnerDefaults.StrokeWidth,
    colors: OinkSpinnerColors = OinkSpinnerDefaults.colors(),
) {
    val arc = if (color != Color.Unspecified) color else colors.indicator
    val track = if (trackColor != Color.Unspecified) trackColor else colors.track
    val transition = rememberInfiniteTransition(label = "spinner")
    val angle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(800, easing = LinearEasing), RepeatMode.Restart),
        label = "angle",
    )
    Canvas(modifier.size(size)) {
        val sw = strokeWidth.toPx()
        val r = (this.size.minDimension - sw) / 2f
        val c = Offset(this.size.width / 2f, this.size.height / 2f)
        drawCircle(track, radius = r, center = c, style = Stroke(sw))
        rotate(angle, pivot = c) {
            drawArc(
                color = arc,
                startAngle = -90f,
                sweepAngle = 90f,
                useCenter = false,
                topLeft = Offset(c.x - r, c.y - r),
                size = androidx.compose.ui.geometry.Size(r * 2, r * 2),
                style = Stroke(sw, cap = StrokeCap.Round),
            )
        }
    }
}

/** 三点加载：依次上跳，1s 循环。 */
@Composable
fun OinkDots(
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    dotSize: Dp = OinkDotsDefaults.DotSize,
    colors: OinkDotsColors = OinkDotsDefaults.colors(),
) {
    val dotColor = if (color != Color.Unspecified) color else colors.dot
    val transition = rememberInfiniteTransition(label = "dots")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1000, easing = OinkMotion.EaseInOut), RepeatMode.Restart),
        label = "phase",
    )
    Canvas(modifier.size(width = dotSize * 3 + 10.dp, height = dotSize + 7.dp)) {
        val d = dotSize.toPx()
        val gap = 5.dp.toPx()
        val baseY = this.size.height - d / 2f
        for (i in 0..2) {
            val p = (phase - i * 0.12f + 1f) % 1f
            // 0/60/100 静止，30% 跳到最高
            val bump = if (p < 0.6f) sin(PI.toFloat() * (p / 0.6f)).coerceAtLeast(0f) else 0f
            val y = baseY - bump * 7.dp.toPx()
            val alpha = 0.45f + 0.55f * bump
            drawCircle(dotColor.copy(alpha = alpha), radius = d / 2f, center = Offset(d / 2f + i * (d + gap), y))
        }
    }
}

private const val TailPathData = "M9 10C9 26 20 38 31 38c7 0 11-5 11-10.5S37.5 18 32 18c-5 0-8.5 3.5-9 8"

/** 品牌加载「猪尾巴」：绕左上角摆动 + 描线游走，1.5s 循环。 */
@Composable
fun OinkTail(
    modifier: Modifier = Modifier,
    size: Dp = OinkTailDefaults.Size,
    color: Color = Color.Unspecified,
    colors: OinkTailColors = OinkTailDefaults.colors(),
) {
    val ink = if (color != Color.Unspecified) color else colors.ink
    val path = remember { PathParser().parsePathString(TailPathData).toPath(Path()) }
    val measure = remember { PathMeasure() }
    val transition = rememberInfiniteTransition(label = "tail")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1500, easing = OinkMotion.EaseInOut), RepeatMode.Restart),
        label = "phase",
    )
    Canvas(modifier.size(size)) {
        val s = this.size.minDimension / 48f
        val sw = 5.5f * s
        val offset = when {
            phase < 0.45f -> 1.05f * (1f - phase / 0.45f)
            phase < 0.55f -> 0f
            else -> -1.05f * ((phase - 0.55f) / 0.45f)
        }
        val start = max(0f, -offset)
        val end = min(1f, 1f - offset)
        val wag = 1f - 11f * cos(2f * PI.toFloat() * phase)
        rotate(wag, pivot = Offset(9.6f * s, 9.6f * s)) {
            scale(s, s, pivot = Offset.Zero) {
                if (end > start) {
                    measure.setPath(path, false)
                    val dst = Path()
                    measure.getSegment(start * measure.length, end * measure.length, dst, true)
                    drawPath(dst, color = ink, style = Stroke(sw / s, cap = StrokeCap.Round))
                }
            }
        }
    }
}

object OinkSpinnerDefaults {
    val Size = 28.dp
    val StrokeWidth = 2.5.dp

    @Composable
    fun colors(): OinkSpinnerColors {
        val c = OinkTheme.colors
        return OinkSpinnerColors(indicator = c.accent, track = c.well2)
    }
}

@Immutable
data class OinkSpinnerColors(
    val indicator: Color,
    val track: Color,
)

object OinkDotsDefaults {
    val DotSize = 8.dp

    @Composable
    fun colors(): OinkDotsColors {
        val c = OinkTheme.colors
        return OinkDotsColors(dot = c.accent)
    }
}

@Immutable
data class OinkDotsColors(
    val dot: Color,
)

object OinkTailDefaults {
    val Size = 28.dp

    @Composable
    fun colors(): OinkTailColors {
        val c = OinkTheme.colors
        return OinkTailColors(ink = c.accent)
    }
}

@Immutable
data class OinkTailColors(
    val ink: Color,
)