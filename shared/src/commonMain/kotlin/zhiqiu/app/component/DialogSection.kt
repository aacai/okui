package zhiqiu.app.component

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import zhiqiu.applewidgets.kmp.basic.Card
import zhiqiu.applewidgets.kmp.basic.SmallTitle
import zhiqiu.applewidgets.kmp.overlay.Alert
import zhiqiu.applewidgets.kmp.overlay.AlertAction
import zhiqiu.applewidgets.kmp.overlay.AlertActionRole
import zhiqiu.applewidgets.kmp.overlay.ProgressDialog
import zhiqiu.applewidgets.kmp.preference.ArrowPreference

fun LazyListScope.dialogSection() {
    item(key = "dialog") {
        var showAlert by remember { mutableStateOf(false) }
        var showProgress by remember { mutableStateOf(false) }

        SmallTitle(text = "Dialog")
        Card(modifier = Modifier.demoCard()) {
            ArrowPreference(
                title = "Alert",
                summary = "Click to show an Alert",
                onClick = { showAlert = true },
            )
            ArrowPreference(
                title = "ProgressDialog",
                summary = "Click to show a ProgressDialog",
                onClick = { showProgress = true },
            )
        }

        if (showAlert) {
            Alert(
                title = "确认删除？",
                message = "此操作无法撤销。",
                actions = listOf(
                    AlertAction("删除", AlertActionRole.Destructive) {},
                    AlertAction("取消", AlertActionRole.Cancel) {},
                ),
                onDismissRequest = { showAlert = false },
            )
        }
        if (showProgress) {
            LaunchedEffect(Unit) {
                delay(1500)
                showProgress = false
            }
            ProgressDialog(
                message = "正在加载…",
                onDismissRequest = { showProgress = false },
            )
        }
    }
}
