package zhiqiu.applewidgets.kmp.basic

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

/**
 * 搜索栏。聚焦时可在右侧显示取消按钮（对应 iOS UISearchBar）。
 *
 * @param cancelLabel 取消按钮文案；null 表示不显示（文案由调用方给，避免硬编码）。
 */
@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "搜索",
    enabled: Boolean = true,
    colors: SearchBarColors = SearchBarDefaults.colors(),
    shape: Shape? = null,
    showClear: Boolean = true,
    cancelLabel: String? = null,
    onCancel: (() -> Unit)? = null,
    insideMargin: PaddingValues = SearchBarDefaults.InsideMargin,
) {
    val styles = AppleWidgetsTheme.componentStyles
    val focusManager = LocalFocusManager.current
    var focused by remember { mutableStateOf(false) }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            singleLine = true,
            textStyle = AppleWidgetsTheme.textStyles.body.copy(color = colors.contentColor),
            cursorBrush = SolidColor(AppleWidgetsTheme.colorScheme.accent),
            modifier = Modifier
                .weight(1f)
                .height(styles.controlMinHeight)
                .clip(shape ?: styles.controlShape)
                .background(colors.containerColor)
                .padding(insideMargin)
                .onFocusChanged { focused = it.isFocused },
            decorationBox = { inner ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("⌕", color = colors.placeholderColor, style = AppleWidgetsTheme.textStyles.body)
                    Spacer(Modifier.width(6.dp))
                    Box(Modifier.weight(1f)) {
                        if (value.isEmpty()) {
                            Text(placeholder, color = colors.placeholderColor, style = AppleWidgetsTheme.textStyles.body)
                        }
                        inner()
                    }
                    if (showClear && value.isNotEmpty()) {
                        ClearButton(onClick = { onValueChange("") }, tint = colors.placeholderColor)
                    }
                }
            },
        )
        if (cancelLabel != null && focused) {
            Spacer(Modifier.width(8.dp))
            Text(
                text = cancelLabel,
                color = AppleWidgetsTheme.colorScheme.accent,
                style = AppleWidgetsTheme.textStyles.body,
                modifier = Modifier.clickable {
                    onCancel?.invoke()
                    onValueChange("")
                    focusManager.clearFocus()
                },
            )
        }
    }
}

object SearchBarDefaults {
    val InsideMargin = PaddingValues(horizontal = 10.dp, vertical = 6.dp)

    @Composable
    fun colors(
        containerColor: Color = AppleWidgetsTheme.colorScheme.tertiaryFill,
        contentColor: Color = AppleWidgetsTheme.colorScheme.label,
        placeholderColor: Color = AppleWidgetsTheme.colorScheme.placeholderText,
    ): SearchBarColors = SearchBarColors(containerColor, contentColor, placeholderColor)
}

@Immutable
data class SearchBarColors(
    val containerColor: Color,
    val contentColor: Color,
    val placeholderColor: Color,
)
