package zhiqiu.applewidgets.kmp.basic

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

/**
 * iOS 风格 Stepper（左右 − / +）。
 * macOS 上下三角见 [zhiqiu.applewidgets.kmp.macos.Stepper]。
 */
@Composable
fun Stepper(
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: IntRange = 0..100,
    enabled: Boolean = true,
    showValue: Boolean = true,
    colors: StepperColors = StepperDefaults.colors(),
) {
    val impl = LocalStepperImpl.current
    if (impl != null) {
        impl(StepperParams(value, onValueChange, modifier, valueRange, enabled, showValue))
        return
    }
    val canDec = enabled && value > valueRange.first
    val canInc = enabled && value < valueRange.last
    val shape = RoundedCornerShape(StepperDefaults.CornerRadius)
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        if (showValue) {
            Text(
                text = value.toString(),
                style = AppleWidgetsTheme.textStyles.body,
                color = if (enabled) colors.contentColor else colors.disabledContentColor,
                textAlign = TextAlign.End,
                modifier = Modifier.width(44.dp).padding(end = 10.dp),
            )
        }
        Row(
            modifier = Modifier
                .clip(shape)
                .background(colors.containerColor)
                .border(0.5.dp, colors.borderColor, shape),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            StepperGlyph(
                text = "−",
                enabled = canDec,
                colors = colors,
                onClick = { onValueChange((value - 1).coerceAtLeast(valueRange.first)) },
            )
            Box(
                Modifier
                    .width(0.5.dp)
                    .height(28.dp)
                    .background(colors.borderColor),
            )
            StepperGlyph(
                text = "+",
                enabled = canInc,
                colors = colors,
                onClick = { onValueChange((value + 1).coerceAtMost(valueRange.last)) },
            )
        }
    }
}

@Composable
private fun StepperGlyph(
    text: String,
    enabled: Boolean,
    colors: StepperColors,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .defaultMinSize(minWidth = 44.dp, minHeight = 32.dp)
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = AppleWidgetsTheme.textStyles.title3,
            color = if (enabled) colors.contentColor else colors.disabledContentColor,
            textAlign = TextAlign.Center,
        )
    }
}

object StepperDefaults {
    val CornerRadius = 9.dp

    @Composable
    fun colors(
        containerColor: Color = AppleWidgetsTheme.colorScheme.tertiaryFill,
        contentColor: Color = AppleWidgetsTheme.colorScheme.label,
        disabledContentColor: Color = AppleWidgetsTheme.colorScheme.tertiaryLabel,
        borderColor: Color = AppleWidgetsTheme.colorScheme.separator,
    ): StepperColors = StepperColors(containerColor, contentColor, disabledContentColor, borderColor)
}

@Immutable
data class StepperColors(
    val containerColor: Color,
    val contentColor: Color,
    val disabledContentColor: Color,
    val borderColor: Color,
)

/** 步进器实现覆盖参数。 */
@Immutable
data class StepperParams(
    val value: Int,
    val onValueChange: (Int) -> Unit,
    val modifier: Modifier,
    val valueRange: IntRange,
    val enabled: Boolean,
    val showValue: Boolean,
)

/** 主题可替换的步进器实现；null 表示使用组件库默认实现。 */
val LocalStepperImpl = compositionLocalOf<(@Composable (StepperParams) -> Unit)?> { null }
