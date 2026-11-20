package zhiqiu.applewidgets.kmp.overlay

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.basic.Separator
import zhiqiu.applewidgets.kmp.basic.Text
import zhiqiu.applewidgets.kmp.liquid.LiquidGlassScrim
import zhiqiu.applewidgets.kmp.platform.safeNavigationBarsPadding
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

data class ActionSheetItem(
    val title: String,
    val role: AlertActionRole = AlertActionRole.Default,
    val onClick: () -> Unit,
)

@Composable
fun ActionSheet(
    title: String? = null,
    message: String? = null,
    actions: List<ActionSheetItem>,
    cancelTitle: String = "取消",
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = AppleWidgetsTheme.colorScheme
    PlatformDialogOverlay(onDismissRequest = onDismissRequest) {
        LiquidGlassScrim(
            color = colors.windowDimming,
            onClick = onDismissRequest,
            contentAlignment = Alignment.BottomCenter,
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .safeNavigationBarsPadding()
                    .clickable(enabled = false, onClick = {}),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(AppleWidgetsTheme.componentStyles.overlayShape)
                        .background(colors.secondarySystemGroupedBackground),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (title != null || message != null) {
                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            if (title != null) {
                                Text(
                                    text = title,
                                    style = AppleWidgetsTheme.textStyles.footnote,
                                    color = colors.secondaryLabel,
                                    textAlign = TextAlign.Center,
                                )
                            }
                            if (message != null) {
                                Text(
                                    text = message,
                                    style = AppleWidgetsTheme.textStyles.footnote,
                                    color = colors.secondaryLabel,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(top = 2.dp),
                                )
                            }
                        }
                        Separator(startIndent = 0.dp)
                    }
                    actions.forEachIndexed { index, item ->
                        val color = when (item.role) {
                            AlertActionRole.Destructive -> colors.destructive
                            else -> colors.accent
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    item.onClick()
                                    onDismissRequest()
                                }
                                .padding(vertical = 16.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(text = item.title, style = AppleWidgetsTheme.textStyles.title3, color = color)
                        }
                        if (index != actions.lastIndex) {
                            Separator(startIndent = 0.dp)
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(AppleWidgetsTheme.componentStyles.overlayShape)
                        .background(colors.secondarySystemGroupedBackground)
                        .clickable(onClick = onDismissRequest)
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = cancelTitle,
                        style = AppleWidgetsTheme.textStyles.headline,
                        color = colors.accent,
                    )
                }
            }
        }
    }
}
