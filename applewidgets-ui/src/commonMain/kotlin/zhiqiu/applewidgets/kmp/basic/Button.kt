package zhiqiu.applewidgets.kmp.basic

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme
import zhiqiu.applewidgets.kmp.theme.LocalContentColor

enum class ButtonStyle {
    Filled,
    Tinted,
    Bordered,
    Plain,
    Destructive,
}

@Composable
fun Button(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    cornerRadius: Dp = ButtonDefaults.CornerRadius,
    minHeight: Dp = ButtonDefaults.MinHeight,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    insideMargin: PaddingValues = ButtonDefaults.InsideMargin,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable RowScope.() -> Unit,
) {
    @Suppress("NAME_SHADOWING")
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    val container = if (enabled) colors.containerColor else colors.disabledContainerColor
    val contentColor = if (enabled) colors.contentColor else colors.disabledContentColor
    val shape = RoundedCornerShape(cornerRadius)

    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Row(
            modifier = modifier
                .defaultMinSize(minHeight = minHeight)
                .clip(shape)
                .background(container)
                .clickable(
                    interactionSource = interactionSource,
                    indication = LocalIndication.current,
                    enabled = enabled,
                    role = Role.Button,
                    onClick = onClick,
                )
                .padding(insideMargin),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            content = content,
        )
    }
}

@Composable
fun Button(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    style: ButtonStyle = ButtonStyle.Filled,
    colors: ButtonColors = ButtonDefaults.colors(style),
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = colors,
    ) {
        Text(
            text = text,
            style = AppleWidgetsTheme.textStyles.headline,
            color = LocalContentColor.current,
        )
    }
}

object ButtonDefaults {
    val MinHeight: Dp = 50.dp
    val CornerRadius: Dp = 12.dp
    val InsideMargin = PaddingValues(horizontal = 20.dp, vertical = 12.dp)

    @Composable
    fun buttonColors(
        containerColor: Color = AppleWidgetsTheme.colorScheme.accent,
        contentColor: Color = AppleWidgetsTheme.colorScheme.onAccent,
        disabledContainerColor: Color = AppleWidgetsTheme.colorScheme.fill,
        disabledContentColor: Color = AppleWidgetsTheme.colorScheme.tertiaryLabel,
    ): ButtonColors = ButtonColors(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColor,
    )

    @Composable
    fun colors(style: ButtonStyle): ButtonColors {
        val c = AppleWidgetsTheme.colorScheme
        return when (style) {
            ButtonStyle.Filled -> buttonColors()
            ButtonStyle.Tinted -> buttonColors(
                containerColor = c.accent.copy(alpha = 0.15f),
                contentColor = c.accent,
            )
            ButtonStyle.Bordered -> buttonColors(
                containerColor = Color.Transparent,
                contentColor = c.accent,
                disabledContainerColor = Color.Transparent,
            )
            ButtonStyle.Plain -> buttonColors(
                containerColor = Color.Transparent,
                contentColor = c.accent,
                disabledContainerColor = Color.Transparent,
            )
            ButtonStyle.Destructive -> buttonColors(
                containerColor = c.destructive,
                contentColor = Color.White,
            )
        }
    }
}

@Immutable
data class ButtonColors(
    val containerColor: Color,
    val contentColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color,
)
