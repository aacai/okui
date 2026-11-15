package zhiqiu.app.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.basic.Card
import zhiqiu.applewidgets.kmp.basic.SmallTitle
import zhiqiu.applewidgets.kmp.basic.Text
import zhiqiu.applewidgets.kmp.overlay.ActionSheet
import zhiqiu.applewidgets.kmp.overlay.ActionSheetItem
import zhiqiu.applewidgets.kmp.overlay.AlertActionRole
import zhiqiu.applewidgets.kmp.overlay.Sheet
import zhiqiu.applewidgets.kmp.preference.ArrowPreference
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

fun LazyListScope.bottomSheetSection() {
    item(key = "bottomSheet") {
        var showSheet by remember { mutableStateOf(false) }
        var showActionSheet by remember { mutableStateOf(false) }

        SmallTitle(text = "BottomSheet")
        Card(modifier = Modifier.demoCard()) {
            ArrowPreference(
                title = "Sheet",
                summary = "Click to show a Sheet",
                onClick = { showSheet = true },
            )
            ArrowPreference(
                title = "ActionSheet",
                summary = "Click to show an ActionSheet",
                onClick = { showActionSheet = true },
            )
        }

        if (showSheet) {
            Sheet(onDismissRequest = { showSheet = false }) {
                Column(Modifier.padding(16.dp)) {
                    Text("Sheet", style = AppleWidgetsTheme.textStyles.title2)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "浮层示意",
                        color = AppleWidgetsTheme.colorScheme.secondaryLabel,
                    )
                }
            }
        }
        if (showActionSheet) {
            ActionSheet(
                title = "分享方式",
                actions = listOf(
                    ActionSheetItem("拷贝链接") {},
                    ActionSheetItem("删除", AlertActionRole.Destructive) {},
                ),
                onDismissRequest = { showActionSheet = false },
            )
        }
    }
}
