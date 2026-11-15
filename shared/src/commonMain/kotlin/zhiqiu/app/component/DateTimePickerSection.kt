package zhiqiu.app.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.basic.Card
import zhiqiu.applewidgets.kmp.basic.DateComponents
import zhiqiu.applewidgets.kmp.basic.DatePicker
import zhiqiu.applewidgets.kmp.basic.SmallTitle
import zhiqiu.applewidgets.kmp.basic.Text
import zhiqiu.applewidgets.kmp.basic.TimePicker
import zhiqiu.applewidgets.kmp.overlay.Sheet
import zhiqiu.applewidgets.kmp.overlay.SheetDetent
import zhiqiu.applewidgets.kmp.preference.ArrowPreference
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

fun LazyListScope.dateTimePickerSection() {
    item(key = "dateTimePicker") {
        var showSheet by remember { mutableStateOf(false) }
        var date by remember { mutableStateOf(DateComponents(2026, 11, 13)) }
        var hour by remember { mutableIntStateOf(18) }
        var minute by remember { mutableIntStateOf(30) }

        SmallTitle(text = "DatePicker / TimePicker")
        Card(modifier = Modifier.demoCard()) {
            ArrowPreference(
                title = "Open in Sheet",
                summary = "完整拖滚轮（避免外层列表抢手势）",
                value = buildString {
                    append(date.year.toString().padStart(4, '0'))
                    append('-')
                    append(date.month.toString().padStart(2, '0'))
                    append('-')
                    append(date.day.toString().padStart(2, '0'))
                    append(' ')
                    append(hour.toString().padStart(2, '0'))
                    append(':')
                    append(minute.toString().padStart(2, '0'))
                },
                onClick = { showSheet = true },
            )
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    "DatePicker",
                    style = AppleWidgetsTheme.textStyles.footnote,
                    color = AppleWidgetsTheme.colorScheme.secondaryLabel,
                )
                DatePicker(value = date, onValueChange = { date = it })
                Text(
                    "TimePicker",
                    style = AppleWidgetsTheme.textStyles.footnote,
                    color = AppleWidgetsTheme.colorScheme.secondaryLabel,
                )
                TimePicker(
                    hour = hour,
                    minute = minute,
                    onValueChange = { h, m ->
                        hour = h
                        minute = m
                    },
                )
            }
        }

        if (showSheet) {
            Sheet(
                onDismissRequest = { showSheet = false },
                detent = SheetDetent.Large,
            ) {
                Column(
                    Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Text("Date / TimePicker", style = AppleWidgetsTheme.textStyles.title2)
                    DatePicker(value = date, onValueChange = { date = it })
                    TimePicker(
                        hour = hour,
                        minute = minute,
                        onValueChange = { h, m ->
                            hour = h
                            minute = m
                        },
                    )
                }
            }
        }
    }
}
