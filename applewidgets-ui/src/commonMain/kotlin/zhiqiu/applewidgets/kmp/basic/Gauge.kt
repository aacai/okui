package zhiqiu.applewidgets.kmp.basic

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

@Composable
fun CircularProgressView(
    progress: Float,
    modifier: Modifier = Modifier,
    size: Dp = GaugeDefaults.Size,
    strokeWidth: Dp = GaugeDefaults.StrokeWidth,
    colors: GaugeColors = GaugeDefaults.colors(),
) {
    val p = progress.coerceIn(0f, 1f)
    Canvas(modifier = modifier.size(size)) {
        val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
        val inset = strokeWidth.toPx() / 2
        val arcSize = Size(this.size.width - strokeWidth.toPx(), this.size.height - strokeWidth.toPx())
        drawArc(
            color = colors.trackColor,
            startAngle = -90f,
            sweepAngle = 360f,
            useCenter = false,
            topLeft = Offset(inset, inset),
            size = arcSize,
            style = stroke,
        )
        drawArc(
            color = colors.progressColor,
            startAngle = -90f,
            sweepAngle = 360f * p,
            useCenter = false,
            topLeft = Offset(inset, inset),
            size = arcSize,
            style = stroke,
        )
    }
}

@Composable
fun Gauge(
    value: Float,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    label: String? = null,
    size: Dp = GaugeDefaults.Size,
    colors: GaugeColors = GaugeDefaults.colors(),
) {
    val span = (valueRange.endInclusive - valueRange.start).takeIf { it != 0f } ?: 1f
    val progress = ((value - valueRange.start) / span).coerceIn(0f, 1f)
    Box(modifier = modifier.size(size), contentAlignment = Alignment.Center) {
        CircularProgressView(progress = progress, size = size, colors = colors)
        Text(
            text = label ?: "${(progress * 100).toInt()}%",
            style = AppleWidgetsTheme.textStyles.footnote,
            color = AppleWidgetsTheme.colorScheme.label,
        )
    }
}

object GaugeDefaults {
    val Size = 64.dp
    val StrokeWidth = 6.dp

    @Composable
    fun colors(
        progressColor: Color = AppleWidgetsTheme.colorScheme.accent,
        trackColor: Color = AppleWidgetsTheme.colorScheme.tertiaryFill,
    ): GaugeColors = GaugeColors(progressColor, trackColor)
}

@Immutable
data class GaugeColors(
    val progressColor: Color,
    val trackColor: Color,
)
