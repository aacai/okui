package zhiqiu.applewidgets.kmp.basic

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme
import kotlin.math.roundToInt

@Composable
fun Slider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    enabled: Boolean = true,
) {
    val colors = AppleWidgetsTheme.colorScheme
    val fraction = ((value - valueRange.start) / (valueRange.endInclusive - valueRange.start)).coerceIn(0f, 1f)
    val density = LocalDensity.current

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        val trackWidthPx = with(density) { maxWidth.toPx() }
        val thumbRadiusPx = with(density) { 14.dp.toPx() }
        val travel = (trackWidthPx - thumbRadiusPx * 2).coerceAtLeast(1f)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(colors.fill),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction)
                    .height(4.dp)
                    .background(if (enabled) colors.accent else colors.tertiaryLabel),
            )
        }

        val dragModifier = if (enabled) {
            Modifier.pointerInput(valueRange, travel) {
                detectHorizontalDragGestures { change, _ ->
                    change.consume()
                    val x = (change.position.x - thumbRadiusPx).coerceIn(0f, travel)
                    val newFraction = x / travel
                    val newValue = valueRange.start + newFraction * (valueRange.endInclusive - valueRange.start)
                    onValueChange(newValue)
                }
            }
        } else {
            Modifier
        }

        Box(
            modifier = Modifier
                .offset { IntOffset((fraction * travel).roundToInt(), 0) }
                .size(28.dp)
                .then(dragModifier)
                .shadow(2.dp, CircleShape)
                .clip(CircleShape)
                .background(Color.White),
        )
    }
}
