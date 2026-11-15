package zhiqiu.applewidgets.kmp.basic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

data class DateComponents(
    val year: Int,
    val month: Int,
    val day: Int,
)

fun daysInMonth(year: Int, month: Int): Int = when (month) {
    2 -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
    4, 6, 9, 11 -> 30
    else -> 31
}

@Composable
fun DatePicker(
    value: DateComponents,
    onValueChange: (DateComponents) -> Unit,
    modifier: Modifier = Modifier,
    yearRange: IntRange = 1970..2035,
) {
    val years = remember(yearRange) { yearRange.map { it.toString() } }
    val months = remember { (1..12).map { it.toString().padStart(2, '0') } }
    val maxDay = daysInMonth(value.year, value.month)
    val days = remember(value.year, value.month) {
        (1..daysInMonth(value.year, value.month)).map { it.toString().padStart(2, '0') }
    }
    val safeDay = value.day.coerceIn(1, maxDay)

    Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            NumberPicker(
                values = years,
                selectedIndex = (value.year - yearRange.first).coerceIn(0, years.lastIndex),
                onSelect = { yi ->
                    val year = yearRange.first + yi
                    val day = value.day.coerceAtMost(daysInMonth(year, value.month))
                    onValueChange(value.copy(year = year, day = day))
                },
                modifier = Modifier.weight(1.25f),
            )
            NumberPicker(
                values = months,
                selectedIndex = (value.month - 1).coerceIn(0, 11),
                onSelect = { mi ->
                    val month = mi + 1
                    val day = value.day.coerceAtMost(daysInMonth(value.year, month))
                    onValueChange(value.copy(month = month, day = day))
                },
                modifier = Modifier.weight(1f),
            )
            NumberPicker(
                values = days,
                selectedIndex = (safeDay - 1).coerceIn(0, days.lastIndex),
                onSelect = { di -> onValueChange(value.copy(day = di + 1)) },
                modifier = Modifier.weight(1f),
            )
        }
        Text(
            text = "${value.year}-${value.month.toString().padStart(2, '0')}-${safeDay.toString().padStart(2, '0')}",
            style = AppleWidgetsTheme.textStyles.footnote,
            color = AppleWidgetsTheme.colorScheme.secondaryLabel,
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}

@Composable
fun TimePicker(
    hour: Int,
    minute: Int,
    onValueChange: (hour: Int, minute: Int) -> Unit,
    modifier: Modifier = Modifier,
    is24Hour: Boolean = true,
) {
    val hours = remember(is24Hour) {
        if (is24Hour) (0..23).map { it.toString().padStart(2, '0') }
        else (1..12).map { it.toString().padStart(2, '0') }
    }
    val minutes = remember { (0..59).map { it.toString().padStart(2, '0') } }
    val safeHour = if (is24Hour) hour.coerceIn(0, 23) else hour.coerceIn(1, 12)
    val safeMinute = minute.coerceIn(0, 59)

    Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            NumberPicker(
                values = hours,
                selectedIndex = if (is24Hour) safeHour else (safeHour - 1),
                onSelect = { h ->
                    onValueChange(if (is24Hour) h else h + 1, safeMinute)
                },
                modifier = Modifier.weight(1f),
            )
            Text(
                text = ":",
                style = AppleWidgetsTheme.textStyles.title1,
                color = AppleWidgetsTheme.colorScheme.label,
            )
            NumberPicker(
                values = minutes,
                selectedIndex = safeMinute,
                onSelect = { onValueChange(safeHour, it) },
                modifier = Modifier.weight(1f),
            )
        }
        Text(
            text = "${safeHour.toString().padStart(2, '0')}:${safeMinute.toString().padStart(2, '0')}",
            style = AppleWidgetsTheme.textStyles.footnote,
            color = AppleWidgetsTheme.colorScheme.secondaryLabel,
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}
