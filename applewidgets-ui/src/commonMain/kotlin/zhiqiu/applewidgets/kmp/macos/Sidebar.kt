package zhiqiu.applewidgets.kmp.macos

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.basic.Text
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

/**
 * macOS 风格侧边栏容器（NavigationSplitView 左侧）。
 */
@Composable
fun Sidebar(
    modifier: Modifier = Modifier,
    width: Dp = 220.dp,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = AppleWidgetsTheme.colorScheme
    Column(
        modifier = modifier
            .width(width)
            .widthIn(min = 180.dp, max = 280.dp)
            .fillMaxHeight()
            .background(colors.secondarySystemBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 10.dp, vertical = 12.dp),
        content = content,
    )
}

@Composable
fun SidebarSection(
    title: String? = null,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        if (title != null) {
            Text(
                text = title.uppercase(),
                style = AppleWidgetsTheme.textStyles.caption1,
                color = AppleWidgetsTheme.colorScheme.secondaryLabel,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            )
        }
        content()
        Spacer(Modifier.height(10.dp))
    }
}

@Composable
fun SidebarItem(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: String? = null,
    badge: String? = null,
) {
    val colors = AppleWidgetsTheme.colorScheme
    val bg = if (selected) colors.accent else androidx.compose.ui.graphics.Color.Transparent
    val fg = if (selected) colors.onAccent else colors.label
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(bg)
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            Text(text = icon, color = fg, style = AppleWidgetsTheme.textStyles.callout)
            Spacer(Modifier.width(8.dp))
        }
        Text(
            text = title,
            color = fg,
            style = AppleWidgetsTheme.textStyles.callout,
            modifier = Modifier.weight(1f),
        )
        if (badge != null) {
            Text(text = badge, color = if (selected) colors.onAccent.copy(alpha = 0.8f) else colors.secondaryLabel, style = AppleWidgetsTheme.textStyles.caption1)
        }
    }
}

@Composable
fun NavigationSplitView(
    sidebar: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    detail: @Composable () -> Unit,
) {
    Row(modifier = modifier.fillMaxWidth().fillMaxHeight()) {
        sidebar()
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(AppleWidgetsTheme.colorScheme.systemBackground),
        ) {
            detail()
        }
    }
}
