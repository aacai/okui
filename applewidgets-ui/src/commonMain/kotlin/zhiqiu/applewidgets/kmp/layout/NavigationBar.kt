package zhiqiu.applewidgets.kmp.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.basic.Separator
import zhiqiu.applewidgets.kmp.basic.Text
import zhiqiu.applewidgets.kmp.platform.safeStatusBarsPadding
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

@Composable
fun NavigationBar(
    title: String,
    modifier: Modifier = Modifier,
    largeTitle: Boolean = false,
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null,
) {
    val colors = AppleWidgetsTheme.colorScheme
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.secondarySystemBackground.copy(alpha = 0.94f))
            .safeStatusBarsPadding(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .padding(horizontal = 8.dp),
        ) {
            Row(
                modifier = Modifier.align(Alignment.CenterStart),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                navigationIcon?.invoke()
            }
            if (!largeTitle) {
                Text(
                    text = title,
                    style = AppleWidgetsTheme.textStyles.headline,
                    color = colors.label,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center).padding(horizontal = 72.dp),
                )
            }
            Row(
                modifier = Modifier.align(Alignment.CenterEnd),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                actions?.invoke(this)
            }
        }
        if (largeTitle) {
            Text(
                text = title,
                style = AppleWidgetsTheme.textStyles.largeTitle,
                color = colors.label,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
            )
            Spacer(Modifier.height(4.dp))
        }
        Separator(startIndent = 0.dp)
    }
}
