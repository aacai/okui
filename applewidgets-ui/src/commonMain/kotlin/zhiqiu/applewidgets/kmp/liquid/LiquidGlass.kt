package zhiqiu.applewidgets.kmp.liquid

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import com.kyant.backdrop.highlight.Highlight
import com.kyant.backdrop.isRuntimeShaderSupported
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme
import zhiqiu.applewidgets.kmp.theme.LocalContentColor

/**
 * 液态玻璃表面。
 *
 * - 在 [LiquidGlassScene] / [LocalLiquidBackdrop] 内且 RuntimeShader 可用时：
 *   使用 Kyant Backdrop（vibrancy + blur + lens）做真实采样。
 * - 否则降级为半透明高光近似（不崩）。
 */
@Composable
fun LiquidGlass(
    modifier: Modifier = Modifier,
    style: LiquidGlassStyle = LiquidGlassDefaults.style(),
    backdrop: Backdrop? = LocalLiquidBackdrop.current,
    content: @Composable BoxScope.() -> Unit,
) {
    val shape = RoundedCornerShape(style.cornerRadius)
    CompositionLocalProvider(LocalContentColor provides AppleWidgetsTheme.colorScheme.label) {
        if (backdrop != null && isRuntimeShaderSupported()) {
            Box(
                modifier = modifier.drawBackdrop(
                    backdrop = backdrop,
                    shape = { shape },
                    effects = {
                        vibrancy()
                        blur(style.blurRadius.toPx())
                        lens(style.lensRefraction.toPx(), style.lensDepth.toPx())
                    },
                    highlight = { if (style.highlight) Highlight.Default else Highlight.Plain },
                    onDrawSurface = {
                        drawRect(style.surfaceTint)
                    },
                ),
                content = content,
            )
        } else {
            FallbackLiquidGlass(modifier = modifier, style = style, shape = shape, content = content)
        }
    }
}

/**
 * 弹窗背景遮罩：在 Scaffold 提供 backdrop 时真实模糊背后内容，否则退化为普通半透明遮罩。
 */
@Composable
fun LiquidGlassScrim(
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    blurRadius: Dp = 16.dp,
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable () -> Unit,
) {
    val backdrop = LocalLiquidBackdrop.current
    val scrimModifier = if (backdrop != null && isRuntimeShaderSupported()) {
        modifier
            .fillMaxSize()
            .clickable(onClick = onClick)
            .drawBackdrop(
                backdrop = backdrop,
                shape = { RectangleShape },
                effects = { blur(blurRadius.toPx()) },
                onDrawSurface = { drawRect(color) },
            )
    } else {
        modifier
            .fillMaxSize()
            .background(color)
            .clickable(onClick = onClick)
    }
    Box(scrimModifier, contentAlignment = contentAlignment) {
        Box(Modifier.clickable(enabled = false, onClick = {})) {
            content()
        }
    }
}

@Composable
private fun FallbackLiquidGlass(
    modifier: Modifier,
    style: LiquidGlassStyle,
    shape: RoundedCornerShape,
    content: @Composable BoxScope.() -> Unit,
) {
    val isDark = AppleWidgetsTheme.isDark
    Box(
        modifier = modifier
            .shadow(14.dp, shape, ambientColor = Color.Black.copy(alpha = 0.2f), spotColor = Color.Black.copy(alpha = 0.2f))
            .clip(shape)
            .border(
                1.dp,
                Brush.linearGradient(
                    listOf(
                        Color.White.copy(alpha = if (isDark) 0.35f else 0.65f),
                        Color.White.copy(alpha = 0.08f),
                    ),
                ),
                shape,
            )
            .background(style.surfaceTint)
            .drawWithContent {
                drawContent()
                val h = size.height * 0.35f
                drawRect(
                    brush = Brush.verticalGradient(
                        listOf(Color.White.copy(alpha = 0.22f), Color.Transparent),
                        endY = h,
                    ),
                    size = size.copy(height = h),
                )
                drawRect(
                    brush = Brush.horizontalGradient(
                        listOf(Color.White.copy(alpha = 0.12f), Color.Transparent),
                        endX = size.width * 0.22f,
                    ),
                    topLeft = Offset.Zero,
                    size = size.copy(width = size.width * 0.22f),
                )
            },
        content = content,
    )
}

@Composable
fun LiquidGlassCard(
    modifier: Modifier = Modifier,
    tone: LiquidGlassTone = LiquidGlassTone.Regular,
    backdrop: Backdrop? = LocalLiquidBackdrop.current,
    content: @Composable BoxScope.() -> Unit,
) {
    LiquidGlass(
        modifier = modifier,
        style = LiquidGlassDefaults.style(tone = tone, cornerRadius = 16.dp),
        backdrop = backdrop,
        content = content,
    )
}

@Composable
fun LiquidGlassPill(
    modifier: Modifier = Modifier,
    tone: LiquidGlassTone = LiquidGlassTone.Thin,
    backdrop: Backdrop? = LocalLiquidBackdrop.current,
    content: @Composable BoxScope.() -> Unit,
) {
    LiquidGlass(
        modifier = modifier,
        style = LiquidGlassDefaults.style(tone = tone, cornerRadius = LiquidGlassDefaults.PillCorner),
        backdrop = backdrop,
        content = content,
    )
}
