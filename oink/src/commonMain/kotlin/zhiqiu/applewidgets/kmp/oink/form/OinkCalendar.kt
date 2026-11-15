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
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.basic.DateComponents
import zhiqiu.applewidgets.kmp.basic.daysInMonth
import zhiqiu.applewidgets.kmp.overlay.PlatformAnchoredPopup
import kotlin.math.roundToInt

private val OinkMonthNames = listOf(
    "January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December",
)

private val OinkWeekdays = listOf("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa")

/**
 * Oink 月历：7 列网格，选中日使用 accent 实心圆，顶部箭头切月。
 *
 * 不读取系统日期，年月必须由调用方提供。翻月只触发 [onMonthChange]，不会回写 [year] / [month]，
 * 调用方需接收新值并更新外部状态，否则重组后会回到原年月。周起始固定周日，
 * 月份与星期名目前为英文常量。
 *
 * @param month 当前月份，取值 1..12
 * @param selectedDay 选中日，`null` 表示未选中；是否属于本月由调用方自行判断
 * @param onMonthChange 翻月回调，参数为新（年, 月）
 *
 * 示例：
 * ```kotlin
 * var day by remember { mutableStateOf<Int?>(null) }
 * OinkCalendar(
 *     year = 2026,
 *     month = 11,
 *     selectedDay = day,
 *     onSelectDay = { day = it },
 * )
 * ```
 */
@Composable
fun OinkCalendar(
    year: Int,
    month: Int,
    selectedDay: Int?,
    onSelectDay: (Int) -> Unit,
    modifier: Modifier = Modifier,
    onMonthChange: (year: Int, month: Int) -> Unit = { _, _ -> },
    colors: OinkCalendarColors = OinkCalendarDefaults.colors(),
) {
    var viewYear by remember(year, month) { mutableStateOf(year) }
    var viewMonth by remember(year, month) { mutableStateOf(month) }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            OinkIconButton(
                icon = OinkIcons.ChevronLeft,
                onClick = {
                    val newMonth = if (viewMonth == 1) 12 else viewMonth - 1
                    val newYear = if (viewMonth == 1) viewYear - 1 else viewYear
                    viewMonth = newMonth
                    viewYear = newYear
                    onMonthChange(newYear, newMonth)
                },
                contentDescription = "Previous month",
                size = OinkButtonSize.Sm,
            )
            OinkText(
                text = "${OinkMonthNames[viewMonth - 1]} $viewYear",
                modifier = Modifier.weight(1f),
                color = colors.title,
                style = OinkTheme.textStyles.h4,
                textAlign = TextAlign.Center,
            )
            OinkIconButton(
                icon = OinkIcons.ChevronRight,
                onClick = {
                    val newMonth = if (viewMonth == 12) 1 else viewMonth + 1
                    val newYear = if (viewMonth == 12) viewYear + 1 else viewYear
                    viewMonth = newMonth
                    viewYear = newYear
                    onMonthChange(newYear, newMonth)
                },
                contentDescription = "Next month",
                size = OinkButtonSize.Sm,
            )
        }

        Row(modifier = Modifier.fillMaxWidth().padding(top = OinkMetrics.Space2)) {
            OinkWeekdays.forEach { weekday ->
                OinkText(
                    text = weekday,
                    modifier = Modifier.weight(1f),
                    color = colors.weekday,
                    style = OinkTheme.textStyles.micro,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                )
            }
        }

        val leading = firstWeekdayOfMonth(viewYear, viewMonth)
        val dayCount = daysInMonth(viewYear, viewMonth)
        val cellCount = ((leading + dayCount + 6) / 7) * 7

        Column(modifier = Modifier.padding(top = OinkMetrics.Space1)) {
            for (row in 0 until cellCount / 7) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    for (column in 0 until 7) {
                        val day = row * 7 + column - leading + 1
                        Box(
                            modifier = Modifier.weight(1f).padding(1.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            if (day in 1..dayCount) {
                                OinkCalendarDay(
                                    day = day,
                                    selected = day == selectedDay,
                                    colors = colors,
                                    onClick = { onSelectDay(day) },
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
private fun OinkCalendarDay(
    day: Int,
    selected: Boolean,
    colors: OinkCalendarColors,
    onClick: () -> Unit,
) {
    val interaction = remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()
    Box(
        modifier = Modifier.fillMaxWidth().aspectRatio(1f),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(OinkCalendarDefaults.DaySize)
                .clip(CircleShape)
                .background(
                    when {
                        selected -> colors.selectedContainer
                        hovered -> colors.hover
                        else -> Color.Transparent
                    },
                )
                .hoverable(interaction)
                .clickable(interactionSource = interaction, indication = null, onClick = onClick),
            contentAlignment = Alignment.Center,
        ) {
            OinkText(
                text = day.toString(),
                color = if (selected) colors.selectedContent else colors.content,
                style = OinkTheme.textStyles.caption,
                textAlign = TextAlign.Center,
                maxLines = 1,
            )
        }
    }
}

/**
 * Oink 日期选择：凹槽触发器 + 锚定月历弹层，选中后回填并收起。
 *
 * 值类型为 basic 包的 [DateComponents]（year / month / day）。未选中时弹层默认打开 2026 年 1 月，
 * 已选中时打开该日期所在月。弹层走 PlatformAnchoredPopup，按调用点定位，
 * 需放在 Scaffold / OverlayHost 内使用；宽度取触发器实测宽度且不小于 288dp。
 *
 * @param value 当前日期，`null` 表示空值
 * @param onValueChange 选中某天后回调，随后弹层自动收起
 * @param placeholder 空值时的占位文案
 *
 * 示例：
 * ```kotlin
 * var date by remember { mutableStateOf<DateComponents?>(null) }
 * OinkDatePicker(
 *     value = date,
 *     onValueChange = { date = it },
 *     placeholder = "选择日期",
 * )
 * ```
 */
@Composable
fun OinkDatePicker(
    value: DateComponents?,
    onValueChange: (DateComponents) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    placeholder: String = "YYYY-MM-DD",
    colors: OinkDatePickerColors = OinkDatePickerDefaults.colors(),
    calendarColors: OinkCalendarColors = OinkCalendarDefaults.colors(),
) {
    val density = LocalDensity.current
    var expanded by remember { mutableStateOf(false) }
    var triggerWidth by remember { mutableStateOf(0) }
    var viewYear by remember { mutableStateOf(value?.year ?: 2026) }
    var viewMonth by remember { mutableStateOf(value?.month ?: 1) }
    val interaction = remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()
    val shape = OinkShape(OinkRadii.r3)
    val chevronRotation by animateFloatAsState(
        if (expanded) 180f else 0f,
        OinkMotion.spring(),
        label = "dateChevron",
    )

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
                        hovered && enabled -> colors.hoverBorder
                        else -> colors.border
                    },
                    shape,
                )
                .hoverable(interaction)
                .clickable(interactionSource = interaction, indication = null, enabled = enabled) {
                    if (!expanded) {
                        viewYear = value?.year ?: viewYear
                        viewMonth = value?.month ?: viewMonth
                    }
                    expanded = !expanded
                }
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OinkIcon(
                imageVector = OinkIcons.Calendar,
                contentDescription = null,
                tint = if (expanded) colors.focusedIcon else colors.icon,
                size = 18.dp,
                modifier = Modifier.padding(end = 9.dp),
            )
            OinkText(
                text = value?.let { formatOinkDate(it) } ?: placeholder,
                modifier = Modifier.weight(1f),
                color = if (value != null) colors.text else colors.placeholder,
                style = OinkTheme.textStyles.body,
                maxLines = 1,
            )
            OinkIcon(
                imageVector = OinkIcons.ChevronDown,
                contentDescription = null,
                tint = colors.icon,
                size = 16.dp,
                modifier = Modifier.graphicsLayer { rotationZ = chevronRotation },
            )
        }

        if (expanded && triggerWidth > 0) {
            val popupWidth = maxOf(with(density) { triggerWidth.toDp() }, OinkDatePickerDefaults.PopupMinWidth)
            PlatformAnchoredPopup(onDismissRequest = { expanded = false }) {
                Box(
                    modifier = Modifier
                        .offset { IntOffset(0, (OinkMetrics.HeightMd + 6.dp).toPx().roundToInt()) }
                        .width(popupWidth)
                        .clip(OinkShape(OinkRadii.r4))
                        .background(colors.popupContainer)
                        .border(OinkMetrics.Hairline, colors.popupBorder, OinkShape(OinkRadii.r4))
                        .padding(OinkMetrics.Space3),
                ) {
                    OinkCalendar(
                        year = viewYear,
                        month = viewMonth,
                        selectedDay = value
                            ?.takeIf { it.year == viewYear && it.month == viewMonth }
                            ?.day,
                        onSelectDay = { day ->
                            onValueChange(DateComponents(year = viewYear, month = viewMonth, day = day))
                            expanded = false
                        },
                        onMonthChange = { newYear, newMonth ->
                            viewYear = newYear
                            viewMonth = newMonth
                        },
                        colors = calendarColors,
                    )
                }
            }
        }
    }
}

object OinkCalendarDefaults {
    val DaySize = 32.dp

    @Composable
    fun colors(): OinkCalendarColors {
        val c = OinkTheme.colors
        return OinkCalendarColors(
            title = c.text,
            weekday = c.text3,
            content = c.text,
            selectedContainer = c.accent,
            selectedContent = c.onAccent,
            hover = c.fillHover,
        )
    }
}

@Immutable
data class OinkCalendarColors(
    val title: Color,
    val weekday: Color,
    val content: Color,
    val selectedContainer: Color,
    val selectedContent: Color,
    val hover: Color,
)

object OinkDatePickerDefaults {
    val PopupMinWidth = 288.dp

    @Composable
    fun colors(): OinkDatePickerColors {
        val c = OinkTheme.colors
        return OinkDatePickerColors(
            container = c.well,
            expandedContainer = c.raised,
            border = c.line,
            hoverBorder = c.line2,
            expandedBorder = c.accent,
            text = c.text,
            placeholder = c.text3,
            icon = c.text3,
            focusedIcon = c.accent,
            popupContainer = c.pop,
            popupBorder = c.line,
        )
    }
}

@Immutable
data class OinkDatePickerColors(
    val container: Color,
    val expandedContainer: Color,
    val border: Color,
    val hoverBorder: Color,
    val expandedBorder: Color,
    val text: Color,
    val placeholder: Color,
    val icon: Color,
    val focusedIcon: Color,
    val popupContainer: Color,
    val popupBorder: Color,
)

/** Sakamoto 算法：返回当月 1 日的星期，0 = 周日。 */
private fun firstWeekdayOfMonth(year: Int, month: Int): Int {
    val offsets = intArrayOf(0, 3, 2, 5, 0, 3, 5, 1, 4, 6, 2, 4)
    val y = if (month < 3) year - 1 else year
    return (y + y / 4 - y / 100 + y / 400 + offsets[month - 1] + 1) % 7
}

private fun formatOinkDate(value: DateComponents): String =
    "${value.year.toString().padStart(4, '0')}-" +
        "${value.month.toString().padStart(2, '0')}-" +
        value.day.toString().padStart(2, '0')
