package zhiqiu.applewidgets.kmp.oink.data

import zhiqiu.applewidgets.kmp.oink.basic.*
import zhiqiu.applewidgets.kmp.oink.form.*
import zhiqiu.applewidgets.kmp.oink.layout.*
import zhiqiu.applewidgets.kmp.oink.nav.*
import zhiqiu.applewidgets.kmp.oink.overlay.*
import zhiqiu.applewidgets.kmp.oink.primitives.*
import zhiqiu.applewidgets.kmp.oink.responsive.*
import zhiqiu.applewidgets.kmp.oink.theme.*

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val OinkCellHorizontalPadding = 12.dp

/**
 * Oink 图片：圆角裁剪 + 顶部高光，无图时显示凹槽底与占位图标。
 *
 * 组件本身不含图片加载器，只接受已解码好的 [ImageBitmap]；网络 / 文件请先在调用方加载。
 * 无图时仍按 [aspectRatio] 保留占位尺寸，避免加载完成后布局跳动。
 *
 * @param aspectRatio 宽高比；传 `null` 时改成最小高度 96dp 的自适应块
 * @param overlay 覆盖在图片之上的内容（徽标、遮罩、按钮等），作用域为 BoxScope
 *
 * 示例：
 * ```kotlin
 * OinkImage(
 *     imageBitmap = cover,
 *     contentDescription = "封面",
 *     aspectRatio = 4f / 3f,
 * ) {
 *     OinkCount(value = 3, max = 99)
 * }
 * ```
 */
@Composable
fun OinkImage(
    imageBitmap: ImageBitmap? = null,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
    aspectRatio: Float? = 16f / 9f,
    shape: Shape = OinkShape(OinkRadii.r3),
    overlay: (@Composable BoxScope.() -> Unit)? = null,
    colors: OinkImageColors = OinkImageDefaults.colors(),
) {
    Box(
        modifier = modifier
            .then(
                if (aspectRatio != null) {
                    Modifier.aspectRatio(aspectRatio)
                } else {
                    Modifier.defaultMinSize(minHeight = OinkImageDefaults.MinHeight)
                },
            )
            .clip(shape)
            .background(colors.container)
            .drawBehind {
                drawLine(
                    color = colors.highlight,
                    start = Offset(0f, 0.75.dp.toPx()),
                    end = Offset(size.width, 0.75.dp.toPx()),
                    strokeWidth = 1.5.dp.toPx(),
                )
            },
        contentAlignment = Alignment.Center,
    ) {
        if (imageBitmap != null) {
            Image(
                bitmap = imageBitmap,
                contentDescription = contentDescription,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        } else {
            OinkIcon(OinkIcons.Image, contentDescription, tint = colors.placeholder, size = 28.dp)
        }
        if (overlay != null) overlay()
    }
}

/**
 * Oink 表格：表头 + 等宽列的静态表格，行间发丝线分隔。
 *
 * 非懒加载，整表一次测量布局，适合 20 行以内的设置 / 汇总表；长列表请用 OinkList。
 * 不支持横向滚动，单元格固定单行（超长文本被裁切而不换行），行高 48dp。
 *
 * @param columnWeights 各列宽度权重，为空则均分；缺失列按 1f 处理
 * @param onRowClick 传入后行可点击并有 hover 反馈，回调参数为行下标
 *
 * 示例：
 * ```kotlin
 * OinkTable(
 *     headers = listOf("Task", "Owner", "Status"),
 *     rows = listOf(listOf("Homepage redesign", "Lena Hart", "In progress")),
 *     columnWeights = listOf(2f, 1f, 1f),
 * )
 * ```
 */
@Composable
fun OinkTable(
    headers: List<String>,
    rows: List<List<String>>,
    modifier: Modifier = Modifier,
    columnWeights: List<Float>? = null,
    onRowClick: ((Int) -> Unit)? = null,
    colors: OinkTableColors = OinkTableDefaults.colors(),
) {
    val weights = headers.indices.map { columnWeights?.getOrNull(it) ?: 1f }
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(OinkTableDefaults.HeaderHeight)
                .padding(horizontal = OinkCellHorizontalPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            headers.forEachIndexed { index, header ->
                OinkText(
                    text = header.uppercase(),
                    modifier = Modifier.weight(weights[index]),
                    color = colors.headerContent,
                    style = OinkTheme.textStyles.micro,
                    maxLines = 1,
                )
            }
        }
        Box(Modifier.fillMaxWidth().height(OinkMetrics.Hairline).background(colors.separator))
        rows.forEachIndexed { index, row ->
            val rowClick: (() -> Unit)? = onRowClick?.let { click -> { click(index) } }
            OinkTableRow(
                cells = row,
                weights = weights,
                colors = colors,
                onClick = rowClick,
            )
            if (index != rows.lastIndex) {
                Box(Modifier.fillMaxWidth().height(OinkMetrics.Hairline).background(colors.separator))
            }
        }
    }
}

@Composable
private fun OinkTableRow(
    cells: List<String>,
    weights: List<Float>,
    colors: OinkTableColors,
    onClick: (() -> Unit)?,
) {
    val interaction = remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(OinkTableDefaults.RowHeight)
            .background(if (hovered && onClick != null) colors.rowHover else Color.Transparent)
            .then(
                if (onClick != null) {
                    Modifier
                        .hoverable(interaction)
                        .clickable(interactionSource = interaction, indication = null, onClick = onClick)
                } else {
                    Modifier
                },
            )
            .padding(horizontal = OinkCellHorizontalPadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        cells.forEachIndexed { index, cell ->
            OinkText(
                text = cell,
                modifier = Modifier.weight(weights.getOrNull(index) ?: 1f),
                color = colors.cellContent,
                style = OinkTheme.textStyles.body,
                maxLines = 1,
            )
        }
    }
}

/**
 * Oink 统计块：标签 / 大数值 / 涨跌 / 迷你折线，放在卡片内使用。
 *
 * 折线按数据自身极值归一化，只表达趋势形状、不表达绝对值；数据全部相同时画成水平线。
 *
 * @param trend 至少 2 个点才绘制折线，顺序为旧 → 新
 * @param deltaPositive `false` 用下跌色；`null` 按上涨处理
 *
 * 示例：
 * ```kotlin
 * OinkStat(
 *     label = "Monthly active users",
 *     value = "24.8k",
 *     delta = "+12.4%",
 *     trend = listOf(12f, 15f, 14f, 19f, 24f),
 * )
 * ```
 */
@Composable
fun OinkStat(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    delta: String? = null,
    deltaPositive: Boolean? = null,
    trend: List<Float>? = null,
    icon: ImageVector? = null,
    colors: OinkStatColors = OinkStatDefaults.colors(),
) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                OinkIcon(icon, null, tint = colors.label, size = 16.dp, modifier = Modifier.padding(end = 6.dp))
            }
            OinkText(text = label, color = colors.label, style = OinkTheme.textStyles.caption)
        }
        OinkText(
            text = value,
            color = colors.value,
            style = OinkTheme.textStyles.h2,
            modifier = Modifier.padding(top = OinkMetrics.Space1),
        )
        if (delta != null) {
            val tint = if (deltaPositive != false) colors.positive else colors.negative
            Row(
                modifier = Modifier.padding(top = OinkMetrics.Space1),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OinkIcon(OinkIcons.TrendingUp, null, tint = tint, size = 14.dp)
                OinkText(
                    text = delta,
                    color = tint,
                    style = OinkTheme.textStyles.caption,
                    modifier = Modifier.padding(start = OinkMetrics.Space1),
                )
            }
        }
        if (trend != null && trend.size >= 2) {
            val points = trend
            Canvas(
                modifier = Modifier
                    .padding(top = OinkMetrics.Space2)
                    .fillMaxWidth()
                    .height(40.dp),
            ) {
                val low = points.minOrNull() ?: 0f
                val high = points.maxOrNull() ?: 0f
                val span = if (high - low == 0f) 1f else high - low
                val path = Path()
                points.forEachIndexed { index, point ->
                    val x = size.width * index / (points.size - 1).toFloat()
                    val y = size.height - (point - low) / span * size.height
                    if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
                }
                drawPath(
                    path = path,
                    color = colors.trend,
                    style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round),
                )
            }
        }
    }
}

/**
 * Oink 数字徽标：accent 胶囊，超过 [max] 显示 “max+”。
 *
 * 固定单行、最小高度 18dp，常用于图标角标或行尾未读计数。
 *
 * @param max 显示上限，超出后显示 “{max}+”；为 `null` 时始终显示真实数字
 *
 * 示例：
 * ```kotlin
 * OinkCount(value = 128, max = 99)
 * ```
 */
@Composable
fun OinkCount(
    value: Int,
    modifier: Modifier = Modifier,
    max: Int? = null,
    colors: OinkCountColors = OinkCountDefaults.colors(),
) {
    Box(
        modifier = modifier
            .heightIn(min = OinkCountDefaults.MinHeight)
            .clip(CircleShape)
            .background(colors.container)
            .padding(horizontal = OinkCountDefaults.HorizontalPadding),
        contentAlignment = Alignment.Center,
    ) {
        OinkText(
            text = if (max != null && value > max) "$max+" else value.toString(),
            color = colors.content,
            style = OinkTheme.textStyles.caption.copy(fontSize = 11.sp, fontWeight = FontWeight.SemiBold),
            maxLines = 1,
        )
    }
}

/**
 * 描述列表条目。
 *
 * @property term 术语 / 字段名，固定 120dp 宽，超长时换行
 * @property description 说明正文，占据剩余宽度
 */
data class OinkDescriptionItem(val term: String, val description: String)

/**
 * Oink 描述列表：左术语右说明，可并排多列。
 *
 * 按 [columns] 对条目分包，每包一行；末包不足时用等宽空白占位，保证列对齐。
 *
 * @param columns 每行并排的条目数，`<= 1` 时为单列列表
 *
 * 示例：
 * ```kotlin
 * OinkDescriptionList(
 *     items = listOf(
 *         OinkDescriptionItem("Owner", "Lena Hart"),
 *         OinkDescriptionItem("Status", "In progress"),
 *     ),
 *     columns = 2,
 * )
 * ```
 */
@Composable
fun OinkDescriptionList(
    items: List<OinkDescriptionItem>,
    modifier: Modifier = Modifier,
    columns: Int = 1,
    colors: OinkDescriptionListColors = OinkDescriptionListDefaults.colors(),
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(OinkMetrics.Space2),
    ) {
        if (columns <= 1) {
            items.forEach { item -> OinkDescriptionRow(item, colors = colors, modifier = Modifier.fillMaxWidth()) }
        } else {
            items.chunked(columns).forEach { chunk ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(OinkMetrics.Space4),
                ) {
                    chunk.forEach { item -> OinkDescriptionRow(item, colors = colors, modifier = Modifier.weight(1f)) }
                    repeat(columns - chunk.size) { Box(Modifier.weight(1f)) }
                }
            }
        }
    }
}

@Composable
private fun OinkDescriptionRow(
    item: OinkDescriptionItem,
    colors: OinkDescriptionListColors,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        OinkText(
            text = item.term,
            modifier = Modifier.width(OinkDescriptionListDefaults.TermWidth).padding(end = OinkMetrics.Space3),
            color = colors.term,
            style = OinkTheme.textStyles.caption,
        )
        OinkText(
            text = item.description,
            modifier = Modifier.weight(1f),
            color = colors.description,
            style = OinkTheme.textStyles.body,
        )
    }
}

/**
 * 时间线条目。
 *
 * @property title 主标题
 * @property description 可选次要说明
 * @property time 可选时间戳文本，原样展示
 * @property done 是否已完成；`true` 为 accent 实心圆 + 勾，`false` 为空心圆
 */
data class OinkTimelineItem(
    val title: String,
    val description: String? = null,
    val time: String? = null,
    val done: Boolean = true,
)

/**
 * Oink 时间线：圆点 + 竖向连接线 + 右侧内容。
 *
 * 行高由内容撑开，最后一行的连接线自动省略；组件不滚动，长内容交由外层容器处理。
 *
 * 示例：
 * ```kotlin
 * OinkTimeline(
 *     items = listOf(
 *         OinkTimelineItem("Created", time = "09:12"),
 *         OinkTimelineItem("Reviewed", "Lena Hart", time = "10:04"),
 *         OinkTimelineItem("Shipped", done = false),
 *     ),
 * )
 * ```
 */
@Composable
fun OinkTimeline(
    items: List<OinkTimelineItem>,
    modifier: Modifier = Modifier,
    colors: OinkTimelineColors = OinkTimelineDefaults.colors(),
) {
    Column(modifier = modifier.fillMaxWidth()) {
        items.forEachIndexed { index, item ->
            OinkTimelineRow(item = item, isLast = index == items.lastIndex, colors = colors)
        }
    }
}

@Composable
private fun OinkTimelineRow(
    item: OinkTimelineItem,
    isLast: Boolean,
    colors: OinkTimelineColors,
) {
    Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
        Box(
            modifier = Modifier
                .width(OinkMetrics.Space5)
                .fillMaxHeight()
                .drawBehind {
                    if (!isLast) {
                        drawLine(
                            color = colors.connector,
                            start = Offset(size.width / 2f, 18.dp.toPx()),
                            end = Offset(size.width / 2f, size.height),
                            strokeWidth = OinkMetrics.Hairline.toPx(),
                        )
                    }
                },
            contentAlignment = Alignment.TopCenter,
        ) {
            Box(
                modifier = Modifier
                    .size(OinkTimelineDefaults.MarkerSize)
                    .clip(CircleShape)
                    .background(if (item.done) colors.doneContainer else colors.pendingContainer)
                    .border(OinkMetrics.Hairline, if (item.done) colors.doneContainer else colors.pendingBorder, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                if (item.done) OinkIcon(OinkIcons.Check, null, tint = colors.check, size = 11.dp)
            }
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = OinkMetrics.Space2, bottom = if (isLast) 0.dp else OinkMetrics.Space3),
        ) {
            OinkText(text = item.title, color = colors.title, style = OinkTheme.textStyles.label)
            if (item.description != null) {
                OinkText(
                    text = item.description,
                    color = colors.description,
                    style = OinkTheme.textStyles.caption,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
            if (item.time != null) {
                OinkText(
                    text = item.time,
                    color = colors.time,
                    style = OinkTheme.textStyles.micro,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
        }
    }
}

/**
 * 树节点。
 *
 * @property label 显示文本，单行
 * @property children 子节点；为空即叶子，无箭头且不可展开
 * @property icon 可选的行首图标
 */
data class OinkTreeNode(
    val label: String,
    val children: List<OinkTreeNode> = emptyList(),
    val icon: ImageVector? = null,
)

/**
 * Oink 树：可展开层级，缩进 16dp/层，箭头随开合旋转。
 *
 * 展开状态由组件内部持有，键为节点在树中的位置路径；整体替换 [nodes] 会重置展开状态。
 * 仅含子节点的行可点击展开，叶子行点击无响应。
 *
 * @param defaultExpanded 初始展开状态，对所有层级生效
 *
 * 示例：
 * ```kotlin
 * OinkTree(
 *     nodes = listOf(
 *         OinkTreeNode("src", children = listOf(OinkTreeNode("App.kt"), OinkTreeNode("Theme.kt"))),
 *         OinkTreeNode("build.gradle.kts"),
 *     ),
 *     defaultExpanded = true,
 * )
 * ```
 */
@Composable
fun OinkTree(
    nodes: List<OinkTreeNode>,
    modifier: Modifier = Modifier,
    defaultExpanded: Boolean = false,
    colors: OinkTreeColors = OinkTreeDefaults.colors(),
) {
    val expanded = remember(nodes) { mutableStateMapOf<String, Boolean>() }
    Column(modifier = modifier.fillMaxWidth()) {
        nodes.forEachIndexed { index, node ->
            OinkTreeBranch(node, "t$index", 0, expanded, defaultExpanded, colors)
        }
    }
}

@Composable
private fun OinkTreeBranch(
    node: OinkTreeNode,
    path: String,
    depth: Int,
    expanded: SnapshotStateMap<String, Boolean>,
    defaultExpanded: Boolean,
    colors: OinkTreeColors,
) {
    val hasChildren = node.children.isNotEmpty()
    val isOpen = expanded[path] ?: defaultExpanded
    val rotation by animateFloatAsState(
        if (isOpen) 90f else 0f,
        OinkMotion.spring(),
        label = "treeChevron",
    )
    val interaction = remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(OinkTreeDefaults.RowHeight)
            .background(if (hovered) colors.hover else Color.Transparent)
            .hoverable(interaction)
            .clickable(interactionSource = interaction, indication = null, enabled = hasChildren) {
                expanded[path] = !isOpen
            }
            .padding(start = OinkTreeDefaults.IndentStep * depth, end = OinkMetrics.Space2),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.size(16.dp), contentAlignment = Alignment.Center) {
            if (hasChildren) {
                OinkIcon(
                    imageVector = OinkIcons.ChevronRight,
                    contentDescription = null,
                    tint = colors.chevron,
                    size = 12.dp,
                    modifier = Modifier.graphicsLayer { rotationZ = rotation },
                )
            }
        }
        if (node.icon != null) {
            OinkIcon(node.icon, null, tint = colors.icon, size = 14.dp, modifier = Modifier.padding(end = 6.dp))
        }
        OinkText(
            text = node.label,
            color = colors.content,
            style = OinkTheme.textStyles.menuItem,
            maxLines = 1,
        )
    }
    if (hasChildren && isOpen) {
        node.children.forEachIndexed { index, child ->
            OinkTreeBranch(child, "$path.$index", depth + 1, expanded, defaultExpanded, colors)
        }
    }
}

/**
 * Oink 代码块：凹槽底等宽字体，可选右上角语言标签，内容可选中。
 *
 * 不做语法高亮，也无横向滚动；超长行按父级宽度自动换行。
 *
 * @param language 右上角标签文本，`null` 时不显示
 *
 * 示例：
 * ```kotlin
 * OinkCode(
 *     code = "val colors = OinkTheme.colors",
 *     language = "kotlin",
 * )
 * ```
 */
@Composable
fun OinkCode(
    code: String,
    modifier: Modifier = Modifier,
    language: String? = null,
    colors: OinkCodeColors = OinkCodeDefaults.colors(),
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(OinkShape(OinkRadii.r2))
            .background(colors.container)
            .padding(OinkMetrics.Space3),
    ) {
        if (language != null) {
            OinkText(
                text = language,
                color = colors.language,
                style = OinkTheme.textStyles.micro,
                modifier = Modifier.align(Alignment.End).padding(bottom = OinkMetrics.Space1),
            )
        }
        SelectionContainer {
            OinkText(text = code, color = colors.content, style = OinkTheme.textStyles.mono)
        }
    }
}

/**
 * Oink 表情回应：胶囊计数，选中时 accent 柔和底 + 描边。
 *
 * 受控展示组件：点击只回调 [onClick]，选中态与计数均由调用方维护，组件内部不改状态。
 *
 * @param selected 是否已被当前用户选中
 * @param onClick 点击切换回调
 *
 * 示例：
 * ```kotlin
 * OinkReaction(
 *     emoji = "👍",
 *     count = 12,
 *     selected = picked.contains("👍"),
 *     onClick = { toggleReaction("👍") },
 * )
 * ```
 */
@Composable
fun OinkReaction(
    emoji: String,
    count: Int,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: OinkReactionColors = OinkReactionDefaults.colors(),
) {
    val interaction = remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()
    Row(
        modifier = modifier
            .height(OinkReactionDefaults.Height)
            .clip(CircleShape)
            .background(
                when {
                    selected -> colors.selectedContainer
                    hovered -> colors.hoverContainer
                    else -> colors.container
                },
            )
            .border(OinkMetrics.Hairline, if (selected) colors.selectedBorder else colors.border, CircleShape)
            .hoverable(interaction)
            .clickable(interactionSource = interaction, indication = null, onClick = onClick)
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OinkText(
            text = emoji,
            style = OinkTheme.textStyles.caption.copy(fontSize = 14.sp),
            maxLines = 1,
        )
        OinkText(
            text = count.toString(),
            color = if (selected) colors.selectedContent else colors.content,
            style = OinkTheme.textStyles.caption,
            modifier = Modifier.padding(start = 5.dp),
            maxLines = 1,
        )
    }
}

object OinkImageDefaults {
    val MinHeight = 96.dp

    @Composable
    fun colors(): OinkImageColors {
        val c = OinkTheme.colors
        return OinkImageColors(container = c.well, highlight = c.hi, placeholder = c.text3)
    }
}

@Immutable
data class OinkImageColors(
    val container: Color,
    val highlight: Color,
    val placeholder: Color,
)

object OinkTableDefaults {
    val HeaderHeight = 36.dp
    val RowHeight = 48.dp

    @Composable
    fun colors(): OinkTableColors {
        val c = OinkTheme.colors
        return OinkTableColors(
            headerContent = c.text3,
            cellContent = c.text2,
            separator = c.line,
            rowHover = c.fillHover,
        )
    }
}

@Immutable
data class OinkTableColors(
    val headerContent: Color,
    val cellContent: Color,
    val separator: Color,
    val rowHover: Color,
)

object OinkStatDefaults {
    @Composable
    fun colors(): OinkStatColors {
        val c = OinkTheme.colors
        return OinkStatColors(
            label = c.text2,
            value = c.text,
            positive = c.successText,
            negative = c.dangerText,
            trend = c.accent,
        )
    }
}

@Immutable
data class OinkStatColors(
    val label: Color,
    val value: Color,
    val positive: Color,
    val negative: Color,
    val trend: Color,
)

object OinkCountDefaults {
    val MinHeight = 18.dp
    val HorizontalPadding = 5.dp

    @Composable
    fun colors(): OinkCountColors {
        val c = OinkTheme.colors
        return OinkCountColors(container = c.accent, content = c.onAccent)
    }
}

@Immutable
data class OinkCountColors(
    val container: Color,
    val content: Color,
)

object OinkDescriptionListDefaults {
    val TermWidth = 120.dp

    @Composable
    fun colors(): OinkDescriptionListColors {
        val c = OinkTheme.colors
        return OinkDescriptionListColors(term = c.text3, description = c.text)
    }
}

@Immutable
data class OinkDescriptionListColors(
    val term: Color,
    val description: Color,
)

object OinkTimelineDefaults {
    val MarkerSize = 18.dp

    @Composable
    fun colors(): OinkTimelineColors {
        val c = OinkTheme.colors
        return OinkTimelineColors(
            connector = c.line,
            doneContainer = c.accent,
            pendingContainer = c.card,
            pendingBorder = c.line,
            check = c.onAccent,
            title = c.text,
            description = c.text2,
            time = c.text3,
        )
    }
}

@Immutable
data class OinkTimelineColors(
    val connector: Color,
    val doneContainer: Color,
    val pendingContainer: Color,
    val pendingBorder: Color,
    val check: Color,
    val title: Color,
    val description: Color,
    val time: Color,
)

object OinkTreeDefaults {
    val RowHeight = 32.dp
    val IndentStep = 16.dp

    @Composable
    fun colors(): OinkTreeColors {
        val c = OinkTheme.colors
        return OinkTreeColors(
            chevron = c.text3,
            icon = c.text3,
            content = c.text2,
            hover = c.fillHover,
        )
    }
}

@Immutable
data class OinkTreeColors(
    val chevron: Color,
    val icon: Color,
    val content: Color,
    val hover: Color,
)

object OinkCodeDefaults {
    @Composable
    fun colors(): OinkCodeColors {
        val c = OinkTheme.colors
        return OinkCodeColors(container = c.well, language = c.text3, content = c.text)
    }
}

@Immutable
data class OinkCodeColors(
    val container: Color,
    val language: Color,
    val content: Color,
)

object OinkReactionDefaults {
    val Height = 28.dp

    @Composable
    fun colors(): OinkReactionColors {
        val c = OinkTheme.colors
        return OinkReactionColors(
            container = c.fill,
            hoverContainer = c.fillHover,
            selectedContainer = c.accentTint,
            border = c.line,
            selectedBorder = c.accent,
            content = c.text2,
            selectedContent = c.accentText,
        )
    }
}

@Immutable
data class OinkReactionColors(
    val container: Color,
    val hoverContainer: Color,
    val selectedContainer: Color,
    val border: Color,
    val selectedBorder: Color,
    val content: Color,
    val selectedContent: Color,
)
