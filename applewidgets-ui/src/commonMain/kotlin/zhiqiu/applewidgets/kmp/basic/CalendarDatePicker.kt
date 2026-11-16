package zhiqiu.applewidgets.kmp.basic

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.Immutable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

private val WeekdayLabels = listOf("日", "一", "二", "三", "四", "五", "六")

@Composable
fun CalendarDatePicker(
    value: DateComponents,
    onValueChange: (DateComponents) -> Unit,
    modifier: Modifier = Modifier,
) {
    val impl = LocalCalendarDatePickerImpl.current
    if (impl != null) {
        impl(CalendarDatePickerParams(value, onValueChange, modifier))
        return
    }
    val firstWeekday = remember(value.year, value.month) {
        // 简化：蔡勒公式变体，返回 0=周日
        val y = if (value.month < 3) value.year - 1 else value.year
        val m = if (value.month < 3) value.month + 12 else value.month
        val k = y % 100
        val j = y / 100
        val h = (1 + 13 * (m + 1) / 5 + k + k / 4 + j / 4 + 5 * j) % 7
        // h: 0=Sat ... convert to 0=Sun
        (h + 6) % 7
    }
    val days = remember(value.year, value.month) { daysInMonth(value.year, value.month) }
    val cells = remember(firstWeekday, days) {
        buildList {
            repeat(firstWeekday) { add(null) }
            for (d in 1..days) add(d)
            while (size % 7 != 0) add(null)
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = {
                val (y, m) = if (value.month == 1) value.year - 1 to 12 else value.year to value.month - 1
                val day = value.day.coerceAtMost(daysInMonth(y, m))
                onValueChange(DateComponents(y, m, day))
            }) { Text("‹", style = AppleWidgetsTheme.textStyles.title2) }
            Text(
                text = "${value.year}年${value.month}月",
                style = AppleWidgetsTheme.textStyles.headline,
            )
            IconButton(onClick = {
                val (y, m) = if (value.month == 12) value.year + 1 to 1 else value.year to value.month + 1
                val day = value.day.coerceAtMost(daysInMonth(y, m))
                onValueChange(DateComponents(y, m, day))
            }) { Text("›", style = AppleWidgetsTheme.textStyles.title2) }
        }
        Row(Modifier.fillMaxWidth()) {
            WeekdayLabels.forEach { w ->
                Text(
                    text = w,
                    style = AppleWidgetsTheme.textStyles.caption1,
                    color = AppleWidgetsTheme.colorScheme.secondaryLabel,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f),
                )
            }
        }
        cells.chunked(7).forEach { week ->
            Row(Modifier.fillMaxWidth()) {
                week.forEach { day ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(2.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (day != null) {
                            val selected = day == value.day
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .then(
                                        if (selected) {
                                            Modifier.background(AppleWidgetsTheme.colorScheme.accent)
                                        } else {
                                            Modifier
                                        },
                                    )
                                    .clickable {
                                        onValueChange(value.copy(day = day))
                                    }
                                    .padding(8.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = day.toString(),
                                    style = AppleWidgetsTheme.textStyles.callout,
                                    color = if (selected) {
                                        AppleWidgetsTheme.colorScheme.onAccent
                                    } else {
                                        AppleWidgetsTheme.colorScheme.label
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/** 日历选择实现覆盖参数。 */
@Immutable
data class CalendarDatePickerParams(
    val value: DateComponents,
    val onValueChange: (DateComponents) -> Unit,
    val modifier: Modifier,
)

/** 主题可替换的日历选择实现；null 表示使用组件库默认实现。 */
val LocalCalendarDatePickerImpl = compositionLocalOf<(@Composable (CalendarDatePickerParams) -> Unit)?> { null }
