package zhiqiu.applewidgets.kmp.basic

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.layout.TabItem
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

@Composable
fun NavigationRail(
    tabs: List<TabItem>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = AppleWidgetsTheme.colorScheme
    Column(
        modifier = modifier
            .width(72.dp)
            .fillMaxHeight()
            .background(colors.secondarySystemBackground)
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        tabs.forEachIndexed { index, tab ->
            val selected = index == selectedIndex
            val tint = if (selected) colors.accent else colors.secondaryLabel
            Column(
                modifier = Modifier
                    .clickable { onSelect(index) }
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(tab.icon, color = tint, style = AppleWidgetsTheme.textStyles.title3)
                Spacer(Modifier.width(2.dp))
                Text(tab.title, color = tint, style = AppleWidgetsTheme.textStyles.caption2)
            }
        }
    }
}
