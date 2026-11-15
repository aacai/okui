package zhiqiu.applewidgets.kmp.oink.theme

import zhiqiu.applewidgets.kmp.oink.basic.*
import zhiqiu.applewidgets.kmp.oink.data.*
import zhiqiu.applewidgets.kmp.oink.form.*
import zhiqiu.applewidgets.kmp.oink.layout.*
import zhiqiu.applewidgets.kmp.oink.nav.*
import zhiqiu.applewidgets.kmp.oink.overlay.*
import zhiqiu.applewidgets.kmp.oink.primitives.*
import zhiqiu.applewidgets.kmp.oink.responsive.*

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Immutable
data class OinkTextStyles(
    val display: TextStyle,
    val h1: TextStyle,
    val h2: TextStyle,
    val h3: TextStyle,
    val h4: TextStyle,
    val bodyLarge: TextStyle,
    val body: TextStyle,
    val label: TextStyle,
    val caption: TextStyle,
    val micro: TextStyle,
    val mono: TextStyle,
    val button: TextStyle,
    val rowTitle: TextStyle,
    val rowDesc: TextStyle,
    val rowValue: TextStyle,
    val menuItem: TextStyle,
)

fun oinkTextStyles(): OinkTextStyles {
    fun style(size: Int, weight: FontWeight, lineHeight: Float, tracking: Float = 0f) = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = weight,
        fontSize = size.sp,
        lineHeight = (size * lineHeight).sp,
        letterSpacing = tracking.sp,
    )
    return OinkTextStyles(
        display = style(56, FontWeight.W700, 1.06f, -1.4f),
        h1 = style(40, FontWeight.W700, 1.15f, -0.8f),
        h2 = style(28, FontWeight.W700, 1.25f, -0.42f),
        h3 = style(20, FontWeight.W600, 1.35f, -0.2f),
        h4 = style(17, FontWeight.W600, 1.35f, -0.085f),
        bodyLarge = style(17, FontWeight.W400, 1.6f),
        body = style(15, FontWeight.W400, 1.6f),
        label = style(15, FontWeight.W600, 1.3f),
        caption = style(13, FontWeight.W500, 1.4f),
        micro = style(11, FontWeight.W700, 1.3f, 0.88f),
        mono = TextStyle(fontFamily = FontFamily.Monospace, fontWeight = FontWeight.W500, fontSize = 13.sp, lineHeight = 19.5.sp),
        button = style(15, FontWeight.W400, 1f, -0.075f),
        rowTitle = style(14, FontWeight.W400, 1.3f),
        rowDesc = style(12, FontWeight.W400, 1.4f),
        rowValue = style(15, FontWeight.W500, 1.3f),
        menuItem = style(14, FontWeight.W400, 1.4f),
    )
}

val LocalOinkTextStyles = staticCompositionLocalOf { oinkTextStyles() }

object OinkTheme {
    val colors: OinkColors
        @Composable @ReadOnlyComposable
        get() = LocalOinkColors.current

    val textStyles: OinkTextStyles
        @Composable @ReadOnlyComposable
        get() = LocalOinkTextStyles.current

    val isDark: Boolean
        @Composable @ReadOnlyComposable
        get() = colors.isDark
}

/**
 * Oink 主题。切换明暗 / 强调色时所有令牌做 240ms 交叉淡入，和 oink-ui 的换肤节奏一致。
 */
@Composable
fun OinkTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    accent: OinkAccent = OinkAccent.Pink,
    textStyles: OinkTextStyles = oinkTextStyles(),
    content: @Composable () -> Unit,
) {
    val target = remember(darkTheme, accent) {
        if (darkTheme) oinkDarkColors(accent) else oinkLightColors(accent)
    }
    OinkTheme(colors = target, textStyles = textStyles, content = content)
}

@Composable
fun OinkTheme(
    colors: OinkColors,
    textStyles: OinkTextStyles = oinkTextStyles(),
    content: @Composable () -> Unit,
) {
    val spec = tween<Color>(durationMillis = 240, easing = OinkMotion.EaseOut)
    val animated = OinkColors(
        bg = animateColorAsState(colors.bg, spec, label = "bg").value,
        surface = animateColorAsState(colors.surface, spec, label = "surface").value,
        card = animateColorAsState(colors.card, spec, label = "card").value,
        raised = animateColorAsState(colors.raised, spec, label = "raised").value,
        well = animateColorAsState(colors.well, spec, label = "well").value,
        well2 = animateColorAsState(colors.well2, spec, label = "well2").value,
        wellHi = animateColorAsState(colors.wellHi, spec, label = "wellHi").value,
        fill = animateColorAsState(colors.fill, spec, label = "fill").value,
        fillHover = animateColorAsState(colors.fillHover, spec, label = "fillHover").value,
        hover = animateColorAsState(colors.hover, spec, label = "hover").value,
        pop = animateColorAsState(colors.pop, spec, label = "pop").value,
        thumb = animateColorAsState(colors.thumb, spec, label = "thumb").value,
        line = animateColorAsState(colors.line, spec, label = "line").value,
        line2 = animateColorAsState(colors.line2, spec, label = "line2").value,
        hi = animateColorAsState(colors.hi, spec, label = "hi").value,
        scrim = animateColorAsState(colors.scrim, spec, label = "scrim").value,
        text = animateColorAsState(colors.text, spec, label = "text").value,
        text2 = animateColorAsState(colors.text2, spec, label = "text2").value,
        text3 = animateColorAsState(colors.text3, spec, label = "text3").value,
        inverse = animateColorAsState(colors.inverse, spec, label = "inverse").value,
        inverseHover = animateColorAsState(colors.inverseHover, spec, label = "inverseHover").value,
        onInverse = animateColorAsState(colors.onInverse, spec, label = "onInverse").value,
        accent = animateColorAsState(colors.accent, spec, label = "accent").value,
        accentDeep = animateColorAsState(colors.accentDeep, spec, label = "accentDeep").value,
        onAccent = animateColorAsState(colors.onAccent, spec, label = "onAccent").value,
        accentHover = animateColorAsState(colors.accentHover, spec, label = "accentHover").value,
        accentSoft = animateColorAsState(colors.accentSoft, spec, label = "accentSoft").value,
        accentRing = animateColorAsState(colors.accentRing, spec, label = "accentRing").value,
        accentText = animateColorAsState(colors.accentText, spec, label = "accentText").value,
        accentTint = animateColorAsState(colors.accentTint, spec, label = "accentTint").value,
        accentTint2 = animateColorAsState(colors.accentTint2, spec, label = "accentTint2").value,
        accentFill = animateColorAsState(colors.accentFill, spec, label = "accentFill").value,
        accentFillHover = animateColorAsState(colors.accentFillHover, spec, label = "accentFillHover").value,
        success = animateColorAsState(colors.success, spec, label = "success").value,
        successText = animateColorAsState(colors.successText, spec, label = "successText").value,
        successSoft = animateColorAsState(colors.successSoft, spec, label = "successSoft").value,
        warning = animateColorAsState(colors.warning, spec, label = "warning").value,
        warningText = animateColorAsState(colors.warningText, spec, label = "warningText").value,
        warningSoft = animateColorAsState(colors.warningSoft, spec, label = "warningSoft").value,
        danger = animateColorAsState(colors.danger, spec, label = "danger").value,
        dangerText = animateColorAsState(colors.dangerText, spec, label = "dangerText").value,
        dangerSoft = animateColorAsState(colors.dangerSoft, spec, label = "dangerSoft").value,
        dangerSoft2 = animateColorAsState(colors.dangerSoft2, spec, label = "dangerSoft2").value,
        info = animateColorAsState(colors.info, spec, label = "info").value,
        infoText = animateColorAsState(colors.infoText, spec, label = "infoText").value,
        infoSoft = animateColorAsState(colors.infoSoft, spec, label = "infoSoft").value,
    )
    CompositionLocalProvider(
        LocalOinkColors provides animated,
        LocalOinkTextStyles provides textStyles,
        LocalOinkContentColor provides animated.text,
    ) {
        content()
    }
}