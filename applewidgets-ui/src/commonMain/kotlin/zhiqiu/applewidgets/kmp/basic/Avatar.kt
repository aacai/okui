package zhiqiu.applewidgets.kmp.basic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

@Composable
fun Avatar(
    initials: String,
    modifier: Modifier = Modifier,
    size: Dp = AvatarDefaults.Size,
    colors: AvatarColors = AvatarDefaults.colors(),
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(colors.containerColor),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = initials.take(2).uppercase(),
            style = AppleWidgetsTheme.textStyles.footnote,
            color = colors.contentColor,
        )
    }
}

object AvatarDefaults {
    val Size = 40.dp

    @Composable
    fun colors(
        containerColor: Color = AppleWidgetsTheme.colorScheme.fill,
        contentColor: Color = AppleWidgetsTheme.colorScheme.label,
    ): AvatarColors = AvatarColors(containerColor, contentColor)
}

@Immutable
data class AvatarColors(
    val containerColor: Color,
    val contentColor: Color,
)
