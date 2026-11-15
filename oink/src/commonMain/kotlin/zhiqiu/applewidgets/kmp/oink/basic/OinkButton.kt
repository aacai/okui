package zhiqiu.applewidgets.kmp.oink.basic

import zhiqiu.applewidgets.kmp.oink.data.*
import zhiqiu.applewidgets.kmp.oink.form.*
import zhiqiu.applewidgets.kmp.oink.layout.*
import zhiqiu.applewidgets.kmp.oink.nav.*
import zhiqiu.applewidgets.kmp.oink.overlay.*
import zhiqiu.applewidgets.kmp.oink.primitives.*
import zhiqiu.applewidgets.kmp.oink.responsive.*
import zhiqiu.applewidgets.kmp.oink.theme.*

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

enum class OinkButtonVariant { Soft, Primary, Accent, Ghost, Outline, Danger }
enum class OinkButtonSize { Sm, Md, Lg }
enum class OinkButtonState { Idle, Loading, Success, Error }

@Composable
fun OinkButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    variant: OinkButtonVariant = OinkButtonVariant.Soft,
    size: OinkButtonSize = OinkButtonSize.Md,
    state: OinkButtonState = OinkButtonState.Idle,
    colors: OinkButtonColors = OinkButtonDefaults.colors(variant),
    interactionSource: MutableInteractionSource? = null,
    insideMargin: PaddingValues? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val source = interactionSource ?: remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()
    val hovered by source.collectIsHoveredAsState()
    val active = enabled && state != OinkButtonState.Loading && state != OinkButtonState.Success

    val height = when (size) {
        OinkButtonSize.Sm -> OinkMetrics.HeightSm
        OinkButtonSize.Md -> OinkMetrics.HeightMd
        OinkButtonSize.Lg -> OinkMetrics.HeightLg
    }
    val gap = if (size == OinkButtonSize.Sm) 6.dp else 8.dp
    val horizontal = height * 0.48f

    val scale by animateFloatAsState(
        targetValue = if (pressed && active) 0.955f else 1f,
        animationSpec = if (pressed && active) tween(OinkMotion.Dur1, easing = OinkMotion.EaseOut) else OinkMotion.bouncy(),
        label = "scale",
    )
    val container by animateColorAsState(
        targetValue = if (hovered && active) colors.hoveredContainer else colors.container,
        animationSpec = tween(OinkMotion.Dur2, easing = OinkMotion.EaseOut),
        label = "container",
    )

    val busy = state != OinkButtonState.Idle
    val labelAlpha by animateFloatAsState(if (busy) 0f else 1f, tween(OinkMotion.Dur2, easing = OinkMotion.EaseOut), label = "labelAlpha")
    val labelShift by animateDpAsState(if (busy) (-14).dp else 0.dp, tween(OinkMotion.Dur3, easing = OinkMotion.EaseOut), label = "labelShift")
    val labelScale by animateFloatAsState(if (busy) 0.9f else 1f, tween(OinkMotion.Dur3, easing = OinkMotion.EaseOut), label = "labelScale")
    val statusAlpha by animateFloatAsState(if (busy) 1f else 0f, tween(OinkMotion.Dur2, easing = OinkMotion.EaseOut), label = "statusAlpha")
    val statusShift by animateDpAsState(
        if (busy) 0.dp else 14.dp,
        if (busy) OinkMotion.bouncy(OinkMotion.DpThreshold) else tween(OinkMotion.Dur2, easing = OinkMotion.EaseIn),
        label = "statusShift",
    )
    val statusScale by animateFloatAsState(
        if (busy) 1f else 0.5f,
        if (busy) OinkMotion.bouncy() else tween(OinkMotion.Dur2, easing = OinkMotion.EaseIn),
        label = "statusScale",
    )

    val shake = remember { Animatable(0f) }
    LaunchedEffect(state) {
        if (state == OinkButtonState.Error) {
            shake.snapTo(0f)
            shake.animateTo(0f, keyframes {
                durationMillis = OinkMotion.Dur5
                -7f at (OinkMotion.Dur5 * 0.15f).toInt()
                6f at (OinkMotion.Dur5 * 0.35f).toInt()
                -4f at (OinkMotion.Dur5 * 0.55f).toInt()
                2f at (OinkMotion.Dur5 * 0.75f).toInt()
                0f at OinkMotion.Dur5
            })
        }
    }

    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                translationX = shake.value
                alpha = if (enabled) 1f else 0.42f
            }
            .height(height)
            .defaultMinSize(minWidth = height)
            .clip(CircleShape)
            .background(container)
            .then(
                if (colors.border != Color.Transparent) Modifier.border(OinkMetrics.ControlStroke, colors.border, CircleShape) else Modifier,
            )
            .clickable(
                interactionSource = source,
                indication = null,
                enabled = active,
                role = Role.Button,
                onClick = onClick,
            )
            .padding(insideMargin ?: PaddingValues(horizontal = horizontal)),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier.graphicsLayer {
                alpha = labelAlpha
                translationY = labelShift.toPx()
                scaleX = labelScale
                scaleY = labelScale
            },
            horizontalArrangement = Arrangement.spacedBy(gap, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            content = content,
        )
        if (busy) {
            Box(
                modifier = Modifier.graphicsLayer {
                    alpha = statusAlpha
                    translationY = statusShift.toPx()
                    scaleX = statusScale
                    scaleY = statusScale
                },
                contentAlignment = Alignment.Center,
            ) {
                val glyphSize = if (size == OinkButtonSize.Sm) 18.dp else 20.dp
                when (state) {
                    OinkButtonState.Loading -> OinkTail(size = glyphSize, color = colors.content)
                    OinkButtonState.Success -> DrawnGlyph(CheckPath, progress = 1f, color = colors.content, size = glyphSize)
                    OinkButtonState.Error -> DrawnGlyph(CrossPathA, CrossPathB, progress = 1f, color = colors.content, size = glyphSize)
                    OinkButtonState.Idle -> Unit
                }
            }
        }
    }
}

@Composable
fun OinkButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    variant: OinkButtonVariant = OinkButtonVariant.Soft,
    size: OinkButtonSize = OinkButtonSize.Md,
    state: OinkButtonState = OinkButtonState.Idle,
    leadingIcon: ImageVector? = null,
) {
    val fontSize = when (size) {
        OinkButtonSize.Sm -> 13
        OinkButtonSize.Md -> 15
        OinkButtonSize.Lg -> 16
    }
    val iconSize = if (size == OinkButtonSize.Sm) 16.dp else 18.dp
    OinkButton(onClick, modifier, enabled, variant, size, state) {
        if (leadingIcon != null) {
            OinkIcon(leadingIcon, null, tint = OinkButtonDefaults.colors(variant).content, size = iconSize)
        }
        OinkText(text, style = OinkTheme.textStyles.button.copy(fontSize = fontSize.sp), color = OinkButtonDefaults.colors(variant).content)
    }
}

@Composable
fun OinkIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    enabled: Boolean = true,
    variant: OinkButtonVariant = OinkButtonVariant.Ghost,
    size: OinkButtonSize = OinkButtonSize.Md,
) {
    val height = when (size) {
        OinkButtonSize.Sm -> OinkMetrics.HeightSm
        OinkButtonSize.Md -> OinkMetrics.HeightMd
        OinkButtonSize.Lg -> OinkMetrics.HeightLg
    }
    val iconSize = if (size == OinkButtonSize.Sm) 16.dp else 18.dp
    OinkButton(
        onClick = onClick,
        modifier = modifier.width(height),
        enabled = enabled,
        variant = variant,
        size = size,
        insideMargin = PaddingValues(0.dp),
    ) {
        OinkIcon(icon, contentDescription, tint = OinkButtonDefaults.colors(variant).content, size = iconSize)
    }
}

object OinkButtonDefaults {
    @Composable
    fun colors(variant: OinkButtonVariant): OinkButtonColors {
        val c = OinkTheme.colors
        return when (variant) {
            OinkButtonVariant.Soft -> OinkButtonColors(c.fill, c.fillHover, c.text)
            OinkButtonVariant.Primary -> OinkButtonColors(c.inverse, c.inverseHover, c.onInverse)
            OinkButtonVariant.Accent -> OinkButtonColors(c.accentFill, c.accentFillHover, c.onAccent)
            OinkButtonVariant.Ghost -> OinkButtonColors(Color.Transparent, c.fill, c.text)
            OinkButtonVariant.Outline -> OinkButtonColors(Color.Transparent, c.fill, c.text, c.line2)
            OinkButtonVariant.Danger -> OinkButtonColors(c.dangerSoft, c.dangerSoft2, c.dangerText)
        }
    }
}

@Immutable
data class OinkButtonColors(
    val container: Color,
    val hoveredContainer: Color,
    val content: Color,
    val border: Color = Color.Transparent,
)

private const val CheckPath = "M4.5 10.5l3.5 3.5 7.5-8"
private const val CrossPathA = "M5.5 5.5l9 9"
private const val CrossPathB = "M14.5 5.5l-9 9"

/** 描线字形：progress 0→1 画出路径。glyph 状态切换时由调用方驱动。 */
@Composable
private fun DrawnGlyph(
    vararg paths: String,
    progress: Float,
    color: Color,
    size: Dp,
) {
    val parsed = remember(paths) { paths.map { PathParser().parsePathString(it).toPath(Path()) } }
    val anim = remember { Animatable(0f) }
    LaunchedEffect(progress) {
        anim.snapTo(0f)
        delay(80)
        anim.animateTo(progress, tween(OinkMotion.Dur4, easing = OinkMotion.EaseOut))
    }
    Canvas(Modifier.size(size)) {
        val s = this.size.minDimension / 20f
        val measure = PathMeasure()
        scale(s, s, pivot = androidx.compose.ui.geometry.Offset.Zero) {
            for (p in parsed) {
                measure.setPath(p, false)
                val dst = Path()
                measure.getSegment(0f, anim.value * measure.length, dst, true)
                drawPath(dst, color = color, style = Stroke(2.4f, cap = StrokeCap.Round, join = StrokeJoin.Round))
            }
        }
    }
}