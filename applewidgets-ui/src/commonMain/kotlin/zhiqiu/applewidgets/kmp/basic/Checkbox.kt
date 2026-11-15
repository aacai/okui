package zhiqiu.applewidgets.kmp.basic

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
