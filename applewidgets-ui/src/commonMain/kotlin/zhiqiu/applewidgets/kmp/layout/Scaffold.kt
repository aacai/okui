package zhiqiu.applewidgets.kmp.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.liquid.LocalLiquidBackdrop
import zhiqiu.applewidgets.kmp.liquid.liquidBackdropSource
import zhiqiu.applewidgets.kmp.liquid.rememberLiquidBackdrop
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

@Composable
fun Scaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable (() -> Unit)? = null,
    bottomBar: @Composable (() -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit,
) {
    val liquidBackdrop = rememberLiquidBackdrop()
    CompositionLocalProvider(LocalLiquidBackdrop provides liquidBackdrop) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(AppleWidgetsTheme.colorScheme.systemGroupedBackground)
                .liquidBackdropSource(liquidBackdrop),
        ) {
            topBar?.invoke()
            Box(modifier = Modifier.weight(1f).fillMaxSize()) {
                content(PaddingValues(0.dp))
            }
            bottomBar?.invoke()
        }
    }
}

@Composable
fun ScaffoldOverlayHost(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
    overlay: @Composable () -> Unit = {},
) {
    Box(modifier = modifier.fillMaxSize()) {
        content()
        Box(modifier = Modifier.fillMaxSize().align(Alignment.Center)) {
            overlay()
        }
    }
}
