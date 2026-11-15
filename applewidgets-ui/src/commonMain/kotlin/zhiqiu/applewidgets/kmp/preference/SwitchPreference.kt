package zhiqiu.applewidgets.kmp.preference

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import zhiqiu.applewidgets.kmp.basic.BasicComponent
import zhiqiu.applewidgets.kmp.basic.BasicComponentColors
import zhiqiu.applewidgets.kmp.basic.BasicComponentDefaults
import zhiqiu.applewidgets.kmp.basic.Switch
import zhiqiu.applewidgets.kmp.basic.SwitchColors
import zhiqiu.applewidgets.kmp.basic.SwitchDefaults

/**
 * 设置行 + Switch（委托 [BasicComponent]）。
 */
@Composable
@NonRestartableComposable
fun SwitchPreference(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    titleColor: BasicComponentColors = BasicComponentDefaults.titleColor(),
    summary: String? = null,
    summaryColor: BasicComponentColors = BasicComponentDefaults.summaryColor(),
    startAction: @Composable (() -> Unit)? = null,
    endActions: @Composable (RowScope.() -> Unit)? = null,
    switchColors: SwitchColors = SwitchDefaults.switchColors(),
    insideMargin: PaddingValues = BasicComponentDefaults.InsideMargin,
    enabled: Boolean = true,
) {
    val currentOnCheckedChange by rememberUpdatedState(onCheckedChange)
    BasicComponent(
        modifier = modifier,
        title = title,
        titleColor = titleColor,
        summary = summary,
        summaryColor = summaryColor,
        startAction = startAction,
        endActions = {
            endActions?.invoke(this)
            Switch(
                checked = checked,
                onCheckedChange = currentOnCheckedChange,
                enabled = enabled,
                colors = switchColors,
            )
        },
        insideMargin = insideMargin,
        onClick = { if (enabled) currentOnCheckedChange(!checked) },
        role = Role.Switch,
        enabled = enabled,
    )
}
