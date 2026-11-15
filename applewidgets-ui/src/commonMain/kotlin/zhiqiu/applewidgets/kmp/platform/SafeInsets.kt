package zhiqiu.applewidgets.kmp.platform

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.Modifier

/**
 * 安全区：有 Insets 就避让，没有就原样（桌面/Web 上通常等价于 no-op）。
 */
fun Modifier.safeStatusBarsPadding(): Modifier =
    if (AppleCapabilities.windowInsets) statusBarsPadding() else this

fun Modifier.safeNavigationBarsPadding(): Modifier =
    if (AppleCapabilities.windowInsets) navigationBarsPadding() else this
