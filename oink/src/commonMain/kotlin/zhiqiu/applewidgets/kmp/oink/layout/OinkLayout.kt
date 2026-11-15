package zhiqiu.applewidgets.kmp.oink.layout

import zhiqiu.applewidgets.kmp.oink.basic.*
import zhiqiu.applewidgets.kmp.oink.data.*
import zhiqiu.applewidgets.kmp.oink.form.*
import zhiqiu.applewidgets.kmp.oink.nav.*
import zhiqiu.applewidgets.kmp.oink.overlay.*
import zhiqiu.applewidgets.kmp.oink.primitives.*
import zhiqiu.applewidgets.kmp.oink.responsive.*
import zhiqiu.applewidgets.kmp.oink.theme.*

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.max

/**
 * Oink 纵向堆叠：固定间距的 Column 薄封装。
 *
 * 只负责纵向排列与间距，自身不滚动；超出父容器高度会被裁剪，整页滚动请用 [OinkScrollArea] 或 [OinkPage]。
 *
 * @param spacing 子项间距，默认 [OinkMetrics.Space2]（8dp）
 *
 * 示例：
 * ```kotlin
 * OinkStack(spacing = OinkMetrics.Space3) {
 *     OinkText("今日概览", style = OinkTheme.textStyles.h4)
 *     OinkStat(label = "Users", value = "1,204", delta = "+4.2%", deltaPositive = true)
 *     OinkButton(text = "查看详情", onClick = { })
 * }
 * ```
 */
@Composable
fun OinkStack(
    modifier: Modifier = Modifier,
    spacing: Dp = OinkMetrics.Space2,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(spacing),
        content = content,
    )
}

/**
 * Oink 横向排布：固定间距的 Row 薄封装。
 *
 * 与 Compose 原生 `Row` 的差别：间距统一由 [spacing] 控制，垂直对齐默认居中。
 *
 * @param verticalAlignment 子项垂直对齐，默认 `Alignment.CenterVertically`
 *
 * 示例：
 * ```kotlin
 * OinkInline(spacing = OinkMetrics.Space2) {
 *     OinkIcon(OinkIcons.Bell, contentDescription = null, tint = OinkTheme.colors.text3)
 *     OinkText("通知")
 *     OinkBadge(text = "3", variant = OinkBadgeVariant.Accent)
 * }
 * ```
 */
@Composable
fun OinkInline(
    modifier: Modifier = Modifier,
    spacing: Dp = OinkMetrics.Space2,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalAlignment = verticalAlignment,
        content = content,
    )
}

/**
 * Oink 等宽网格：内容按 [columns] 等宽换行排列。
 *
 * 需要有界宽度；无界（如横向滚动容器内）时回退到最小宽度计算。行高取该行最高子项，自身不做纵向滚动。
 *
 * @param columns 每行列数，小于 1 时按 1 处理
 * @param spacing 行列间距，默认 [OinkMetrics.Space3]
 *
 * 示例：
 * ```kotlin
 * OinkGrid(columns = 3) {
 *     OinkStat(label = "Users", value = "1,204", delta = "+4.2%", deltaPositive = true)
 *     OinkStat(label = "Revenue", value = "$94.5k")
 *     OinkStat(label = "Churn", value = "1.8%")
 * }
 * ```
 */
@Composable
fun OinkGrid(
    columns: Int,
    modifier: Modifier = Modifier,
    spacing: Dp = OinkMetrics.Space3,
    content: @Composable () -> Unit,
) {
    val cols = columns.coerceAtLeast(1)
    Layout(content = content, modifier = modifier) { measurables, constraints ->
        val gap = spacing.roundToPx()
        val width = if (constraints.maxWidth != Constraints.Infinity) constraints.maxWidth else constraints.minWidth
        val cellWidth = ((width - gap * (cols - 1)) / cols).coerceAtLeast(0)
        val placeables = measurables.map {
            it.measure(
                Constraints(
                    minWidth = cellWidth,
                    maxWidth = cellWidth,
                    minHeight = 0,
                    maxHeight = constraints.maxHeight,
                ),
            )
        }
        val rowCount = (placeables.size + cols - 1) / cols
        val rowHeights = IntArray(rowCount)
        placeables.forEachIndexed { index, placeable ->
            val row = index / cols
            rowHeights[row] = max(rowHeights[row], placeable.height)
        }
        val totalHeight = if (rowCount == 0) 0 else rowHeights.sum() + gap * (rowCount - 1)
        layout(width, totalHeight) {
            var y = 0
            var lastRow = -1
            placeables.forEachIndexed { index, placeable ->
                val row = index / cols
                if (row != lastRow) {
                    if (row > 0) y += rowHeights[row - 1] + gap
                    lastRow = row
                }
                placeable.placeRelative(x = (index % cols) * (cellWidth + gap), y = y)
            }
        }
    }
}

/**
 * Oink 发丝分隔线，可选左缩进。
 *
 * 与 Material `Divider` 的差别：厚度固定 [OinkMetrics.Hairline] 且取主题 `line` 色，[inset] 用于与列表文字对齐。
 *
 * @param inset 左侧缩进，默认 0dp
 *
 * 示例：
 * ```kotlin
 * OinkStack(spacing = OinkMetrics.Space2) {
 *     OinkText("账户")
 *     OinkDivider(inset = OinkMetrics.Space4)
 *     OinkText("安全")
 * }
 * ```
 */
@Composable
fun OinkDivider(
    modifier: Modifier = Modifier,
    inset: Dp = 0.dp,
    colors: OinkDividerColors = OinkDividerDefaults.colors(),
) {
    Box(
        modifier
            .fillMaxWidth()
            .padding(start = inset)
            .height(OinkMetrics.Hairline)
            .background(colors.line),
    )
}

/**
 * Oink 滚动区域：LazyColumn + 右侧可拖动的细滚动条。
 *
 * 需要父容器给出有界高度（如 `fillMaxSize()` 或 `weight(1f)`），否则内容高度为 0 不可见。
 * 滚动条仅在内容超出视口时出现，hover 时加宽并可用拖拽滚动。
 * [state] 由调用方持有，便于「回到顶部」等外部控制；条目通过 `item` / `items` 提供。
 *
 * @param state 外部持有的 [LazyListState]
 * @param contentPadding 内容内边距，用于首尾留白
 *
 * 示例：
 * ```kotlin
 * val state = rememberLazyListState()
 * OinkScrollArea(state = state, contentPadding = PaddingValues(OinkMetrics.Space4)) {
 *     item { OinkPageHeader(title = "Overview") }
 *     items(3) { index -> OinkRow(title = "Item $index") }
 * }
 * ```
 */
@Composable
fun OinkScrollArea(
    state: LazyListState,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    content: LazyListScope.() -> Unit,
) {
    Box(Modifier.fillMaxSize()) {
        LazyColumn(
            state = state,
            modifier = modifier.fillMaxSize(),
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(0.dp),
            content = content,
        )
        OinkVerticalScrollbar(
            state = state,
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
        )
    }
}

/**
 * Oink 页面标题：大标题 + 可选描述 + 右侧操作区。
 *
 * 标题列占满剩余宽度，[actions] 靠右水平排列且不换行；窄宽度下请自行精简操作按钮。
 *
 * @param description 副标题/说明，null 时不占位
 * @param actions 右侧操作槽，默认空
 *
 * 示例：
 * ```kotlin
 * OinkPageHeader(title = "Dashboard", description = "最近 7 天") {
 *     OinkButton(text = "刷新", onClick = { }, variant = OinkButtonVariant.Ghost)
 * }
 * ```
 */
@Composable
fun OinkPageHeader(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    colors: OinkPageHeaderColors = OinkPageHeaderDefaults.colors(),
    actions: @Composable RowScope.() -> Unit = {},
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(OinkMetrics.Space4),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            OinkText(text = title, color = colors.title, style = OinkTheme.textStyles.h2)
            if (description != null) {
                OinkText(
                    text = description,
                    color = colors.description,
                    style = OinkTheme.textStyles.body,
                    modifier = Modifier.padding(top = OinkMetrics.Space1),
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(OinkMetrics.Space2),
            verticalAlignment = Alignment.CenterVertically,
            content = actions,
        )
    }
}

/**
 * Oink 内容分组：可选大写小标题 + 内容列。
 *
 * 标题会转大写并以 micro 样式弱化；内容不自动加卡片或间距，需自行组合 [OinkCard] / [OinkStack]。
 *
 * @param title 分组标题，null 时只渲染内容
 *
 * 示例：
 * ```kotlin
 * OinkSection(title = "Metrics") {
 *     OinkCard {
 *         OinkRow(title = "Users", trailing = { OinkText("1,204") })
 *     }
 * }
 * ```
 */
@Composable
fun OinkSection(
    title: String? = null,
    modifier: Modifier = Modifier,
    colors: OinkSectionColors = OinkSectionDefaults.colors(),
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier.fillMaxWidth()) {
        if (title != null) {
            OinkText(
                text = title.uppercase(),
                color = colors.title,
                style = OinkTheme.textStyles.micro,
                modifier = Modifier.padding(bottom = OinkMetrics.Space2),
            )
        }
        content()
    }
}

/**
 * Oink 工具栏：52dp 高，底部发丝线，两端槽位夹标题。
 *
 * 高度固定且内部不滚动；[start] / [end] 内应放图标按钮等低高度控件。
 *
 * @param title 中间标题，null 时仅保留两端槽位
 *
 * 示例：
 * ```kotlin
 * OinkToolbar(
 *     title = "Inbox",
 *     start = { OinkIconButton(icon = OinkIcons.Menu, onClick = { }) },
 *     end = { OinkIconButton(icon = OinkIcons.Search, onClick = { }) },
 * )
 * ```
 */
@Composable
fun OinkToolbar(
    title: String? = null,
    modifier: Modifier = Modifier,
    colors: OinkToolbarColors = OinkToolbarDefaults.colors(),
    start: @Composable RowScope.() -> Unit = {},
    end: @Composable RowScope.() -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(OinkToolbarDefaults.Height)
            .drawBehind {
                val stroke = OinkMetrics.Hairline.toPx()
                drawLine(
                    color = colors.border,
                    start = Offset(0f, size.height - stroke / 2f),
                    end = Offset(size.width, size.height - stroke / 2f),
                    strokeWidth = stroke,
                )
            }
            .padding(horizontal = OinkMetrics.Space3),
        horizontalArrangement = Arrangement.spacedBy(OinkMetrics.Space2),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        start()
        if (title != null) {
            OinkText(text = title, color = colors.title, style = OinkTheme.textStyles.h4)
        }
        Box(Modifier.weight(1f))
        end()
    }
}

/**
 * Oink 应用外框：左侧固定宽侧栏 + 右侧顶栏与内容。
 *
 * 侧栏宽度完全由 [sidebar] 自身决定（通常用 [OinkNav] 并自行加宽度），框架不做响应式收折；
 * 根节点按 `fillMaxSize()` 布局，需要外层给出有界尺寸。
 *
 * @param sidebar 左侧栏内容
 * @param topBar 右侧顶栏，默认空
 *
 * 示例：
 * ```kotlin
 * var selected by remember { mutableStateOf(0) }
 * OinkAppShell(
 *     sidebar = { OinkNav(items = navItems, selectedIndex = selected, onSelectedChange = { selected = it }) },
 *     topBar = { OinkToolbar(title = "Overview") },
 * ) {
 *     OinkPage { OinkPageHeader(title = "Overview") }
 * }
 * ```
 */
@Composable
fun OinkAppShell(
    modifier: Modifier = Modifier,
    sidebar: @Composable () -> Unit,
    topBar: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Row(modifier.fillMaxSize()) {
        sidebar()
        Column(Modifier.weight(1f).fillMaxHeight()) {
            topBar()
            Box(Modifier.weight(1f).fillMaxWidth()) { content() }
        }
    }
}

/**
 * Oink 侧栏导航项。
 *
 * @property label 显示文本
 * @property icon 前置图标，null 时只显示文本
 * @property badge 右侧徽标文本（如未读数），null 时不显示；固定使用 [OinkBadgeVariant.Accent]
 *
 * 示例：
 * ```kotlin
 * val items = listOf(
 *     OinkNavItem(label = "Overview", icon = OinkIcons.Monitor),
 *     OinkNavItem(label = "Alerts", icon = OinkIcons.Bell, badge = "3"),
 * )
 * ```
 */
data class OinkNavItem(
    val label: String,
    val icon: ImageVector? = null,
    val badge: String? = null,
)

/**
 * Oink 侧栏导航：竖排条目，选中项 accent 柔和底与强调文字。
 *
 * 自身不滚动，条目多时应放在可滚动容器中。选中态由 [selectedIndex] 受控，
 * 点击条目只回调索引，不会自行修改选中态。
 *
 * @param header 分组标题，组件内转大写显示；null 时不占位
 *
 * 示例：
 * ```kotlin
 * OinkNav(
 *     items = listOf(OinkNavItem("Overview", OinkIcons.Monitor), OinkNavItem("Settings", OinkIcons.Settings)),
 *     selectedIndex = 0,
 *     onSelectedChange = { },
 *     header = "Workspace",
 * )
 * ```
 */
@Composable
fun OinkNav(
    items: List<OinkNavItem>,
    selectedIndex: Int,
    onSelectedChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    header: String? = null,
    colors: OinkNavColors = OinkNavDefaults.colors(),
) {
    Column(modifier.fillMaxWidth().padding(OinkMetrics.Space2)) {
        if (header != null) {
            OinkText(
                text = header.uppercase(),
                color = colors.header,
                style = OinkTheme.textStyles.micro,
                modifier = Modifier.padding(
                    start = OinkMetrics.Space3,
                    top = OinkMetrics.Space2,
                    bottom = OinkMetrics.Space2,
                ),
            )
        }
        items.forEachIndexed { index, item ->
            OinkNavRow(
                item = item,
                selected = index == selectedIndex,
                colors = colors,
                onClick = { onSelectedChange(index) },
            )
        }
    }
}

@Composable
private fun OinkNavRow(
    item: OinkNavItem,
    selected: Boolean,
    colors: OinkNavColors,
    onClick: () -> Unit,
) {
    val interaction = remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()
    val background by animateColorAsState(
        when {
            selected -> colors.selectedContainer
            hovered -> colors.hoverContainer
            else -> Color.Transparent
        },
        tween(OinkMotion.Dur2, easing = OinkMotion.EaseOut),
        label = "navBg",
    )
    val foreground by animateColorAsState(
        when {
            selected -> colors.selectedContent
            hovered -> colors.hoverContent
            else -> colors.content
        },
        tween(OinkMotion.Dur2, easing = OinkMotion.EaseOut),
        label = "navFg",
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(OinkMetrics.ItemHeight)
            .clip(OinkShape(OinkRadii.r2))
            .background(background)
            .hoverable(interaction)
            .clickable(interactionSource = interaction, indication = null, onClick = onClick)
            .padding(horizontal = OinkMetrics.Space3),
        horizontalArrangement = Arrangement.spacedBy(OinkMetrics.Space2),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (item.icon != null) {
            OinkIcon(item.icon, null, tint = foreground, size = 18.dp)
        }
        OinkText(
            text = item.label,
            color = foreground,
            style = OinkTheme.textStyles.menuItem,
            modifier = Modifier.weight(1f),
        )
        if (item.badge != null) {
            OinkBadge(text = item.badge, variant = OinkBadgeVariant.Accent)
        }
    }
}

/**
 * Oink 底部标签栏项。
 *
 * @property label 标签文本，以 micro 样式显示在图标下方
 * @property icon 图标，必填；选中时转为强调色
 *
 * 示例：
 * ```kotlin
 * val tabs = listOf(
 *     OinkTabBarItem(label = "首页", icon = OinkIcons.Home),
 *     OinkTabBarItem(label = "设置", icon = OinkIcons.Settings),
 * )
 * ```
 */
data class OinkTabBarItem(
    val label: String,
    val icon: ImageVector,
)

/**
 * Oink 底部标签栏：56dp 高，顶部发丝线，选中强调色。
 *
 * 条目等宽平分，选中态受控于 [selectedIndex]；点击只回调索引。
 * 通常放在页面 `Column` 末尾或配合 [OinkAppShell] 使用。
 *
 * 示例：
 * ```kotlin
 * OinkTabBar(
 *     items = listOf(OinkTabBarItem("首页", OinkIcons.Home), OinkTabBarItem("我的", OinkIcons.User)),
 *     selectedIndex = 0,
 *     onSelectedChange = { },
 * )
 * ```
 */
@Composable
fun OinkTabBar(
    items: List<OinkTabBarItem>,
    selectedIndex: Int,
    onSelectedChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    colors: OinkTabBarColors = OinkTabBarDefaults.colors(),
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(OinkTabBarDefaults.Height)
            .background(colors.container)
            .drawBehind {
                drawLine(
                    color = colors.border,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = OinkMetrics.Hairline.toPx(),
                )
            },
    ) {
        items.forEachIndexed { index, item ->
            OinkTabBarCell(
                item = item,
                selected = index == selectedIndex,
                colors = colors,
                modifier = Modifier.weight(1f),
                onClick = { onSelectedChange(index) },
            )
        }
    }
}

@Composable
private fun OinkTabBarCell(
    item: OinkTabBarItem,
    selected: Boolean,
    colors: OinkTabBarColors,
    modifier: Modifier,
    onClick: () -> Unit,
) {
    val interaction = remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()
    val foreground by animateColorAsState(
        when {
            selected -> colors.selectedContent
            hovered -> colors.hoverContent
            else -> colors.content
        },
        tween(OinkMotion.Dur2, easing = OinkMotion.EaseOut),
        label = "tabBarFg",
    )
    Column(
        modifier = modifier
            .fillMaxHeight()
            .hoverable(interaction)
            .clickable(interactionSource = interaction, indication = null, onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        OinkIcon(item.icon, null, tint = foreground, size = 20.dp)
        OinkText(
            text = item.label,
            color = foreground,
            style = OinkTheme.textStyles.micro,
            modifier = Modifier.padding(top = 2.dp),
        )
    }
}

/**
 * Oink 分栏面板：两栏 + 可拖动分隔条，拖动时强调色。
 *
 * 与 Compose 原生布局的差别：分隔条自带 8dp 拖拽热区并实时改变两栏比例。
 * 需要父容器给出有界宽高（`fillMaxSize()` 或固定尺寸）；比例夹取在 [minRatio] 与 `1 - minRatio` 之间，
 * 仅保存在内部状态，重组不会重置。
 *
 * @param initialRatio 第一栏初始占比，默认 0.5
 * @param minRatio 单栏最小占比，实际夹取到 0.001..0.5
 * @param orientation 横向为左右分栏，纵向为上下分栏
 *
 * 示例：
 * ```kotlin
 * OinkSplitPane(
 *     first = { OinkNav(items = navItems, selectedIndex = 0, onSelectedChange = { }) },
 *     second = { OinkPage { OinkText("Detail") } },
 *     initialRatio = 0.28f,
 * )
 * ```
 */
@Composable
fun OinkSplitPane(
    modifier: Modifier = Modifier,
    first: @Composable () -> Unit,
    second: @Composable () -> Unit,
    initialRatio: Float = 0.5f,
    minRatio: Float = 0.2f,
    orientation: Orientation = Orientation.Horizontal,
    colors: OinkSplitPaneColors = OinkSplitPaneDefaults.colors(),
) {
    val bound = minRatio.coerceIn(0.001f, 0.5f)
    var ratio by remember { mutableStateOf(initialRatio.coerceIn(bound, 1f - bound)) }
    var dragging by remember { mutableStateOf(false) }
    val dividerColor by animateColorAsState(
        if (dragging) colors.activeDivider else colors.divider,
        tween(OinkMotion.Dur2, easing = OinkMotion.EaseOut),
        label = "paneDivider",
    )
    BoxWithConstraints(modifier.fillMaxSize()) {
        val totalPx = if (orientation == Orientation.Horizontal) {
            constraints.maxWidth.toFloat()
        } else {
            constraints.maxHeight.toFloat()
        }
        val onDrag: (Float) -> Unit = { delta ->
            if (totalPx > 0f) ratio = (ratio + delta / totalPx).coerceIn(bound, 1f - bound)
        }
        if (orientation == Orientation.Horizontal) {
            Row(Modifier.fillMaxSize()) {
                Box(Modifier.weight(ratio).fillMaxHeight()) { first() }
                OinkPaneDivider(
                    orientation = orientation,
                    modifier = Modifier.fillMaxHeight(),
                    color = dividerColor,
                    onDragStart = { dragging = true },
                    onDragEnd = { dragging = false },
                    onDrag = onDrag,
                )
                Box(Modifier.weight(1f - ratio).fillMaxHeight()) { second() }
            }
        } else {
            Column(Modifier.fillMaxSize()) {
                Box(Modifier.weight(ratio).fillMaxWidth()) { first() }
                OinkPaneDivider(
                    orientation = orientation,
                    modifier = Modifier.fillMaxWidth(),
                    color = dividerColor,
                    onDragStart = { dragging = true },
                    onDragEnd = { dragging = false },
                    onDrag = onDrag,
                )
                Box(Modifier.weight(1f - ratio).fillMaxWidth()) { second() }
            }
        }
    }
}

@Composable
private fun OinkPaneDivider(
    orientation: Orientation,
    modifier: Modifier,
    color: Color,
    onDragStart: () -> Unit,
    onDragEnd: () -> Unit,
    onDrag: (Float) -> Unit,
) {
    val thickness = OinkSplitPaneDefaults.HandleThickness
    Box(
        modifier = modifier
            .then(
                if (orientation == Orientation.Horizontal) {
                    Modifier.width(thickness).fillMaxHeight()
                } else {
                    Modifier.height(thickness).fillMaxWidth()
                },
            )
            .pointerInput(orientation) {
                detectDragGestures(
                    onDragStart = { onDragStart() },
                    onDragEnd = { onDragEnd() },
                    onDragCancel = { onDragEnd() },
                ) { change, dragAmount ->
                    change.consume()
                    onDrag(if (orientation == Orientation.Horizontal) dragAmount.x else dragAmount.y)
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        Box(
            Modifier
                .then(
                    if (orientation == Orientation.Horizontal) {
                        Modifier.width(OinkMetrics.Hairline).fillMaxHeight()
                    } else {
                        Modifier.height(OinkMetrics.Hairline).fillMaxWidth()
                    },
                )
                .background(color),
        )
    }
}

object OinkDividerDefaults {
    @Composable
    fun colors(): OinkDividerColors {
        val c = OinkTheme.colors
        return OinkDividerColors(line = c.line)
    }
}

@Immutable
data class OinkDividerColors(
    val line: Color,
)

object OinkPageHeaderDefaults {
    @Composable
    fun colors(): OinkPageHeaderColors {
        val c = OinkTheme.colors
        return OinkPageHeaderColors(title = c.text, description = c.text2)
    }
}

@Immutable
data class OinkPageHeaderColors(
    val title: Color,
    val description: Color,
)

object OinkSectionDefaults {
    @Composable
    fun colors(): OinkSectionColors {
        val c = OinkTheme.colors
        return OinkSectionColors(title = c.text3)
    }
}

@Immutable
data class OinkSectionColors(
    val title: Color,
)

object OinkToolbarDefaults {
    val Height = 52.dp

    @Composable
    fun colors(): OinkToolbarColors {
        val c = OinkTheme.colors
        return OinkToolbarColors(border = c.line, title = c.text)
    }
}

@Immutable
data class OinkToolbarColors(
    val border: Color,
    val title: Color,
)

object OinkNavDefaults {
    @Composable
    fun colors(): OinkNavColors {
        val c = OinkTheme.colors
        return OinkNavColors(
            header = c.text3,
            selectedContainer = c.accentTint,
            selectedContent = c.accent,
            hoverContainer = c.fillHover,
            hoverContent = c.text2,
            content = c.text3,
        )
    }
}

@Immutable
data class OinkNavColors(
    val header: Color,
    val selectedContainer: Color,
    val selectedContent: Color,
    val hoverContainer: Color,
    val hoverContent: Color,
    val content: Color,
)

object OinkTabBarDefaults {
    val Height = 56.dp

    @Composable
    fun colors(): OinkTabBarColors {
        val c = OinkTheme.colors
        return OinkTabBarColors(
            container = c.surface,
            border = c.line,
            selectedContent = c.accent,
            hoverContent = c.text2,
            content = c.text3,
        )
    }
}

@Immutable
data class OinkTabBarColors(
    val container: Color,
    val border: Color,
    val selectedContent: Color,
    val hoverContent: Color,
    val content: Color,
)

object OinkSplitPaneDefaults {
    val HandleThickness = 8.dp

    @Composable
    fun colors(): OinkSplitPaneColors {
        val c = OinkTheme.colors
        return OinkSplitPaneColors(divider = c.line, activeDivider = c.accent)
    }
}

@Immutable
data class OinkSplitPaneColors(
    val divider: Color,
    val activeDivider: Color,
)
