package zhiqiu.applewidgets.kmp.oink.form

import zhiqiu.applewidgets.kmp.oink.basic.*
import zhiqiu.applewidgets.kmp.oink.data.*
import zhiqiu.applewidgets.kmp.oink.layout.*
import zhiqiu.applewidgets.kmp.oink.nav.*
import zhiqiu.applewidgets.kmp.oink.overlay.*
import zhiqiu.applewidgets.kmp.oink.primitives.*
import zhiqiu.applewidgets.kmp.oink.responsive.*
import zhiqiu.applewidgets.kmp.oink.theme.*

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.overlay.PlatformAnchoredPopup
import kotlin.math.roundToInt

/**
 * Oink 下拉选择：凹槽触发器 + 下方弹出菜单，箭头随开合旋转。
 */
@Composable
fun OinkSelect(
    options: List<String>,
    selectedIndex: Int,
    onSelectedChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    placeholder: String = "Select…",
    colors: OinkSelectColors = OinkSelectDefaults.colors(),
) {
    val density = LocalDensity.current
    var expanded by remember { mutableStateOf(false) }
    var triggerWidth by remember { mutableStateOf(0) }
    val interaction = remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()
    val shape = OinkShape(OinkSelectDefaults.CornerRadius)
    val chevronRotation by animateFloatAsState(if (expanded) 180f else 0f, OinkMotion.spring(), label = "chevron")

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(OinkSelectDefaults.Height)
                .onGloballyPositioned { triggerWidth = it.size.width }
                .clip(shape)
                .background(if (expanded) colors.expandedContainer else colors.container)
                .border(
                    OinkMetrics.Hairline,
                    when {
                        expanded -> colors.expandedBorder
                        hovered -> colors.hoverBorder
                        else -> colors.border
                    },
                    shape,
                )
                .hoverable(interaction)
                .clickable(interactionSource = interaction, indication = null, enabled = enabled) { expanded = !expanded }
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val hasSelection = selectedIndex in options.indices
            OinkText(
                text = if (hasSelection) options[selectedIndex] else placeholder,
                color = if (hasSelection) colors.text else colors.placeholder,
                style = OinkTheme.textStyles.body,
                modifier = Modifier.weight(1f),
            )
            OinkIcon(
                imageVector = OinkIcons.ChevronDown,
                contentDescription = null,
                tint = colors.icon,
                modifier = Modifier.size(16.dp).graphicsLayer { rotationZ = chevronRotation },
            )
        }

        if (expanded && triggerWidth > 0) {
            val enter = remember { Animatable(0f) }
            LaunchedEffect(Unit) { enter.animateTo(1f, OinkMotion.spring()) }
            val gapPx = with(density) { OinkSelectDefaults.Gap.toPx() }
            PlatformAnchoredPopup(onDismissRequest = { expanded = false }) {
                Column(
                    modifier = Modifier
                        .offset { IntOffset(0, (OinkSelectDefaults.Height.toPx() + gapPx).roundToInt()) }
                        .width(with(density) { triggerWidth.toDp() })
                        .graphicsLayer {
                            val s = 0.94f + 0.06f * enter.value
                            scaleX = s
                            scaleY = s
                            alpha = enter.value
                            transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0.5f, 0f)
                        }
                        .clip(OinkShape(OinkSelectDefaults.PopupCornerRadius))
                        .background(colors.popupContainer)
                        .border(OinkMetrics.Hairline, colors.popupBorder, OinkShape(OinkSelectDefaults.PopupCornerRadius))
                        .padding(6.dp),
                ) {
                    options.forEachIndexed { index, option ->
                        OinkMenuItem(
                            text = option,
                            selected = index == selectedIndex,
                            onClick = {
                                onSelectedChange(index)
                                expanded = false
                            },
                        )
                    }
                }
            }
        }
    }
}

object OinkSelectDefaults {
    val Height = 40.dp
    val CornerRadius = 14.dp
    val PopupCornerRadius = 18.dp
    val Gap = 6.dp

    @Composable
    fun colors(): OinkSelectColors {
        val c = OinkTheme.colors
        return OinkSelectColors(
            container = c.well,
            expandedContainer = c.raised,
            border = c.line,
            hoverBorder = c.line2,
            expandedBorder = c.accent,
            text = c.text,
            placeholder = c.text3,
            icon = c.text3,
            popupContainer = c.pop,
            popupBorder = c.line,
        )
    }
}

@Immutable
data class OinkSelectColors(
    val container: Color,
    val expandedContainer: Color,
    val border: Color,
    val hoverBorder: Color,
    val expandedBorder: Color,
    val text: Color,
    val placeholder: Color,
    val icon: Color,
    val popupContainer: Color,
    val popupBorder: Color,
)