package zhiqiu.applewidgets.kmp.macos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.basic.Text
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

/**
 * macOS 工具栏 / 侧边栏搜索框。
 */
@Composable
fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "搜索",
    enabled: Boolean = true,
) {
    val colors = AppleWidgetsTheme.colorScheme
    val shape = RoundedCornerShape(6.dp)
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        singleLine = true,
        textStyle = AppleWidgetsTheme.textStyles.callout.copy(color = colors.label),
        cursorBrush = SolidColor(colors.accent),
        modifier = modifier
            .defaultMinSize(minHeight = 28.dp)
            .clip(shape)
            .background(colors.tertiaryFill)
            .padding(horizontal = 8.dp, vertical = 5.dp),
        decorationBox = { inner ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("⌕", color = colors.secondaryLabel, style = AppleWidgetsTheme.textStyles.callout)
                Spacer(Modifier.width(6.dp))
                Box(Modifier.weight(1f)) {
                    if (value.isEmpty()) {
                        Text(placeholder, color = colors.placeholderText, style = AppleWidgetsTheme.textStyles.callout)
                    }
                    inner()
                }
            }
        },
    )
}

@Composable
fun ToolbarSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "搜索",
) {
    SearchField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.width(200.dp),
        placeholder = placeholder,
    )
}
