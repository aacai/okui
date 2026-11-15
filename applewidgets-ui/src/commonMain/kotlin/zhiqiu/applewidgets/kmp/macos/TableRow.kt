package zhiqiu.applewidgets.kmp.macos

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.basic.Text
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

@Composable
fun TableHeader(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(AppleWidgetsTheme.colorScheme.tertiaryFill)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}

@Composable
fun TableRow(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    val colors = AppleWidgetsTheme.colorScheme
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(if (selected) colors.accent.copy(alpha = 0.18f) else androidx.compose.ui.graphics.Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}

@Composable
fun RowScope.TableCell(
    text: String,
    modifier: Modifier = Modifier,
    weight: Float = 1f,
    header: Boolean = false,
) {
    Text(
        text = text,
        style = if (header) AppleWidgetsTheme.textStyles.caption1 else AppleWidgetsTheme.textStyles.callout,
        color = if (header) {
            AppleWidgetsTheme.colorScheme.secondaryLabel
        } else {
            AppleWidgetsTheme.colorScheme.label
        },
        modifier = modifier.weight(weight),
    )
}
