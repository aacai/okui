package zhiqiu.applewidgets.kmp.oink.theme

import zhiqiu.applewidgets.kmp.oink.basic.*
import zhiqiu.applewidgets.kmp.oink.data.*
import zhiqiu.applewidgets.kmp.oink.form.*
import zhiqiu.applewidgets.kmp.oink.layout.*
import zhiqiu.applewidgets.kmp.oink.nav.*
import zhiqiu.applewidgets.kmp.oink.overlay.*
import zhiqiu.applewidgets.kmp.oink.primitives.*
import zhiqiu.applewidgets.kmp.oink.responsive.*

import zhiqiu.applewidgets.kmp.theme.Colors
import zhiqiu.applewidgets.kmp.theme.TextStyles

/**
 * 把 Oink 令牌映射到 AppleWidgets 语义槽，让 basic/preference/layout 组件直接套 OinkUI 皮肤。
 */
fun oinkAppleColors(c: OinkColors): Colors = Colors(
    accent = c.accent,
    onAccent = c.onAccent,
    label = c.text,
    secondaryLabel = c.text2,
    tertiaryLabel = c.text3,
    quaternaryLabel = oinkMix(c.text3, c.bg, 0.55f),
    placeholderText = c.text3,
    systemBackground = c.bg,
    secondarySystemBackground = c.surface,
    tertiarySystemBackground = c.card,
    systemGroupedBackground = c.bg,
    secondarySystemGroupedBackground = c.card,
    tertiarySystemGroupedBackground = c.well,
    fill = c.fill,
    secondaryFill = oinkMix(c.fill, c.card, 0.65f),
    tertiaryFill = oinkMix(c.fill, c.card, 0.4f),
    quaternaryFill = c.hover,
    separator = c.line2,
    opaqueSeparator = oinkMix(c.line2, c.bg, c.line2.alpha),
    link = c.accentText,
    destructive = c.danger,
    success = c.success,
    warning = c.warning,
    switchTrackOff = c.well2,
    switchTrackOn = c.accent,
    windowDimming = c.scrim,
)

fun oinkAppleTextStyles(t: OinkTextStyles): TextStyles = TextStyles(
    largeTitle = t.h1,
    title1 = t.h2,
    title2 = t.h3,
    title3 = t.h4,
    headline = t.h4,
    body = t.body,
    callout = t.body,
    subheadline = t.rowTitle,
    footnote = t.caption,
    caption1 = t.rowDesc,
    caption2 = t.micro,
)