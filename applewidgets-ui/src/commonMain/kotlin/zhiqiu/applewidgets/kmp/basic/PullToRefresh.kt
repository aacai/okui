package zhiqiu.applewidgets.kmp.basic

import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 简化版下拉刷新容器。完整弹簧阻尼后续可迭代。
 */
@Composable
fun PullToRefresh(
    refreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    var drag by remember { mutableStateOf(0f) }
    val scope = rememberCoroutineScope()
    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(refreshing) {
                detectVerticalDragGestures(
                    onDragEnd = {
                        if (drag > 120f && !refreshing) {
                            onRefresh()
                            scope.launch {
                                delay(50)
                                drag = 0f
                            }
                        } else {
                            drag = 0f
                        }
                    },
                    onVerticalDrag = { change, amount ->
                        change.consume()
                        if (amount > 0 || drag > 0) {
                            drag = (drag + amount).coerceIn(0f, 180f)
                        }
                    },
                )
            },
    ) {
        if (refreshing || drag > 40f) {
            ProgressView(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 12.dp),
            )
        }
        Box(Modifier.fillMaxSize(), content = content)
    }
}
