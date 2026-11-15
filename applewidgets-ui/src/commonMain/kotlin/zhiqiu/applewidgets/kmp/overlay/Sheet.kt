package zhiqiu.applewidgets.kmp.overlay

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.platform.safeNavigationBarsPadding
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

enum class SheetDetent {
    Medium,
    Large,
}

@Composable
fun Sheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    detent: SheetDetent = SheetDetent.Medium,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = AppleWidgetsTheme.colorScheme
    val heightFraction = when (detent) {
        SheetDetent.Medium -> 0.5f
        SheetDetent.Large -> 0.92f
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.windowDimming)
            .clickable(onClick = onDismissRequest),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(heightFraction)
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                .background(colors.secondarySystemGroupedBackground)
                .clickable(enabled = false, onClick = {})
                .safeNavigationBarsPadding()
                .padding(bottom = 8.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.12f)
                        .height(5.dp)
                        .clip(RoundedCornerShape(2.5.dp))
                        .background(colors.tertiaryFill),
                )
            }
            content()
        }
    }
}
