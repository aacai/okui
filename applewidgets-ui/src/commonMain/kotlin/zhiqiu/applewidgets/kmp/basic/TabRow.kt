package zhiqiu.applewidgets.kmp.basic

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

@Composable
fun TabRow(
    tabs: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    colors: TabRowColors = TabRowDefaults.colors(),
) {
    val impl = LocalTabRowImpl.current
    if (impl != null) {
        impl(TabRowParams(tabs, selectedIndex, onSelect, modifier, colors))
        return
    }
    Column(modifier = modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth()) {
            tabs.forEachIndexed { index, title ->
                val selected = index == selectedIndex
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onSelect(index) }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = title,
                        style = AppleWidgetsTheme.textStyles.body,
                        color = if (selected) colors.selectedContentColor else colors.unselectedContentColor,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
        Box(Modifier.fillMaxWidth().height(0.5.dp).background(colors.dividerColor))
        Row(Modifier.fillMaxWidth()) {
            tabs.forEachIndexed { index, _ ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(2.dp)
                        .background(if (index == selectedIndex) colors.indicatorColor else Color.Transparent),
                )
            }
        }
    }
}

object TabRowDefaults {
    @Composable
    fun colors(
        selectedContentColor: Color = AppleWidgetsTheme.colorScheme.label,
        unselectedContentColor: Color = AppleWidgetsTheme.colorScheme.secondaryLabel,
        indicatorColor: Color = AppleWidgetsTheme.colorScheme.accent,
        dividerColor: Color = AppleWidgetsTheme.colorScheme.separator,
    ): TabRowColors = TabRowColors(
        selectedContentColor,
        unselectedContentColor,
        indicatorColor,
        dividerColor,
    )
}

@Immutable
data class TabRowColors(
    val selectedContentColor: Color,
    val unselectedContentColor: Color,
    val indicatorColor: Color,
    val dividerColor: Color,
)

/** 标签栏实现覆盖参数。 */
@Immutable
data class TabRowParams(
    val tabs: List<String>,
    val selectedIndex: Int,
    val onSelect: (Int) -> Unit,
    val modifier: Modifier,
    val colors: TabRowColors,
)

/** 主题可替换的标签栏实现；null 表示使用组件库默认实现。 */
val LocalTabRowImpl = compositionLocalOf<(@Composable (TabRowParams) -> Unit)?> { null }
