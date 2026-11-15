package zhiqiu.applewidgets.kmp.oink.form

import zhiqiu.applewidgets.kmp.oink.basic.*
import zhiqiu.applewidgets.kmp.oink.data.*
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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Oink 多行文本域：与输入框同款凹槽 / 聚焦光环，顶部对齐。
 *
 * 完全受控组件，文本只由外部 [value] 决定，输入时通过 [onValueChange] 回传；
 * 高度不足 [minHeight] 时按最小高度撑开，超过 [maxLines] 行后内部滚动而不继续长高。
 *
 * @param minHeight 最小高度，默认 96dp
 * @param maxLines 最大行数，默认 6
 *
 * 示例：
 * ```kotlin
 * var bio by remember { mutableStateOf("") }
 * OinkField(label = "Bio", description = "Markdown supported") {
 *     OinkTextarea(value = bio, onValueChange = { bio = it }, placeholder = "Tell us about you")
 * }
 * ```
 */
@Composable
fun OinkTextarea(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    placeholder: String? = null,
    minHeight: Dp = OinkTextareaDefaults.MinHeight,
    maxLines: Int = 6,
    colors: OinkTextFieldColors = OinkTextareaDefaults.colors(),
) {
    val shape = OinkShape(OinkTextFieldDefaults.CornerRadius)
    var focused by remember { mutableStateOf(false) }
    val interaction = remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()

    val container by animateColorAsState(
        if (focused) colors.focusedContainer else colors.container,
        tween(OinkMotion.Dur2, easing = OinkMotion.EaseOut),
        label = "textareaBg",
    )
    val borderColor by animateColorAsState(
        when {
            focused -> colors.focusedBorder
            hovered && enabled -> colors.hoverBorder
            else -> colors.border
        },
        tween(OinkMotion.Dur2, easing = OinkMotion.EaseOut),
        label = "textareaBorder",
    )
    val ringAlpha by animateFloatAsState(
        if (focused) 1f else 0f,
        tween(OinkMotion.Dur2, easing = OinkMotion.EaseOut),
        label = "textareaRing",
    )

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .alpha(if (enabled) 1f else 0.5f)
            .onFocusChanged { focused = it.isFocused }
            .hoverable(interaction),
        enabled = enabled,
        textStyle = OinkTheme.textStyles.body.copy(color = colors.text),
        cursorBrush = SolidColor(colors.cursor),
        singleLine = false,
        maxLines = maxLines,
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = minHeight)
                    .clip(shape)
                    .background(container)
                    .border(3.dp, colors.ring.copy(alpha = ringAlpha), shape)
                    .border(OinkMetrics.Hairline, borderColor, shape)
                    .padding(horizontal = 14.dp, vertical = 10.dp),
            ) {
                if (value.isEmpty() && placeholder != null) {
                    OinkText(text = placeholder, color = colors.placeholder, style = OinkTheme.textStyles.body)
                }
                innerTextField()
            }
        },
    )
}

/**
 * Oink 数字输入：可编辑数值 + 右侧上下微调箭头，非法输入失焦回落到当前值。
 *
 * 受控组件：内部只缓存输入文本，仅在解析结果落在 [range] 内时才回调 [onValueChange]；
 * 失焦时若文本非法或越界，会恢复为外部 [value]。箭头按 [step] 增减并夹在 [range] 边界，到界后按钮禁用。
 *
 * @param range 允许的数值区间，默认 0..999
 * @param step 箭头单次增减步长，默认 1
 * @param prefix 前缀文案（如货币符号），不参与输入
 * @param suffix 后缀文案（如单位），不参与输入
 *
 * 示例：
 * ```kotlin
 * var qty by remember { mutableStateOf(1) }
 * OinkNumberInput(value = qty, onValueChange = { qty = it }, range = 1..99, suffix = "pcs")
 * ```
 */
@Composable
fun OinkNumberInput(
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    range: IntRange = 0..999,
    step: Int = 1,
    prefix: String? = null,
    suffix: String? = null,
    colors: OinkNumberInputColors = OinkNumberInputDefaults.colors(),
) {
    val shape = OinkShape(OinkTextFieldDefaults.CornerRadius)
    var text by remember { mutableStateOf(value.toString()) }
    var focused by remember { mutableStateOf(false) }
    val interaction = remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()

    LaunchedEffect(value) {
        if (text.toIntOrNull() != value) text = value.toString()
    }

    val borderColor by animateColorAsState(
        when {
            focused -> colors.focusedBorder
            hovered && enabled -> colors.hoverBorder
            else -> colors.border
        },
        tween(OinkMotion.Dur2, easing = OinkMotion.EaseOut),
        label = "numberBorder",
    )
    val ringAlpha by animateFloatAsState(
        if (focused) 1f else 0f,
        tween(OinkMotion.Dur2, easing = OinkMotion.EaseOut),
        label = "numberRing",
    )

    Row(
        modifier = modifier
            .height(OinkMetrics.HeightMd)
            .alpha(if (enabled) 1f else 0.5f)
            .clip(shape)
            .background(if (focused) colors.focusedContainer else colors.container)
            .border(3.dp, colors.ring.copy(alpha = ringAlpha), shape)
            .border(OinkMetrics.Hairline, borderColor, shape)
            .hoverable(interaction)
            .padding(start = 12.dp, end = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (prefix != null) {
            OinkText(
                text = prefix,
                color = colors.affix,
                style = OinkTheme.textStyles.body,
                modifier = Modifier.padding(end = OinkMetrics.Space1),
            )
        }
        BasicTextField(
            value = text,
            onValueChange = { raw ->
                val digits = raw.filter { it.isDigit() }.take(9)
                text = digits
                val parsed = digits.toIntOrNull()
                if (parsed != null && parsed in range) onValueChange(parsed)
            },
            modifier = Modifier
                .weight(1f)
                .onFocusChanged { state ->
                    val wasFocused = focused
                    focused = state.isFocused
                    if (wasFocused && !state.isFocused) {
                        val parsed = text.toIntOrNull()
                        if (parsed == null || parsed !in range) text = value.toString()
                    }
                },
            enabled = enabled,
            singleLine = true,
            textStyle = OinkTheme.textStyles.body.copy(color = colors.text),
            cursorBrush = SolidColor(colors.cursor),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )
        if (suffix != null) {
            OinkText(
                text = suffix,
                color = colors.affix,
                style = OinkTheme.textStyles.body,
                modifier = Modifier.padding(start = OinkMetrics.Space1),
            )
        }
        Column(
            modifier = Modifier
                .padding(start = OinkMetrics.Space1)
                .width(26.dp)
                .height(34.dp),
        ) {
            NumberStepButton(
                icon = OinkIcons.ChevronUp,
                enabled = enabled && value < range.last,
                colors = colors,
                onClick = {
                    val next = (value + step).coerceAtMost(range.last)
                    text = next.toString()
                    onValueChange(next)
                },
                modifier = Modifier.weight(1f),
            )
            NumberStepButton(
                icon = OinkIcons.ChevronDown,
                enabled = enabled && value > range.first,
                colors = colors,
                onClick = {
                    val next = (value - step).coerceAtLeast(range.first)
                    text = next.toString()
                    onValueChange(next)
                },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun NumberStepButton(
    icon: ImageVector,
    enabled: Boolean,
    colors: OinkNumberInputColors,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interaction = remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(OinkShape(OinkRadii.r1))
            .background(if (hovered && enabled) colors.stepHover else Color.Transparent)
            .hoverable(interaction)
            .clickable(interactionSource = interaction, indication = null, enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        OinkIcon(
            imageVector = icon,
            contentDescription = null,
            tint = if (enabled) colors.stepIcon else colors.stepDisabledIcon,
            size = 12.dp,
        )
    }
}

/**
 * Oink 验证码 / PIN 输入：等宽方格，当前格高亮，隐藏输入框承载键盘与退格。
 *
 * 受控组件：只接受数字并截断到 [length] 位，实际字符由外部 [value] 保存；
 * 点击任意格子都会把焦点交给隐藏输入框，键盘输入 / 退格直接改动 [value]。
 * [onComplete] 仅在输入使长度达到 [length] 时触发，外部直接赋值 [value] 不会触发；
 * [length] 小于等于 0 时直接不渲染。
 *
 * @param length 位数，默认 6
 * @param onComplete 填满后的回调，仅在长度达到 [length] 时触发
 * @param error 错误态：所有格子描边转危险色，文字也转危险色
 *
 * 示例：
 * ```kotlin
 * var code by remember { mutableStateOf("") }
 * OinkField(label = "Verification code", required = true) {
 *     OinkPinInput(length = 6, value = code, onValueChange = { code = it }, onComplete = { verify(it) })
 * }
 * ```
 */
@Composable
fun OinkPinInput(
    length: Int = 6,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onComplete: ((String) -> Unit)? = null,
    error: Boolean = false,
    colors: OinkPinInputColors = OinkPinInputDefaults.colors(),
) {
    if (length <= 0) return
    val focusRequester = remember { FocusRequester() }
    var focused by remember { mutableStateOf(false) }
    val cellInteraction = remember { MutableInteractionSource() }
    val shape = OinkShape(OinkRadii.r2)
    val gap = OinkMetrics.Space2

    BoxWithConstraints(modifier = modifier) {
        val cell = ((maxWidth - gap * (length - 1).toFloat()) / length.toFloat())
            .coerceIn(OinkPinInputDefaults.CellSizeMin, OinkPinInputDefaults.CellSizeMax)
        Row(
            horizontalArrangement = Arrangement.spacedBy(gap),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            repeat(length) { index ->
                val char = value.getOrNull(index)?.toString()
                val isCurrent = focused && index == value.length
                val borderColor = when {
                    error -> colors.errorBorder
                    isCurrent -> colors.currentBorder
                    char != null -> colors.filledBorder
                    else -> colors.border
                }
                Box(
                    modifier = Modifier
                        .size(cell)
                        .clip(shape)
                        .background(if (isCurrent) colors.currentContainer else colors.container)
                        .border(
                            3.dp,
                            if (isCurrent && !error) colors.ring else Color.Transparent,
                            shape,
                        )
                        .border(OinkMetrics.Hairline, borderColor, shape)
                        .clickable(
                            interactionSource = cellInteraction,
                            indication = null,
                            enabled = enabled,
                        ) { focusRequester.requestFocus() },
                    contentAlignment = Alignment.Center,
                ) {
                    if (char != null) {
                        OinkText(
                            text = char,
                            color = if (error) colors.errorText else colors.text,
                            style = OinkTheme.textStyles.rowValue,
                        )
                    }
                }
            }
        }
        BasicTextField(
            value = value,
            onValueChange = { raw ->
                val filtered = raw.filter { it.isDigit() }.take(length)
                if (filtered != value) {
                    onValueChange(filtered)
                    if (filtered.length == length) onComplete?.invoke(filtered)
                }
            },
            modifier = Modifier
                .size(1.dp)
                .alpha(0f)
                .focusRequester(focusRequester)
                .onFocusChanged { focused = it.isFocused },
            enabled = enabled,
            singleLine = true,
            textStyle = OinkTheme.textStyles.body.copy(color = Color.Transparent),
            cursorBrush = SolidColor(Color.Transparent),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        )
    }
}

/**
 * Oink 表单字段：标题 + 必填星号 + 说明 / 错误文案包裹任意输入控件。
 *
 * 纯布局容器，不持有状态、不校验内容：标签在上，[description] 紧随其后，
 * 中间固定 [OinkMetrics.Space2] 间距后放 [content]，[error] 追加在控件下方。
 * [error] 与 [description] 可同时存在，错误文案不覆盖说明文案。
 *
 * @param description 控件上方的辅助说明
 * @param error 控件下方的错误文案，非空即展示
 * @param required 是否在标签后追加红色星号
 *
 * 示例：
 * ```kotlin
 * var email by remember { mutableStateOf("") }
 * OinkField(label = "Email", required = true, error = if (email.contains("@")) null else "Invalid address") {
 *     OinkTextField(value = email, onValueChange = { email = it }, placeholder = "name@example.com")
 * }
 * ```
 */
@Composable
fun OinkField(
    label: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    error: String? = null,
    required: Boolean = false,
    colors: OinkFieldColors = OinkFieldDefaults.colors(),
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            OinkText(text = label, color = colors.label, style = OinkTheme.textStyles.caption)
            if (required) {
                OinkText(
                    text = "*",
                    color = colors.required,
                    style = OinkTheme.textStyles.caption,
                    modifier = Modifier.padding(start = 2.dp),
                )
            }
        }
        if (description != null) {
            OinkText(
                text = description,
                color = colors.description,
                style = OinkTheme.textStyles.rowDesc,
                modifier = Modifier.padding(top = 2.dp),
            )
        }
        Spacer(Modifier.height(OinkMetrics.Space2))
        content()
        if (error != null) {
            OinkText(
                text = error,
                color = colors.error,
                style = OinkTheme.textStyles.caption,
                modifier = Modifier.padding(top = OinkMetrics.Space2),
            )
        }
    }
}

/**
 * Oink 选项卡片：整卡可点，选中强调描边 + 柔和底 + 右上角对勾。
 *
 * 受控组件：选中态完全由 [selected] 决定，点击只回调 [onClick]，不自行切换；
 * 单选场景由外部把 [selected] 指向唯一选项，多选场景由外部维护集合。
 * [trailing] 内容靠右排布，选中时右侧会预留对勾空间。
 *
 * @param description 标题下方的补充说明
 * @param leadingIcon 左侧 32dp 图标块，选中时图标转强调色
 * @param trailing 行尾自定义内容（如开关、价格）
 *
 * 示例：
 * ```kotlin
 * var plan by remember { mutableStateOf("pro") }
 * OinkOptionCard(
 *     title = "Pro", description = "Unlimited projects", selected = plan == "pro",
 *     onClick = { plan = "pro" },
 * )
 * ```
 */
@Composable
fun OinkOptionCard(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    description: String? = null,
    leadingIcon: ImageVector? = null,
    colors: OinkOptionCardColors = OinkOptionCardDefaults.colors(),
    trailing: @Composable RowScope.() -> Unit = {},
) {
    val interaction = remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()
    val shape = OinkShape(OinkRadii.r3)

    val bg by animateColorAsState(
        when {
            selected -> colors.selectedContainer
            hovered && enabled -> colors.hoverContainer
            else -> colors.container
        },
        tween(OinkMotion.Dur2, easing = OinkMotion.EaseOut),
        label = "optionBg",
    )
    val borderColor by animateColorAsState(
        when {
            selected -> colors.selectedBorder
            hovered && enabled -> colors.hoverBorder
            else -> colors.border
        },
        tween(OinkMotion.Dur2, easing = OinkMotion.EaseOut),
        label = "optionBorder",
    )

    Box(
        modifier = modifier
            .alpha(if (enabled) 1f else 0.5f)
            .clip(shape)
            .background(bg)
            .border(OinkMetrics.Hairline, borderColor, shape)
            .hoverable(interaction)
            .clickable(interactionSource = interaction, indication = null, enabled = enabled, onClick = onClick),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
                .padding(end = if (selected) 22.dp else 0.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (leadingIcon != null) {
                Box(
                    modifier = Modifier
                        .padding(end = OinkMetrics.Space3)
                        .size(32.dp)
                        .clip(OinkShape(OinkRadii.r1))
                        .background(colors.iconContainer),
                    contentAlignment = Alignment.Center,
                ) {
                    OinkIcon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        tint = if (selected) colors.selectedIcon else colors.icon,
                        size = 16.dp,
                    )
                }
            }
            Column(Modifier.weight(1f)) {
                OinkText(text = title, color = colors.title, style = OinkTheme.textStyles.label)
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
        if (selected) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(OinkMetrics.Space2)
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(colors.checkContainer),
                contentAlignment = Alignment.Center,
            ) {
                OinkIcon(OinkIcons.Check, contentDescription = null, tint = colors.checkContent, size = 11.dp)
            }
        }
    }
}

/**
 * Oink 组合键：多个键帽之间用 “+” 连接。
 *
 * 纯展示组件，按列表顺序渲染 [OinkKbd]，不处理键盘事件；空列表不渲染任何内容。
 *
 * @param keys 键帽文案列表，顺序即展示顺序
 *
 * 示例：
 * ```kotlin
 * OinkHotkey(keys = listOf("⌘", "K"))
 * ```
 */
@Composable
fun OinkHotkey(
    keys: List<String>,
    modifier: Modifier = Modifier,
    colors: OinkHotkeyColors = OinkHotkeyDefaults.colors(),
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        keys.forEachIndexed { index, key ->
            if (index > 0) {
                OinkText(
                    text = "+",
                    color = colors.separator,
                    style = OinkTheme.textStyles.caption,
                    modifier = Modifier.padding(horizontal = OinkMetrics.Space1),
                )
            }
            OinkKbd(text = key)
        }
    }
}

/**
 * Oink 组合键：按 “+” 拆分字符串后渲染。
 *
 * 便捷重载：拆项会 trim 并丢弃空项，因此 `"⌘ + K"` 这类带空格的写法也能正确渲染；
 * 需要键帽包含 “+” 本身时请改用列表重载。
 *
 * @param keys 以 “+” 分隔的键位串，如 "⌘+Shift+P"
 *
 * 示例：
 * ```kotlin
 * OinkHotkey(keys = "⌘+Shift+P")
 * ```
 */
@Composable
fun OinkHotkey(
    keys: String,
    modifier: Modifier = Modifier,
    colors: OinkHotkeyColors = OinkHotkeyDefaults.colors(),
) {
    OinkHotkey(
        keys = keys.split("+").map { it.trim() }.filter { it.isNotEmpty() },
        modifier = modifier,
        colors = colors,
    )
}

/**
 * Oink 文件拖放区：虚线圆角框 + 上传图标，hover 描边转强调色，仅提供点击入口。
 *
 * 纯视觉占位，不接管系统拖放事件：把系统选择器 / 文件读取逻辑挂在 [onClick] 里即可，
 * 拖拽到此处不会触发任何回调。最小高度 120dp，宽度填满父容器。
 *
 * @param hint 主提示文案，展示在上传图标下方
 *
 * 示例：
 * ```kotlin
 * OinkFileDrop(hint = "拖入图片或点击选择", onClick = { picker.launch(listOf("image/png")) })
 * ```
 */
@Composable
fun OinkFileDrop(
    modifier: Modifier = Modifier,
    hint: String = "Drag files here or click to browse",
    enabled: Boolean = true,
    colors: OinkFileDropColors = OinkFileDropDefaults.colors(),
    onClick: () -> Unit,
) {
    val interaction = remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()
    val shape = OinkShape(OinkRadii.r3)
    val borderColor = if (hovered && enabled) colors.hoverBorder else colors.border

    Column(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = OinkFileDropDefaults.MinHeight)
            .alpha(if (enabled) 1f else 0.5f)
            .clip(shape)
            .background(if (hovered && enabled) colors.hoverContainer else colors.container)
            .drawBehind {
                drawRoundRect(
                    color = borderColor,
                    cornerRadius = CornerRadius(OinkRadii.r3.toPx()),
                    style = Stroke(
                        width = OinkMetrics.Hairline.toPx(),
                        pathEffect = PathEffect.dashPathEffect(
                            floatArrayOf(6.dp.toPx(), 4.dp.toPx()),
                            0f,
                        ),
                    ),
                )
            }
            .hoverable(interaction)
            .clickable(interactionSource = interaction, indication = null, enabled = enabled, onClick = onClick)
            .padding(OinkMetrics.Space5),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(if (hovered && enabled) colors.hoverIconContainer else colors.iconContainer),
            contentAlignment = Alignment.Center,
        ) {
            OinkIcon(
                imageVector = OinkIcons.Upload,
                contentDescription = null,
                tint = if (hovered && enabled) colors.hoverIcon else colors.icon,
                size = 20.dp,
            )
        }
        OinkText(
            text = hint,
            color = colors.hint,
            style = OinkTheme.textStyles.caption,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = OinkMetrics.Space3),
        )
        OinkText(
            text = "Browse",
            color = colors.browse,
            style = OinkTheme.textStyles.caption,
            modifier = Modifier.padding(top = OinkMetrics.Space1),
        )
    }
}

object OinkTextareaDefaults {
    val MinHeight = 96.dp

    @Composable
    fun colors(): OinkTextFieldColors = OinkTextFieldDefaults.colors()
}

object OinkNumberInputDefaults {
    @Composable
    fun colors(): OinkNumberInputColors {
        val c = OinkTheme.colors
        return OinkNumberInputColors(
            container = c.well,
            focusedContainer = c.raised,
            border = c.line,
            hoverBorder = c.line2,
            focusedBorder = c.accent,
            ring = c.accentRing,
            text = c.text,
            affix = c.text3,
            cursor = c.accent,
            stepIcon = c.text2,
            stepDisabledIcon = c.text3,
            stepHover = c.fillHover,
        )
    }
}

@Immutable
data class OinkNumberInputColors(
    val container: Color,
    val focusedContainer: Color,
    val border: Color,
    val hoverBorder: Color,
    val focusedBorder: Color,
    val ring: Color,
    val text: Color,
    val affix: Color,
    val cursor: Color,
    val stepIcon: Color,
    val stepDisabledIcon: Color,
    val stepHover: Color,
)

object OinkPinInputDefaults {
    val CellSizeMin = 44.dp
    val CellSizeMax = 48.dp

    @Composable
    fun colors(): OinkPinInputColors {
        val c = OinkTheme.colors
        return OinkPinInputColors(
            container = c.well,
            currentContainer = c.raised,
            border = c.line,
            filledBorder = c.line2,
            currentBorder = c.accent,
            ring = c.accentRing,
            errorBorder = c.danger,
            text = c.text,
            errorText = c.dangerText,
        )
    }
}

@Immutable
data class OinkPinInputColors(
    val container: Color,
    val currentContainer: Color,
    val border: Color,
    val filledBorder: Color,
    val currentBorder: Color,
    val ring: Color,
    val errorBorder: Color,
    val text: Color,
    val errorText: Color,
)

object OinkFieldDefaults {
    @Composable
    fun colors(): OinkFieldColors {
        val c = OinkTheme.colors
        return OinkFieldColors(
            label = c.text2,
            description = c.text3,
            required = c.dangerText,
            error = c.dangerText,
        )
    }
}

@Immutable
data class OinkFieldColors(
    val label: Color,
    val description: Color,
    val required: Color,
    val error: Color,
)

object OinkOptionCardDefaults {
    @Composable
    fun colors(): OinkOptionCardColors {
        val c = OinkTheme.colors
        return OinkOptionCardColors(
            container = c.card,
            selectedContainer = c.accentTint,
            hoverContainer = c.fillHover,
            border = c.line,
            hoverBorder = c.line2,
            selectedBorder = c.accent,
            iconContainer = c.fill,
            icon = c.text2,
            selectedIcon = c.accent,
            title = c.text,
            description = c.text3,
            checkContainer = c.accent,
            checkContent = c.onAccent,
        )
    }
}

@Immutable
data class OinkOptionCardColors(
    val container: Color,
    val selectedContainer: Color,
    val hoverContainer: Color,
    val border: Color,
    val hoverBorder: Color,
    val selectedBorder: Color,
    val iconContainer: Color,
    val icon: Color,
    val selectedIcon: Color,
    val title: Color,
    val description: Color,
    val checkContainer: Color,
    val checkContent: Color,
)

object OinkHotkeyDefaults {
    @Composable
    fun colors(): OinkHotkeyColors {
        val c = OinkTheme.colors
        return OinkHotkeyColors(separator = c.text3)
    }
}

@Immutable
data class OinkHotkeyColors(
    val separator: Color,
)

object OinkFileDropDefaults {
    val MinHeight = 120.dp

    @Composable
    fun colors(): OinkFileDropColors {
        val c = OinkTheme.colors
        return OinkFileDropColors(
            container = c.well,
            hoverContainer = c.wellHi,
            border = c.line2,
            hoverBorder = c.accent,
            iconContainer = c.fill,
            hoverIconContainer = c.fillHover,
            icon = c.text3,
            hoverIcon = c.accent,
            hint = c.text2,
            browse = c.accentText,
        )
    }
}

@Immutable
data class OinkFileDropColors(
    val container: Color,
    val hoverContainer: Color,
    val border: Color,
    val hoverBorder: Color,
    val iconContainer: Color,
    val hoverIconContainer: Color,
    val icon: Color,
    val hoverIcon: Color,
    val hint: Color,
    val browse: Color,
)
