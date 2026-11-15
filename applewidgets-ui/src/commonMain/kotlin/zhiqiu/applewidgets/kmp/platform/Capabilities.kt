package zhiqiu.applewidgets.kmp.platform

/**
 * 平台能力探测：有就用，没有就跳过（不崩溃、不强制依赖）。
 *
 * 组件库 API 保持 common 可用；高级效果（真实模糊、系统菜单栏等）仅在能力为 true 时启用。
 */
object AppleCapabilities {
    /** 触感反馈（手机端通常有，桌面/Web 一般为 false） */
    val haptics: Boolean get() = platformHasHaptics()

    /**
     * 真实 backdrop 采样（Kyant RuntimeShader / RenderEffect）。
     * Desktop / iOS / Wasm 通常为 true；Android 视系统版本而定。
     */
    val backdropBlur: Boolean get() = platformHasBackdropBlur()

    /**
     * 内容层 Modifier.blur（非 backdrop）。
     * 有则用于液态玻璃雾层；没有就跳过，仅保留半透明+高光。
     */
    val contentBlur: Boolean get() = platformHasContentBlur()

    /** 窗口级 Dialog / Popup */
    val windowOverlay: Boolean get() = platformHasWindowOverlay()

    /** 安全区 Insets（移动端有效，桌面/Web 通常为 0） */
    val windowInsets: Boolean get() = platformHasWindowInsets()
}

internal expect fun platformHasHaptics(): Boolean
internal expect fun platformHasBackdropBlur(): Boolean
internal expect fun platformHasContentBlur(): Boolean
internal expect fun platformHasWindowOverlay(): Boolean
internal expect fun platformHasWindowInsets(): Boolean
