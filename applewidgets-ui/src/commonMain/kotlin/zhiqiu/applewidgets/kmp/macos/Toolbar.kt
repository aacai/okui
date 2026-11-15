package zhiqiu.applewidgets.kmp.macos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.basic.Separator
import zhiqiu.applewidgets.kmp.basic.Text
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

/**
 * macOS 窗口工具栏。
 */
@Composable
fun Toolbar(
    modifier: Modifier = Modifier,
    title: String? = null,
    leading: @Composable RowScope.() -> Unit = {},
    trailing: @Composable RowScope.() -> Unit = {},
) {
    val colors = AppleWidgetsTheme.colorScheme
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.secondarySystemBackground.copy(alpha = 0.92f)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
                content = leading,
            )
            Spacer(Modifier.weight(1f))
            if (title != null) {
                Text(
                    text = title,
                    style = AppleWidgetsTheme.textStyles.headline,
                    color = colors.label,
                )
                Spacer(Modifier.weight(1f))
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
                content = trailing,
            )
        }
        Separator(
            modifier = Modifier.align(Alignment.BottomCenter),
            startIndent = 0.dp,
        )
    }
}

@Composable
fun ToolbarSpacer(modifier: Modifier = Modifier) {
    Spacer(modifier.width(8.dp))
}
