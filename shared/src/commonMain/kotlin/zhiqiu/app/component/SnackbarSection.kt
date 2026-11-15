package zhiqiu.app.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.basic.Card
import zhiqiu.applewidgets.kmp.basic.SmallTitle
import zhiqiu.applewidgets.kmp.basic.Snackbar
import zhiqiu.applewidgets.kmp.preference.ArrowPreference

fun LazyListScope.snackbarSection(
    onShowToast: (String) -> Unit,
) {
    item(key = "snackbar") {
        SmallTitle(text = "Snackbar")
        Card(modifier = Modifier.demoCard()) {
            Snackbar(
                message = "已复制到剪贴板",
                actionLabel = "撤销",
                onAction = { onShowToast("已撤销") },
                modifier = Modifier.padding(12.dp),
            )
            ArrowPreference(
                title = "Toast",
                summary = "Click to show a Toast",
                onClick = { onShowToast("已保存") },
            )
        }
    }
}
