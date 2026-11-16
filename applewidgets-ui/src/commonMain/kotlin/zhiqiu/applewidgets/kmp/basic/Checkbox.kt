package zhiqiu.applewidgets.kmp.basic

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

@Composable
fun Checkbox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val impl = LocalCheckboxImpl.current
    if (impl != null) {
        impl(CheckboxParams(checked, onCheckedChange, modifier, enabled))
        return
    }
    val colors = AppleWidgetsTheme.colorScheme
    val fill by animateColorAsState(
        when {
            checked && enabled -> colors.accent
            checked -> colors.accent.copy(alpha = 0.4f)
            else -> colors.systemBackground
        },
    )
    val borderColor = if (checked) fill else colors.opaqueSeparator

    val toggle = if (onCheckedChange != null) {
        Modifier.toggleable(
            value = checked,
            enabled = enabled,
            role = Role.Checkbox,
            onValueChange = onCheckedChange,
        )
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .then(toggle)
            .size(22.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(fill)
            .border(1.5.dp, borderColor, RoundedCornerShape(6.dp)),
        contentAlignment = Alignment.Center,
    ) {
        if (checked) {
            Text(
                text = "✓",
                style = AppleWidgetsTheme.textStyles.footnote,
                color = colors.onAccent,
            )
        }
    }
}

/** 复选框实现覆盖参数。 */
@Immutable
data class CheckboxParams(
    val checked: Boolean,
    val onCheckedChange: ((Boolean) -> Unit)?,
    val modifier: Modifier,
    val enabled: Boolean,
)

/** 主题可替换的复选框实现；null 表示使用组件库默认实现。 */
val LocalCheckboxImpl = compositionLocalOf<(@Composable (CheckboxParams) -> Unit)?> { null }
