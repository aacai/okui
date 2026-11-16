package zhiqiu.applewidgets.kmp.basic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

@Composable
fun Snackbar(
    message: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    colors: SnackbarColors = SnackbarDefaults.colors(),
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clip(AppleWidgetsTheme.componentStyles.overlayShape)
            .background(colors.containerColor)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = message,
            style = AppleWidgetsTheme.textStyles.footnote,
            color = colors.contentColor,
            modifier = Modifier.weight(1f),
        )
        if (actionLabel != null && onAction != null) {
            Spacer(Modifier.width(12.dp))
            Button(
                text = actionLabel,
                onClick = onAction,
                style = ButtonStyle.Plain,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = colors.actionColor,
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = colors.actionColor.copy(alpha = 0.4f),
                ),
            )
        }
    }
}

object SnackbarDefaults {
    @Composable
    fun colors(
        containerColor: Color = if (AppleWidgetsTheme.isDark) Color(0xFF2C2C2E) else Color(0xFF3A3A3C),
        contentColor: Color = Color.White,
        actionColor: Color = AppleWidgetsTheme.colorScheme.accent,
    ): SnackbarColors = SnackbarColors(containerColor, contentColor, actionColor)
}

@Immutable
data class SnackbarColors(
    val containerColor: Color,
    val contentColor: Color,
    val actionColor: Color,
)
