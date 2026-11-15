package zhiqiu.applewidgets.kmp.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.basic.Separator
import zhiqiu.applewidgets.kmp.basic.Text
import zhiqiu.applewidgets.kmp.platform.safeNavigationBarsPadding
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

data class TabItem(
    val title: String,
    val icon: String = "●",
)

@Composable
fun TabBar(
    tabs: List<TabItem>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = AppleWidgetsTheme.colorScheme
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.secondarySystemBackground.copy(alpha = 0.94f))
            .safeNavigationBarsPadding(),
    ) {
        Separator(startIndent = 0.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 2.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            tabs.forEachIndexed { index, tab ->
                TabBarItem(
                    tab = tab,
                    selected = index == selectedIndex,
                    onClick = { onSelect(index) },
                )
            }
        }
    }
}

@Composable
private fun RowScope.TabBarItem(
    tab: TabItem,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val colors = AppleWidgetsTheme.colorScheme
    val tint = if (selected) colors.accent else colors.secondaryLabel
    Column(
        modifier = Modifier
            .weight(1f)
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = tab.icon, color = tint, style = AppleWidgetsTheme.textStyles.title3)
        Text(text = tab.title, color = tint, style = AppleWidgetsTheme.textStyles.caption2)
    }
}
