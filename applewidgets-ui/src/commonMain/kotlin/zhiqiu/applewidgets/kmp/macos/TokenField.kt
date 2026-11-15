package zhiqiu.applewidgets.kmp.macos

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.basic.Text
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TokenField(
    tokens: List<String>,
    onTokensChange: (List<String>) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "添加…",
    enabled: Boolean = true,
) {
    var draft by remember { mutableStateOf("") }
    val colors = AppleWidgetsTheme.colorScheme
    val shape = RoundedCornerShape(6.dp)

    fun commit() {
        val t = draft.trim()
        if (t.isNotEmpty() && t !in tokens) {
            onTokensChange(tokens + t)
        }
        draft = ""
    }

    FlowRow(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 32.dp)
            .clip(shape)
            .background(colors.tertiaryFill)
            .border(0.5.dp, colors.separator, shape)
            .padding(6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        tokens.forEach { token ->
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(colors.secondaryFill)
                    .clickable(enabled = enabled) {
                        onTokensChange(tokens - token)
                    }
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(token, style = AppleWidgetsTheme.textStyles.caption1)
            }
        }
        BasicTextField(
            value = draft,
            onValueChange = { draft = it },
            enabled = enabled,
            singleLine = true,
            textStyle = AppleWidgetsTheme.textStyles.callout.copy(color = colors.label),
            cursorBrush = SolidColor(colors.accent),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { commit() }),
            modifier = Modifier
                .defaultMinSize(minWidth = 72.dp)
                .padding(vertical = 4.dp),
            decorationBox = { inner ->
                Box {
                    if (draft.isEmpty() && tokens.isEmpty()) {
                        Text(placeholder, style = AppleWidgetsTheme.textStyles.callout, color = colors.placeholderText)
                    }
                    inner()
                }
            },
        )
    }
}
