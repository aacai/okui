package zhiqiu.app.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.basic.BasicComponent
import zhiqiu.applewidgets.kmp.basic.Card
import zhiqiu.applewidgets.kmp.basic.SmallTitle
import zhiqiu.applewidgets.kmp.basic.Text
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

fun LazyListScope.basicComponentSection() {
    item(key = "basicComponent") {
        SmallTitle(text = "Basic Component")
        Card(modifier = Modifier.demoCard()) {
            BasicComponent(
                title = "Title",
                summary = "Summary",
                startAction = {
                    Text(text = "Start")
                },
                endActions = {
                    Text(
                        text = "End1",
                        style = AppleWidgetsTheme.textStyles.footnote,
                        color = AppleWidgetsTheme.colorScheme.secondaryLabel,
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "End2",
                        style = AppleWidgetsTheme.textStyles.footnote,
                        color = AppleWidgetsTheme.colorScheme.secondaryLabel,
                    )
                },
            )
            BasicComponent(
                title = "Title",
                summary = "Summary",
                startAction = {
                    Text(
                        text = "Start",
                        color = AppleWidgetsTheme.colorScheme.tertiaryLabel,
                    )
                },
                endActions = {
                    Text(
                        text = "End1",
                        style = AppleWidgetsTheme.textStyles.footnote,
                        color = AppleWidgetsTheme.colorScheme.tertiaryLabel,
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "End2",
                        style = AppleWidgetsTheme.textStyles.footnote,
                        color = AppleWidgetsTheme.colorScheme.tertiaryLabel,
                    )
                },
                enabled = false,
            )
        }
    }
}
