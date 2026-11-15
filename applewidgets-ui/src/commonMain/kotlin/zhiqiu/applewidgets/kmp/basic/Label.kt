package zhiqiu.applewidgets.kmp.basic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

@Composable
fun Label(
    title: String,
    modifier: Modifier = Modifier,
    systemImage: String? = null,
    subtitle: String? = null,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        if (systemImage != null) {
            Text(systemImage, style = AppleWidgetsTheme.textStyles.title3)
            Spacer(Modifier.width(8.dp))
        }
        Column {
            Text(title, style = AppleWidgetsTheme.textStyles.body, color = AppleWidgetsTheme.colorScheme.label)
            if (subtitle != null) {
                Text(subtitle, style = AppleWidgetsTheme.textStyles.footnote, color = AppleWidgetsTheme.colorScheme.secondaryLabel)
            }
        }
    }
}
