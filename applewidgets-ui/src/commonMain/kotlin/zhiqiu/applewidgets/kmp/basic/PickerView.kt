package zhiqiu.applewidgets.kmp.basic

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.filter
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * 滚轮选择器（对应 iOS UIPickerView）。
 * 居中行高亮、两侧渐弱缩放；滚动停止后吸附到最近行并回调。
 */
@Composable
fun PickerView(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    visibleRows: Int = 5,
    itemHeight: Dp = 40.dp,
    colors: PickerViewColors = PickerViewDefaults.colors(),
) {
    if (options.isEmpty()) return
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = selectedIndex.coerceIn(options.indices))
    var settledIndex by remember { mutableIntStateOf(selectedIndex.coerceIn(options.indices)) }
    val itemHeightPx = with(LocalDensity.current) { itemHeight.toPx() }

    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }
            .filter { !it }
            .collect {
                val offset = listState.firstVisibleItemIndex * itemHeightPx + listState.firstVisibleItemScrollOffset
                val index = (offset / itemHeightPx).roundToInt().coerceIn(options.indices)
                settledIndex = index
                if (index != selectedIndex) onSelect(index)
            }
    }

    LaunchedEffect(selectedIndex) {
        if (selectedIndex != settledIndex) {
            settledIndex = selectedIndex
            listState.animateScrollToItem(selectedIndex.coerceIn(options.indices))
        }
    }

    Box(
        modifier = modifier.height(itemHeight * visibleRows),
        contentAlignment = Alignment.Center,
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = itemHeight * (visibleRows - 1) / 2),
        ) {
            items(options.size) { index ->
                val distance = abs(index - listState.firstVisibleItemIndex).coerceAtMost(visibleRows)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(itemHeight),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = options[index],
                        style = AppleWidgetsTheme.textStyles.title3,
                        color = if (distance == 0) colors.selectedContentColor else colors.contentColor,
                        modifier = Modifier.graphicsLayer {
                            val scale = 1f - distance * 0.07f
                            scaleX = scale
                            scaleY = scale
                            alpha = 1f - distance * 0.16f
                        },
                    )
                }
            }
        }
    }
}

object PickerViewDefaults {
    @Composable
    fun colors(
        contentColor: Color = AppleWidgetsTheme.colorScheme.secondaryLabel,
        selectedContentColor: Color = AppleWidgetsTheme.colorScheme.label,
    ): PickerViewColors = PickerViewColors(contentColor, selectedContentColor)
}

@Immutable
data class PickerViewColors(
    val contentColor: Color,
    val selectedContentColor: Color,
)
