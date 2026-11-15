package zhiqiu.applewidgets.kmp.layout

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import zhiqiu.applewidgets.kmp.basic.IconButton
import zhiqiu.applewidgets.kmp.basic.Text
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

@Stable
class NavigationController<T>(initial: T) {
    var stack by mutableStateOf(listOf(initial))
        private set

    val current: T get() = stack.last()
    val canPop: Boolean get() = stack.size > 1

    fun push(destination: T) {
        stack = stack + destination
    }

    fun pop() {
        if (canPop) stack = stack.dropLast(1)
    }

    fun replaceAll(destination: T) {
        stack = listOf(destination)
    }
}

@Composable
fun <T> rememberNavigationController(initial: T): NavigationController<T> =
    remember { NavigationController(initial) }

@Composable
fun <T> NavigationStack(
    controller: NavigationController<T>,
    modifier: Modifier = Modifier,
    titleOf: (T) -> String = { it.toString() },
    content: @Composable (destination: T) -> Unit,
) {
    Column(modifier = modifier.fillMaxSize()) {
        NavigationBar(
            title = titleOf(controller.current),
            navigationIcon = if (controller.canPop) {
                {
                    IconButton(onClick = { controller.pop() }) {
                        Text("‹", style = AppleWidgetsTheme.textStyles.title1)
                    }
                }
            } else {
                null
            },
        )
        AnimatedContent(
            targetState = controller.current,
            transitionSpec = {
                (slideInHorizontally(tween(280)) { it / 3 } + fadeIn(tween(220))) togetherWith
                    (slideOutHorizontally(tween(280)) { -it / 5 } + fadeOut(tween(180)))
            },
            label = "nav-stack",
            modifier = Modifier.weight(1f).fillMaxSize(),
        ) { dest ->
            Box(Modifier.fillMaxSize()) {
                content(dest)
            }
        }
    }
}
