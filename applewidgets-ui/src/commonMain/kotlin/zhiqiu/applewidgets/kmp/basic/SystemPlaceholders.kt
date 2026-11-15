package zhiqiu.applewidgets.kmp.basic

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.platform.AppleSystemBridge
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

/**
 * 系统桥占位：能力未就绪时展示降级文案，避免调用方硬崩。
 */
@Composable
fun ShareSheetPlaceholder(
    modifier: Modifier = Modifier,
    onUnavailable: (() -> Unit)? = null,
) {
    CapabilityPlaceholder(
        title = "ShareSheet",
        available = AppleSystemBridge.shareSheet,
        modifier = modifier,
        onUnavailable = onUnavailable,
    )
}

@Composable
fun PhotosPickerPlaceholder(
    modifier: Modifier = Modifier,
    onUnavailable: (() -> Unit)? = null,
) {
    CapabilityPlaceholder(
        title = "PhotosPicker",
        available = AppleSystemBridge.photosPicker,
        modifier = modifier,
        onUnavailable = onUnavailable,
    )
}

@Composable
fun MapPlaceholder(
    modifier: Modifier = Modifier,
    onUnavailable: (() -> Unit)? = null,
) {
    CapabilityPlaceholder(
        title = "Map",
        available = AppleSystemBridge.maps,
        modifier = modifier,
        onUnavailable = onUnavailable,
    )
}

@Composable
private fun CapabilityPlaceholder(
    title: String,
    available: Boolean,
    modifier: Modifier = Modifier,
    onUnavailable: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
    ) {
        Text(title, style = AppleWidgetsTheme.textStyles.headline)
        Text(
            text = if (available) "能力已就绪" else "当前平台未接系统桥，已降级跳过",
            style = AppleWidgetsTheme.textStyles.footnote,
            color = AppleWidgetsTheme.colorScheme.secondaryLabel,
            modifier = Modifier.padding(top = 4.dp),
        )
        if (!available) {
            Button(
                text = "知道了",
                onClick = { onUnavailable?.invoke() },
                style = ButtonStyle.Tinted,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
    }
}
