package zhiqiu.applewidgets.kmp.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.basic.Separator
import zhiqiu.applewidgets.kmp.basic.Text
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

internal val LocalGroupedSection = compositionLocalOf { false }

/**
 * Inset Grouped 风格分区：圆角白底卡片 + 可选 header/footer。
 */
@Composable
fun GroupedSection(
    modifier: Modifier = Modifier,
    header: String? = null,
    footer: String? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = AppleWidgetsTheme.colorScheme
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        if (header != null) {
            Text(
                text = header.uppercase(),
                style = AppleWidgetsTheme.textStyles.footnote,
                color = colors.secondaryLabel,
                modifier = Modifier.padding(start = 16.dp, bottom = 6.dp, end = 16.dp),
            )
        }
        CompositionLocalProvider(LocalGroupedSection provides true) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(AppleWidgetsTheme.componentStyles.cardShape)
                    .background(colors.secondarySystemGroupedBackground),
                content = content,
            )
        }
        if (footer != null) {
            Text(
                text = footer,
                style = AppleWidgetsTheme.textStyles.footnote,
                color = colors.secondaryLabel,
                modifier = Modifier.padding(start = 16.dp, top = 6.dp, end = 16.dp),
            )
        }
    }
}

@Composable
fun GroupedList(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(vertical = 16.dp),
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(AppleWidgetsTheme.colorScheme.systemGroupedBackground)
            .padding(contentPadding),
        content = content,
    )
}

@Composable
fun GroupedDivider(
    startIndent: androidx.compose.ui.unit.Dp = 16.dp,
) {
    Separator(startIndent = startIndent)
}
