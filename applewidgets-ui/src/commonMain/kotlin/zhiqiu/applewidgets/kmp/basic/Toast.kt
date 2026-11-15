package zhiqiu.applewidgets.kmp.basic

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

@Composable
fun Toast(
    message: String,
    modifier: Modifier = Modifier,
    visible: Boolean = true,
    colors: ToastColors = ToastDefaults.colors(),
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideInVertically { it / 2 },
        exit = fadeOut() + slideOutVertically { it / 2 },
        modifier = modifier,
    ) {
        Box(
            Modifier
                .clip(RoundedCornerShape(ToastDefaults.CornerRadius))
                .background(colors.containerColor)
                .padding(horizontal = 18.dp, vertical = 12.dp),
        ) {
            Text(
                text = message,
                style = AppleWidgetsTheme.textStyles.footnote,
                color = colors.contentColor,
            )
        }
    }
}

/**
 * 轻量 Toast 宿主：show 后自动消失。
 */
@Composable
fun ToastHost(
    message: String?,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    durationMs: Long = ToastDefaults.DurationMs,
    contentAlignment: Alignment = Alignment.BottomCenter,
) {
    var visible by remember(message) { mutableStateOf(message != null) }
    LaunchedEffect(message) {
        if (message == null) return@LaunchedEffect
        visible = true
        delay(durationMs)
        visible = false
        onDismiss()
    }
    Box(modifier = modifier.fillMaxSize(), contentAlignment = contentAlignment) {
        if (message != null) {
            Toast(
                message = message,
                visible = visible,
                modifier = Modifier.padding(bottom = 48.dp),
            )
        }
    }
}

@Composable
fun BoxScope.ToastOverlay(
    message: String?,
    onDismiss: () -> Unit,
) {
    ToastHost(message = message, onDismiss = onDismiss, modifier = Modifier.matchParentSize())
}

object ToastDefaults {
    val CornerRadius = 20.dp
    const val DurationMs = 1800L

    @Composable
    fun colors(
        containerColor: Color = if (AppleWidgetsTheme.isDark) {
            Color(0xE62C2C2E)
        } else {
            Color(0xE63A3A3C)
        },
        contentColor: Color = Color.White,
    ): ToastColors = ToastColors(containerColor, contentColor)
}

@Immutable
data class ToastColors(
    val containerColor: Color,
    val contentColor: Color,
)
