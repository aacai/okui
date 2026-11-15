package zhiqiu.applewidgets.kmp.oink.basic

import zhiqiu.applewidgets.kmp.oink.data.*
import zhiqiu.applewidgets.kmp.oink.form.*
import zhiqiu.applewidgets.kmp.oink.layout.*
import zhiqiu.applewidgets.kmp.oink.nav.*
import zhiqiu.applewidgets.kmp.oink.overlay.*
import zhiqiu.applewidgets.kmp.oink.primitives.*
import zhiqiu.applewidgets.kmp.oink.responsive.*
import zhiqiu.applewidgets.kmp.oink.theme.*

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

/**
 * Oink 标签栏：44 高，底部 3px 墨条随选中项滑动（spring），未选中弱化、hover 提亮。
 */
@Composable
fun OinkTabs(
    tabs: List<String>,
    selectedIndex: Int,
    onSelectedChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    colors: OinkTabsColors = OinkTabsDefaults.colors(),
) {
    if (tabs.isEmpty()) return
    val density = LocalDensity.current
    val n = tabs.size
    val barIndex by animateFloatAsState(
        selectedIndex.coerceIn(0, n - 1).toFloat(),
        OinkMotion.spring(),
        label = "tabBar",
    )
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(OinkTabsDefaults.Height),
    ) {
        val w = constraints.maxWidth.toFloat()
        val tabW = w / n
        Row(Modifier.fillMaxWidth().fillMaxHeight()) {
            tabs.forEachIndexed { index, label ->
                val selected = index == selectedIndex
                val interaction = remember { MutableInteractionSource() }
                val hovered by interaction.collectIsHoveredAsState()
                val textColor by animateColorAsState(
                    when {
                        selected -> colors.selectedContent
                        hovered -> colors.hoverContent
                        else -> colors.content
                    },
                    tween(OinkMotion.Dur2, easing = OinkMotion.EaseOut),
                    label = "tabText",
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .hoverable(interaction)
                        .clickable(interactionSource = interaction, indication = null) { onSelectedChange(index) },
                    contentAlignment = Alignment.Center,
                ) {
                    OinkText(
                        text = label,
                        color = textColor,
                        style = OinkTheme.textStyles.label.copy(
                            fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal,
                        ),
                    )
                }
            }
        }
        // 底部基线
        Box(
            Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .height(OinkMetrics.Hairline)
                .background(colors.line),
        )
        // 滑动墨条
        Box(
            Modifier
                .align(Alignment.BottomStart)
                .offset { IntOffset((barIndex * tabW).roundToInt(), 0) }
                .width(with(density) { tabW.toDp() })
                .height(OinkTabsDefaults.IndicatorHeight)
                .padding(horizontal = 0.dp)
                .clip(OinkShape(OinkTabsDefaults.IndicatorHeight))
                .background(colors.indicator),
        )
    }
}

object OinkTabsDefaults {
    val Height = 44.dp
    val IndicatorHeight = 3.dp

    @Composable
    fun colors(): OinkTabsColors {
        val c = OinkTheme.colors
        return OinkTabsColors(
            selectedContent = c.text,
            hoverContent = c.text2,
            content = c.text3,
            line = c.line,
            indicator = c.text,
        )
    }
}

@Immutable
data class OinkTabsColors(
    val selectedContent: Color,
    val hoverContent: Color,
    val content: Color,
    val line: Color,
    val indicator: Color,
)