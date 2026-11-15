package zhiqiu.applewidgets.kmp.macos

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.basic.Text
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

/**
 * macOS 偏好设置顶部 Tab（非 iOS 底栏）。
 */
@Composable
fun MacTabView(
    tabs: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (Int) -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        MacTabBar(tabs = tabs, selectedIndex = selectedIndex, onSelect = onSelect)
        Box(Modifier.padding(top = 12.dp)) {
            content(selectedIndex)
        }
    }
}

@Composable
fun MacTabBar(
    tabs: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = AppleWidgetsTheme.colorScheme
    val shape = RoundedCornerShape(6.dp)
    Row(
        modifier = modifier
            .clip(shape)
            .border(0.5.dp, colors.opaqueSeparator, shape)
            .background(colors.tertiaryFill),
    ) {
        tabs.forEachIndexed { index, title ->
            val selected = index == selectedIndex
            Box(
                modifier = Modifier
                    .weight(1f)
                    .then(
                        if (selected) {
                            Modifier.background(if (AppleWidgetsTheme.isDark) colors.fill else colors.systemBackground)
                        } else {
                            Modifier
                        },
                    )
                    .clickable { onSelect(index) }
                    .padding(vertical = 6.dp, horizontal = 8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = title,
                    style = AppleWidgetsTheme.textStyles.callout,
                    color = colors.label,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
