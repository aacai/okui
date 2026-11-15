package zhiqiu.applewidgets.kmp.basic

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

@Composable
fun ProgressView(
    modifier: Modifier = Modifier,
    size: Dp = 28.dp,
    strokeWidth: Dp = 3.dp,
) {
    val color = AppleWidgetsTheme.colorScheme.secondaryLabel
    val transition = rememberInfiniteTransition(label = "progress")
    val rotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(900, easing = LinearEasing), RepeatMode.Restart),
        label = "rotation",
    )
    Canvas(modifier = modifier.size(size)) {
        val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
        rotate(rotation) {
            drawArc(
                color = color,
                startAngle = 0f,
                sweepAngle = 270f,
                useCenter = false,
                topLeft = Offset(strokeWidth.toPx() / 2, strokeWidth.toPx() / 2),
                size = Size(this.size.width - strokeWidth.toPx(), this.size.height - strokeWidth.toPx()),
                style = stroke,
            )
        }
    }
}

@Composable
fun LinearProgressView(
    progress: Float,
    modifier: Modifier = Modifier,
) {
    val colors = AppleWidgetsTheme.colorScheme
    Canvas(
        modifier = modifier.size(width = 200.dp, height = 4.dp),
    ) {
        drawRoundRect(color = colors.fill, size = size, cornerRadius = androidx.compose.ui.geometry.CornerRadius(2.dp.toPx()))
        drawRoundRect(
            color = colors.accent,
            size = Size(size.width * progress.coerceIn(0f, 1f), size.height),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(2.dp.toPx()),
        )
    }
}
