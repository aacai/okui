package zhiqiu.applewidgets.kmp.oink.responsive

import zhiqiu.applewidgets.kmp.oink.basic.*
import zhiqiu.applewidgets.kmp.oink.data.*
import zhiqiu.applewidgets.kmp.oink.form.*
import zhiqiu.applewidgets.kmp.oink.layout.*
import zhiqiu.applewidgets.kmp.oink.nav.*
import zhiqiu.applewidgets.kmp.oink.overlay.*
import zhiqiu.applewidgets.kmp.oink.primitives.*
import zhiqiu.applewidgets.kmp.oink.theme.*

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Oink 断点，数值与 oink-ui 一致。
 *
 * - [Sm]：520dp 及以上。
 * - [Md]：768dp 及以上。
 * - [Lg]：1024dp 及以上。
 * - [Xl]：1280dp 及以上。
 *
 * 供 [OinkShow] / [OinkHide] 做区间判断，本身不触发任何布局。
 *
 * @property minWidth 该断点的最小宽度
 *
 * 示例：
 * ```kotlin
 * OinkShow(above = OinkBreakpoint.Lg) { OinkText("Desktop") }
 * ```
 */
enum class OinkBreakpoint(val minWidth: Dp) {
    Sm(520.dp),
    Md(768.dp),
    Lg(1024.dp),
    Xl(1280.dp),
}

/**
 * 宽度匹配时渲染内容，等价于 oink-ui 的 Show。
 *
 * [above] 含断点本身（宽度 >= minWidth），[below] 不含（宽度 < minWidth）；两者同时给出时需同时满足，
 * 都为 null 时总是渲染。说明：Compose 侧按容器宽度判断（放在根布局即视口宽度），非窗口级查询。
 *
 * @param above 最小断点（含），null 表示不限制下限
 * @param below 最大断点（不含），null 表示不限制上限
 *
 * 示例：
 * ```kotlin
 * OinkShow(above = OinkBreakpoint.Lg) {
 *     OinkNav(items = navItems, selectedIndex = 0, onSelectedChange = { })
 * }
 * ```
 */
@Composable
fun OinkShow(
    modifier: Modifier = Modifier,
    above: OinkBreakpoint? = null,
    below: OinkBreakpoint? = null,
    content: @Composable () -> Unit,
) {
    BoxWithConstraints(modifier) {
        if (matchesWidth(maxWidth, above, below)) content()
    }
}

/**
 * 宽度不匹配时渲染内容，等价于 oink-ui 的 Hide。
 *
 * 判定条件与 [OinkShow] 取反；[above] / [below] 都为 null 时不渲染。
 *
 * @param above 最小断点（含），null 表示不限制下限
 * @param below 最大断点（不含），null 表示不限制上限
 *
 * 示例：
 * ```kotlin
 * OinkHide(above = OinkBreakpoint.Lg) {
 *     OinkButton(text = "菜单", onClick = { }, leadingIcon = OinkIcons.Menu)
 * }
 * ```
 */
@Composable
fun OinkHide(
    modifier: Modifier = Modifier,
    above: OinkBreakpoint? = null,
    below: OinkBreakpoint? = null,
    content: @Composable () -> Unit,
) {
    BoxWithConstraints(modifier) {
        if (!matchesWidth(maxWidth, above, below)) content()
    }
}

private fun matchesWidth(width: Dp, above: OinkBreakpoint?, below: OinkBreakpoint?): Boolean {
    val fitsAbove = above == null || width >= above.minWidth
    val fitsBelow = below == null || width < below.minWidth
    return fitsAbove && fitsBelow
}
