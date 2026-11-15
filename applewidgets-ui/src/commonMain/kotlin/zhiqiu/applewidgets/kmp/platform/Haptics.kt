package zhiqiu.applewidgets.kmp.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback

/**
 * 有触感就触发，没有就空操作。
 */
fun HapticFeedback.performToggleHaptic(on: Boolean) {
    if (!AppleCapabilities.haptics) return
    performHapticFeedback(
        if (on) HapticFeedbackType.ToggleOn else HapticFeedbackType.ToggleOff,
    )
}

fun HapticFeedback.performClickHaptic() {
    if (!AppleCapabilities.haptics) return
    performHapticFeedback(HapticFeedbackType.TextHandleMove)
}

@Composable
fun rememberHapticFeedbackOrNull(): HapticFeedback? =
    if (AppleCapabilities.haptics) LocalHapticFeedback.current else null
