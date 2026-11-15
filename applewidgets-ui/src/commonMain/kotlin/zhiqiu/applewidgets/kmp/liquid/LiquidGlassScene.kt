package zhiqiu.applewidgets.kmp.liquid

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.backdrops.LayerBackdrop
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop

/**
 * 当前场景的 Backdrop 采样源。玻璃组件从这里读；为 null 时降级为半透明近似。
 */
val LocalLiquidBackdrop = compositionLocalOf<Backdrop?> { null }

/**
 * 液态玻璃场景：底层 [background] 作为采样源，上层 [content] 绘制玻璃。
 *
 * ```
 * LiquidGlassScene(
 *   background = { /* 渐变/壁纸/列表 */ },
 * ) {
 *   LiquidGlassCard { Text("玻璃") }
 * }
 * ```
 */
@Composable
fun LiquidGlassScene(
    modifier: Modifier = Modifier,
    background: @Composable BoxScope.() -> Unit,
    content: @Composable BoxScope.() -> Unit,
) {
    val backdrop = rememberLayerBackdrop()
    CompositionLocalProvider(LocalLiquidBackdrop provides backdrop) {
        Box(modifier = modifier) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .layerBackdrop(backdrop),
                content = background,
            )
            Box(
                modifier = Modifier.fillMaxSize(),
                content = content,
            )
        }
    }
}

@Composable
fun rememberLiquidBackdrop(): LayerBackdrop = rememberLayerBackdrop()

fun Modifier.liquidBackdropSource(backdrop: LayerBackdrop): Modifier =
    layerBackdrop(backdrop)
