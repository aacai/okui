package zhiqiu.applewidgets.kmp.oink.basic

import zhiqiu.applewidgets.kmp.oink.data.*
import zhiqiu.applewidgets.kmp.oink.form.*
import zhiqiu.applewidgets.kmp.oink.layout.*
import zhiqiu.applewidgets.kmp.oink.nav.*
import zhiqiu.applewidgets.kmp.oink.overlay.*
import zhiqiu.applewidgets.kmp.oink.primitives.*
import zhiqiu.applewidgets.kmp.oink.responsive.*
import zhiqiu.applewidgets.kmp.oink.theme.*

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private const val InkPathData =
    "M10 3H18A7 7 0 0 1 25 10V18A7 7 0 0 1 18 25H5A2 2 0 0 1 3 23V10A7 7 0 0 1 10 3Z"

/**
 * Oink UI 标志：墨色圆角方块（左下角收小）+ 右下角强调色猪鼻子，整体逆时针 8°。
 * [animated] 时猪鼻子入场拱一下。
 */
@Composable
fun OinkLogo(
    modifier: Modifier = Modifier,
    size: Dp = OinkLogoDefaults.Size,
    wordmark: Boolean = false,
    animated: Boolean = false,
    ink: Color = Color.Unspecified,
    snout: Color = Color.Unspecified,
    colors: OinkLogoColors = OinkLogoDefaults.colors(),
) {
    val inkColor = if (ink != Color.Unspecified) ink else colors.ink
    val snoutColor = if (snout != Color.Unspecified) snout else colors.snout
    val path = remember { PathParser().parsePathString(InkPathData).toPath(Path()) }
    val pop = remember { Animatable(if (animated) 0f else 1f) }
    LaunchedEffect(animated) {
        if (animated) {
            pop.snapTo(0f)
            pop.animateTo(1f, OinkMotion.wobbly())
        }
    }
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Canvas(Modifier.size(size)) {
            val s = this.size.minDimension / 32f
            rotate(-8f, pivot = center) {
                scale(s, s, pivot = Offset.Zero) {
                    drawPath(path, color = inkColor)
                    val snoutCenter = Offset(22.25f, 20f)
                    scale(pop.value, pop.value, pivot = snoutCenter) {
                        drawRoundRect(
                            color = snoutColor,
                            topLeft = Offset(15.5f, 15f),
                            size = Size(13.5f, 10f),
                            cornerRadius = CornerRadius(5f, 5f),
                        )
                        drawOval(inkColor, topLeft = Offset(18.7f, 18.1f), size = Size(2.6f, 3.8f))
                        drawOval(inkColor, topLeft = Offset(23.2f, 18.1f), size = Size(2.6f, 3.8f))
                    }
                }
            }
        }
        if (wordmark) {
            Spacer(Modifier.width(size * 0.32f))
            OinkText(
                text = "Oink UI",
                color = inkColor,
                style = OinkTheme.textStyles.h4.copy(
                    fontSize = (size.value * 0.58f).sp,
                    letterSpacing = (-0.02f * size.value * 0.58f).sp,
                ),
            )
        }
    }
}

object OinkLogoDefaults {
    val Size = 28.dp

    @Composable
    fun colors(): OinkLogoColors {
        val c = OinkTheme.colors
        return OinkLogoColors(ink = c.text, snout = c.accent)
    }
}

@Immutable
data class OinkLogoColors(
    val ink: Color,
    val snout: Color,
)