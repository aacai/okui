package zhiqiu.applewidgets.kmp.basic

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.Immutable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

@Composable
fun SegmentedControl(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val impl = LocalSegmentedControlImpl.current
    if (impl != null) {
        impl(SegmentedControlParams(options, selectedIndex, onSelect, modifier))
        return
    }
    require(options.isNotEmpty())
    val colors = AppleWidgetsTheme.colorScheme
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(32.dp)
            .clip(RoundedCornerShape(9.dp))
            .background(colors.tertiaryFill)
            .padding(2.dp),
    ) {
        val segmentWidth = maxWidth / options.size
        val offsetX by animateDpAsState(
            targetValue = segmentWidth * selectedIndex.coerceIn(0, options.lastIndex),
            animationSpec = spring(stiffness = 500f),
            label = "segment",
        )
        Box(
            modifier = Modifier
                .offset(x = offsetX)
                .width(segmentWidth)
                .fillMaxHeight()
                .shadow(1.dp, RoundedCornerShape(7.dp))
                .clip(RoundedCornerShape(7.dp))
                .background(if (AppleWidgetsTheme.isDark) Color(0xFF636366) else Color.White),
        )
        Row(Modifier.fillMaxSize()) {
            options.forEachIndexed { index, label ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable { onSelect(index) },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = label,
                        style = AppleWidgetsTheme.textStyles.footnote.copy(
                            fontWeight = if (index == selectedIndex) {
                                androidx.compose.ui.text.font.FontWeight.SemiBold
                            } else {
                                androidx.compose.ui.text.font.FontWeight.Normal
                            },
                        ),
                        color = colors.label,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

/** 分段控件实现覆盖参数。 */
@Immutable
data class SegmentedControlParams(
    val options: List<String>,
    val selectedIndex: Int,
    val onSelect: (Int) -> Unit,
    val modifier: Modifier,
)

/** 主题可替换的分段控件实现；null 表示使用组件库默认实现。 */
val LocalSegmentedControlImpl = compositionLocalOf<(@Composable (SegmentedControlParams) -> Unit)?> { null }
