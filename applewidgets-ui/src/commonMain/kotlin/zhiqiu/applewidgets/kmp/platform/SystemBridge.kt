package zhiqiu.applewidgets.kmp.platform

/**
 * 系统桥能力：有就用，没有就降级（不崩溃）。
 * Map / ShareSheet / PhotosPicker / 系统 Color&Font Panel 等。
 */
object AppleSystemBridge {
    val shareSheet: Boolean get() = platformHasShareSheet()
    val photosPicker: Boolean get() = platformHasPhotosPicker()
    val maps: Boolean get() = platformHasMaps()
    val systemColorPanel: Boolean get() = platformHasSystemColorPanel()
    val systemFontPanel: Boolean get() = platformHasSystemFontPanel()
}

internal expect fun platformHasShareSheet(): Boolean
internal expect fun platformHasPhotosPicker(): Boolean
internal expect fun platformHasMaps(): Boolean
internal expect fun platformHasSystemColorPanel(): Boolean
internal expect fun platformHasSystemFontPanel(): Boolean
