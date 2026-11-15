package zhiqiu.applewidgets.kmp.basic

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

@Composable
fun ColorPalette(
    colors: List<Color>,
    selected: Color?,
    onSelect: (Color) -> Unit,
    modifier: Modifier = Modifier,
    columns: Int = 5,
) {
    val rows = colors.chunked(columns.coerceAtLeast(1))
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        rows.forEach { rowColors ->
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                rowColors.forEach { color ->
                    val isSelected = selected == color
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(color)
                            .then(
                                if (isSelected) {
                                    Modifier.border(2.dp, AppleWidgetsTheme.colorScheme.label, CircleShape)
                                } else {
                                    Modifier.border(1.dp, AppleWidgetsTheme.colorScheme.separator, CircleShape)
                                },
                            )
                            .clickable { onSelect(color) },
                    )
                }
            }
        }
    }
}

object ColorPaletteDefaults {
    val SystemSwatches = listOf(
        Color(0xFF007AFF),
        Color(0xFF34C759),
        Color(0xFFFF9500),
        Color(0xFFFF3B30),
        Color(0xFFAF52DE),
        Color(0xFFFF2D55),
        Color(0xFF5856D6),
        Color(0xFF5AC8FA),
        Color(0xFF8E8E93),
        Color(0xFF000000),
    )
}
