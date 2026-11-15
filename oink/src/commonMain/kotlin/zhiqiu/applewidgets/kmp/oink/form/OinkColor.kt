package zhiqiu.applewidgets.kmp.oink.form

import zhiqiu.applewidgets.kmp.oink.basic.*
import zhiqiu.applewidgets.kmp.oink.data.*
import zhiqiu.applewidgets.kmp.oink.layout.*
import zhiqiu.applewidgets.kmp.oink.nav.*
import zhiqiu.applewidgets.kmp.oink.overlay.*
import zhiqiu.applewidgets.kmp.oink.primitives.*
import zhiqiu.applewidgets.kmp.oink.responsive.*
import zhiqiu.applewidgets.kmp.oink.theme.*

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * Oink 色板：圆形色块，选中显示外圈光环与自适应对勾，hover 轻微放大。
 *
 * 受控组件：选中项只由 [selectedIndex] 决定，点击回调下标后需外部更新；
 * 传入 `-1` 或越界值即无选中项。对勾颜色按色块亮度自动在深 / 浅色间切换，深色块上用白勾。
 *
 * @param selectedIndex 选中下标，非 0..lastIndex 时视为未选中
 * @param size 单个色块直径，默认 22dp
 *
 * 示例：
 * ```kotlin
 * val palette = listOf(Color(0xFF334155), Color(0xFF2563EB), Color(0xFFE11D48))
 * var picked by remember { mutableStateOf(0) }
 * OinkColorSwatches(colors = palette, selectedIndex = picked, onSelectedChange = { picked = it })
 * ```
 */
@Composable
fun OinkColorSwatches(
    colors: List<Color>,
    selectedIndex: Int,
    onSelectedChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = OinkColorSwatchesDefaults.Size,
    swatchColors: OinkColorSwatchesColors = OinkColorSwatchesDefaults.colors(),
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(OinkMetrics.Space2),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        colors.forEachIndexed { index, color ->
            val interaction = remember { MutableInteractionSource() }
            val hovered by interaction.collectIsHoveredAsState()
            val scale by animateFloatAsState(
                if (hovered) 1.12f else 1f,
                OinkMotion.spring(),
                label = "swatchScale",
            )
            val selected = index == selectedIndex
            Box(
                modifier = Modifier
                    .graphicsLayer { scaleX = scale; scaleY = scale }
                    .size(size)
                    .clip(CircleShape)
                    .background(if (selected) swatchColors.ring else Color.Transparent)
                    .hoverable(interaction)
                    .clickable(interactionSource = interaction, indication = null) {
                        onSelectedChange(index)
                    }
                    .padding(2.dp),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(color),
                    contentAlignment = Alignment.Center,
                ) {
                    if (selected) {
                        OinkIcon(
                            imageVector = OinkIcons.Check,
                            contentDescription = null,
                            tint = if (color.luminance() > 0.5f) swatchColors.checkOnLight else swatchColors.checkOnDark,
                            size = (size.value * 0.55f).dp,
                        )
                    }
                }
            }
        }
    }
}

/**
 * Oink 取色器：饱和度 / 明度面板 + 色相滑条 + 色值预览与十六进制文本。
 *
 * 受控组件：内部维护 HSV 三个分量用于绘制手柄，拖动时持续回调 [onColorChange]；
 * 外部 [color] 变化且与内部 HSV 不一致时（如重置、联动其他控件）会同步回填手柄位置。
 * [enabled] 为 false 时整体降透明度且面板 / 滑条不响应点击与拖拽。
 * 输出为不透明 sRGB，不含 alpha 编辑能力。
 *
 * @param color 当前颜色，需由调用方持有
 *
 * 示例：
 * ```kotlin
 * var accent by remember { mutableStateOf(Color(0xFF2563EB)) }
 * OinkField(label = "Accent color") {
 *     OinkColorPicker(color = accent, onColorChange = { accent = it })
 * }
 * ```
 */
@Composable
fun OinkColorPicker(
    color: Color,
    onColorChange: (Color) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: OinkColorPickerColors = OinkColorPickerDefaults.colors(),
) {
    val initial = remember { oinkRgbToHsv(color) }
    var hue by remember { mutableStateOf(initial[0]) }
    var saturation by remember { mutableStateOf(initial[1]) }
    var brightness by remember { mutableStateOf(initial[2]) }
    var panelSize by remember { mutableStateOf(IntSize.Zero) }
    var sliderWidth by remember { mutableStateOf(0) }

    LaunchedEffect(color) {
        if (oinkHsvToColor(hue, saturation, brightness) != color) {
            val hsv = oinkRgbToHsv(color)
            hue = hsv[0]
            saturation = hsv[1]
            brightness = hsv[2]
        }
    }

    val updateSv: (Float, Float) -> Unit = { x, y ->
        if (panelSize.width > 0 && panelSize.height > 0) {
            val s = (x / panelSize.width).coerceIn(0f, 1f)
            val v = (1f - y / panelSize.height).coerceIn(0f, 1f)
            saturation = s
            brightness = v
            onColorChange(oinkHsvToColor(hue, s, v))
        }
    }
    val updateHue: (Float) -> Unit = { x ->
        if (sliderWidth > 0) {
            val h = (x / sliderWidth).coerceIn(0f, 1f) * 360f
            hue = h
            onColorChange(oinkHsvToColor(h, saturation, brightness))
        }
    }

    Column(modifier = modifier.alpha(if (enabled) 1f else 0.5f)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clip(OinkShape(OinkRadii.r2))
                .onSizeChanged { panelSize = it }
                .pointerInput(enabled) {
                    if (!enabled) return@pointerInput
                    detectTapGestures { pos -> updateSv(pos.x, pos.y) }
                }
                .pointerInput(enabled) {
                    if (!enabled) return@pointerInput
                    detectDragGestures(
                        onDragStart = { pos -> updateSv(pos.x, pos.y) },
                        onDrag = { change, _ ->
                            change.consume()
                            updateSv(change.position.x, change.position.y)
                        },
                    )
                },
        ) {
            Canvas(Modifier.fillMaxSize()) {
                drawRect(oinkHsvToColor(hue, 1f, 1f))
                drawRect(Brush.horizontalGradient(listOf(Color.White, Color.Transparent)))
                drawRect(Brush.verticalGradient(listOf(Color.Transparent, Color.Black)))
                val cx = (saturation * size.width).coerceIn(0f, size.width)
                val cy = ((1f - brightness) * size.height).coerceIn(0f, size.height)
                drawCircle(
                    color = Color.White,
                    radius = 7.dp.toPx(),
                    center = Offset(cx, cy),
                    style = Stroke(width = 2.dp.toPx()),
                )
                drawCircle(
                    color = Color.Black.copy(alpha = 0.3f),
                    radius = 8.5.dp.toPx(),
                    center = Offset(cx, cy),
                    style = Stroke(width = 1.dp.toPx()),
                )
            }
        }

        Spacer(Modifier.height(OinkMetrics.Space3))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .onSizeChanged { sliderWidth = it.width }
                .pointerInput(enabled) {
                    if (!enabled) return@pointerInput
                    detectTapGestures { pos -> updateHue(pos.x) }
                }
                .pointerInput(enabled) {
                    if (!enabled) return@pointerInput
                    detectDragGestures(
                        onDragStart = { pos -> updateHue(pos.x) },
                        onDrag = { change, _ ->
                            change.consume()
                            updateHue(change.position.x)
                        },
                    )
                },
        ) {
            Canvas(Modifier.fillMaxSize()) {
                val barHeight = 12.dp.toPx()
                val top = (size.height - barHeight) / 2f
                drawRoundRect(
                    brush = Brush.horizontalGradient(
                        listOf(
                            Color.Red,
                            Color.Yellow,
                            Color.Green,
                            Color.Cyan,
                            Color.Blue,
                            Color.Magenta,
                            Color.Red,
                        ),
                    ),
                    topLeft = Offset(0f, top),
                    size = Size(size.width, barHeight),
                    cornerRadius = CornerRadius(barHeight / 2f),
                )
                val x = (hue / 360f * size.width).coerceIn(0f, size.width)
                drawCircle(
                    color = Color.White,
                    radius = 8.dp.toPx(),
                    center = Offset(x, size.height / 2f),
                )
                drawCircle(
                    color = oinkHsvToColor(hue, 1f, 1f),
                    radius = 5.5.dp.toPx(),
                    center = Offset(x, size.height / 2f),
                )
            }
        }

        Row(
            modifier = Modifier.padding(top = OinkMetrics.Space3),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(color)
                    .border(OinkMetrics.Hairline, colors.border, CircleShape),
            )
            OinkText(
                text = oinkHex(color),
                color = colors.valueText,
                style = OinkTheme.textStyles.mono,
                modifier = Modifier.padding(start = OinkMetrics.Space2),
            )
        }
    }
}

object OinkColorSwatchesDefaults {
    val Size = 22.dp

    @Composable
    fun colors(): OinkColorSwatchesColors {
        val c = OinkTheme.colors
        return OinkColorSwatchesColors(
            ring = c.accentRing,
            checkOnLight = c.inverse,
            checkOnDark = c.onAccent,
        )
    }
}

@Immutable
data class OinkColorSwatchesColors(
    val ring: Color,
    val checkOnLight: Color,
    val checkOnDark: Color,
)

object OinkColorPickerDefaults {
    @Composable
    fun colors(): OinkColorPickerColors {
        val c = OinkTheme.colors
        return OinkColorPickerColors(border = c.line, valueText = c.text2)
    }
}

@Immutable
data class OinkColorPickerColors(
    val border: Color,
    val valueText: Color,
)

/** 读取 RGB 并转换为 [hue, saturation, value]。 */
private fun oinkRgbToHsv(color: Color): FloatArray {
    val r = color.red
    val g = color.green
    val b = color.blue
    val max = maxOf(r, g, b)
    val min = minOf(r, g, b)
    val delta = max - min
    var h = when {
        delta == 0f -> 0f
        max == r -> ((g - b) / delta) % 6f
        max == g -> (b - r) / delta + 2f
        else -> (r - g) / delta + 4f
    } * 60f
    if (h < 0f) h += 360f
    val s = if (max == 0f) 0f else delta / max
    return floatArrayOf(h, s, max)
}

/** HSV 转 Color（sRGB、不透明）。 */
private fun oinkHsvToColor(hue: Float, saturation: Float, value: Float): Color {
    val chroma = value * saturation
    val h = ((hue % 360f) + 360f) % 360f / 60f
    val x = chroma * (1f - abs(h % 2f - 1f))
    val (r, g, b) = when (h.toInt() % 6) {
        0 -> Triple(chroma, x, 0f)
        1 -> Triple(x, chroma, 0f)
        2 -> Triple(0f, chroma, x)
        3 -> Triple(0f, x, chroma)
        4 -> Triple(x, 0f, chroma)
        else -> Triple(chroma, 0f, x)
    }
    val m = value - chroma
    return Color(
        red = (r + m).coerceIn(0f, 1f),
        green = (g + m).coerceIn(0f, 1f),
        blue = (b + m).coerceIn(0f, 1f),
    )
}

/** 格式化 `#RRGGBB`。 */
private fun oinkHex(color: Color): String {
    fun channel(value: Float): String =
        (value * 255f).roundToInt().coerceIn(0, 255).toString(16).padStart(2, '0').uppercase()
    return "#" + channel(color.red) + channel(color.green) + channel(color.blue)
}
