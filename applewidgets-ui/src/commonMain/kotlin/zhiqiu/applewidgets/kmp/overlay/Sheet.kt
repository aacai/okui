package zhiqiu.applewidgets.kmp.overlay

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.Immutable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.liquid.LiquidGlassScrim
import zhiqiu.applewidgets.kmp.platform.safeNavigationBarsPadding
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

enum class SheetDetent {
    Medium,
    Large,
}

@Composable
fun Sheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    detent: SheetDetent = SheetDetent.Medium,
    content: @Composable ColumnScope.() -> Unit,
) {
    val impl = LocalSheetImpl.current
    if (impl != null) {
        impl(SheetParams(onDismissRequest, modifier, detent), content)
        return
    }
    val colors = AppleWidgetsTheme.colorScheme
    val heightFraction = when (detent) {
        SheetDetent.Medium -> 0.5f
        SheetDetent.Large -> 0.92f
    }
    PlatformDialogOverlay(onDismissRequest = onDismissRequest) {
        LiquidGlassScrim(
            color = colors.windowDimming,
            onClick = onDismissRequest,
            contentAlignment = Alignment.BottomCenter,
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight(heightFraction)
                    .clip(AppleWidgetsTheme.componentStyles.sheetShape)
                    .background(colors.secondarySystemGroupedBackground)
                    .safeNavigationBarsPadding()
                    .padding(bottom = 8.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.12f)
                            .height(5.dp)
                            .clip(RoundedCornerShape(2.5.dp))
                            .background(colors.tertiaryFill),
                    )
                }
                content()
            }
        }
    }
}

/** 底部弹层实现覆盖参数。 */
@Immutable
data class SheetParams(
    val onDismissRequest: () -> Unit,
    val modifier: Modifier,
    val detent: SheetDetent,
)

/** 主题可替换的底部弹层实现；null 表示使用组件库默认实现。 */
val LocalSheetImpl = compositionLocalOf<(@Composable (SheetParams, @Composable ColumnScope.() -> Unit) -> Unit)?> { null }
