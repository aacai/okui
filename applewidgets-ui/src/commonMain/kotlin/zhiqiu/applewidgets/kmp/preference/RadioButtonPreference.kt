package zhiqiu.applewidgets.kmp.preference

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import zhiqiu.applewidgets.kmp.basic.BasicComponent
import zhiqiu.applewidgets.kmp.basic.BasicComponentColors
import zhiqiu.applewidgets.kmp.basic.BasicComponentDefaults
import zhiqiu.applewidgets.kmp.basic.RadioButton

@Composable
@NonRestartableComposable
fun RadioButtonPreference(
    selected: Boolean,
    onClick: () -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    titleColor: BasicComponentColors = BasicComponentDefaults.titleColor(),
    summary: String? = null,
    summaryColor: BasicComponentColors = BasicComponentDefaults.summaryColor(),
    insideMargin: PaddingValues = BasicComponentDefaults.InsideMargin,
    enabled: Boolean = true,
) {
    BasicComponent(
        modifier = modifier,
        title = title,
        titleColor = titleColor,
        summary = summary,
        summaryColor = summaryColor,
        endActions = {
            RadioButton(selected = selected, onClick = onClick, enabled = enabled)
        },
        insideMargin = insideMargin,
        onClick = onClick,
        role = Role.RadioButton,
        enabled = enabled,
    )
}
