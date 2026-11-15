package zhiqiu.applewidgets.kmp.basic

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastRoundToInt
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme
import kotlin.math.abs

/**
 * iOS 风格滚轮。用 [draggable] + 鼠标滚轮，并吞掉纵向 nested scroll，
 * 避免外层 LazyColumn 抢走手势。
 */
@Composable
fun NumberPicker(
    values: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    visibleItemCount: Int = NumberPickerDefaults.VisibleItemCount,
    itemHeight: Dp = NumberPickerDefaults.ItemHeight,
) {
    require(values.isNotEmpty()) { "values must not be empty" }
    require(visibleItemCount % 2 == 1 && visibleItemCount >= 3) {
        "visibleItemCount must be odd and >= 3"
    }

    val currentOnSelect by rememberUpdatedState(onSelect)
    val itemCount = values.size
    val index = selectedIndex.coerceIn(0, itemCount - 1)
    val half = visibleItemCount / 2
    val scope = rememberCoroutineScope()

    var dragOffset by remember { mutableFloatStateOf(0f) }
    val fling = remember { Animatable(0f) }
    var dragging by remember { mutableStateOf(false) }
    var itemHeightPx by remember { mutableIntStateOf(0) }

    val totalOffset by remember {
        derivedStateOf { dragOffset + fling.value }
    }

    LaunchedEffect(index) {
        if (!dragging && abs(dragOffset) < 0.001f) {
            fling.snapTo(0f)
        }
    }

    fun coerceOffset(raw: Float): Float =
        raw.coerceIn(-index.toFloat(), (itemCount - 1 - index).toFloat())

    fun settle(velocityPx: Float) {
        scope.launch {
            val transferred = dragOffset
            dragOffset = 0f
            fling.snapTo(transferred)
            if (itemHeightPx > 0) {
                val velocityInItems = -velocityPx / itemHeightPx
                val decay = exponentialDecay<Float>(frictionMultiplier = 2.2f)
                fling.updateBounds(-index.toFloat(), (itemCount - 1 - index).toFloat())
                fling.animateDecay(velocityInItems, decay)
                fling.updateBounds(Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY)
            }
            val snapped = fling.value.fastRoundToInt().toFloat()
                .coerceIn(-index.toFloat(), (itemCount - 1 - index).toFloat())
            fling.animateTo(snapped, spring(dampingRatio = 1f, stiffness = 400f))
            val newIndex = (index + fling.value.fastRoundToInt()).coerceIn(0, itemCount - 1)
            fling.snapTo(0f)
            if (newIndex != index) currentOnSelect(newIndex)
        }
    }

    fun nudgeByItems(deltaItems: Float) {
        scope.launch(start = CoroutineStart.UNDISPATCHED) {
            fling.stop()
            dragOffset = coerceOffset(dragOffset + fling.value + deltaItems)
            fling.snapTo(0f)
            settle(0f)
        }
    }

    val blockParentScroll = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                // 吃掉纵向，外层 LazyColumn 不会跟着滚
                return if (abs(available.y) > abs(available.x)) Offset(0f, available.y) else Offset.Zero
            }

            override suspend fun onPreFling(available: Velocity): Velocity {
                return if (abs(available.y) > abs(available.x)) available else Velocity.Zero
            }
        }
    }

    val draggableState = rememberDraggableState { delta ->
        if (itemHeightPx <= 0) return@rememberDraggableState
        dragOffset = coerceOffset(dragOffset - delta / itemHeightPx)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(itemHeight * visibleItemCount)
            .clipToBounds()
            .nestedScroll(blockParentScroll)
            .onSizeChanged { itemHeightPx = (it.height / visibleItemCount).coerceAtLeast(1) }
            .pointerInput(index, itemCount) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        if (event.type == PointerEventType.Scroll) {
                            val dy = event.changes.fold(0f) { acc, c -> acc + c.scrollDelta.y }
                            if (dy != 0f && itemHeightPx > 0) {
                                // 滚轮：正值通常向下，对应选更大 index
                                nudgeByItems(dy / itemHeightPx)
                                event.changes.forEach { it.consume() }
                            }
                        }
                    }
                }
            }
            .draggable(
                orientation = Orientation.Vertical,
                state = draggableState,
                onDragStarted = {
                    dragging = true
                    scope.launch(start = CoroutineStart.UNDISPATCHED) {
                        fling.stop()
                        dragOffset = coerceOffset(dragOffset + fling.value)
                        fling.snapTo(0f)
                    }
                },
                onDragStopped = { velocity ->
                    dragging = false
                    settle(velocity)
                },
            ),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(itemHeight)
                .background(
                    AppleWidgetsTheme.colorScheme.tertiaryFill,
                    RoundedCornerShape(8.dp),
                ),
        )

        if (itemHeightPx > 0) {
            val centerItemOffset = totalOffset - totalOffset.fastRoundToInt()
            val roundedOffset = totalOffset.fastRoundToInt()

            for (i in -half - 1..half + 1) {
                val raw = index + i + roundedOffset
                if (raw !in 0 until itemCount) continue
                val distanceFromCenter = i.toFloat() - centerItemOffset
                val normalized = (abs(distanceFromCenter) / (half + 0.5f)).coerceIn(0f, 1f)
                val alpha = (1f - normalized) * (1f - normalized * 0.45f)
                val scale = 1f - 0.18f * normalized
                val color = if (normalized < 0.35f) {
                    AppleWidgetsTheme.colorScheme.label.copy(alpha = alpha.coerceAtLeast(0.85f))
                } else {
                    AppleWidgetsTheme.colorScheme.tertiaryLabel.copy(alpha = alpha.coerceAtLeast(0.3f))
                }
                Text(
                    text = values[raw],
                    style = AppleWidgetsTheme.textStyles.title2,
                    color = color,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer {
                            this.alpha = alpha.coerceIn(0.2f, 1f)
                            scaleX = scale
                            scaleY = scale
                            translationY = distanceFromCenter * itemHeightPx
                        },
                )
            }
        }
    }
}

object NumberPickerDefaults {
    val Height = 180.dp
    val ItemHeight = 36.dp
    const val VisibleItemCount = 5
}
