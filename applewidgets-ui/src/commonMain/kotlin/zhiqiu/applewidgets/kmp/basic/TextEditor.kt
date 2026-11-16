package zhiqiu.applewidgets.kmp.basic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

@Composable
fun TextEditor(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    placeholder: String? = null,
    minHeight: Dp = TextEditorDefaults.MinHeight,
    colors: TextEditorColors = TextEditorDefaults.colors(),
) {
    val textColor = if (enabled) colors.contentColor else colors.disabledContentColor
    Box(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = minHeight)
            .clip(AppleWidgetsTheme.componentStyles.controlShape)
            .background(colors.containerColor)
            .padding(TextEditorDefaults.ContentPadding),
    ) {
        if (value.isEmpty() && placeholder != null) {
            Text(
                text = placeholder,
                style = AppleWidgetsTheme.textStyles.body,
                color = colors.placeholderColor,
            )
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            textStyle = AppleWidgetsTheme.textStyles.body.copy(color = textColor),
            cursorBrush = SolidColor(AppleWidgetsTheme.colorScheme.accent),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

object TextEditorDefaults {
    val MinHeight = 120.dp
    val CornerRadius = 10.dp
    val ContentPadding = androidx.compose.foundation.layout.PaddingValues(12.dp)

    @Composable
    fun colors(
        containerColor: Color = AppleWidgetsTheme.colorScheme.tertiaryFill,
        contentColor: Color = AppleWidgetsTheme.colorScheme.label,
        disabledContentColor: Color = AppleWidgetsTheme.colorScheme.tertiaryLabel,
        placeholderColor: Color = AppleWidgetsTheme.colorScheme.placeholderText,
    ): TextEditorColors = TextEditorColors(
        containerColor, contentColor, disabledContentColor, placeholderColor,
    )
}

@Immutable
data class TextEditorColors(
    val containerColor: Color,
    val contentColor: Color,
    val disabledContentColor: Color,
    val placeholderColor: Color,
)
