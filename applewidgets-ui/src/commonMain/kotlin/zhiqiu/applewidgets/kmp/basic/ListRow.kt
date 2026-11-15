package zhiqiu.applewidgets.kmp.basic

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.preference.ArrowPreference
import zhiqiu.applewidgets.kmp.preference.SwitchPreference

/**
 * 兼容别名：新代码请优先使用 [BasicComponent] / Preference。
 */
@Deprecated("使用 BasicComponent", ReplaceWith("BasicComponent(title=title, summary=subtitle, startAction=leading, endActions=trailing, onClick=onClick, enabled=enabled, modifier=modifier)"))
@Composable
@NonRestartableComposable
fun ListRow(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable RowScope.() -> Unit)? = null,
    showChevron: Boolean = false,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null,
    contentPadding: PaddingValues = BasicComponentDefaults.InsideMargin,
) {
    if (showChevron) {
        ArrowPreference(
            title = title,
            onClick = onClick ?: {},
            modifier = modifier,
            summary = subtitle,
            startAction = leading,
            endActions = trailing,
            insideMargin = contentPadding,
            enabled = enabled,
        )
    } else {
        BasicComponent(
            modifier = modifier,
            title = title,
            summary = subtitle,
            startAction = leading,
            endActions = trailing,
            insideMargin = contentPadding,
            onClick = onClick,
            enabled = enabled,
        )
    }
}

@Deprecated("使用 SwitchPreference", ReplaceWith("SwitchPreference(checked,onCheckedChange,title,modifier,summary=subtitle,enabled=enabled)", "zhiqiu.applewidgets.kmp.preference.SwitchPreference"))
@Composable
@NonRestartableComposable
fun SwitchListRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    enabled: Boolean = true,
) {
    SwitchPreference(
        checked = checked,
        onCheckedChange = onCheckedChange,
        title = title,
        modifier = modifier,
        summary = subtitle,
        enabled = enabled,
    )
}

@Deprecated("使用 ArrowPreference", ReplaceWith("ArrowPreference(title,onClick,modifier,summary=subtitle,value=value,startAction=leading,enabled=enabled)", "zhiqiu.applewidgets.kmp.preference.ArrowPreference"))
@Composable
@NonRestartableComposable
fun NavigationListRow(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    value: String? = null,
    leading: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    ArrowPreference(
        title = title,
        onClick = onClick,
        modifier = modifier,
        summary = subtitle,
        value = value,
        startAction = leading,
        enabled = enabled,
    )
}

object ListRowDefaults {
    val MinHeight = BasicComponentDefaults.MinHeight
    val ContentPadding = BasicComponentDefaults.InsideMargin
}
