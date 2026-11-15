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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.overlay.PlatformAnchoredPopup
import kotlin.math.roundToInt

/**
 * Oink 多选下拉：触发器堆叠已选标签（超出折叠为 +N），浮层内勾选不关闭。
 *
 * 受控组件：[selectedIndices] 必须由调用方保存，勾选 / 取消只回调 [onSelectedChange]，组件不自行改集合；
 * 下标按升序展示，越界下标会退化为下标数字。浮层挂在组件自身 Box 内、
 * 宽度对齐触发器、最高 260dp 内滚动，点击选项不收起，点击外部或再次点触发器才关闭。
 *
 * @param maxTags 触发器最多直接展示的标签数，默认 3；0 表示只显示 +N
 *
 * 示例：
 * ```kotlin
 * var picked by remember { mutableStateOf(setOf(0)) }
 * OinkMultiSelect(
 *     options = listOf("Design", "Engineering", "Marketing"),
 *     selectedIndices = picked, onSelectedChange = { picked = it },
 * )
 * ```
 */
@Composable
fun OinkMultiSelect(
    options: List<String>,
    selectedIndices: Set<Int>,
    onSelectedChange: (Set<Int>) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    placeholder: String = "Select…",
    maxTags: Int = 3,
    colors: OinkMultiSelectColors = OinkMultiSelectDefaults.colors(),
) {
    val density = LocalDensity.current
    var expanded by remember { mutableStateOf(false) }
    var triggerWidth by remember { mutableStateOf(0) }
    val interaction = remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()
    val shape = OinkShape(OinkSelectDefaults.CornerRadius)
    val chevronRotation by animateFloatAsState(
        if (expanded) 180f else 0f,
        OinkMotion.spring(),
        label = "multiChevron",
    )
    val selectedSorted = selectedIndices.sorted()

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(OinkMetrics.HeightMd)
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
                .clickable(interactionSource = interaction, indication = null, enabled = enabled) {
                    expanded = !expanded
                }
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (selectedSorted.isEmpty()) {
                OinkText(
                    text = placeholder,
                    color = colors.placeholder,
                    style = OinkTheme.textStyles.body,
                    modifier = Modifier.weight(1f),
                )
            } else {
                Row(
                    modifier = Modifier.weight(1f).clipToBounds(),
                    horizontalArrangement = Arrangement.spacedBy(OinkMetrics.Space1),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    selectedSorted.take(maxTags.coerceAtLeast(0)).forEach { index ->
                        OinkTag(text = options.getOrElse(index) { index.toString() })
                    }
                    val extra = selectedSorted.size - maxTags.coerceAtLeast(0)
                    if (extra > 0) OinkTag(text = "+$extra")
                }
            }
            OinkIcon(
                imageVector = OinkIcons.ChevronDown,
                contentDescription = null,
                tint = colors.icon,
                modifier = Modifier
                    .padding(start = OinkMetrics.Space1)
                    .size(16.dp)
                    .graphicsLayer { rotationZ = chevronRotation },
            )
        }

        if (expanded && triggerWidth > 0) {
            val enter = remember { Animatable(0f) }
            LaunchedEffect(Unit) { enter.animateTo(1f, OinkMotion.spring()) }
            val gapPx = with(density) { 6.dp.toPx() }
            PlatformAnchoredPopup(onDismissRequest = { expanded = false }) {
                Column(
                    modifier = Modifier
                        .offset { IntOffset(0, (OinkMetrics.HeightMd.toPx() + gapPx).roundToInt()) }
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
                        .padding(6.dp)
                        .heightIn(max = OinkMultiSelectDefaults.MaxPopupHeight)
                        .verticalScroll(rememberScrollState()),
                ) {
                    options.forEachIndexed { index, option ->
                        MultiSelectOptionRow(
                            text = option,
                            checked = index in selectedIndices,
                            enabled = enabled,
                            colors = colors,
                            onToggle = {
                                val next = selectedIndices.toMutableSet()
                                if (!next.add(index)) next.remove(index)
                                onSelectedChange(next)
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MultiSelectOptionRow(
    text: String,
    checked: Boolean,
    enabled: Boolean,
    colors: OinkMultiSelectColors,
    onToggle: () -> Unit,
) {
    val interaction = remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(OinkMetrics.ItemHeight)
            .clip(OinkShape(OinkRadii.r2))
            .background(if (hovered && enabled) colors.optionHover else Color.Transparent)
            .hoverable(interaction)
            .clickable(interactionSource = interaction, indication = null, enabled = enabled, onClick = onToggle)
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OinkCheckbox(checked = checked, onCheckedChange = { onToggle() }, enabled = enabled)
        OinkText(
            text = text,
            color = colors.optionText,
            style = OinkTheme.textStyles.menuItem,
            modifier = Modifier.padding(start = 10.dp).weight(1f),
        )
    }
}

/**
 * Oink 组合框：输入框触发，聚焦或有输入时在锚定浮层展示过滤结果。
 *
 * 受控组件：[value] 由调用方持有；每次输入先回调 [onValueChange] 再打开浮层，
 * 空白输入展示全部选项，否则按 `contains(ignoreCase = true)` 过滤。
 * 选中时先回调 [onSelect] 再写入 [onValueChange] 并收起浮层——两者都会触发，
 * 若调用方在 [onValueChange] 里做搜索联调需自行区分来源。
 * 失焦本身不关闭浮层，收起依赖选中或点击外部；浮层锚定在组件自身 Box 内、宽度对齐输入框。
 *
 * @param emptyText 过滤结果为空时的提示文案
 *
 * 示例：
 * ```kotlin
 * var query by remember { mutableStateOf("") }
 * var picked by remember { mutableStateOf("") }
 * OinkCombobox(
 *     options = listOf("Apple", "Banana", "Cherry"), value = query,
 *     onValueChange = { query = it }, onSelect = { picked = it },
 *     placeholder = "Search fruit",
 * )
 * ```
 */
@Composable
fun OinkCombobox(
    options: List<String>,
    value: String,
    onValueChange: (String) -> Unit,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    placeholder: String = "Search…",
    emptyText: String = "No results",
    textFieldColors: OinkTextFieldColors = OinkTextFieldDefaults.colors(),
    colors: OinkComboboxColors = OinkComboboxDefaults.colors(),
) {
    val density = LocalDensity.current
    var open by remember { mutableStateOf(false) }
    var triggerWidth by remember { mutableStateOf(0) }
    val filtered = remember(options, value) {
        if (value.isBlank()) options else options.filter { it.contains(value, ignoreCase = true) }
    }

    Box(modifier = modifier.onGloballyPositioned { triggerWidth = it.size.width }) {
        OinkTextField(
            value = value,
            onValueChange = {
                onValueChange(it)
                open = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { state -> if (state.hasFocus) open = true },
            enabled = enabled,
            placeholder = placeholder,
            colors = textFieldColors,
        )

        if (open && triggerWidth > 0) {
            val enter = remember { Animatable(0f) }
            LaunchedEffect(Unit) { enter.animateTo(1f, OinkMotion.spring()) }
            val gapPx = with(density) { 6.dp.toPx() }
            PlatformAnchoredPopup(onDismissRequest = { open = false }) {
                Column(
                    modifier = Modifier
                        .offset { IntOffset(0, (OinkMetrics.HeightMd.toPx() + gapPx).roundToInt()) }
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
                        .padding(6.dp)
                        .heightIn(max = OinkComboboxDefaults.MaxPopupHeight)
                        .verticalScroll(rememberScrollState()),
                ) {
                    if (filtered.isEmpty()) {
                        OinkText(
                            text = emptyText,
                            color = colors.emptyText,
                            style = OinkTheme.textStyles.menuItem,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                        )
                    } else {
                        filtered.forEach { option ->
                            ComboboxOptionRow(
                                text = option,
                                selected = option == value,
                                colors = colors,
                                onClick = {
                                    onSelect(option)
                                    onValueChange(option)
                                    open = false
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ComboboxOptionRow(
    text: String,
    selected: Boolean,
    colors: OinkComboboxColors,
    onClick: () -> Unit,
) {
    val interaction = remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(OinkMetrics.ItemHeight)
            .clip(OinkShape(OinkRadii.r2))
            .background(if (hovered) colors.optionHover else Color.Transparent)
            .hoverable(interaction)
            .clickable(interactionSource = interaction, indication = null, onClick = onClick)
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OinkText(
            text = text,
            color = if (selected) colors.optionSelected else colors.optionText,
            style = OinkTheme.textStyles.menuItem,
            modifier = Modifier.weight(1f),
        )
        if (selected) {
            OinkIcon(
                imageVector = OinkIcons.Check,
                contentDescription = null,
                tint = colors.check,
                size = 15.dp,
            )
        }
    }
}

object OinkMultiSelectDefaults {
    val MaxPopupHeight = 260.dp

    @Composable
    fun colors(): OinkMultiSelectColors {
        val c = OinkTheme.colors
        return OinkMultiSelectColors(
            container = c.well,
            expandedContainer = c.raised,
            border = c.line,
            hoverBorder = c.line2,
            expandedBorder = c.accent,
            placeholder = c.text3,
            icon = c.text3,
            popupContainer = c.pop,
            popupBorder = c.line,
            optionText = c.text,
            optionHover = c.fillHover,
        )
    }
}

@Immutable
data class OinkMultiSelectColors(
    val container: Color,
    val expandedContainer: Color,
    val border: Color,
    val hoverBorder: Color,
    val expandedBorder: Color,
    val placeholder: Color,
    val icon: Color,
    val popupContainer: Color,
    val popupBorder: Color,
    val optionText: Color,
    val optionHover: Color,
)

object OinkComboboxDefaults {
    val MaxPopupHeight = 260.dp

    @Composable
    fun colors(): OinkComboboxColors {
        val c = OinkTheme.colors
        return OinkComboboxColors(
            popupContainer = c.pop,
            popupBorder = c.line,
            emptyText = c.text3,
            optionText = c.text,
            optionSelected = c.accentText,
            optionHover = c.fillHover,
            check = c.accent,
        )
    }
}

@Immutable
data class OinkComboboxColors(
    val popupContainer: Color,
    val popupBorder: Color,
    val emptyText: Color,
    val optionText: Color,
    val optionSelected: Color,
    val optionHover: Color,
    val check: Color,
)
