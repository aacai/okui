package zhiqiu.applewidgets.kmp.basic

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

@Composable
fun RadioButton(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: RadioButtonColors = RadioButtonDefaults.colors(),
) {
    val border by animateColorAsState(
        if (selected) colors.selectedColor(enabled) else colors.unselectedBorderColor(enabled),
    )
    val selectable = if (onClick != null) {
        Modifier.selectable(
            selected = selected,
            enabled = enabled,
            role = Role.RadioButton,
            onClick = onClick,
        )
    } else {
        Modifier
    }
    Box(
        modifier = modifier
            .then(selectable)
            .size(RadioButtonDefaults.Size)
            .clip(CircleShape)
            .border(1.5.dp, border, CircleShape)
            .background(colors.containerColor),
        contentAlignment = Alignment.Center,
    ) {
        if (selected) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(colors.selectedColor(enabled)),
            )
        }
    }
}

object RadioButtonDefaults {
    val Size = 22.dp

    @Composable
    fun colors(
        selectedColor: Color = AppleWidgetsTheme.colorScheme.accent,
        unselectedBorderColor: Color = AppleWidgetsTheme.colorScheme.opaqueSeparator,
        disabledSelectedColor: Color = AppleWidgetsTheme.colorScheme.accent.copy(alpha = 0.4f),
        disabledUnselectedBorderColor: Color = AppleWidgetsTheme.colorScheme.tertiaryLabel,
        containerColor: Color = AppleWidgetsTheme.colorScheme.systemBackground,
    ): RadioButtonColors = RadioButtonColors(
        selectedColor = selectedColor,
        unselectedBorderColor = unselectedBorderColor,
        disabledSelectedColor = disabledSelectedColor,
        disabledUnselectedBorderColor = disabledUnselectedBorderColor,
        containerColor = containerColor,
    )
}

@Immutable
data class RadioButtonColors(
    val selectedColor: Color,
    val unselectedBorderColor: Color,
    val disabledSelectedColor: Color,
    val disabledUnselectedBorderColor: Color,
    val containerColor: Color,
) {
    @Stable
    fun selectedColor(enabled: Boolean): Color =
        if (enabled) selectedColor else disabledSelectedColor

    @Stable
    fun unselectedBorderColor(enabled: Boolean): Color =
        if (enabled) unselectedBorderColor else disabledUnselectedBorderColor
}
