package zhiqiu.applewidgets.kmp.basic

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme
import kotlin.math.roundToInt

enum class ListStyle {
    InsetGrouped,
    Plain,
}

data class SwipeAction(
    val title: String,
    val containerColor: Color,
    val contentColor: Color = Color.White,
    val onClick: () -> Unit,
)

@Composable
fun AppleList(
    modifier: Modifier = Modifier,
    style: ListStyle = ListStyle.InsetGrouped,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = AppleWidgetsTheme.colorScheme
    when (style) {
        ListStyle.InsetGrouped -> {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(colors.secondarySystemGroupedBackground),
                content = content,
            )
        }
        ListStyle.Plain -> {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .background(colors.systemBackground),
                content = content,
            )
        }
    }
}

@Composable
fun SwipeableListRow(
    modifier: Modifier = Modifier,
    trailingActions: List<SwipeAction> = emptyList(),
    editing: Boolean = false,
    onDelete: (() -> Unit)? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()
    val actionWidth = with(density) { (72.dp * trailingActions.size.coerceAtLeast(1)).toPx() }
    val offsetX = remember { Animatable(0f) }
    val revealWidth = if (trailingActions.isEmpty()) 0f else actionWidth

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(ListDefaults.RowMinHeight),
    ) {
        if (trailingActions.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .matchParentSize()
                    .align(Alignment.CenterEnd),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(Modifier.weight(1f))
                trailingActions.forEach { action ->
                    Box(
                        modifier = Modifier
                            .width(72.dp)
                            .fillMaxHeight()
                            .background(action.containerColor)
                            .clickable {
                                action.onClick()
                                scope.launch { offsetX.animateTo(0f, spring()) }
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(action.title, color = action.contentColor, style = AppleWidgetsTheme.textStyles.footnote)
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .fillMaxWidth()
                .fillMaxHeight()
                .background(AppleWidgetsTheme.colorScheme.secondarySystemGroupedBackground)
                .then(
                    if (trailingActions.isNotEmpty() && !editing) {
                        Modifier.draggable(
                            orientation = Orientation.Horizontal,
                            state = rememberDraggableState { delta ->
                                scope.launch {
                                    val next = (offsetX.value + delta).coerceIn(-revealWidth, 0f)
                                    offsetX.snapTo(next)
                                }
                            },
                            onDragStopped = {
                                scope.launch {
                                    val target = if (offsetX.value < -revealWidth * 0.4f) -revealWidth else 0f
                                    offsetX.animateTo(target, spring())
                                }
                            },
                        )
                    } else {
                        Modifier
                    },
                )
                .padding(horizontal = if (editing) 8.dp else 0.dp),
            verticalAlignment = Alignment.CenterVertically,
            content = {
                if (editing && onDelete != null) {
                    Text(
                        text = "−",
                        color = AppleWidgetsTheme.colorScheme.destructive,
                        style = AppleWidgetsTheme.textStyles.title2,
                        modifier = Modifier
                            .clickable(onClick = onDelete)
                            .padding(horizontal = 8.dp),
                    )
                }
                content()
            },
        )
    }
}

@Composable
fun ListRowDivider(startIndent: androidx.compose.ui.unit.Dp = 16.dp) {
    Separator(startIndent = startIndent)
}

object ListDefaults {
    val RowMinHeight = 44.dp
}

@Immutable
data class ListColors(
    val containerColor: Color,
    val separatorColor: Color,
)
