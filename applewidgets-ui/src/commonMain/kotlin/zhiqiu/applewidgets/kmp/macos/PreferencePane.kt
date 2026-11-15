package zhiqiu.applewidgets.kmp.macos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.basic.Text
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

/**
 * macOS 系统偏好设置风格表单区（标签左对齐控件）。
 */
@Composable
fun PreferencePane(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        content = content,
    )
}

@Composable
fun PreferenceGroup(
    title: String? = null,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        if (title != null) {
            Text(
                text = title,
                style = AppleWidgetsTheme.textStyles.headline,
                color = AppleWidgetsTheme.colorScheme.label,
            )
        }
        content()
    }
}

@Composable
fun PreferenceRow(
    label: String,
    modifier: Modifier = Modifier,
    labelWidth: Dp = 140.dp,
    control: @Composable () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = AppleWidgetsTheme.textStyles.body,
            color = AppleWidgetsTheme.colorScheme.label,
            modifier = Modifier.width(labelWidth),
        )
        Spacer(Modifier.width(12.dp))
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            control()
        }
    }
}

@Composable
fun PreferenceFooter(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = AppleWidgetsTheme.textStyles.footnote,
        color = AppleWidgetsTheme.colorScheme.secondaryLabel,
        modifier = modifier.widthIn(max = 420.dp),
    )
}
