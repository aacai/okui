package zhiqiu.applewidgets.kmp.basic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

enum class TextFieldStyle {
    Plain,
    Rounded,
}

@Composable
fun TextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    placeholder: String? = null,
    singleLine: Boolean = true,
    style: TextFieldStyle = TextFieldStyle.Rounded,
    textStyle: TextStyle = AppleWidgetsTheme.textStyles.body,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    val colors = AppleWidgetsTheme.colorScheme
    val mergedStyle = textStyle.copy(color = if (enabled) colors.label else colors.tertiaryLabel)
    val containerModifier = when (style) {
        TextFieldStyle.Plain -> Modifier
        TextFieldStyle.Rounded -> Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(colors.tertiaryFill)
            .padding(TextFieldDefaults.ContentPadding)
    }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 36.dp)
            .then(containerModifier),
        enabled = enabled,
        readOnly = readOnly,
        textStyle = mergedStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        visualTransformation = visualTransformation,
        cursorBrush = SolidColor(colors.accent),
        decorationBox = { inner ->
            Box(contentAlignment = Alignment.CenterStart) {
                if (value.isEmpty() && placeholder != null) {
                    Text(
                        text = placeholder,
                        style = textStyle,
                        color = colors.placeholderText,
                    )
                }
                inner()
            }
        },
    )
}

object TextFieldDefaults {
    val ContentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
}
