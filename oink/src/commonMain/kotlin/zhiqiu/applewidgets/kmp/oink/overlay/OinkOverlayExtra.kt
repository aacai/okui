package zhiqiu.applewidgets.kmp.oink.overlay

import zhiqiu.applewidgets.kmp.oink.basic.*
import zhiqiu.applewidgets.kmp.oink.data.*
import zhiqiu.applewidgets.kmp.oink.form.*
import zhiqiu.applewidgets.kmp.oink.layout.*
import zhiqiu.applewidgets.kmp.oink.nav.*
import zhiqiu.applewidgets.kmp.oink.primitives.*
import zhiqiu.applewidgets.kmp.oink.responsive.*
import zhiqiu.applewidgets.kmp.oink.theme.*

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.overlay.PlatformAnchoredPopup
import zhiqiu.applewidgets.kmp.overlay.PlatformDialogOverlay

/**
 * 锚定浮层相对触发元素的方向。
 *
 * - [Bottom]：向下展开，缩放入场原点在顶边中部
 * - [Top]：向上展开，缩放入场原点在底边中部
 */
enum class OinkPopoverPlacement { Bottom, Top }

/**
 * 侧边抽屉贴附的一侧。
 *
 * - [Start]：贴左缘，仅右侧为圆角
 * - [End]：贴右缘，仅左侧为圆角
 */
enum class OinkDrawerSide { Start, End }

/**
 * Oink 锚定浮层：pop 纸面 + 18 圆角，从触发元素一侧缩放回弹进场。
 *
 * 定位基于 PlatformAnchoredPopup，取的是调用点在父容器中的位置，不会自动贴着触发元素边缘对齐；
 * 所以要把触发器与 OinkPopover 放进同一个 Box，浮层从该 Box 左上角起算。
 * 平台无 window overlay 能力时退化为居中 Dialog。`expanded = false` 时不组合内容，进场动画随之重置。
 *
 * @param placement 展开方向，决定缩放入场原点
 *
 * 示例：
 * ```kotlin
 * Box {
 *     OinkButton(text = "Options", onClick = { open = true })
 *     OinkPopover(expanded = open, onDismissRequest = { open = false }) {
 *         OinkMenuItem(text = "Duplicate", onClick = { open = false })
 *         OinkMenuItem(text = "Delete", destructive = true, onClick = { open = false })
 *     }
 * }
 * ```
 */
@Composable
fun OinkPopover(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    placement: OinkPopoverPlacement = OinkPopoverPlacement.Bottom,
    colors: OinkPopoverColors = OinkPopoverDefaults.colors(),
    content: @Composable ColumnScope.() -> Unit,
) {
    if (!expanded) return
    val enter = remember { Animatable(0f) }
    LaunchedEffect(Unit) { enter.animateTo(1f, OinkMotion.spring()) }
    val shape = OinkShape(OinkPopoverDefaults.CornerRadius)
    val origin = if (placement == OinkPopoverPlacement.Bottom) {
        TransformOrigin(0.5f, 0f)
    } else {
        TransformOrigin(0.5f, 1f)
    }
    PlatformAnchoredPopup(onDismissRequest = onDismissRequest) {
        Column(
            modifier = modifier
                .graphicsLayer {
                    val s = 0.92f + 0.08f * enter.value
                    scaleX = s
                    scaleY = s
                    alpha = enter.value
                    transformOrigin = origin
                }
                .widthIn(min = OinkPopoverDefaults.MinWidth)
                .clip(shape)
                .background(colors.container)
                .border(OinkMetrics.Hairline, colors.border, shape)
                .padding(OinkPopoverDefaults.Padding),
            content = content,
        )
    }
}

/**
 * Oink 右键菜单：紧凑、无标题区，pop 底 14 圆角，0.96 缩放淡入进场。
 *
 * 只负责外观与定位，不监听鼠标右键：需调用方在右键事件里把 `expanded` 置 true。
 * 与 OinkPopover 同源，同样要与目标区域放进同一个 Box。
 *
 * 示例：
 * ```kotlin
 * Box {
 *     OinkButton(text = "Actions", onClick = { open = true })
 *     OinkContextMenu(expanded = open, onDismissRequest = { open = false }) {
 *         OinkMenuItem(text = "Rename", onClick = { open = false })
 *         OinkMenuItem(text = "Share", onClick = { open = false })
 *     }
 * }
 * ```
 */
@Composable
fun OinkContextMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    colors: OinkPopoverColors = OinkContextMenuDefaults.colors(),
    content: @Composable ColumnScope.() -> Unit,
) {
    if (!expanded) return
    val enter = remember { Animatable(0f) }
    LaunchedEffect(Unit) { enter.animateTo(1f, OinkMotion.snappy()) }
    val shape = OinkShape(OinkContextMenuDefaults.CornerRadius)
    PlatformAnchoredPopup(onDismissRequest = onDismissRequest) {
        Column(
            modifier = modifier
                .graphicsLayer {
                    val s = 0.96f + 0.04f * enter.value
                    scaleX = s
                    scaleY = s
                    alpha = enter.value
                }
                .widthIn(min = OinkContextMenuDefaults.MinWidth)
                .clip(shape)
                .background(colors.container)
                .border(OinkMetrics.Hairline, colors.border, shape)
                .padding(OinkMetrics.Space1),
            content = content,
        )
    }
}

/**
 * Oink 侧边抽屉：全屏遮罩 + 贴边滑入面板，内侧圆角、外侧直角。
 *
 * 通过 PlatformDialogOverlay 呈现，需在 Scaffold / OverlayHost 内使用；内容列自带纵向滚动，
 * 标题行固定。遮罩点击与关闭按钮都走 [onDismissRequest]，组件不持有开关状态。
 *
 * @param side 贴附侧，决定滑入方向与圆角位置
 * @param title 标题，`null` 时仅保留关闭按钮
 * @param width 面板宽度
 *
 * 示例：
 * ```kotlin
 * OinkDrawer(
 *     open = drawerOpen,
 *     onDismissRequest = { drawerOpen = false },
 *     side = OinkDrawerSide.End,
 *     title = "Filters",
 * ) {
 *     OinkText("Drawer content")
 * }
 * ```
 */
@Composable
fun OinkDrawer(
    open: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    side: OinkDrawerSide = OinkDrawerSide.End,
    width: Dp = OinkDrawerDefaults.Width,
    title: String? = null,
    colors: OinkDrawerColors = OinkDrawerDefaults.colors(),
    content: @Composable ColumnScope.() -> Unit,
) {
    if (!open) return
    val enter = remember { Animatable(0f) }
    LaunchedEffect(Unit) { enter.animateTo(1f, OinkMotion.soft()) }
    val shape = if (side == OinkDrawerSide.End) {
        OinkShape(topStart = OinkRadii.r4, bottomStart = OinkRadii.r4, topEnd = 0.dp, bottomEnd = 0.dp)
    } else {
        OinkShape(topStart = 0.dp, bottomStart = 0.dp, topEnd = OinkRadii.r4, bottomEnd = OinkRadii.r4)
    }
    PlatformDialogOverlay(onDismissRequest = onDismissRequest) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = if (side == OinkDrawerSide.End) Alignment.CenterEnd else Alignment.CenterStart,
        ) {
            OinkScrim(alpha = enter.value, onDismissRequest = onDismissRequest)
            Column(
                modifier = modifier
                    .width(width)
                    .fillMaxHeight()
                    .graphicsLayer {
                        translationX = (1f - enter.value) *
                            (if (side == OinkDrawerSide.End) size.width else -size.width)
                    }
                    .clip(shape)
                    .background(colors.container)
                    .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {},
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = OinkMetrics.Space4, end = OinkMetrics.Space2, top = OinkMetrics.Space3),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (title != null) {
                        OinkText(
                            text = title,
                            color = colors.title,
                            style = OinkTheme.textStyles.h4,
                            modifier = Modifier.weight(1f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    } else {
                        Spacer(Modifier.weight(1f))
                    }
                    OinkIconButton(
                        icon = OinkIcons.X,
                        onClick = onDismissRequest,
                        contentDescription = "Close",
                        size = OinkButtonSize.Sm,
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = OinkMetrics.Space4, vertical = OinkMetrics.Space2),
                ) {
                    content()
                }
            }
        }
    }
}

/**
 * Oink 底部抽屉：全屏遮罩 + 贴底 r4 面板，拖拽条、标题行与可滚动内容。
 *
 * 高度固定为可用高度的 88%，拖拽条仅为视觉提示、不响应手势；关闭靠遮罩点击或 [onDismissRequest]。
 * 依赖 PlatformDialogOverlay，需在 Scaffold / OverlayHost 内使用。
 *
 * @param title 标题，`null` 时省略标题行（拖拽条仍保留）
 *
 * 示例：
 * ```kotlin
 * OinkSheet(open = sheetOpen, onDismissRequest = { sheetOpen = false }, title = "Share") {
 *     OinkText("Sheet content")
 * }
 * ```
 */
@Composable
fun OinkSheet(
    open: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    colors: OinkSheetColors = OinkSheetDefaults.colors(),
    content: @Composable ColumnScope.() -> Unit,
) {
    if (!open) return
    val enter = remember { Animatable(0f) }
    LaunchedEffect(Unit) { enter.animateTo(1f, OinkMotion.soft()) }
    val shape = OinkShape(topStart = OinkRadii.r4, topEnd = OinkRadii.r4, bottomStart = 0.dp, bottomEnd = 0.dp)
    PlatformDialogOverlay(onDismissRequest = onDismissRequest) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            OinkScrim(alpha = enter.value, onDismissRequest = onDismissRequest)
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight(OinkSheetDefaults.HeightFraction)
                    .graphicsLayer { translationY = (1f - enter.value) * size.height }
                    .clip(shape)
                    .background(colors.container)
                    .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {},
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(vertical = OinkMetrics.Space2),
                    contentAlignment = Alignment.Center,
                ) {
                    Box(
                        Modifier
                            .width(36.dp)
                            .height(OinkMetrics.Space1)
                            .clip(OinkShape(2.dp))
                            .background(colors.handle),
                    )
                }
                if (title != null) {
                    OinkText(
                        text = title,
                        color = colors.title,
                        style = OinkTheme.textStyles.h4,
                        modifier = Modifier.padding(horizontal = OinkMetrics.Space4, vertical = OinkMetrics.Space2),
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = OinkMetrics.Space4, vertical = OinkMetrics.Space2),
                ) {
                    content()
                }
            }
        }
    }
}

/**
 * Oink 内联侧栏：非模态、无遮罩，card 底 + 左侧发丝线 + 可滚动内容。
 *
 * 始终参与布局而非浮层，需放在 Row 等横向容器里并由调用方控制显隐；高度撑满父级。
 * 只有传入 [onClose] 才显示关闭按钮。
 *
 * @param onClose 关闭回调，`null` 时不显示按钮
 * @param width 侧栏宽度
 *
 * 示例：
 * ```kotlin
 * Row {
 *     OinkText("Main content", modifier = Modifier.weight(1f))
 *     OinkPanel(title = "Inspector", onClose = { showPanel = false }) {
 *         OinkText("Inline side panel")
 *     }
 * }
 * ```
 */
@Composable
fun OinkPanel(
    title: String? = null,
    modifier: Modifier = Modifier,
    onClose: (() -> Unit)? = null,
    width: Dp = OinkPanelDefaults.Width,
    colors: OinkPanelColors = OinkPanelDefaults.colors(),
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .width(width)
            .fillMaxHeight()
            .background(colors.container)
            .drawBehind {
                drawLine(
                    color = colors.border,
                    start = Offset(0f, 0f),
                    end = Offset(0f, size.height),
                    strokeWidth = OinkMetrics.Hairline.toPx(),
                )
            },
    ) {
        if (title != null || onClose != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = OinkMetrics.Space4, end = OinkMetrics.Space2, top = OinkMetrics.Space3),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (title != null) {
                    OinkText(
                        text = title,
                        color = colors.title,
                        style = OinkTheme.textStyles.h4,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                } else {
                    Spacer(Modifier.weight(1f))
                }
                if (onClose != null) {
                    OinkIconButton(
                        icon = OinkIcons.X,
                        onClick = onClose,
                        contentDescription = "Close",
                        size = OinkButtonSize.Sm,
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = OinkMetrics.Space4, vertical = OinkMetrics.Space2),
        ) {
            content()
        }
    }
}

/**
 * 命令面板条目。
 *
 * @property label 主文本，同时是搜索匹配字段（忽略大小写）
 * @property hint 右侧快捷键提示，也参与搜索匹配
 * @property icon 可选的行首图标
 * @property group 分组名，`null` 表示不显示分组标题
 */
data class OinkCommandItem(
    val label: String,
    val hint: String? = null,
    val icon: ImageVector? = null,
    val group: String? = null,
)

/**
 * Oink 命令面板：全屏遮罩 + 顶部居中卡片，搜索过滤、分组、键盘与悬停高亮。
 *
 * 通过 PlatformDialogOverlay 呈现，需在 Scaffold / OverlayHost 内使用；打开时自动请求焦点，
 * 支持 ↑/↓ 移动高亮、Enter 选中、Esc 关闭。搜索框为受控组件，不传 [onQueryChange] 时输入不生效。
 *
 * @param query 搜索关键字，空白时显示全部条目
 * @param onQueryChange 关键字变更回调
 * @param placeholder 搜索框占位文案
 * @param emptyText 无匹配结果时的提示文案
 *
 * 示例：
 * ```kotlin
 * OinkCommandPalette(
 *     open = paletteOpen,
 *     onDismissRequest = { paletteOpen = false },
 *     items = listOf(
 *         OinkCommandItem("New file", hint = "⌘N", group = "File"),
 *         OinkCommandItem("Toggle theme", hint = "⌘J", group = "View"),
 *     ),
 *     onSelect = { item -> runCommand(item); paletteOpen = false },
 * )
 * ```
 */
@Composable
fun OinkCommandPalette(
    open: Boolean,
    onDismissRequest: () -> Unit,
    items: List<OinkCommandItem>,
    onSelect: (OinkCommandItem) -> Unit,
    modifier: Modifier = Modifier,
    query: String = "",
    onQueryChange: (String) -> Unit = {},
    placeholder: String = "Type a command or search…",
    emptyText: String = "No commands found",
    colors: OinkCommandPaletteColors = OinkCommandPaletteDefaults.colors(),
) {
    if (!open) return
    val filtered = remember(items, query) {
        if (query.isBlank()) {
            items
        } else {
            items.filter {
                it.label.contains(query, ignoreCase = true) ||
                    (it.hint?.contains(query, ignoreCase = true) == true)
            }
        }
    }
    var highlighted by remember { mutableStateOf(0) }
    LaunchedEffect(filtered) { highlighted = 0 }
    val enter = remember { Animatable(0f) }
    LaunchedEffect(Unit) { enter.animateTo(1f, OinkMotion.soft()) }
    val focus = remember { FocusRequester() }
    val shape = OinkShape(OinkRadii.r4)
    PlatformDialogOverlay(onDismissRequest = onDismissRequest) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
            OinkScrim(alpha = enter.value, onDismissRequest = onDismissRequest)
            Column(
                modifier = modifier
                    .padding(top = OinkMetrics.Space16)
                    .padding(horizontal = OinkMetrics.Space4)
                    .widthIn(max = OinkCommandPaletteDefaults.MaxWidth)
                    .fillMaxWidth()
                    .graphicsLayer {
                        translationY = (1f - enter.value) * (-12).dp.toPx()
                        alpha = enter.value
                    }
                    .clip(shape)
                    .background(colors.container)
                    .border(OinkMetrics.Hairline, colors.border, shape)
                    .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {}
                    .focusRequester(focus)
                    .onPreviewKeyEvent { event ->
                        if (event.type != KeyEventType.KeyDown) return@onPreviewKeyEvent false
                        when (event.key) {
                            Key.DirectionDown -> {
                                if (filtered.isNotEmpty()) highlighted = (highlighted + 1) % filtered.size
                                true
                            }
                            Key.DirectionUp -> {
                                if (filtered.isNotEmpty()) {
                                    highlighted = (highlighted - 1 + filtered.size) % filtered.size
                                }
                                true
                            }
                            Key.Enter -> {
                                filtered.getOrNull(highlighted)?.let(onSelect)
                                true
                            }
                            Key.Escape -> {
                                onDismissRequest()
                                true
                            }
                            else -> false
                        }
                    }
                    .focusable()
                    .padding(OinkMetrics.Space2),
            ) {
                OinkSearchInput(
                    value = query,
                    onValueChange = onQueryChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = placeholder,
                )
                Spacer(Modifier.height(OinkMetrics.Space2))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = OinkCommandPaletteDefaults.MaxListHeight)
                        .verticalScroll(rememberScrollState()),
                ) {
                    if (filtered.isEmpty()) {
                        OinkText(
                            text = emptyText,
                            color = colors.emptyText,
                            style = OinkTheme.textStyles.body,
                            modifier = Modifier.padding(horizontal = OinkMetrics.Space3, vertical = OinkMetrics.Space4),
                        )
                    } else {
                        var index = 0
                        filtered.groupBy { it.group }.forEach { (group, groupItems) ->
                            if (group != null) {
                                OinkText(
                                    text = group.uppercase(),
                                    color = colors.group,
                                    style = OinkTheme.textStyles.micro,
                                    modifier = Modifier.padding(
                                        start = OinkMetrics.Space3,
                                        top = OinkMetrics.Space3,
                                        bottom = OinkMetrics.Space1,
                                    ),
                                )
                            }
                            groupItems.forEach { item ->
                                val position = index++
                                OinkCommandRow(
                                    item = item,
                                    highlighted = position == highlighted,
                                    colors = colors,
                                    onClick = { onSelect(item) },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OinkCommandRow(
    item: OinkCommandItem,
    highlighted: Boolean,
    colors: OinkCommandPaletteColors,
    onClick: () -> Unit,
) {
    val interaction = remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()
    val active = hovered || highlighted
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(OinkCommandPaletteDefaults.RowHeight)
            .clip(OinkShape(OinkRadii.r2))
            .background(if (active) colors.rowActive else Color.Transparent)
            .hoverable(interaction)
            .clickable(interactionSource = interaction, indication = null, onClick = onClick)
            .padding(horizontal = OinkMetrics.Space3),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (item.icon != null) {
            OinkIcon(
                imageVector = item.icon,
                contentDescription = null,
                tint = if (active) colors.rowActiveIcon else colors.rowIcon,
                modifier = Modifier.padding(end = OinkMetrics.Space2).size(16.dp),
            )
        }
        OinkText(
            text = item.label,
            color = colors.rowText,
            style = OinkTheme.textStyles.body,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        if (item.hint != null) {
            OinkKbd(text = item.hint, modifier = Modifier.padding(start = OinkMetrics.Space2))
        }
    }
}

@Composable
private fun OinkScrim(alpha: Float, onDismissRequest: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer { this.alpha = alpha }
            .background(OinkTheme.colors.scrim)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onDismissRequest,
            ),
    )
}

object OinkPopoverDefaults {
    val MinWidth = 180.dp
    val CornerRadius = 18.dp
    val Padding = 6.dp

    @Composable
    fun colors(): OinkPopoverColors {
        val c = OinkTheme.colors
        return OinkPopoverColors(container = c.pop, border = c.line)
    }
}

@Immutable
data class OinkPopoverColors(
    val container: Color,
    val border: Color,
)

object OinkContextMenuDefaults {
    val MinWidth = 200.dp
    val CornerRadius = 14.dp

    @Composable
    fun colors(): OinkPopoverColors {
        val c = OinkTheme.colors
        return OinkPopoverColors(container = c.pop, border = c.line)
    }
}

object OinkDrawerDefaults {
    val Width = 320.dp

    @Composable
    fun colors(): OinkDrawerColors {
        val c = OinkTheme.colors
        return OinkDrawerColors(container = c.card, title = c.text)
    }
}

@Immutable
data class OinkDrawerColors(
    val container: Color,
    val title: Color,
)

object OinkSheetDefaults {
    const val HeightFraction = 0.88f

    @Composable
    fun colors(): OinkSheetColors {
        val c = OinkTheme.colors
        return OinkSheetColors(container = c.pop, title = c.text, handle = c.line2)
    }
}

@Immutable
data class OinkSheetColors(
    val container: Color,
    val title: Color,
    val handle: Color,
)

object OinkPanelDefaults {
    val Width = 300.dp

    @Composable
    fun colors(): OinkPanelColors {
        val c = OinkTheme.colors
        return OinkPanelColors(container = c.card, border = c.line, title = c.text)
    }
}

@Immutable
data class OinkPanelColors(
    val container: Color,
    val border: Color,
    val title: Color,
)

object OinkCommandPaletteDefaults {
    val MaxWidth = 560.dp
    val MaxListHeight = 420.dp
    val RowHeight = 44.dp

    @Composable
    fun colors(): OinkCommandPaletteColors {
        val c = OinkTheme.colors
        return OinkCommandPaletteColors(
            container = c.pop,
            border = c.line,
            emptyText = c.text3,
            group = c.text3,
            rowActive = c.accentTint,
            rowText = c.text,
            rowIcon = c.text2,
            rowActiveIcon = c.accentText,
        )
    }
}

@Immutable
data class OinkCommandPaletteColors(
    val container: Color,
    val border: Color,
    val emptyText: Color,
    val group: Color,
    val rowActive: Color,
    val rowText: Color,
    val rowIcon: Color,
    val rowActiveIcon: Color,
)
