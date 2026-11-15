package zhiqiu.applewidgets.kmp.preference

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import zhiqiu.applewidgets.kmp.basic.BasicComponent
import zhiqiu.applewidgets.kmp.basic.BasicComponentColors
import zhiqiu.applewidgets.kmp.basic.BasicComponentDefaults
import zhiqiu.applewidgets.kmp.basic.Checkbox

@Composable
@NonRestartableComposable
fun CheckboxPreference(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    titleColor: BasicComponentColors = BasicComponentDefaults.titleColor(),
    summary: String? = null,
    summaryColor: BasicComponentColors = BasicComponentDefaults.summaryColor(),
    insideMargin: PaddingValues = BasicComponentDefaults.InsideMargin,
    enabled: Boolean = true,
) {
    val current by rememberUpdatedState(onCheckedChange)
    BasicComponent(
        modifier = modifier,
        title = title,
        titleColor = titleColor,
        summary = summary,
        summaryColor = summaryColor,
        endActions = {
            Checkbox(checked = checked, onCheckedChange = current, enabled = enabled)
        },
        insideMargin = insideMargin,
        onClick = { if (enabled) current(!checked) },
        role = Role.Checkbox,
        enabled = enabled,
    )
}
