package zhiqiu.applewidgets.kmp.basic

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.semantics.Role
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
    showClear: Boolean = false,
    textStyle: TextStyle = AppleWidgetsTheme.textStyles.body,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    val impl = LocalTextFieldImpl.current
    if (impl != null) {
        impl(
            TextFieldParams(
                value = value,
                onValueChange = onValueChange,
                modifier = modifier,
                enabled = enabled,
                readOnly = readOnly,
                placeholder = placeholder,
                singleLine = singleLine,
                style = style,
                showClear = showClear,
                textStyle = textStyle,
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                visualTransformation = visualTransformation,
            ),
        )
        return
    }
    val colors = AppleWidgetsTheme.colorScheme
    val mergedStyle = textStyle.copy(color = if (enabled) colors.label else colors.tertiaryLabel)
    val containerModifier = when (style) {
        TextFieldStyle.Plain -> Modifier
        TextFieldStyle.Rounded -> Modifier
            .clip(AppleWidgetsTheme.componentStyles.controlShape)
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                    if (value.isEmpty() && placeholder != null) {
                        Text(
                            text = placeholder,
                            style = textStyle,
                            color = colors.placeholderText,
                        )
                    }
                    inner()
                }
                if (showClear && value.isNotEmpty()) {
                    ClearButton(onClick = { onValueChange("") }, tint = colors.placeholderText)
                }
            }
        },
    )
}

/** 清除按钮（对应 iOS UITextField 的 clearButtonMode）。TextField 与 SearchBar 共用。 */
@Composable
internal fun ClearButton(
    onClick: () -> Unit,
    tint: Color,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = modifier
            .size(22.dp)
            .clip(CircleShape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                role = Role.Button,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(Modifier.size(10.dp)) {
            val stroke = 1.6.dp.toPx()
            drawLine(tint, Offset(0f, 0f), Offset(size.width, size.height), strokeWidth = stroke, cap = StrokeCap.Round)
            drawLine(tint, Offset(size.width, 0f), Offset(0f, size.height), strokeWidth = stroke, cap = StrokeCap.Round)
        }
    }
}

object TextFieldDefaults {
    val ContentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
}

/** 输入框实现覆盖参数。 */
@Immutable
data class TextFieldParams(
    val value: String,
    val onValueChange: (String) -> Unit,
    val modifier: Modifier,
    val enabled: Boolean,
    val readOnly: Boolean,
    val placeholder: String?,
    val singleLine: Boolean,
    val style: TextFieldStyle,
    val showClear: Boolean,
    val textStyle: TextStyle,
    val keyboardOptions: KeyboardOptions,
    val keyboardActions: KeyboardActions,
    val visualTransformation: VisualTransformation,
)

/** 主题可替换的输入框实现；null 表示使用组件库默认实现。 */
val LocalTextFieldImpl = compositionLocalOf<(@Composable (TextFieldParams) -> Unit)?> { null }
