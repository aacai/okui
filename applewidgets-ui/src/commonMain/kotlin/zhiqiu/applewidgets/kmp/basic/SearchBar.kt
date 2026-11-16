package zhiqiu.applewidgets.kmp.basic

import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

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
    insideMargin: PaddingValues = SearchBarDefaults.InsideMargin,
) {
    val styles = AppleWidgetsTheme.componentStyles
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        singleLine = true,
        textStyle = AppleWidgetsTheme.textStyles.body.copy(color = colors.contentColor),
        cursorBrush = SolidColor(AppleWidgetsTheme.colorScheme.accent),
        modifier = modifier
            .fillMaxWidth()
            .height(styles.controlMinHeight)
            .clip(shape ?: styles.controlShape)
            .background(colors.containerColor)
            .padding(insideMargin),
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
