package zhiqiu.applewidgets.kmp.oink.nav

import zhiqiu.applewidgets.kmp.oink.basic.*
import zhiqiu.applewidgets.kmp.oink.data.*
import zhiqiu.applewidgets.kmp.oink.form.*
import zhiqiu.applewidgets.kmp.oink.layout.*
import zhiqiu.applewidgets.kmp.oink.overlay.*
import zhiqiu.applewidgets.kmp.oink.primitives.*
import zhiqiu.applewidgets.kmp.oink.responsive.*
import zhiqiu.applewidgets.kmp.oink.theme.*

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlin.math.max
import kotlin.math.min

/**
 * Oink 面包屑：末项为当前页，其余项可点击并 hover 提亮。
 *
 * 末项始终用正文色且不可点击；未提供 [onItemClick] 时所有项都只做展示。分隔符为内置 ChevronRight。
 *
 * @param items 从根到当前的路径文本
 * @param onItemClick 非末项点击回调，参数为该项索引
 *
 * 示例：
 * ```kotlin
 * OinkBreadcrumb(
 *     items = listOf("Workspace", "Projects", "Oink"),
 *     onItemClick = { index -> println("go $index") },
 * )
 * ```
 */
@Composable
fun OinkBreadcrumb(
    items: List<String>,
    modifier: Modifier = Modifier,
    onItemClick: ((Int) -> Unit)? = null,
    colors: OinkBreadcrumbColors = OinkBreadcrumbDefaults.colors(),
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        items.forEachIndexed { index, label ->
            val last = index == items.lastIndex
            val clickable = !last && onItemClick != null
            val interaction = remember { MutableInteractionSource() }
            val hovered by interaction.collectIsHoveredAsState()
            val color by animateColorAsState(
                when {
                    last -> colors.current
                    hovered && clickable -> colors.current
                    else -> colors.content
                },
                tween(OinkMotion.Dur2, easing = OinkMotion.EaseOut),
                label = "crumbFg",
            )
            OinkText(
                text = label,
                color = color,
                style = OinkTheme.textStyles.caption,
                modifier = if (clickable) {
                    Modifier
                        .hoverable(interaction)
                        .clickable(interactionSource = interaction, indication = null) { onItemClick?.invoke(index) }
                } else {
                    Modifier
                },
            )
            if (!last) {
                OinkIcon(
                    imageVector = OinkIcons.ChevronRight,
                    contentDescription = null,
                    tint = colors.separator,
                    size = 12.dp,
                    modifier = Modifier.padding(horizontal = OinkMetrics.Space1),
                )
            }
        }
    }
}

/**
 * Oink 分页：上一页 / 下一页 + 页码，超过 5 页折叠为省略号。
 *
 * 页码从 1 开始；[pageCount] 小于等于 0 时不渲染任何内容。
 * 首页禁用「上一页」、末页禁用「下一页」，点击回调需由调用方更新 [page]。
 *
 * @param page 当前页，1-based
 * @param pageCount 总页数
 *
 * 示例：
 * ```kotlin
 * var page by remember { mutableStateOf(1) }
 * OinkPagination(page = page, pageCount = 12, onPageChange = { page = it })
 * ```
 */
@Composable
fun OinkPagination(
    page: Int,
    pageCount: Int,
    onPageChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    colors: OinkPaginationColors = OinkPaginationDefaults.colors(),
) {
    if (pageCount <= 0) return
    val pages = remember(page, pageCount) { pageItems(page, pageCount) }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(OinkMetrics.Space1),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OinkPageArrow(
            icon = OinkIcons.ChevronLeft,
            description = "Previous page",
            enabled = page > 1,
            colors = colors,
            onClick = { onPageChange(page - 1) },
        )
        pages.forEach { number ->
            if (number == null) {
                OinkText(
                    text = "…",
                    color = colors.ellipsis,
                    style = OinkTheme.textStyles.caption,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(OinkPaginationDefaults.EllipsisWidth),
                )
            } else {
                OinkPageNumber(
                    number = number,
                    selected = number == page,
                    colors = colors,
                    onClick = { onPageChange(number) },
                )
            }
        }
        OinkPageArrow(
            icon = OinkIcons.ChevronRight,
            description = "Next page",
            enabled = page < pageCount,
            colors = colors,
            onClick = { onPageChange(page + 1) },
        )
    }
}

private fun pageItems(page: Int, pageCount: Int): List<Int?> {
    if (pageCount <= 5) return (1..pageCount).toList()
    return buildList {
        add(1)
        if (page > 3) add(null)
        for (p in max(2, page - 1)..min(pageCount - 1, page + 1)) add(p)
        if (page < pageCount - 2) add(null)
        add(pageCount)
    }
}

@Composable
private fun OinkPageNumber(
    number: Int,
    selected: Boolean,
    colors: OinkPaginationColors,
    onClick: () -> Unit,
) {
    val interaction = remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()
    val background by animateColorAsState(
        when {
            selected -> colors.selectedContainer
            hovered -> colors.hover
            else -> Color.Transparent
        },
        tween(OinkMotion.Dur2, easing = OinkMotion.EaseOut),
        label = "pageBg",
    )
    val foreground by animateColorAsState(
        if (selected) colors.selectedContent else colors.content,
        tween(OinkMotion.Dur2, easing = OinkMotion.EaseOut),
        label = "pageFg",
    )
    Box(
        modifier = Modifier
            .size(OinkMetrics.HeightSm)
            .clip(OinkShape(OinkRadii.r1))
            .background(background)
            .hoverable(interaction)
            .clickable(interactionSource = interaction, indication = null, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        OinkText(text = number.toString(), color = foreground, style = OinkTheme.textStyles.caption)
    }
}

@Composable
private fun OinkPageArrow(
    icon: ImageVector,
    description: String,
    enabled: Boolean,
    colors: OinkPaginationColors,
    onClick: () -> Unit,
) {
    val interaction = remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()
    Box(
        modifier = Modifier
            .size(OinkMetrics.HeightSm)
            .clip(OinkShape(OinkRadii.r1))
            .background(if (hovered && enabled) colors.hover else Color.Transparent)
            .hoverable(interaction)
            .clickable(interactionSource = interaction, indication = null, enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        OinkIcon(
            imageVector = icon,
            contentDescription = description,
            tint = if (enabled) colors.content else colors.disabledContent,
            size = 16.dp,
        )
    }
}

/**
 * Oink 步骤项。
 *
 * @property label 步骤标题
 * @property description 步骤说明，null 时不占位
 *
 * 示例：
 * ```kotlin
 * val steps = listOf(OinkStep("账户"), OinkStep("资料", "填写基本信息"), OinkStep("完成"))
 * ```
 */
data class OinkStep(
    val label: String,
    val description: String? = null,
)

/**
 * Oink 横向步骤条：已完成圆点填强调色，当前项文字加粗。
 *
 * 每步等宽且不滚动；索引小于 [current] 视为已完成（显示对勾），等于 [current] 视为进行中。
 * [current] 越界时不会有任何步骤被高亮。
 *
 * @param current 当前步骤索引，0-based
 *
 * 示例：
 * ```kotlin
 * OinkSteps(
 *     steps = listOf(OinkStep("账户"), OinkStep("资料", "填写基本信息"), OinkStep("完成")),
 *     current = 1,
 * )
 * ```
 */
@Composable
fun OinkSteps(
    steps: List<OinkStep>,
    current: Int,
    modifier: Modifier = Modifier,
    colors: OinkStepsColors = OinkStepsDefaults.colors(),
) {
    if (steps.isEmpty()) return
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        steps.forEachIndexed { index, step ->
            val done = index < current
            val active = index == current
            Column(Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    OinkStepDot(number = index + 1, done = done, active = active, colors = colors)
                    if (index < steps.lastIndex) {
                        Box(
                            Modifier
                                .weight(1f)
                                .padding(horizontal = OinkMetrics.Space1)
                                .height(OinkMetrics.Hairline)
                                .background(if (done) colors.doneContainer else colors.connector),
                        )
                    } else {
                        Box(Modifier.weight(1f))
                    }
                }
                OinkText(
                    text = step.label,
                    color = if (done || active) colors.label else colors.inactiveLabel,
                    style = OinkTheme.textStyles.label.copy(
                        fontWeight = if (active) FontWeight.Bold else FontWeight.Normal,
                    ),
                    modifier = Modifier.padding(top = OinkMetrics.Space2),
                )
                if (step.description != null) {
                    OinkText(
                        text = step.description,
                        color = colors.description,
                        style = OinkTheme.textStyles.rowDesc,
                    )
                }
            }
        }
    }
}

@Composable
private fun OinkStepDot(number: Int, done: Boolean, active: Boolean, colors: OinkStepsColors) {
    val background = when {
        done -> colors.doneContainer
        active -> colors.activeContainer
        else -> colors.pendingContainer
    }
    val foreground = when {
        done -> colors.doneContent
        active -> colors.activeContent
        else -> colors.pendingContent
    }
    Box(
        modifier = Modifier.size(20.dp).clip(CircleShape).background(background),
        contentAlignment = Alignment.Center,
    ) {
        if (done) {
            OinkIcon(OinkIcons.Check, null, tint = foreground, size = 11.dp)
        } else {
            OinkText(text = number.toString(), color = foreground, style = OinkTheme.textStyles.micro)
        }
    }
}

object OinkBreadcrumbDefaults {
    @Composable
    fun colors(): OinkBreadcrumbColors {
        val c = OinkTheme.colors
        return OinkBreadcrumbColors(current = c.text, content = c.text3, separator = c.text3)
    }
}

@Immutable
data class OinkBreadcrumbColors(
    val current: Color,
    val content: Color,
    val separator: Color,
)

object OinkPaginationDefaults {
    val EllipsisWidth = 24.dp

    @Composable
    fun colors(): OinkPaginationColors {
        val c = OinkTheme.colors
        return OinkPaginationColors(
            content = c.text2,
            disabledContent = c.text3,
            selectedContainer = c.accentTint,
            selectedContent = c.accent,
            hover = c.fillHover,
            ellipsis = c.text3,
        )
    }
}

@Immutable
data class OinkPaginationColors(
    val content: Color,
    val disabledContent: Color,
    val selectedContainer: Color,
    val selectedContent: Color,
    val hover: Color,
    val ellipsis: Color,
)

object OinkStepsDefaults {
    @Composable
    fun colors(): OinkStepsColors {
        val c = OinkTheme.colors
        return OinkStepsColors(
            doneContainer = c.accent,
            doneContent = c.onAccent,
            activeContainer = c.accentTint,
            activeContent = c.accent,
            pendingContainer = c.fill,
            pendingContent = c.text3,
            connector = c.line,
            label = c.text,
            inactiveLabel = c.text3,
            description = c.text3,
        )
    }
}

@Immutable
data class OinkStepsColors(
    val doneContainer: Color,
    val doneContent: Color,
    val activeContainer: Color,
    val activeContent: Color,
    val pendingContainer: Color,
    val pendingContent: Color,
    val connector: Color,
    val label: Color,
    val inactiveLabel: Color,
    val description: Color,
)
