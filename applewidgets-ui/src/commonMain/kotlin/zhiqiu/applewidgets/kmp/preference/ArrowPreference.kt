package zhiqiu.applewidgets.kmp.preference

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import zhiqiu.applewidgets.kmp.basic.BasicComponent
import zhiqiu.applewidgets.kmp.basic.BasicComponentColors
import zhiqiu.applewidgets.kmp.basic.BasicComponentDefaults
import zhiqiu.applewidgets.kmp.basic.Text
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

/**
 * 带箭头的导航设置行（委托 [BasicComponent]）。
 */
@Composable
@NonRestartableComposable
fun ArrowPreference(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    titleColor: BasicComponentColors = BasicComponentDefaults.titleColor(),
    summary: String? = null,
    summaryColor: BasicComponentColors = BasicComponentDefaults.summaryColor(),
    value: String? = null,
    startAction: @Composable (() -> Unit)? = null,
    endActions: @Composable (RowScope.() -> Unit)? = null,
    insideMargin: PaddingValues = BasicComponentDefaults.InsideMargin,
    enabled: Boolean = true,
) {
    val tertiary = AppleWidgetsTheme.colorScheme.tertiaryLabel
    val secondary = AppleWidgetsTheme.colorScheme.secondaryLabel
    BasicComponent(
        modifier = modifier,
        title = title,
        titleColor = titleColor,
        summary = summary,
        summaryColor = summaryColor,
        startAction = startAction,
        endActions = {
            endActions?.invoke(this)
            if (value != null) {
                Text(
                    text = value,
                    style = AppleWidgetsTheme.textStyles.body,
                    color = if (enabled) secondary else tertiary,
                )
            }
            Text(
                text = "›",
                style = AppleWidgetsTheme.textStyles.title2,
                color = tertiary,
            )
        },
        insideMargin = insideMargin,
        onClick = onClick,
        role = Role.Button,
        enabled = enabled,
    )
}
