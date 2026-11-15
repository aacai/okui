package zhiqiu.applewidgets.kmp.macos

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.basic.Text
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme
import zhiqiu.applewidgets.kmp.theme.LocalContentColor

enum class PushButtonStyle {
    /** 默认边框按钮 */
    Regular,
    /** 强调（默认按钮，蓝色） */
    Prominent,
    /** 无边框文字按钮 */
    Borderless,
    Destructive,
}

@Composable
fun PushButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    style: PushButtonStyle = PushButtonStyle.Regular,
    content: @Composable RowScope.() -> Unit,
) {
    val colors = AppleWidgetsTheme.colorScheme
    val shape = RoundedCornerShape(6.dp)
    val (bg, border, fg) = when (style) {
        PushButtonStyle.Prominent -> Triple(colors.accent, Color.Transparent, colors.onAccent)
        PushButtonStyle.Destructive -> Triple(colors.destructive, Color.Transparent, Color.White)
        PushButtonStyle.Borderless -> Triple(Color.Transparent, Color.Transparent, colors.accent)
        PushButtonStyle.Regular -> Triple(
            if (AppleWidgetsTheme.isDark) Color(0xFF3A3A3C) else Color(0xFFF6F6F6),
            if (AppleWidgetsTheme.isDark) Color(0xFF545458) else Color(0xFFC6C6C8),
            colors.label,
        )
    }
    val disabledFg = colors.tertiaryLabel
    val disabledBg = colors.fill

    Row(
        modifier = modifier
            .defaultMinSize(minHeight = 28.dp, minWidth = 60.dp)
            .clip(shape)
            .background(if (enabled) bg else disabledBg)
            .then(
                if (border != Color.Transparent && enabled) {
                    Modifier.border(0.5.dp, border, shape)
                } else {
                    Modifier
                },
            )
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick)
            .padding(PaddingValues(horizontal = 12.dp, vertical = 4.dp)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        androidx.compose.runtime.CompositionLocalProvider(
            LocalContentColor provides if (enabled) fg else disabledFg,
        ) {
            content()
        }
    }
}

@Composable
fun PushButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    style: PushButtonStyle = PushButtonStyle.Regular,
) {
    PushButton(onClick = onClick, modifier = modifier, enabled = enabled, style = style) {
        Text(text = text, style = AppleWidgetsTheme.textStyles.callout, color = LocalContentColor.current)
    }
}

@Composable
fun HelpButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = AppleWidgetsTheme.colorScheme
    Row(
        modifier = modifier
            .defaultMinSize(minWidth = 22.dp, minHeight = 22.dp)
            .clip(RoundedCornerShape(50))
            .border(1.dp, colors.secondaryLabel, RoundedCornerShape(50))
            .clickable(onClick = onClick)
            .padding(horizontal = 6.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("?", style = AppleWidgetsTheme.textStyles.caption1, color = colors.secondaryLabel)
    }
}
