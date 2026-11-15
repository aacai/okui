package zhiqiu.applewidgets.kmp.liquid

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

enum class LiquidGlassTone {
    Regular,
    Thin,
    Thick,
    Clear,
}

@Immutable
data class LiquidGlassStyle(
    val cornerRadius: Dp,
    val blurRadius: Dp,
    val lensRefraction: Dp,
    val lensDepth: Dp,
    val surfaceTint: Color,
    val highlight: Boolean,
)

object LiquidGlassDefaults {
    val CornerRadius = 22.dp
    val PillCorner = 28.dp

    @Composable
    fun style(
        tone: LiquidGlassTone = LiquidGlassTone.Regular,
        cornerRadius: Dp = CornerRadius,
        isDark: Boolean = AppleWidgetsTheme.isDark,
    ): LiquidGlassStyle {
        val (blur, refraction, depth, tintAlpha) = when (tone) {
            LiquidGlassTone.Clear -> Quad(2.dp, 8.dp, 16.dp, if (isDark) 0.08f else 0.18f)
            LiquidGlassTone.Thin -> Quad(3.dp, 12.dp, 24.dp, if (isDark) 0.12f else 0.28f)
            LiquidGlassTone.Regular -> Quad(4.dp, 16.dp, 32.dp, if (isDark) 0.18f else 0.40f)
            LiquidGlassTone.Thick -> Quad(8.dp, 22.dp, 40.dp, if (isDark) 0.28f else 0.55f)
        }
        return LiquidGlassStyle(
            cornerRadius = cornerRadius,
            blurRadius = blur,
            lensRefraction = refraction,
            lensDepth = depth,
            surfaceTint = if (isDark) {
                Color.Black.copy(alpha = tintAlpha)
            } else {
                Color.White.copy(alpha = tintAlpha)
            },
            highlight = tone != LiquidGlassTone.Clear,
        )
    }
}

private data class Quad(val a: Dp, val b: Dp, val c: Dp, val d: Float)
