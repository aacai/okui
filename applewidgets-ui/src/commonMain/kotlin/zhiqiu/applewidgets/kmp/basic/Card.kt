package zhiqiu.applewidgets.kmp.basic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

/**
 * Inset 分组卡片容器（Gallery / Settings 分组底）。
 */
@Composable
fun Card(
    modifier: Modifier = Modifier,
    colors: CardColors = CardDefaults.cardColors(),
    cornerRadius: Dp = CardDefaults.CornerRadius,
    content: @Composable ColumnScope.() -> Unit,
) {
    val shape: Shape = RoundedCornerShape(cornerRadius)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(colors.containerColor),
        content = content,
    )
}

object CardDefaults {
    val CornerRadius = 10.dp

    @Composable
    fun cardColors(
        containerColor: Color = AppleWidgetsTheme.colorScheme.secondarySystemGroupedBackground,
    ): CardColors = CardColors(containerColor = containerColor)
}

@Immutable
data class CardColors(
    val containerColor: Color,
)
