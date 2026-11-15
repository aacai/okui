package zhiqiu.applewidgets.kmp.overlay

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import zhiqiu.applewidgets.kmp.platform.AppleCapabilities
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

/**
 * 跨端浮层：优先系统 Dialog；若平台声明无 window overlay，则退化为全屏遮罩 Box。
 */
@Composable
fun PlatformDialogOverlay(
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit,
) {
    if (AppleCapabilities.windowOverlay) {
        Dialog(
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = false,
            ),
        ) {
            content()
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppleWidgetsTheme.colorScheme.windowDimming)
                .clickable(onClick = onDismissRequest),
            contentAlignment = Alignment.Center,
        ) {
            Box(Modifier.clickable(enabled = false, onClick = {})) {
                content()
            }
        }
    }
}

/**
 * 锚定弹出：有 Popup 就用；否则退化为居中 Dialog 内容。
 */
@Composable
fun PlatformAnchoredPopup(
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit,
) {
    if (AppleCapabilities.windowOverlay) {
        Popup(
            onDismissRequest = onDismissRequest,
            properties = PopupProperties(focusable = true),
        ) {
            content()
        }
    } else {
        PlatformDialogOverlay(onDismissRequest = onDismissRequest, content = content)
    }
}
