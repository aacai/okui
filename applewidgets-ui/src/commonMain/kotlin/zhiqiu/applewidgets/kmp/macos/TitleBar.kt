package zhiqiu.applewidgets.kmp.macos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.basic.Separator
import zhiqiu.applewidgets.kmp.basic.Text
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

/**
 * 仿 macOS 交通灯 + 标题栏（跨平台视觉示意，非真实窗口控件）。
 */
@Composable
fun TitleBar(
    title: String,
    modifier: Modifier = Modifier,
    showTrafficLights: Boolean = true,
    trailing: @Composable RowScope.() -> Unit = {},
) {
    val colors = AppleWidgetsTheme.colorScheme
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.secondarySystemBackground),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(38.dp)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (showTrafficLights) {
                TrafficLights()
                Spacer(Modifier.width(12.dp))
            }
            Text(
                text = title,
                style = AppleWidgetsTheme.textStyles.callout,
                color = colors.secondaryLabel,
                modifier = Modifier.weight(1f),
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
                content = trailing,
            )
        }
        Separator(modifier = Modifier.align(Alignment.BottomCenter), startIndent = 0.dp)
    }
}

@Composable
fun TrafficLights(modifier: Modifier = Modifier) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        TrafficLightDot(Color(0xFFFF5F57))
        TrafficLightDot(Color(0xFFFEBC2E))
        TrafficLightDot(Color(0xFF28C840))
    }
}

@Composable
private fun TrafficLightDot(color: Color) {
    Box(
        modifier = Modifier
            .size(12.dp)
            .clip(CircleShape)
            .background(color),
    )
}
