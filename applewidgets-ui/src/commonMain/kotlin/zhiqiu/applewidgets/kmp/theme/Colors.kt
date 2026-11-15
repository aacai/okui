package zhiqiu.applewidgets.kmp.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.graphics.Color

/**
 * iOS Human Interface 语义色板。
 */
@Stable
class Colors(
    accent: Color,
    onAccent: Color,
    label: Color,
    secondaryLabel: Color,
    tertiaryLabel: Color,
    quaternaryLabel: Color,
    placeholderText: Color,
    systemBackground: Color,
    secondarySystemBackground: Color,
    tertiarySystemBackground: Color,
    systemGroupedBackground: Color,
    secondarySystemGroupedBackground: Color,
    tertiarySystemGroupedBackground: Color,
    fill: Color,
    secondaryFill: Color,
    tertiaryFill: Color,
    quaternaryFill: Color,
    separator: Color,
    opaqueSeparator: Color,
    link: Color,
    destructive: Color,
    success: Color,
    warning: Color,
    switchTrackOff: Color,
    switchTrackOn: Color,
    windowDimming: Color,
) {
    var accent by mutableStateOf(accent, structuralEqualityPolicy())
        internal set
    var onAccent by mutableStateOf(onAccent, structuralEqualityPolicy())
        internal set
    var label by mutableStateOf(label, structuralEqualityPolicy())
        internal set
    var secondaryLabel by mutableStateOf(secondaryLabel, structuralEqualityPolicy())
        internal set
    var tertiaryLabel by mutableStateOf(tertiaryLabel, structuralEqualityPolicy())
        internal set
    var quaternaryLabel by mutableStateOf(quaternaryLabel, structuralEqualityPolicy())
        internal set
    var placeholderText by mutableStateOf(placeholderText, structuralEqualityPolicy())
        internal set
    var systemBackground by mutableStateOf(systemBackground, structuralEqualityPolicy())
        internal set
    var secondarySystemBackground by mutableStateOf(secondarySystemBackground, structuralEqualityPolicy())
        internal set
    var tertiarySystemBackground by mutableStateOf(tertiarySystemBackground, structuralEqualityPolicy())
        internal set
    var systemGroupedBackground by mutableStateOf(systemGroupedBackground, structuralEqualityPolicy())
        internal set
    var secondarySystemGroupedBackground by mutableStateOf(secondarySystemGroupedBackground, structuralEqualityPolicy())
        internal set
    var tertiarySystemGroupedBackground by mutableStateOf(tertiarySystemGroupedBackground, structuralEqualityPolicy())
        internal set
    var fill by mutableStateOf(fill, structuralEqualityPolicy())
        internal set
    var secondaryFill by mutableStateOf(secondaryFill, structuralEqualityPolicy())
        internal set
    var tertiaryFill by mutableStateOf(tertiaryFill, structuralEqualityPolicy())
        internal set
    var quaternaryFill by mutableStateOf(quaternaryFill, structuralEqualityPolicy())
        internal set
    var separator by mutableStateOf(separator, structuralEqualityPolicy())
        internal set
    var opaqueSeparator by mutableStateOf(opaqueSeparator, structuralEqualityPolicy())
        internal set
    var link by mutableStateOf(link, structuralEqualityPolicy())
        internal set
    var destructive by mutableStateOf(destructive, structuralEqualityPolicy())
        internal set
    var success by mutableStateOf(success, structuralEqualityPolicy())
        internal set
    var warning by mutableStateOf(warning, structuralEqualityPolicy())
        internal set
    var switchTrackOff by mutableStateOf(switchTrackOff, structuralEqualityPolicy())
        internal set
    var switchTrackOn by mutableStateOf(switchTrackOn, structuralEqualityPolicy())
        internal set
    var windowDimming by mutableStateOf(windowDimming, structuralEqualityPolicy())
        internal set

    fun copy(
        accent: Color = this.accent,
        onAccent: Color = this.onAccent,
        label: Color = this.label,
        secondaryLabel: Color = this.secondaryLabel,
        tertiaryLabel: Color = this.tertiaryLabel,
        quaternaryLabel: Color = this.quaternaryLabel,
        placeholderText: Color = this.placeholderText,
        systemBackground: Color = this.systemBackground,
        secondarySystemBackground: Color = this.secondarySystemBackground,
        tertiarySystemBackground: Color = this.tertiarySystemBackground,
        systemGroupedBackground: Color = this.systemGroupedBackground,
        secondarySystemGroupedBackground: Color = this.secondarySystemGroupedBackground,
        tertiarySystemGroupedBackground: Color = this.tertiarySystemGroupedBackground,
        fill: Color = this.fill,
        secondaryFill: Color = this.secondaryFill,
        tertiaryFill: Color = this.tertiaryFill,
        quaternaryFill: Color = this.quaternaryFill,
        separator: Color = this.separator,
        opaqueSeparator: Color = this.opaqueSeparator,
        link: Color = this.link,
        destructive: Color = this.destructive,
        success: Color = this.success,
        warning: Color = this.warning,
        switchTrackOff: Color = this.switchTrackOff,
        switchTrackOn: Color = this.switchTrackOn,
        windowDimming: Color = this.windowDimming,
    ): Colors = Colors(
        accent, onAccent, label, secondaryLabel, tertiaryLabel, quaternaryLabel, placeholderText,
        systemBackground, secondarySystemBackground, tertiarySystemBackground,
        systemGroupedBackground, secondarySystemGroupedBackground, tertiarySystemGroupedBackground,
        fill, secondaryFill, tertiaryFill, quaternaryFill,
        separator, opaqueSeparator, link, destructive, success, warning, switchTrackOff, switchTrackOn, windowDimming,
    )

    fun updateColorsFrom(other: Colors) {
        accent = other.accent
        onAccent = other.onAccent
        label = other.label
        secondaryLabel = other.secondaryLabel
        tertiaryLabel = other.tertiaryLabel
        quaternaryLabel = other.quaternaryLabel
        placeholderText = other.placeholderText
        systemBackground = other.systemBackground
        secondarySystemBackground = other.secondarySystemBackground
        tertiarySystemBackground = other.tertiarySystemBackground
        systemGroupedBackground = other.systemGroupedBackground
        secondarySystemGroupedBackground = other.secondarySystemGroupedBackground
        tertiarySystemGroupedBackground = other.tertiarySystemGroupedBackground
        fill = other.fill
        secondaryFill = other.secondaryFill
        tertiaryFill = other.tertiaryFill
        quaternaryFill = other.quaternaryFill
        separator = other.separator
        opaqueSeparator = other.opaqueSeparator
        link = other.link
        destructive = other.destructive
        success = other.success
        warning = other.warning
        switchTrackOff = other.switchTrackOff
        switchTrackOn = other.switchTrackOn
        windowDimming = other.windowDimming
    }
}

fun lightColorScheme(
    accent: Color = Color(0xFF007AFF),
): Colors = Colors(
    accent = accent,
    onAccent = Color.White,
    label = Color(0xFF000000),
    secondaryLabel = Color(0x993C3C43),
    tertiaryLabel = Color(0x4D3C3C43),
    quaternaryLabel = Color(0x2E3C3C43),
    placeholderText = Color(0x4D3C3C43),
    systemBackground = Color(0xFFFFFFFF),
    secondarySystemBackground = Color(0xFFF2F2F7),
    tertiarySystemBackground = Color(0xFFFFFFFF),
    systemGroupedBackground = Color(0xFFF2F2F7),
    secondarySystemGroupedBackground = Color(0xFFFFFFFF),
    tertiarySystemGroupedBackground = Color(0xFFF2F2F7),
    fill = Color(0x33787880),
    secondaryFill = Color(0x29787880),
    tertiaryFill = Color(0x1F787880),
    quaternaryFill = Color(0x14787880),
    separator = Color(0x493C3C43),
    opaqueSeparator = Color(0xFFC6C6C8),
    link = accent,
    destructive = Color(0xFFFF3B30),
    success = Color(0xFF34C759),
    warning = Color(0xFFFF9500),
    switchTrackOff = Color(0xFFE9E9EB),
    switchTrackOn = Color(0xFF34C759),
    windowDimming = Color(0x66000000),
)

fun darkColorScheme(
    accent: Color = Color(0xFF0A84FF),
): Colors = Colors(
    accent = accent,
    onAccent = Color.White,
    label = Color(0xFFFFFFFF),
    secondaryLabel = Color(0x99EBEBF5),
    tertiaryLabel = Color(0x4DEBEBF5),
    quaternaryLabel = Color(0x2EEBEBF5),
    placeholderText = Color(0x4DEBEBF5),
    systemBackground = Color(0xFF000000),
    secondarySystemBackground = Color(0xFF1C1C1E),
    tertiarySystemBackground = Color(0xFF2C2C2E),
    systemGroupedBackground = Color(0xFF000000),
    secondarySystemGroupedBackground = Color(0xFF1C1C1E),
    tertiarySystemGroupedBackground = Color(0xFF2C2C2E),
    fill = Color(0x5C787880),
    secondaryFill = Color(0x52787880),
    tertiaryFill = Color(0x3D787880),
    quaternaryFill = Color(0x2E787880),
    separator = Color(0xA6545458),
    opaqueSeparator = Color(0xFF38383A),
    link = accent,
    destructive = Color(0xFFFF453A),
    success = Color(0xFF30D158),
    warning = Color(0xFFFF9F0A),
    switchTrackOff = Color(0xFF39393D),
    switchTrackOn = Color(0xFF30D158),
    windowDimming = Color(0x99000000),
)

val LocalColors = staticCompositionLocalOf { lightColorScheme() }
