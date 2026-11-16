package zhiqiu.applewidgets.kmp.basic

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.platform.performToggleHaptic
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

@Composable
fun Switch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: SwitchColors = SwitchDefaults.switchColors(),
) {
    val impl = LocalSwitchImpl.current
    if (impl != null) {
        impl(SwitchParams(checked, onCheckedChange, modifier, enabled, colors))
        return
    }
    val currentOnCheckedChange by rememberUpdatedState(onCheckedChange)
    val haptic = LocalHapticFeedback.current
    val trackColor by animateColorAsState(
        targetValue = when {
            checked -> colors.checkedTrackColor(enabled)
            else -> colors.uncheckedTrackColor(enabled)
        },
        animationSpec = spring(stiffness = 500f),
    )
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) 20.dp else 2.dp,
        animationSpec = spring(dampingRatio = 0.75f, stiffness = 600f),
    )
    val thumbColor by animateColorAsState(colors.thumbColor(checked, enabled))

    val toggleModifier = if (currentOnCheckedChange != null) {
        Modifier.toggleable(
            value = checked,
            enabled = enabled,
            role = Role.Switch,
            onValueChange = { value ->
                haptic.performToggleHaptic(value)
                currentOnCheckedChange?.invoke(value)
            },
        )
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .then(toggleModifier)
            .size(width = SwitchDefaults.Width, height = SwitchDefaults.Height)
            .clip(RoundedCornerShape(16.dp))
            .background(trackColor),
        contentAlignment = Alignment.CenterStart,
    ) {
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(SwitchDefaults.ThumbSize)
                .shadow(2.dp, CircleShape)
                .clip(CircleShape)
                .background(thumbColor),
        )
    }
}

object SwitchDefaults {
    val Width = 51.dp
    val Height = 31.dp
    val ThumbSize = 27.dp

    @Composable
    fun switchColors(
        checkedTrackColor: Color = AppleWidgetsTheme.colorScheme.switchTrackOn,
        uncheckedTrackColor: Color = AppleWidgetsTheme.colorScheme.switchTrackOff,
        disabledCheckedTrackColor: Color = AppleWidgetsTheme.colorScheme.switchTrackOn.copy(alpha = 0.4f),
        disabledUncheckedTrackColor: Color = AppleWidgetsTheme.colorScheme.switchTrackOff.copy(alpha = 0.5f),
        checkedThumbColor: Color = Color.White,
        uncheckedThumbColor: Color = Color.White,
        disabledThumbColor: Color = Color.White.copy(alpha = 0.9f),
    ): SwitchColors = SwitchColors(
        checkedTrackColor = checkedTrackColor,
        uncheckedTrackColor = uncheckedTrackColor,
        disabledCheckedTrackColor = disabledCheckedTrackColor,
        disabledUncheckedTrackColor = disabledUncheckedTrackColor,
        checkedThumbColor = checkedThumbColor,
        uncheckedThumbColor = uncheckedThumbColor,
        disabledThumbColor = disabledThumbColor,
    )
}

@Immutable
data class SwitchColors(
    val checkedTrackColor: Color,
    val uncheckedTrackColor: Color,
    val disabledCheckedTrackColor: Color,
    val disabledUncheckedTrackColor: Color,
    val checkedThumbColor: Color,
    val uncheckedThumbColor: Color,
    val disabledThumbColor: Color,
) {
    @Stable
    fun checkedTrackColor(enabled: Boolean): Color =
        if (enabled) checkedTrackColor else disabledCheckedTrackColor

    @Stable
    fun uncheckedTrackColor(enabled: Boolean): Color =
        if (enabled) uncheckedTrackColor else disabledUncheckedTrackColor

    @Stable
    fun thumbColor(checked: Boolean, enabled: Boolean): Color = when {
        !enabled -> disabledThumbColor
        checked -> checkedThumbColor
        else -> uncheckedThumbColor
    }
}

/** 开关实现覆盖参数。 */
@Immutable
data class SwitchParams(
    val checked: Boolean,
    val onCheckedChange: ((Boolean) -> Unit)?,
    val modifier: Modifier,
    val enabled: Boolean,
    val colors: SwitchColors,
)

/** 主题可替换的开关实现；null 表示使用组件库默认实现。 */
val LocalSwitchImpl = compositionLocalOf<(@Composable (SwitchParams) -> Unit)?> { null }
