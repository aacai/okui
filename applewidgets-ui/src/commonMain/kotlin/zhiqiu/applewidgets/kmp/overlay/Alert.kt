package zhiqiu.applewidgets.kmp.overlay

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.Immutable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.basic.Separator
import zhiqiu.applewidgets.kmp.basic.Text
import zhiqiu.applewidgets.kmp.liquid.LiquidGlassScrim
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

data class AlertAction(
    val title: String,
    val role: AlertActionRole = AlertActionRole.Default,
    val onClick: () -> Unit,
)

enum class AlertActionRole {
    Default,
    Cancel,
    Destructive,
}

@Composable
fun Alert(
    title: String,
    message: String? = null,
    actions: List<AlertAction>,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val impl = LocalAlertImpl.current
    if (impl != null) {
        impl(AlertParams(title, message, actions, onDismissRequest, modifier))
        return
    }
    val colors = AppleWidgetsTheme.colorScheme
    PlatformDialogOverlay(onDismissRequest = onDismissRequest) {
        LiquidGlassScrim(
            color = colors.windowDimming,
            onClick = onDismissRequest,
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = modifier
                    .widthIn(max = 270.dp)
                    .clip(AppleWidgetsTheme.componentStyles.overlayShape)
                    .background(colors.secondarySystemGroupedBackground),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = title,
                        style = AppleWidgetsTheme.textStyles.headline,
                        color = colors.label,
                        textAlign = TextAlign.Center,
                    )
                    if (message != null) {
                        Text(
                            text = message,
                            style = AppleWidgetsTheme.textStyles.footnote,
                            color = colors.label,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 4.dp),
                        )
                    }
                }
                Separator(startIndent = 0.dp)
                actions.forEachIndexed { index, action ->
                    val actionColor = when (action.role) {
                        AlertActionRole.Destructive -> colors.destructive
                        AlertActionRole.Cancel -> colors.accent
                        AlertActionRole.Default -> colors.accent
                    }
                    val style = if (action.role == AlertActionRole.Cancel) {
                        AppleWidgetsTheme.textStyles.headline
                    } else {
                        AppleWidgetsTheme.textStyles.body
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                action.onClick()
                                onDismissRequest()
                            }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(text = action.title, style = style, color = actionColor)
                    }
                    if (index != actions.lastIndex) {
                        Separator(startIndent = 0.dp)
                    }
                }
            }
        }
    }
}

/** 弹窗实现覆盖参数。 */
@Immutable
data class AlertParams(
    val title: String,
    val message: String?,
    val actions: List<AlertAction>,
    val onDismissRequest: () -> Unit,
    val modifier: Modifier,
)

/** 主题可替换的弹窗实现；null 表示使用组件库默认实现。 */
val LocalAlertImpl = compositionLocalOf<(@Composable (AlertParams) -> Unit)?> { null }
