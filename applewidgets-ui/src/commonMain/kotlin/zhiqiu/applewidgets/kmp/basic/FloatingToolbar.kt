package zhiqiu.applewidgets.kmp.basic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

@Composable
fun FloatingToolbar(
    modifier: Modifier = Modifier,
    colors: FloatingToolbarColors = FloatingToolbarDefaults.colors(),
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier
            .shadow(8.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(colors.containerColor)
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        content = content,
    )
}

object FloatingToolbarDefaults {
    @Composable
    fun colors(
        containerColor: Color = AppleWidgetsTheme.colorScheme.secondarySystemGroupedBackground,
    ): FloatingToolbarColors = FloatingToolbarColors(containerColor)
}

@Immutable
data class FloatingToolbarColors(
    val containerColor: Color,
)
