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
import androidx.compose.foundation.border
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
 * Oink 分段控件：凹槽容器 + 滑动白底 thumb（spring），选中项加重，未选中弱化。
 */
@Composable
fun OinkSegmented(
    options: List<String>,
    selectedIndex: Int,
    onSelectedChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: OinkSegmentedColors = OinkSegmentedDefaults.colors(),
) {
    if (options.isEmpty()) return
    val density = LocalDensity.current
    val pad = OinkSegmentedDefaults.Padding
    val gap = OinkSegmentedDefaults.Gap
    val n = options.size
    val thumbIndex by animateFloatAsState(
        selectedIndex.coerceIn(0, n - 1).toFloat(),
        OinkMotion.spring(),
        label = "segThumb",
    )

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(OinkSegmentedDefaults.Height)
            .clip(OinkShape(OinkRadii.r3))
            .background(colors.container)
            .border(OinkMetrics.Hairline, colors.border, OinkShape(OinkRadii.r3)),
    ) {
        val w = constraints.maxWidth.toFloat()
        val padPx = with(density) { pad.toPx() }
        val gapPx = with(density) { gap.toPx() }
        val innerW = w - 2 * padPx
        val segW = (innerW - gapPx * (n - 1)) / n
        val thumbX = padPx + thumbIndex * (segW + gapPx)

        // 滑动 thumb
        Box(
            Modifier
                .align(Alignment.CenterStart)
                .offset { IntOffset(thumbX.roundToInt(), 0) }
                .width(with(density) { segW.toDp() })
                .fillMaxHeight()
                .padding(vertical = pad)
                .clip(OinkShape(OinkRadii.r2))
                .background(colors.thumb),
        )
        // 选项
        Row(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(pad),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(gap),
        ) {
            options.forEachIndexed { index, label ->
                val selected = index == selectedIndex
                val interaction = remember { MutableInteractionSource() }
                val hovered by interaction.collectIsHoveredAsState()
                val textColor by animateColorAsState(
                    when {
                        selected -> colors.selectedContent
                        hovered && enabled -> colors.hoverContent
                        else -> colors.content
                    },
                    tween(OinkMotion.Dur2, easing = OinkMotion.EaseOut),
                    label = "segText",
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(OinkShape(OinkRadii.r2))
                        .hoverable(interaction)
                        .clickable(
                            interactionSource = interaction,
                            indication = null,
                            enabled = enabled,
                        ) { onSelectedChange(index) },
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
    }
}

object OinkSegmentedDefaults {
    val Height = 40.dp
    val Padding = 3.dp
    val Gap = 2.dp

    @Composable
    fun colors(): OinkSegmentedColors {
        val c = OinkTheme.colors
        return OinkSegmentedColors(
            container = c.fill,
            border = c.line,
            thumb = c.raised,
            selectedContent = c.text,
            hoverContent = c.text2,
            content = c.text3,
        )
    }
}

@Immutable
data class OinkSegmentedColors(
    val container: Color,
    val border: Color,
    val thumb: Color,
    val selectedContent: Color,
    val hoverContent: Color,
    val content: Color,
)
