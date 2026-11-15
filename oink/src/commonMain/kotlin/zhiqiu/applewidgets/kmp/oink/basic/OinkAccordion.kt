package zhiqiu.applewidgets.kmp.oink.basic

import zhiqiu.applewidgets.kmp.oink.data.*
import zhiqiu.applewidgets.kmp.oink.form.*
import zhiqiu.applewidgets.kmp.oink.layout.*
import zhiqiu.applewidgets.kmp.oink.nav.*
import zhiqiu.applewidgets.kmp.oink.overlay.*
import zhiqiu.applewidgets.kmp.oink.primitives.*
import zhiqiu.applewidgets.kmp.oink.responsive.*
import zhiqiu.applewidgets.kmp.oink.theme.*

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Oink 手风琴项：可点击标题行 + 旋转箭头，内容垂直展开 / 收起。
 */
@Composable
fun OinkAccordionItem(
    title: String,
    expanded: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
    colors: OinkAccordionColors = OinkAccordionDefaults.colors(),
    content: @Composable () -> Unit,
) {
    val interaction = remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()
    val rotation by animateFloatAsState(if (expanded) 180f else 0f, OinkMotion.spring(), label = "accChevron")
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = OinkAccordionDefaults.MinHeight)
                .background(if (hovered) colors.hover else Color.Transparent)
                .hoverable(interaction)
                .clickable(interactionSource = interaction, indication = null, onClick = onToggle)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(Modifier.weight(1f)) {
                OinkText(
                    text = title,
                    color = colors.title,
                    style = OinkTheme.textStyles.rowTitle.copy(fontWeight = FontWeight.Medium),
                )
                if (description != null) {
                    OinkText(text = description, color = colors.description, style = OinkTheme.textStyles.rowDesc, modifier = Modifier.padding(top = 2.dp))
                }
            }
            OinkIcon(
                imageVector = OinkIcons.ChevronDown,
                contentDescription = null,
                tint = colors.chevron,
                size = 18.dp,
                modifier = Modifier.graphicsLayer { rotationZ = rotation },
            )
        }
        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically(),
        ) {
            Box(Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
                content()
            }
        }
    }
}

object OinkAccordionDefaults {
    val MinHeight = 52.dp

    @Composable
    fun colors(): OinkAccordionColors {
        val c = OinkTheme.colors
        return OinkAccordionColors(
            title = c.text,
            description = c.text3,
            chevron = c.text3,
            hover = c.fillHover,
        )
    }
}

@Immutable
data class OinkAccordionColors(
    val title: Color,
    val description: Color,
    val chevron: Color,
    val hover: Color,
)