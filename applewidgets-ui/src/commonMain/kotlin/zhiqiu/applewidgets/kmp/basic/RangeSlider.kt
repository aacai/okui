package zhiqiu.applewidgets.kmp.basic

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

@Composable
fun RangeSlider(
    value: ClosedFloatingPointRange<Float>,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    enabled: Boolean = true,
) {
    val colors = AppleWidgetsTheme.colorScheme
    val density = LocalDensity.current
    var activeThumb by remember { mutableStateOf<Thumb?>(null) }

    BoxWithConstraints(modifier = modifier.fillMaxWidth().height(44.dp)) {
        val widthPx = with(density) { maxWidth.toPx() }
        val thumbR = with(density) { 14.dp.toPx() }
        val travel = (widthPx - thumbR * 2).coerceAtLeast(1f)

        fun toFraction(v: Float): Float =
            ((v - valueRange.start) / (valueRange.endInclusive - valueRange.start)).coerceIn(0f, 1f)

        fun toValue(f: Float): Float =
            valueRange.start + f.coerceIn(0f, 1f) * (valueRange.endInclusive - valueRange.start)

        val startF = toFraction(value.start)
        val endF = toFraction(value.endInclusive)

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .then(
                    if (enabled) {
                        Modifier.pointerInput(value, valueRange) {
                            detectHorizontalDragGestures(
                                onDragStart = { offset ->
                                    val startX = thumbR + startF * travel
                                    val endX = thumbR + endF * travel
                                    activeThumb = if (kotlin.math.abs(offset.x - startX) <= kotlin.math.abs(offset.x - endX)) {
                                        Thumb.Start
                                    } else {
                                        Thumb.End
                                    }
                                },
                                onDragEnd = { activeThumb = null },
                                onDragCancel = { activeThumb = null },
                                onHorizontalDrag = { change, _ ->
                                    change.consume()
                                    val f = ((change.position.x - thumbR) / travel).coerceIn(0f, 1f)
                                    when (activeThumb) {
                                        Thumb.Start -> onValueChange(toValue(f.coerceAtMost(endF))..value.endInclusive)
                                        Thumb.End -> onValueChange(value.start..toValue(f.coerceAtLeast(startF)))
                                        null -> Unit
                                    }
                                },
                            )
                        }
                    } else {
                        Modifier
                    },
                ),
        ) {
            val cy = size.height / 2
            val trackH = 4.dp.toPx()
            drawRoundRect(
                color = colors.fill,
                topLeft = Offset(thumbR, cy - trackH / 2),
                size = Size(travel, trackH),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(2.dp.toPx()),
            )
            val accent = if (enabled) colors.accent else colors.tertiaryLabel
            drawRoundRect(
                color = accent,
                topLeft = Offset(thumbR + startF * travel, cy - trackH / 2),
                size = Size((endF - startF) * travel, trackH),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(2.dp.toPx()),
            )
            fun drawThumb(f: Float) {
                drawCircle(
                    color = Color.White,
                    radius = thumbR,
                    center = Offset(thumbR + f * travel, cy),
                )
                drawCircle(
                    color = Color(0x22000000),
                    radius = thumbR,
                    center = Offset(thumbR + f * travel, cy),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.dp.toPx()),
                )
            }
            drawThumb(startF)
            drawThumb(endF)
        }
    }
}

private enum class Thumb { Start, End }
