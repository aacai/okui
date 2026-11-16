package zhiqiu.applewidgets.kmp.basic

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.delay
import zhiqiu.applewidgets.kmp.theme.Motion

private const val CheckPath = "M4.5 10.5l3.5 3.5 7.5-8"
private const val CrossPathA = "M5.5 5.5l9 9"
private const val CrossPathB = "M14.5 5.5l-9 9"

/** 状态字形：加载圆环 / 成功对勾 / 失败叉号。Button 与 IconButton 共用。 */
@Composable
internal fun ButtonStateGlyph(state: ButtonState, color: Color, size: Dp) {
    when (state) {
        ButtonState.Loading -> ButtonSpinner(color = color, size = size)
        ButtonState.Success -> DrawnGlyph(CheckPath, progress = 1f, color = color, size = size)
        ButtonState.Error -> DrawnGlyph(CrossPathA, CrossPathB, progress = 1f, color = color, size = size)
        ButtonState.Idle -> Unit
    }
}

/** 圆环加载：单段弧匀速旋转。 */
@Composable
private fun ButtonSpinner(color: Color, size: Dp) {
    val transition = rememberInfiniteTransition(label = "buttonSpinner")
    val angle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(900, easing = LinearEasing), RepeatMode.Restart),
        label = "angle",
    )
    Canvas(Modifier.size(size)) {
        val stroke = size.toPx() * 0.11f
        drawArc(
            color = color,
            startAngle = angle,
            sweepAngle = 280f,
            useCenter = false,
            topLeft = Offset(stroke / 2f, stroke / 2f),
            size = androidx.compose.ui.geometry.Size(
                this.size.width - stroke,
                this.size.height - stroke,
            ),
            style = Stroke(width = stroke, cap = StrokeCap.Round),
        )
    }
}

/** 描线字形：progress 0→1 画出路径。 */
@Composable
private fun DrawnGlyph(
    vararg paths: String,
    progress: Float,
    color: Color,
    size: Dp,
) {
    val parsed = remember(paths) { paths.map { PathParser().parsePathString(it).toPath(Path()) } }
    val anim = remember { Animatable(0f) }
    LaunchedEffect(progress) {
        anim.snapTo(0f)
        delay(80)
        anim.animateTo(progress, tween(Motion.Dur4, easing = Motion.EaseOut))
    }
    Canvas(Modifier.size(size)) {
        val s = this.size.minDimension / 20f
        val measure = PathMeasure()
        scale(s, s, pivot = Offset.Zero) {
            for (p in parsed) {
                measure.setPath(p, false)
                val dst = Path()
                measure.getSegment(0f, anim.value * measure.length, dst, true)
                drawPath(
                    dst,
                    color = color,
                    style = Stroke(2.4f, cap = StrokeCap.Round, join = StrokeJoin.Round),
                )
            }
        }
    }
}
