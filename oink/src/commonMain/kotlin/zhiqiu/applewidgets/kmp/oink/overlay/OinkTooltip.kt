package zhiqiu.applewidgets.kmp.oink.overlay

import zhiqiu.applewidgets.kmp.oink.basic.*
import zhiqiu.applewidgets.kmp.oink.data.*
import zhiqiu.applewidgets.kmp.oink.form.*
import zhiqiu.applewidgets.kmp.oink.layout.*
import zhiqiu.applewidgets.kmp.oink.nav.*
import zhiqiu.applewidgets.kmp.oink.primitives.*
import zhiqiu.applewidgets.kmp.oink.responsive.*
import zhiqiu.applewidgets.kmp.oink.theme.*

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import zhiqiu.applewidgets.kmp.overlay.PlatformAnchoredPopup
import kotlin.math.roundToInt

/**
 * Oink 提示气泡：悬停 350ms 后在目标上方弹出墨色小气泡（10 圆角）。
 */
@Composable
fun OinkTooltip(
    tooltip: String,
    modifier: Modifier = Modifier,
    colors: OinkTooltipColors = OinkTooltipDefaults.colors(),
    content: @Composable () -> Unit,
) {
    val interaction = remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()
    var show by remember { mutableStateOf(false) }
    LaunchedEffect(hovered) {
        if (hovered) {
            delay(OinkTooltipDefaults.ShowDelayMillis)
            show = true
        } else {
            show = false
        }
    }
    Box(modifier = modifier.hoverable(interaction)) {
        content()
        if (show) {
            val enter = remember { Animatable(0f) }
            LaunchedEffect(Unit) { enter.animateTo(1f, OinkMotion.spring()) }
            PlatformAnchoredPopup(onDismissRequest = { show = false }) {
                Box(
                    modifier = Modifier
                        .offset { IntOffset(0, -OinkTooltipDefaults.Offset.roundToPx()) }
                        .graphicsLayer {
                            val s = 0.9f + 0.1f * enter.value
                            scaleX = s
                            scaleY = s
                            alpha = enter.value
                            transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0f, 1f)
                        }
                        .clip(OinkShape(OinkTooltipDefaults.CornerRadius))
                        .background(colors.container)
                        .padding(horizontal = 9.dp, vertical = 6.dp),
                ) {
                    OinkText(
                        text = tooltip,
                        color = colors.content,
                        style = OinkTheme.textStyles.caption.copy(fontSize = 12.sp),
                    )
                }
            }
        }
    }
}

object OinkTooltipDefaults {
    val CornerRadius = 10.dp
    val Offset = 34.dp
    const val ShowDelayMillis = 350L

    @Composable
    fun colors(): OinkTooltipColors {
        val c = OinkTheme.colors
        return OinkTooltipColors(container = c.inverse, content = c.onInverse)
    }
}

@Immutable
data class OinkTooltipColors(
    val container: Color,
    val content: Color,
)