package zhiqiu.applewidgets.kmp.preference

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.basic.BasicComponent
import zhiqiu.applewidgets.kmp.basic.BasicComponentColors
import zhiqiu.applewidgets.kmp.basic.BasicComponentDefaults
import zhiqiu.applewidgets.kmp.basic.Slider
import zhiqiu.applewidgets.kmp.basic.Text
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

@Composable
@NonRestartableComposable
fun SliderPreference(
    value: Float,
    onValueChange: (Float) -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    titleColor: BasicComponentColors = BasicComponentDefaults.titleColor(),
    summary: String? = null,
    summaryColor: BasicComponentColors = BasicComponentDefaults.summaryColor(),
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    showValue: Boolean = true,
    insideMargin: PaddingValues = BasicComponentDefaults.InsideMargin,
    enabled: Boolean = true,
) {
    BasicComponent(
        modifier = modifier,
        title = title,
        titleColor = titleColor,
        summary = summary,
        summaryColor = summaryColor,
        endActions = if (showValue) {
            {
                Text(
                    text = "${(value * 100).toInt()}%",
                    style = AppleWidgetsTheme.textStyles.footnote,
                    color = AppleWidgetsTheme.colorScheme.secondaryLabel,
                )
            }
        } else {
            null
        },
        bottomAction = {
            Slider(
                value = value,
                onValueChange = onValueChange,
                valueRange = valueRange,
                enabled = enabled,
                modifier = Modifier.padding(top = 4.dp),
            )
        },
        insideMargin = insideMargin,
        enabled = enabled,
    )
}
