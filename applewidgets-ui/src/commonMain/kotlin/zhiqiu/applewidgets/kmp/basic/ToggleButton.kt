package zhiqiu.applewidgets.kmp.basic

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.theme.LocalContentColor

@Composable
fun ToggleButton(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        onClick = { onCheckedChange(!checked) },
        modifier = modifier,
        enabled = enabled,
        colors = if (checked) {
            ButtonDefaults.buttonColors()
        } else {
            ButtonDefaults.colors(ButtonStyle.Tinted)
        },
        insideMargin = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
        content = content,
    )
}

@Composable
fun ToggleButton(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    ToggleButton(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
    ) {
        Text(text = text, color = LocalContentColor.current)
    }
}
