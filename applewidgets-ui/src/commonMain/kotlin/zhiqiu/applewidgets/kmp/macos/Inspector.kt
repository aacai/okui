package zhiqiu.applewidgets.kmp.macos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.basic.Separator
import zhiqiu.applewidgets.kmp.basic.Text
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

/**
 * macOS 右侧 Inspector 面板。
 */
@Composable
fun Inspector(
    title: String = "检查器",
    modifier: Modifier = Modifier,
    width: Dp = 260.dp,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = AppleWidgetsTheme.colorScheme
    Column(
        modifier = modifier
            .width(width)
            .fillMaxHeight()
            .background(colors.secondarySystemBackground),
    ) {
        Text(
            text = title,
            style = AppleWidgetsTheme.textStyles.headline,
            color = colors.label,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
        )
        Separator(startIndent = 0.dp)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(12.dp),
            content = content,
        )
    }
}

@Composable
fun InspectorSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth().padding(bottom = 14.dp)) {
        Text(
            text = title.uppercase(),
            style = AppleWidgetsTheme.textStyles.caption1,
            color = AppleWidgetsTheme.colorScheme.secondaryLabel,
            modifier = Modifier.padding(bottom = 6.dp),
        )
        content()
    }
}
