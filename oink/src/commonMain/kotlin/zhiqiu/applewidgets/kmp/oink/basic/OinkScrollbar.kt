package zhiqiu.applewidgets.kmp.oink.basic

import zhiqiu.applewidgets.kmp.oink.data.*
import zhiqiu.applewidgets.kmp.oink.form.*
import zhiqiu.applewidgets.kmp.oink.layout.*
import zhiqiu.applewidgets.kmp.oink.nav.*
import zhiqiu.applewidgets.kmp.oink.overlay.*
import zhiqiu.applewidgets.kmp.oink.primitives.*
import zhiqiu.applewidgets.kmp.oink.responsive.*
import zhiqiu.applewidgets.kmp.oink.theme.*

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * Oink 滚动条：细胶囊 thumb（accent 柔和色），hover 加宽，可拖动跳转。用于 LazyColumn。
 */
@Composable
fun OinkVerticalScrollbar(
    state: LazyListState,
    modifier: Modifier = Modifier,
    colors: OinkScrollbarColors = OinkScrollbarDefaults.colors(),
) {
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()
    val interaction = remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()
    val layoutInfo = state.layoutInfo
    val totalItems = layoutInfo.totalItemsCount
    if (totalItems == 0) return
    val visible = layoutInfo.visibleItemsInfo
    if (visible.isEmpty()) return
    val avgItem = visible.sumOf { it.size }.toFloat() / visible.size
    val contentHeight = (avgItem * totalItems).coerceAtLeast(1f)
    val viewport = layoutInfo.viewportSize.height.toFloat().coerceAtLeast(1f)
    if (contentHeight <= viewport) return
    val scrolled = state.firstVisibleItemIndex * avgItem + state.firstVisibleItemScrollOffset

    BoxWithConstraints(
        modifier = modifier
            .fillMaxHeight()
            .width(OinkScrollbarDefaults.TrackWidth)
            .hoverable(interaction)
            .pointerInput(contentHeight, viewport) {
                detectVerticalDragGestures { change, dy ->
                    change.consume()
                    val trackPx = size.height.toFloat()
                    val delta = dy * contentHeight / trackPx
                    scope.launch { state.scrollBy(delta) }
                }
            },
    ) {
        val trackH = constraints.maxHeight.toFloat()
        val thumbH = (viewport / contentHeight * trackH).coerceAtLeast(with(density) { OinkScrollbarDefaults.MinThumbHeight.toPx() })
        val maxOffset = (trackH - thumbH).coerceAtLeast(0f)
        val thumbY = (scrolled / (contentHeight - viewport) * maxOffset).coerceIn(0f, maxOffset)
        val thumbWidth = if (hovered) OinkScrollbarDefaults.ThumbWidthHover else OinkScrollbarDefaults.ThumbWidth
        Box(
            Modifier
                .align(Alignment.TopCenter)
                .offset { IntOffset(0, thumbY.roundToInt()) }
                .padding(vertical = 2.dp)
                .width(thumbWidth)
                .height(with(density) { thumbH.toDp() })
                .clip(CircleShape)
                .background(if (hovered) colors.thumbHover else colors.thumb),
        )
    }
}

object OinkScrollbarDefaults {
    val TrackWidth = 14.dp
    val ThumbWidth = 6.dp
    val ThumbWidthHover = 8.dp
    val MinThumbHeight = 28.dp

    @Composable
    fun colors(): OinkScrollbarColors {
        val c = OinkTheme.colors
        return OinkScrollbarColors(thumb = c.line2, thumbHover = c.accentRing)
    }
}

@Immutable
data class OinkScrollbarColors(
    val thumb: Color,
    val thumbHover: Color,
)