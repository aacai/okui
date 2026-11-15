package zhiqiu.applewidgets.kmp.basic

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

@Composable
fun BreadcrumbBar(
    items: List<String>,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        items.forEachIndexed { index, title ->
            val isLast = index == items.lastIndex
            Text(
                text = title,
                style = AppleWidgetsTheme.textStyles.footnote,
                color = if (isLast) {
                    AppleWidgetsTheme.colorScheme.label
                } else {
                    AppleWidgetsTheme.colorScheme.accent
                },
                modifier = Modifier.clickable(enabled = !isLast) { onSelect(index) },
            )
            if (!isLast) {
                Text(
                    text = "›",
                    style = AppleWidgetsTheme.textStyles.footnote,
                    color = AppleWidgetsTheme.colorScheme.tertiaryLabel,
                )
            }
        }
    }
}
