package zhiqiu.applewidgets.kmp.overlay

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.basic.Separator
import zhiqiu.applewidgets.kmp.basic.Text
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

data class ListPopupItem(
    val title: String,
    val destructive: Boolean = false,
    val onClick: () -> Unit = {},
)

@Composable
fun ListPopup(
    items: List<ListPopupItem>,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = AppleWidgetsTheme.colorScheme
    PlatformDialogOverlay(onDismissRequest = onDismissRequest) {
        Column(
            modifier = modifier
                .widthIn(min = 220.dp, max = 300.dp)
                .shadow(12.dp, AppleWidgetsTheme.componentStyles.overlayShape)
                .clip(AppleWidgetsTheme.componentStyles.overlayShape)
                .background(colors.secondarySystemGroupedBackground)
                .padding(vertical = 6.dp),
        ) {
            items.forEachIndexed { index, item ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            item.onClick()
                            onDismissRequest()
                        }
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                ) {
                    Text(
                        text = item.title,
                        style = AppleWidgetsTheme.textStyles.body,
                        color = if (item.destructive) colors.destructive else colors.label,
                    )
                }
                if (index != items.lastIndex) {
                    Separator(startIndent = 16.dp)
                }
            }
        }
    }
}
