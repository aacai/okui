package zhiqiu.applewidgets.kmp.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

/**
 * iOS Form：分组滚动容器（内部用 [GroupedSection] 搭行）。
 */
@Composable
fun Form(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(vertical = 12.dp),
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(AppleWidgetsTheme.colorScheme.systemGroupedBackground)
            .verticalScroll(rememberScrollState())
            .padding(contentPadding),
        content = content,
    )
}

@Composable
fun FormSection(
    modifier: Modifier = Modifier,
    header: String? = null,
    footer: String? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    GroupedSection(
        modifier = modifier,
        header = header,
        footer = footer,
        content = content,
    )
}
