package zhiqiu.applewidgets.kmp.basic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

@Composable
fun Badge(
    count: Int? = null,
    modifier: Modifier = Modifier,
    color: Color = AppleWidgetsTheme.colorScheme.destructive,
) {
    val label = when {
        count == null -> null
        count > 99 -> "99+"
        else -> count.toString()
    }
    Box(
        modifier = modifier
            .defaultMinSize(minWidth = if (label == null) 10.dp else 18.dp, minHeight = if (label == null) 10.dp else 18.dp)
            .clip(CircleShape)
            .background(color)
            .padding(horizontal = if (label != null && label.length > 1) 5.dp else 0.dp),
        contentAlignment = Alignment.Center,
    ) {
        if (label != null) {
            Text(
                text = label,
                style = AppleWidgetsTheme.textStyles.caption2,
                color = Color.White,
            )
        }
    }
}
