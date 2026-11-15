package zhiqiu.applewidgets.kmp.oink.theme

import zhiqiu.applewidgets.kmp.oink.basic.*
import zhiqiu.applewidgets.kmp.oink.data.*
import zhiqiu.applewidgets.kmp.oink.form.*
import zhiqiu.applewidgets.kmp.oink.layout.*
import zhiqiu.applewidgets.kmp.oink.nav.*
import zhiqiu.applewidgets.kmp.oink.overlay.*
import zhiqiu.applewidgets.kmp.oink.primitives.*
import zhiqiu.applewidgets.kmp.oink.responsive.*

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

/**
 * Oink UI 色彩令牌：墨色中性 + 单一强调色，扁平无阴影，靠表面色阶与发丝线分层。
 * 数值一比一取自 oink-ui tokens.css；派生色用 sRGB 线性混合复刻 color-mix。
 */
@Immutable
data class OinkColors(
    val bg: Color,
    val surface: Color,
    val card: Color,
    val raised: Color,
    val well: Color,
    val well2: Color,
    val wellHi: Color,
    val fill: Color,
    val fillHover: Color,
    val hover: Color,
    val pop: Color,
    val thumb: Color,
    val line: Color,
    val line2: Color,
    val hi: Color,
    val scrim: Color,
    val text: Color,
    val text2: Color,
    val text3: Color,
    val inverse: Color,
    val inverseHover: Color,
    val onInverse: Color,
    val accent: Color,
    val accentDeep: Color,
    val onAccent: Color,
    val accentHover: Color,
    val accentSoft: Color,
    val accentRing: Color,
    val accentText: Color,
    val accentTint: Color,
    val accentTint2: Color,
    val accentFill: Color,
    val accentFillHover: Color,
    val success: Color,
    val successText: Color,
    val successSoft: Color,
    val warning: Color,
    val warningText: Color,
    val warningSoft: Color,
    val danger: Color,
    val dangerText: Color,
    val dangerSoft: Color,
    val dangerSoft2: Color,
    val info: Color,
    val infoText: Color,
    val infoSoft: Color,
) {
    val isDark: Boolean get() = bg.luminance() < 0.5f
    val scrollbar: Color get() = oinkMix(text3, bg, 0.55f)
    val scrollbarHover: Color get() = oinkMix(text3, bg, 0.85f)
}

enum class OinkAccent(val label: String) {
    Pink("Pink"),
    Orange("Orange"),
    Amber("Amber"),
    Blue("Blue"),
    Violet("Violet"),
}

internal val OinkInk = Color(0xFF1B1C22)
internal val OinkPaper = Color(0xFFF4F4F6)
private val White = Color(0xFFFFFFFF)
private val DarkCard = Color(0xFF2A2927)

/** CSS color-mix(in srgb, a fraction, b)：在 sRGB 分量上直接加权。 */
internal fun oinkMix(a: Color, b: Color, fraction: Float): Color = Color(
    red = a.red * fraction + b.red * (1f - fraction),
    green = a.green * fraction + b.green * (1f - fraction),
    blue = a.blue * fraction + b.blue * (1f - fraction),
    alpha = a.alpha * fraction + b.alpha * (1f - fraction),
)

private data class AccentSeed(val accent: Color, val deep: Color, val onAccent: Color)

private fun seedOf(accent: OinkAccent): AccentSeed = when (accent) {
    OinkAccent.Pink -> AccentSeed(Color(0xFFFF6B8E), Color(0xFFD42E62), OinkInk)
    OinkAccent.Orange -> AccentSeed(Color(0xFFFF7A2F), Color(0xFFC2470A), OinkInk)
    OinkAccent.Amber -> AccentSeed(Color(0xFFFFBE3B), Color(0xFF96600A), OinkInk)
    OinkAccent.Blue -> AccentSeed(Color(0xFF2E7BFF), Color(0xFF1D5FD1), White)
    OinkAccent.Violet -> AccentSeed(Color(0xFFC77DFF), Color(0xFF8B36D6), OinkInk)
}

fun oinkLightColors(accent: OinkAccent = OinkAccent.Pink): OinkColors =
    oinkLightColors(seedOf(accent))

fun oinkDarkColors(accent: OinkAccent = OinkAccent.Pink): OinkColors =
    oinkDarkColors(seedOf(accent))

/** 自定义强调色：onAccent 按对比度自动选墨/白，deep 用降明度提饱和近似。 */
fun oinkLightColors(accent: Color): OinkColors = oinkLightColors(customSeed(accent))

fun oinkDarkColors(accent: Color): OinkColors = oinkDarkColors(customSeed(accent))

private fun customSeed(accent: Color): AccentSeed {
    val on = if (accent.luminance() > 0.45f) OinkInk else White
    val deep = oinkMix(accent, OinkInk, 0.78f)
    return AccentSeed(accent, deep, on)
}

private fun oinkLightColors(seed: AccentSeed): OinkColors {
    val a = seed.accent
    val blue = seed.onAccent == White
    return OinkColors(
        bg = Color(0xFFF9F9FB),
        surface = Color(0xFFFDFDFE),
        card = White,
        raised = White,
        well = Color(0xFFECECEF),
        well2 = Color(0xFFE0E0E5),
        wellHi = Color(0xFFF6F6F8),
        fill = Color(0xFFECECEF),
        fillHover = Color(0xFFE0E0E5),
        hover = Color(0x0E1B1C22),
        pop = White,
        thumb = White,
        line = Color(0x121B1C22),
        line2 = Color(0x241B1C22),
        hi = Color(0xE6FFFFFF),
        scrim = Color(0x61111216),
        text = OinkInk,
        text2 = Color(0xFF5F606A),
        text3 = Color(0xFF989AA4),
        inverse = OinkInk,
        inverseHover = Color(0xFF2D2E36),
        onInverse = OinkPaper,
        accent = a,
        accentDeep = seed.deep,
        onAccent = seed.onAccent,
        accentHover = oinkMix(a, OinkInk, 0.88f),
        accentSoft = oinkMix(a, White, 0.16f),
        accentRing = oinkMix(a, White, 0.36f),
        accentText = seed.deep,
        accentTint = oinkMix(a, White, 0.26f),
        accentTint2 = oinkMix(a, White, 0.40f),
        accentFill = if (blue) a else oinkMix(a, White, 0.70f),
        accentFillHover = if (blue) oinkMix(a, OinkInk, 0.88f) else oinkMix(a, White, 0.86f),
        success = Color(0xFF2FBF71),
        successText = Color(0xFF17794D),
        successSoft = oinkMix(Color(0xFF2FBF71), White, 0.16f),
        warning = Color(0xFFF5A524),
        warningText = Color(0xFF96600A),
        warningSoft = oinkMix(Color(0xFFF5A524), White, 0.18f),
        danger = Color(0xFFF04452),
        dangerText = Color(0xFFC7283A),
        dangerSoft = oinkMix(Color(0xFFF04452), White, 0.14f),
        dangerSoft2 = oinkMix(Color(0xFFF04452), White, 0.24f),
        info = Color(0xFF3B82F6),
        infoText = Color(0xFF1D5FD1),
        infoSoft = oinkMix(Color(0xFF3B82F6), White, 0.14f),
    )
}

private fun oinkDarkColors(seed: AccentSeed): OinkColors {
    val a = seed.accent
    val blue = seed.onAccent == White
    return OinkColors(
        bg = Color(0xFF1B1A18),
        surface = Color(0xFF232220),
        card = DarkCard,
        raised = Color(0xFF3E3C38),
        well = Color(0xFF181716),
        well2 = Color(0xFF383632),
        wellHi = Color(0xFF2E2D2A),
        fill = Color(0xFF33322E),
        fillHover = Color(0xFF413F3A),
        hover = Color(0x0EFFFFFF),
        pop = Color(0xFF312F2C),
        thumb = Color(0xFFEEEEF0),
        line = Color(0x12FFFFFF),
        line2 = Color(0x26FFFFFF),
        hi = Color(0x0DFFFFFF),
        scrim = Color(0x8F000000),
        text = OinkPaper,
        text2 = Color(0xFFAEACA8),
        text3 = Color(0xFF7A7875),
        inverse = Color(0xFFEEEEF0),
        inverseHover = Color(0xFFF9F9F9),
        onInverse = OinkInk,
        accent = a,
        accentDeep = seed.deep,
        onAccent = seed.onAccent,
        accentHover = oinkMix(a, White, 0.86f),
        accentSoft = oinkMix(a, DarkCard, 0.16f),
        accentRing = oinkMix(a, DarkCard, 0.36f),
        accentText = oinkMix(a, White, 0.78f),
        accentTint = oinkMix(a, DarkCard, 0.34f),
        accentTint2 = oinkMix(a, DarkCard, 0.48f),
        accentFill = if (blue) a else oinkMix(a, White, 0.70f),
        accentFillHover = if (blue) oinkMix(a, White, 0.86f) else oinkMix(a, White, 0.56f),
        success = Color(0xFF2FBF71),
        successText = Color(0xFF5FD99A),
        successSoft = oinkMix(Color(0xFF2FBF71), DarkCard, 0.16f),
        warning = Color(0xFFF5A524),
        warningText = Color(0xFFFFC35C),
        warningSoft = oinkMix(Color(0xFFF5A524), DarkCard, 0.18f),
        danger = Color(0xFFF04452),
        dangerText = Color(0xFFFF7C87),
        dangerSoft = oinkMix(Color(0xFFF04452), DarkCard, 0.14f),
        dangerSoft2 = oinkMix(Color(0xFFF04452), DarkCard, 0.24f),
        info = Color(0xFF3B82F6),
        infoText = Color(0xFF7FB0FF),
        infoSoft = oinkMix(Color(0xFF3B82F6), DarkCard, 0.14f),
    )
}

val LocalOinkColors = staticCompositionLocalOf { oinkLightColors() }
val LocalOinkContentColor = staticCompositionLocalOf { OinkInk }