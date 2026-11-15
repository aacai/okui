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
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 * Oink 卡片：card 底色、24 超椭圆圆角、1px 描边 + 顶部 1.5px 高光（无投影的“纸面”层次）。
 */
@Composable
fun OinkCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    colors: OinkCardColors = OinkCardDefaults.colors(),
    contentPadding: androidx.compose.foundation.layout.PaddingValues = androidx.compose.foundation.layout.PaddingValues(0.dp),
    content: @Composable ColumnScope.() -> Unit,
) {
    val shape = OinkShape(OinkRadii.r4)
    val interaction = remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()
    val hi = colors.highlight
    Column(
        modifier = modifier
            .clip(shape)
            .background(colors.container)
            .drawBehind {
                drawLine(
                    color = hi,
                    start = Offset(0f, 0.75.dp.toPx()),
                    end = Offset(size.width, 0.75.dp.toPx()),
                    strokeWidth = 1.5.dp.toPx(),
                )
            }
            .border(OinkMetrics.Hairline, colors.border, shape)
            .then(
                if (onClick != null) {
                    Modifier
                        .hoverable(interaction)
                        .clickable(interactionSource = interaction, indication = null, onClick = onClick)
                } else {
                    Modifier
                },
            )
            .padding(contentPadding),
        content = content,
    )
}

/**
 * Oink 分组：可选小标题 + 卡片容器，用于设置类列表。
 */
@Composable
fun OinkGroup(
    modifier: Modifier = Modifier,
    title: String? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        if (title != null) OinkGroupTitle(text = title)
        OinkCard(content = content)
    }
}

@Composable
fun OinkGroupTitle(text: String, modifier: Modifier = Modifier) {
    OinkText(
        text = text.uppercase(),
        color = OinkTheme.colors.text3,
        style = OinkTheme.textStyles.micro,
        modifier = modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
    )
}

/**
 * Oink 设置行：左侧标题 / 描述，右侧控件槽，最小高度 56，可点击时 hover 浅填充。
 */
@Composable
fun OinkRow(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    leadingIcon: ImageVector? = null,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    colors: OinkRowColors = OinkRowDefaults.colors(),
    trailing: @Composable RowScope.() -> Unit = {},
) {
    val interaction = remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = OinkMetrics.RowMinHeight)
            .background(if (hovered && onClick != null && enabled) colors.hover else Color.Transparent)
            .then(
                if (onClick != null) {
                    Modifier
                        .hoverable(interaction)
                        .clickable(interactionSource = interaction, indication = null, enabled = enabled, onClick = onClick)
                } else {
                    Modifier
                },
            )
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (leadingIcon != null) {
            Box(
                Modifier
                    .padding(end = 12.dp)
                    .size(30.dp)
                    .clip(OinkShape(OinkRadii.r1))
                    .background(colors.leadingContainer),
                contentAlignment = Alignment.Center,
            ) {
                OinkIcon(leadingIcon, null, tint = colors.leadingIcon, size = 16.dp)
            }
        }
        Column(Modifier.weight(1f)) {
            OinkText(
                text = title,
                color = if (enabled) colors.title else colors.disabledTitle,
                style = OinkTheme.textStyles.rowTitle,
            )
            if (description != null) {
                OinkText(
                    text = description,
                    color = colors.description,
                    style = OinkTheme.textStyles.rowDesc,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
        }
        trailing()
    }
}

@Composable
fun OinkRowDivider(modifier: Modifier = Modifier) {
    Box(
        modifier
            .fillMaxWidth()
            .padding(start = 16.dp)
            .height(OinkMetrics.Hairline)
            .background(OinkTheme.colors.line),
    )
}

object OinkCardDefaults {
    @Composable
    fun colors(): OinkCardColors {
        val c = OinkTheme.colors
        return OinkCardColors(container = c.card, border = c.line, highlight = c.hi)
    }
}

@Immutable
data class OinkCardColors(
    val container: Color,
    val border: Color,
    val highlight: Color,
)

object OinkRowDefaults {
    @Composable
    fun colors(): OinkRowColors {
        val c = OinkTheme.colors
        return OinkRowColors(
            title = c.text,
            disabledTitle = c.text3,
            description = c.text3,
            hover = c.fillHover,
            leadingContainer = c.fill,
            leadingIcon = c.text2,
        )
    }
}

@Immutable
data class OinkRowColors(
    val title: Color,
    val disabledTitle: Color,
    val description: Color,
    val hover: Color,
    val leadingContainer: Color,
    val leadingIcon: Color,
)