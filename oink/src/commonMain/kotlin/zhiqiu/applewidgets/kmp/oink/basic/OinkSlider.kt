package zhiqiu.applewidgets.kmp.oink.basic

import zhiqiu.applewidgets.kmp.oink.data.*
import zhiqiu.applewidgets.kmp.oink.form.*
import zhiqiu.applewidgets.kmp.oink.layout.*
import zhiqiu.applewidgets.kmp.oink.nav.*
import zhiqiu.applewidgets.kmp.oink.overlay.*
import zhiqiu.applewidgets.kmp.oink.primitives.*
import zhiqiu.applewidgets.kmp.oink.responsive.*
import zhiqiu.applewidgets.kmp.oink.theme.*

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

/**
 * Oink 滑块：6px 凹槽轨道 + 42×28 胶囊白滑块（1px 描边），拖动时滑块放大并弹出数值气泡。
 * [steps] > 0 时吸附到离散档位；[ticks] 在轨道上画刻度点。
 */
@Composable
fun OinkSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    ticks: Int = 0,
    showBubble: Boolean = true,
    valueLabel: (Float) -> String = ::defaultSliderLabel,
    onValueChangeFinished: (() -> Unit)? = null,
    colors: OinkSliderColors = OinkSliderDefaults.colors(),
) {
    val density = LocalDensity.current
    val thumbW = OinkSliderDefaults.ThumbWidth
    val thumbH = OinkSliderDefaults.ThumbHeight
    var dragging by remember { mutableStateOf(false) }
    val span = valueRange.endInclusive - valueRange.start
    val fraction = if (span == 0f) 0f else ((value - valueRange.start) / span).coerceIn(0f, 1f)
    val displayFraction by animateFloatAsState(
        fraction,
        if (dragging) snap() else OinkMotion.spring(),
        label = "fraction",
    )
    val thumbScaleX by animateFloatAsState(if (dragging) 1.12f else 1f, OinkMotion.bouncy(), label = "sx")
    val thumbScaleY by animateFloatAsState(if (dragging) 1.06f else 1f, OinkMotion.bouncy(), label = "sy")
    val bubbleScale by animateFloatAsState(if (dragging) 1f else 0.4f, OinkMotion.bouncy(), label = "bubbleScale")
    val bubbleAlpha by animateFloatAsState(if (dragging) 1f else 0f, tween(OinkMotion.Dur2), label = "bubbleAlpha")

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(OinkSliderDefaults.Height)
            .alpha(if (enabled) 1f else 0.5f)
            .pointerInput(enabled, valueRange, steps) {
                if (!enabled) return@pointerInput
                detectTapGestures { pos ->
                    onValueChange(valueAt(pos.x, size.width.toFloat(), thumbW.toPx(), valueRange, steps))
                    onValueChangeFinished?.invoke()
                }
            }
            .pointerInput(enabled, valueRange, steps) {
                if (!enabled) return@pointerInput
                detectDragGestures(
                    onDragStart = { pos ->
                        dragging = true
                        onValueChange(valueAt(pos.x, size.width.toFloat(), thumbW.toPx(), valueRange, steps))
                    },
                    onDrag = { change, _ ->
                        change.consume()
                        onValueChange(valueAt(change.position.x, size.width.toFloat(), thumbW.toPx(), valueRange, steps))
                    },
                    onDragEnd = {
                        dragging = false
                        onValueChangeFinished?.invoke()
                    },
                    onDragCancel = { dragging = false },
                )
            },
    ) {
        val w = constraints.maxWidth.toFloat()
        val thumbWPx = with(density) { thumbW.toPx() }
        val travel = (w - thumbWPx).coerceAtLeast(0f)
        val thumbX = displayFraction * travel
        val fillW = thumbX + thumbWPx / 2f

        // 轨道
        Box(
            Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .height(OinkSliderDefaults.TrackHeight)
                .clip(CircleShape)
                .background(colors.track),
        )
        // 刻度
        if (ticks > 1) {
            val dotColor = colors.tick
            val inset = thumbWPx / 2f
            val tickTravel = w - thumbWPx
            for (i in 0 until ticks) {
                val x = inset + tickTravel * i / (ticks - 1)
                Box(
                    Modifier
                        .align(Alignment.Center)
                        .offset { IntOffset((x - 1.5.dp.toPx()).roundToInt(), 0) }
                        .size(3.dp)
                        .clip(CircleShape)
                        .background(dotColor),
                )
            }
        }
        // 已选填充
        Box(
            Modifier
                .align(Alignment.CenterStart)
                .width(with(density) { fillW.toDp() })
                .height(OinkSliderDefaults.TrackHeight)
                .clip(CircleShape)
                .background(colors.activeTrack),
        )
        // 气泡
        if (showBubble) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset { IntOffset((thumbX + thumbWPx / 2f - w / 2f).roundToInt(), (-30).dp.toPx().roundToInt()) }
                    .graphicsLayer {
                        scaleX = bubbleScale
                        scaleY = bubbleScale
                        alpha = bubbleAlpha
                        transformOrigin = TransformOrigin(0.5f, 1.3f)
                    }
                    .clip(OinkShape(10.dp))
                    .background(colors.bubble)
                    .padding(horizontal = 9.dp, vertical = 5.dp),
            ) {
                OinkText(
                    text = valueLabel(value),
                    color = colors.bubbleContent,
                    style = OinkTheme.textStyles.caption.copy(fontSize = 12.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold),
                )
            }
        }
        // 滑块
        Box(
            Modifier
                .align(Alignment.CenterStart)
                .offset { IntOffset(thumbX.roundToInt(), 0) }
                .graphicsLayer { scaleX = thumbScaleX; scaleY = thumbScaleY }
                .size(thumbW, thumbH)
                .clip(CircleShape)
                .background(colors.thumb)
                .border(OinkMetrics.Hairline, colors.thumbBorder, CircleShape),
        )
    }
}

object OinkSliderDefaults {
    val Height = 32.dp
    val TrackHeight = 6.dp
    val ThumbWidth = 42.dp
    val ThumbHeight = 28.dp

    @Composable
    fun colors(): OinkSliderColors {
        val c = OinkTheme.colors
        return OinkSliderColors(
            track = c.well2,
            activeTrack = c.accent,
            tick = oinkMix(c.text3, c.well2, 0.55f),
            thumb = c.thumb,
            thumbBorder = c.line2,
            bubble = c.inverse,
            bubbleContent = c.onInverse,
        )
    }
}

@Immutable
data class OinkSliderColors(
    val track: Color,
    val activeTrack: Color,
    val tick: Color,
    val thumb: Color,
    val thumbBorder: Color,
    val bubble: Color,
    val bubbleContent: Color,
)

private fun valueAt(    x: Float,
    width: Float,
    thumbW: Float,
    range: ClosedFloatingPointRange<Float>,
    steps: Int,
): Float {
    val travel = (width - thumbW).coerceAtLeast(1f)
    var f = ((x - thumbW / 2f) / travel).coerceIn(0f, 1f)
    if (steps > 0) f = (f * steps).roundToInt() / steps.toFloat()
    return range.start + (range.endInclusive - range.start) * f
}

private fun defaultSliderLabel(value: Float): String {
    val rounded = value.roundToInt()
    return if (kotlin.math.abs(value - rounded) < 0.05f) rounded.toString() else {
        val one = (value * 10).roundToInt() / 10f
        one.toString()
    }
}