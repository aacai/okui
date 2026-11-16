package zhiqiu.applewidgets.kmp.oink.theme

import zhiqiu.applewidgets.kmp.theme.ComponentStyles

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.unit.dp

/**
 * Oink 皮肤的组件样式令牌。
 *
 * 与 Apple 默认（圆角矩形 + iOS 尺寸）并列，换主题时由 [OinkTheme] 注入，
 * applewidgets-ui 的组件无需替换即可呈现 Oink 外观。
 */
val OinkComponentStyles = ComponentStyles(
    buttonShape = CircleShape,
    buttonMinHeight = OinkMetrics.HeightMd,
    cardShape = OinkShape(OinkRadii.r3),
    controlShape = OinkShape(OinkRadii.r2),
    controlMinHeight = 36.dp,
    chipShape = CircleShape,
    chipMinHeight = OinkMetrics.HeightSm,
    overlayShape = OinkShape(OinkRadii.r4),
    sheetShape = OinkShape(
        topStart = OinkRadii.r4,
        topEnd = OinkRadii.r4,
        bottomEnd = 0.dp,
        bottomStart = 0.dp,
    ),
)
