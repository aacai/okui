package zhiqiu.applewidgets.kmp.macos

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.basic.Text
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

@Composable
fun Popover(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = AppleWidgetsTheme.colorScheme
    val shape = RoundedCornerShape(10.dp)
    Column(
        modifier = modifier
            .widthIn(min = 180.dp, max = 320.dp)
            .shadow(12.dp, shape)
            .clip(shape)
            .background(colors.secondarySystemGroupedBackground)
            .border(0.5.dp, colors.separator, shape)
            .padding(12.dp),
        content = content,
    )
}

@Composable
fun Popover(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
) {
    Popover(modifier = modifier) {
        Text(text = title, style = AppleWidgetsTheme.textStyles.headline)
        Text(
            text = message,
            style = AppleWidgetsTheme.textStyles.footnote,
            color = AppleWidgetsTheme.colorScheme.secondaryLabel,
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}
