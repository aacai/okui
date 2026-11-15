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
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.platform.performToggleHaptic
import kotlin.math.roundToInt

enum class OinkSwitchSize { Sm, Md, Lg }

private data class SwitchGeo(val trackW: Dp, val trackH: Dp, val thumbW: Dp, val thumbH: Dp, val travel: Dp)

private fun geoOf(size: OinkSwitchSize): SwitchGeo = when (size) {
    OinkSwitchSize.Sm -> SwitchGeo(36.dp, 22.dp, 20.dp, 16.dp, 10.dp)
    OinkSwitchSize.Md -> SwitchGeo(44.dp, 26.dp, 26.dp, 20.dp, 12.dp)
    OinkSwitchSize.Lg -> SwitchGeo(54.dp, 32.dp, 32.dp, 26.dp, 16.dp)
}

/**
 * Oink 开关：44×26 轨道、3px 内缩、按压时滑块拉伸 5px，弹簧位移。
 * 禁用态换画法（空槽描边 + 灰滑块），不靠整体变淡。
 */
@Composable
fun OinkSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: OinkSwitchSize = OinkSwitchSize.Md,
    colors: OinkSwitchColors = OinkSwitchDefaults.colors(),
    interactionSource: MutableInteractionSource? = null,
) {
    val source = interactionSource ?: remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()
    val haptic: HapticFeedback = LocalHapticFeedback.current
    val geo = geoOf(size)
    val inset = OinkSwitchDefaults.ThumbInset
    val stretch = if (pressed && enabled) OinkSwitchDefaults.ThumbStretch else 0.dp

    val trackColor by animateColorAsState(
        targetValue = when {
            !enabled && checked -> colors.trackDisabledOn
            !enabled -> colors.trackDisabledOff
            checked -> colors.trackOn
            else -> colors.trackOff
        },
        animationSpec = tween(OinkMotion.Dur3, easing = OinkMotion.EaseOut),
        label = "track",
    )
    val thumbColor = when {
        !enabled && checked -> colors.thumbDisabledOn
        !enabled -> colors.thumbDisabledOff
        else -> colors.thumb
    }

    val thumbX by animateDpAsState(
        targetValue = if (checked) geo.travel - stretch else 0.dp,
        animationSpec = OinkMotion.spring(OinkMotion.DpThreshold),
        label = "thumbX",
    )
    val thumbW by animateDpAsState(
        targetValue = geo.thumbW + stretch,
        animationSpec = tween(OinkMotion.Dur2, easing = OinkMotion.EaseOut),
        label = "thumbW",
    )

    Box(
        modifier = modifier
            .size(geo.trackW, geo.trackH)
            .clip(CircleShape)
            .background(trackColor)
            .then(
                if (!enabled && !checked) Modifier.border(OinkMetrics.ControlStroke, colors.disabledBorder, CircleShape) else Modifier,
            )
            .clickable(
                interactionSource = source,
                indication = null,
                enabled = enabled,
                role = Role.Switch,
                onClick = {
                    haptic.performToggleHaptic(!checked)
                    onCheckedChange(!checked)
                },
            ),
    ) {
        Box(
            modifier = Modifier
                .offset { IntOffset((inset + thumbX).toPx().roundToInt(), inset.toPx().roundToInt()) }
                .size(thumbW, geo.thumbH)
                .clip(CircleShape)
                .background(thumbColor),
        )
    }
}

object OinkSwitchDefaults {
    val ThumbInset = 3.dp
    val ThumbStretch = 5.dp

    @Composable
    fun colors(): OinkSwitchColors {
        val c = OinkTheme.colors
        return OinkSwitchColors(
            trackOn = c.accent,
            trackOff = c.well2,
            trackDisabledOn = c.accentTint,
            trackDisabledOff = Color.Transparent,
            thumb = c.thumb,
            thumbDisabledOn = c.raised,
            thumbDisabledOff = c.well2,
            disabledBorder = c.line2,
        )
    }
}

@Immutable
data class OinkSwitchColors(
    val trackOn: Color,
    val trackOff: Color,
    val trackDisabledOn: Color,
    val trackDisabledOff: Color,
    val thumb: Color,
    val thumbDisabledOn: Color,
    val thumbDisabledOff: Color,
    val disabledBorder: Color,
)