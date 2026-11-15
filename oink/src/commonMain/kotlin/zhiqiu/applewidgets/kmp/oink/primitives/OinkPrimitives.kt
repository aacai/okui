package zhiqiu.applewidgets.kmp.oink.primitives

import zhiqiu.applewidgets.kmp.oink.basic.*
import zhiqiu.applewidgets.kmp.oink.data.*
import zhiqiu.applewidgets.kmp.oink.form.*
import zhiqiu.applewidgets.kmp.oink.layout.*
import zhiqiu.applewidgets.kmp.oink.nav.*
import zhiqiu.applewidgets.kmp.oink.overlay.*
import zhiqiu.applewidgets.kmp.oink.responsive.*
import zhiqiu.applewidgets.kmp.oink.theme.*

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Oink 表面三档。
 *
 * - [Plain]：使用 `surface` 底色，最扁平的基础表面。
 * - [Raised]：使用 `card` 底色并在顶部绘制高光线，模拟浮起。
 * - [Well]：使用 `well` 下沉底色，用于凹槽 / 内嵌区域。
 *
 * 示例：
 * ```kotlin
 * OinkSurface(variant = OinkSurfaceVariant.Raised) { OinkText("Raised") }
 * ```
 */
enum class OinkSurfaceVariant { Plain, Raised, Well }

/**
 * Oink 表面：无投影，靠表面色阶 + 发丝线分层，统一超椭圆圆角。
 *
 * 内容默认贴边，需要留白时传 [contentPadding]；`Raised` 的顶部高光由组件内部绘制，不要重复加边框。
 *
 * @param variant 表面档位，决定底色与是否绘制顶部高光
 * @param shape 外轮廓，默认 [OinkRadii.r3] 超椭圆
 * @param contentPadding 内容内边距，默认 0
 *
 * 示例：
 * ```kotlin
 * OinkSurface(
 *     variant = OinkSurfaceVariant.Raised,
 *     contentPadding = PaddingValues(OinkMetrics.Space4),
 * ) {
 *     OinkText("Surface content")
 * }
 * ```
 */
@Composable
fun OinkSurface(
    modifier: Modifier = Modifier,
    variant: OinkSurfaceVariant = OinkSurfaceVariant.Plain,
    shape: Shape = OinkShape(OinkRadii.r3),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    colors: OinkSurfaceColors = OinkSurfaceDefaults.colors(variant),
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(colors.container)
            .drawBehind {
                if (variant == OinkSurfaceVariant.Raised) {
                    drawLine(
                        color = colors.highlight,
                        start = Offset(0f, 0.75.dp.toPx()),
                        end = Offset(size.width, 0.75.dp.toPx()),
                        strokeWidth = 1.5.dp.toPx(),
                    )
                }
            }
            .border(OinkMetrics.Hairline, colors.border, shape)
            .padding(contentPadding),
        content = content,
    )
}

/**
 * Oink 盒子：可选的表面色与圆角 + 内边距，等价于 oink-ui 的 Box。
 *
 * [tone] 为 null 时背景透明且不画边框，只应用圆角裁剪与内边距。
 *
 * @param tone 表面档位，null 表示无底色
 * @param shape 圆角轮廓，默认 [OinkRadii.r2]
 * @param padding 内边距，默认 0
 *
 * 示例：
 * ```kotlin
 * OinkBox(tone = OinkSurfaceVariant.Well, padding = PaddingValues(OinkMetrics.Space3)) {
 *     OinkText("Inset panel")
 * }
 * ```
 */
@Composable
fun OinkBox(
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues(0.dp),
    tone: OinkSurfaceVariant? = null,
    shape: Shape = OinkShape(OinkRadii.r2),
    colors: OinkBoxColors = OinkBoxDefaults.colors(),
    content: @Composable BoxScope.() -> Unit,
) {
    val bg = when (tone) {
        null -> Color.Transparent
        OinkSurfaceVariant.Plain -> colors.surface
        OinkSurfaceVariant.Raised -> colors.card
        OinkSurfaceVariant.Well -> colors.well
    }
    Box(
        modifier = modifier
            .clip(shape)
            .background(bg)
            .then(if (tone != null) Modifier.border(OinkMetrics.Hairline, colors.border, shape) else Modifier)
            .padding(padding),
        content = content,
    )
}

/**
 * Oink 容器：限制最大宽度并水平居中，等价于 oink-ui 的 Container。
 *
 * 需要父级给出宽度；可用宽度小于 [maxWidth] 时占满可用宽度。
 *
 * @param maxWidth 内容最大宽度，默认 960dp
 *
 * 示例：
 * ```kotlin
 * OinkContainer(maxWidth = 720.dp) {
 *     OinkPageHeader(title = "Settings")
 * }
 * ```
 */
@Composable
fun OinkContainer(
    modifier: Modifier = Modifier,
    maxWidth: Dp = OinkPrimitiveDefaults.MaxContentWidth,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
        Box(Modifier.fillMaxWidth().widthIn(max = maxWidth), content = content)
    }
}

/**
 * Oink 居中容器，等价于 oink-ui 的 Center。
 *
 * 只在父级给出的尺寸内居中；配合 `Modifier.fillMaxSize()` 可占满整屏后居中。
 *
 * 示例：
 * ```kotlin
 * OinkCenter(Modifier.fillMaxSize()) {
 *     OinkSpinner()
 * }
 * ```
 */
@Composable
fun OinkCenter(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier, contentAlignment = Alignment.Center, content = content)
}

/**
 * Oink 正方形留白。
 *
 * @param size 宽高，通常取 [OinkMetrics] 的 Space 值
 *
 * 示例：
 * ```kotlin
 * OinkInline {
 *     OinkText("Left")
 *     OinkSpacer(OinkMetrics.Space4)
 *     OinkText("Right")
 * }
 * ```
 */
@Composable
fun OinkSpacer(size: Dp) {
    Spacer(Modifier.size(size))
}

/**
 * Oink 指定宽高留白，等价于 oink-ui 的 Spacer。
 *
 * 宽高分别独立生效，用于非正方形间距。
 *
 * 示例：
 * ```kotlin
 * OinkSpacer(width = OinkMetrics.Space6, height = OinkMetrics.Space2)
 * ```
 */
@Composable
fun OinkSpacer(width: Dp, height: Dp) {
    Spacer(Modifier.width(width).height(height))
}

/**
 * Oink 固定比例容器，等价于 oink-ui 的 AspectRatio。
 *
 * 需要至少一个有界维度；宽高都由父级固定时，比例以宽度为准。
 *
 * @param ratio 宽高比，默认 16:9
 *
 * 示例：
 * ```kotlin
 * OinkAspectRatio(ratio = 4f / 3f) {
 *     OinkImage(modifier = Modifier.fillMaxSize())
 * }
 * ```
 */
@Composable
fun OinkAspectRatio(
    ratio: Float = 16f / 9f,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier.aspectRatio(ratio), content = content)
}

/**
 * Oink 页面骨架：可滚动 + 限宽居中 + 统一内边距，等价于 oink-ui 的 Page。
 *
 * 整页使用 `verticalScroll`，内部不要再嵌套同方向滚动容器；
 * [header] 与 [content] 同在限宽列内，[contentPadding] 同时作用于两者。
 *
 * @param maxWidth 内容最大宽度，默认 960dp
 * @param contentPadding 页面内边距，默认四周 [OinkMetrics.Space6]
 * @param header 页面头部槽，随页面一起滚动
 *
 * 示例：
 * ```kotlin
 * OinkPage(header = { OinkPageHeader(title = "Overview") }) {
 *     OinkSection(title = "Metrics") {
 *         OinkText("1,204 users")
 *     }
 * }
 * ```
 */
@Composable
fun OinkPage(
    modifier: Modifier = Modifier,
    maxWidth: Dp = OinkPrimitiveDefaults.MaxContentWidth,
    contentPadding: PaddingValues = PaddingValues(horizontal = OinkMetrics.Space6, vertical = OinkMetrics.Space6),
    header: @Composable ColumnScope.() -> Unit = {},
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = maxWidth)
                .align(Alignment.CenterHorizontally)
                .padding(contentPadding),
        ) {
            header()
            content()
        }
    }
}

/**
 * Oink 列表：card 表面 + 发丝线分隔的懒加载行列表，等价于 oink-ui 的 List。
 *
 * 每行内容通常用 [OinkRow] 填充；[maxHeight] 给定时列表内部滚动，否则高度随内容增长。
 * 行间自动插入分隔线，最后一行不画。
 *
 * @param count 行数
 * @param maxHeight 列表最大高度，[Dp.Unspecified] 表示不限制
 * @param key 行 key 生成器，用于稳定复用与保持滚动位置
 * @param row 行内容，参数为行索引
 *
 * 示例：
 * ```kotlin
 * OinkList(count = 3, maxHeight = 240.dp) { index ->
 *     OinkRow(title = "Item $index", description = "Description")
 * }
 * ```
 */
@Composable
fun OinkList(
    count: Int,
    modifier: Modifier = Modifier,
    maxHeight: Dp = Dp.Unspecified,
    key: ((Int) -> Any)? = null,
    colors: OinkListColors = OinkListDefaults.colors(),
    row: @Composable (Int) -> Unit,
) {
    val shape = OinkShape(OinkRadii.r3)
    Column(
        modifier = modifier
            .clip(shape)
            .background(colors.container)
            .drawBehind {
                drawLine(
                    color = colors.highlight,
                    start = Offset(0f, 0.75.dp.toPx()),
                    end = Offset(size.width, 0.75.dp.toPx()),
                    strokeWidth = 1.5.dp.toPx(),
                )
            }
            .border(OinkMetrics.Hairline, colors.border, shape)
            .then(if (maxHeight != Dp.Unspecified) Modifier.heightIn(max = maxHeight) else Modifier),
    ) {
        LazyColumn {
            items(count = count, key = key) { index ->
                Column {
                    row(index)
                    if (index < count - 1) OinkRowDivider()
                }
            }
        }
    }
}

/**
 * Oink 懒列表吸顶头部，等价于 oink-ui 的 Sticky。
 *
 * 只能在 `LazyColumn` / [OinkScrollArea] 的 scope 内调用；多个吸顶头按滚动位置依次接管。
 *
 * @param key 吸顶头的 key，用于区分多个吸顶层
 *
 * 示例：
 * ```kotlin
 * LazyColumn {
 *     oinkStickyHeader { OinkStickyHeaderBar(title = "Today") }
 *     items(3) { index -> OinkRow(title = "Item $index") }
 * }
 * ```
 */
@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.oinkStickyHeader(
    key: Any? = null,
    content: @Composable () -> Unit,
) {
    stickyHeader(key = key) { content() }
}

/**
 * Oink 吸顶头部条：card 底 + 小号标题。
 *
 * 只提供外观，不含吸顶逻辑，需配合 [oinkStickyHeader] 使用；标题按原文显示，不自动转大写。
 *
 * @param title 标题文本
 *
 * 示例：
 * ```kotlin
 * LazyColumn {
 *     oinkStickyHeader { OinkStickyHeaderBar(title = "TODAY") }
 *     item { OinkRow(title = "Standup", description = "10:00") }
 * }
 * ```
 */
@Composable
fun OinkStickyHeaderBar(
    title: String,
    modifier: Modifier = Modifier,
    colors: OinkStickyHeaderColors = OinkStickyHeaderDefaults.colors(),
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.container)
            .padding(horizontal = OinkMetrics.Space4, vertical = OinkMetrics.Space2),
    ) {
        OinkText(text = title, color = colors.content, style = OinkTheme.textStyles.label)
    }
}

object OinkSurfaceDefaults {
    /** 不同表面变体取对应底色，仅 Raised 带顶部高光。 */
    @Composable
    fun colors(variant: OinkSurfaceVariant = OinkSurfaceVariant.Plain): OinkSurfaceColors {
        val c = OinkTheme.colors
        val bg = when (variant) {
            OinkSurfaceVariant.Plain -> c.surface
            OinkSurfaceVariant.Raised -> c.card
            OinkSurfaceVariant.Well -> c.well
        }
        return OinkSurfaceColors(container = bg, border = c.line, highlight = c.hi)
    }
}

@Immutable
data class OinkSurfaceColors(
    val container: Color,
    val border: Color,
    val highlight: Color,
)

object OinkBoxDefaults {
    @Composable
    fun colors(): OinkBoxColors {
        val c = OinkTheme.colors
        return OinkBoxColors(surface = c.surface, card = c.card, well = c.well, border = c.line)
    }
}

@Immutable
data class OinkBoxColors(
    val surface: Color,
    val card: Color,
    val well: Color,
    val border: Color,
)

object OinkListDefaults {
    @Composable
    fun colors(): OinkListColors {
        val c = OinkTheme.colors
        return OinkListColors(container = c.card, border = c.line, highlight = c.hi)
    }
}

@Immutable
data class OinkListColors(
    val container: Color,
    val border: Color,
    val highlight: Color,
)

object OinkStickyHeaderDefaults {
    @Composable
    fun colors(): OinkStickyHeaderColors {
        val c = OinkTheme.colors
        return OinkStickyHeaderColors(container = c.card, content = c.text2)
    }
}

@Immutable
data class OinkStickyHeaderColors(
    val container: Color,
    val content: Color,
)

object OinkPrimitiveDefaults {
    val MaxContentWidth = 960.dp
}
