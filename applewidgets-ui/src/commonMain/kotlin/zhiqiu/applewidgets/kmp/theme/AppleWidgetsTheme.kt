package zhiqiu.applewidgets.kmp.theme

import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.invalidateDraw
import kotlinx.coroutines.launch

val LocalContentColor = compositionLocalOf { Color.Black }

@Composable
fun AppleWidgetsTheme(
    controller: ThemeController,
    textStyles: TextStyles = defaultTextStyles(),
    content: @Composable () -> Unit,
) {
    val rawColors = controller.currentColors()
    val colors = remember { rawColors.copy() }.apply { updateColorsFrom(rawColors) }
    val styles = remember { textStyles.copy() }.apply { updateTextStylesFrom(textStyles) }
    val indication = remember(colors.label) { ApplePressIndication(colors.label) }
    CompositionLocalProvider(
        LocalColors provides colors,
        LocalTextStyles provides styles,
        LocalContentColor provides colors.label,
        LocalColorSchemeMode provides controller.colorSchemeMode,
        LocalIndication provides indication,
    ) {
        content()
    }
}

@Composable
fun AppleWidgetsTheme(
    colors: Colors = AppleWidgetsTheme.colorScheme,
    textStyles: TextStyles = AppleWidgetsTheme.textStyles,
    content: @Composable () -> Unit,
) {
    val resolvedColors = remember { colors.copy() }.apply { updateColorsFrom(colors) }
    val styles = remember { textStyles.copy() }.apply { updateTextStylesFrom(textStyles) }
    val indication = remember(resolvedColors.label) { ApplePressIndication(resolvedColors.label) }
    CompositionLocalProvider(
        LocalColors provides resolvedColors,
        LocalTextStyles provides styles,
        LocalContentColor provides resolvedColors.label,
        LocalIndication provides indication,
    ) {
        content()
    }
}

object AppleWidgetsTheme {
    val colorScheme: Colors
        @Composable @ReadOnlyComposable
        get() = LocalColors.current

    val textStyles: TextStyles
        @Composable @ReadOnlyComposable
        get() = LocalTextStyles.current

    val colorSchemeMode: ColorSchemeMode?
        @Composable @ReadOnlyComposable
        get() = LocalColorSchemeMode.current

    val isDark: Boolean
        @Composable @ReadOnlyComposable
        get() = colorScheme.systemBackground.luminance() < 0.5f
}

internal class ApplePressIndication(
    private val color: Color,
) : IndicationNodeFactory {
    private val pressedAlpha = if (color.luminance() > 0.5f) 0.08f else 0.16f

    override fun create(interactionSource: InteractionSource): DelegatableNode =
        ApplePressIndicationNode(interactionSource, color, pressedAlpha)

    override fun equals(other: Any?): Boolean =
        other is ApplePressIndication && other.color == color

    override fun hashCode(): Int = color.hashCode()
}

private class ApplePressIndicationNode(
    private val interactionSource: InteractionSource,
    private val color: Color,
    private val pressedAlpha: Float,
) : Modifier.Node(), DrawModifierNode {
    private var isPressed = false

    override fun onAttach() {
        coroutineScope.launch {
            interactionSource.interactions.collect { interaction ->
                when (interaction) {
                    is PressInteraction.Press -> {
                        isPressed = true
                        invalidateDraw()
                    }
                    is PressInteraction.Release,
                    is PressInteraction.Cancel,
                    -> {
                        isPressed = false
                        invalidateDraw()
                    }
                }
            }
        }
    }

    override fun ContentDrawScope.draw() {
        drawContent()
        if (isPressed) {
            drawRect(color.copy(alpha = pressedAlpha))
        }
    }
}
