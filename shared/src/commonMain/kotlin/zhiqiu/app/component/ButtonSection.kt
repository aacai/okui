package zhiqiu.app.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.basic.Button
import zhiqiu.applewidgets.kmp.basic.ButtonStyle
import zhiqiu.applewidgets.kmp.basic.SmallTitle

fun LazyListScope.buttonSection() {
    item(key = "button") {
        var cancelText by remember { mutableStateOf("Cancel") }
        var submitText by remember { mutableStateOf("Submit") }
        var cancelCount by remember { mutableIntStateOf(0) }
        var submitCount by remember { mutableIntStateOf(0) }

        SmallTitle(text = "Button")
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Button(
                text = cancelText,
                onClick = {
                    cancelCount++
                    cancelText = "Click: $cancelCount"
                },
                modifier = Modifier.weight(1f),
                style = ButtonStyle.Bordered,
            )
            Spacer(Modifier.width(12.dp))
            Button(
                text = submitText,
                onClick = {
                    submitCount++
                    submitText = "Click: $submitCount"
                },
                modifier = Modifier.weight(1f),
                style = ButtonStyle.Filled,
            )
        }
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Button(
                text = "Disabled",
                onClick = {},
                modifier = Modifier.weight(1f),
                enabled = false,
                style = ButtonStyle.Bordered,
            )
            Spacer(Modifier.width(12.dp))
            Button(
                text = "Disabled",
                onClick = {},
                modifier = Modifier.weight(1f),
                enabled = false,
                style = ButtonStyle.Filled,
            )
        }
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Button(
                text = "Tinted",
                onClick = {},
                modifier = Modifier.weight(1f),
                style = ButtonStyle.Tinted,
            )
            Spacer(Modifier.width(12.dp))
            Button(
                text = "Destructive",
                onClick = {},
                modifier = Modifier.weight(1f),
                style = ButtonStyle.Destructive,
            )
        }
    }
}
