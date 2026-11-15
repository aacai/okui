package zhiqiu.applewidgets.kmp.overlay

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.basic.ProgressView
import zhiqiu.applewidgets.kmp.basic.Text
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

@Composable
fun ProgressDialog(
    message: String = "请稍候…",
    onDismissRequest: (() -> Unit)? = null,
) {
    PlatformDialogOverlay(onDismissRequest = { onDismissRequest?.invoke() }) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .background(AppleWidgetsTheme.colorScheme.secondarySystemGroupedBackground)
                .padding(horizontal = 28.dp, vertical = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ProgressView()
            Spacer(Modifier.height(12.dp))
            Text(
                text = message,
                style = AppleWidgetsTheme.textStyles.footnote,
                color = AppleWidgetsTheme.colorScheme.secondaryLabel,
            )
        }
    }
}
