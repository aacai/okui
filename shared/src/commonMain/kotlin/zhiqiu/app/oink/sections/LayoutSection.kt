package zhiqiu.app.oink

import zhiqiu.applewidgets.kmp.oink.basic.*
import zhiqiu.applewidgets.kmp.oink.data.*
import zhiqiu.applewidgets.kmp.oink.form.*
import zhiqiu.applewidgets.kmp.oink.layout.*
import zhiqiu.applewidgets.kmp.oink.nav.*
import zhiqiu.applewidgets.kmp.oink.overlay.*
import zhiqiu.applewidgets.kmp.oink.primitives.*
import zhiqiu.applewidgets.kmp.oink.responsive.*
import zhiqiu.applewidgets.kmp.oink.theme.*

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 布局补件：页头、网格、面包屑、步骤条、分页、侧栏导航、底部标签栏与可拖动分栏。
 */
@Composable
internal fun LayoutSection() {
    Column {
        SectionHeader(
            kicker = "Layout",
            title = "Pages, nav and panes",
            desc = "Page header, grid, breadcrumb, steps, pagination, sidebar nav, tab bar and a draggable split pane.",
        )
        DemoCard {
            OinkPageHeader(
                title = "Workspace",
                description = "Everything in one place.",
                actions = {
                    OinkButton(text = "New", onClick = {}, variant = OinkButtonVariant.Accent, size = OinkButtonSize.Sm)
                },
            )
            OinkBreadcrumb(items = listOf("Home", "Team", "Design"), onItemClick = {}, modifier = Modifier.padding(top = 16.dp))
            OinkSteps(
                steps = listOf(OinkStep("Account"), OinkStep("Profile"), OinkStep("Done")),
                current = 1,
                modifier = Modifier.padding(top = 20.dp),
            )
            OinkGrid(columns = 3, modifier = Modifier.padding(top = 20.dp)) {
                OinkStat(label = "Users", value = "1,204", delta = "+4.2%", deltaPositive = true)
                OinkStat(label = "Revenue", value = "$94.5k", delta = "+12.4%", deltaPositive = true)
                OinkStat(label = "Churn", value = "1.8%", delta = "-0.3%", deltaPositive = false)
            }
            OinkPagination(page = 2, pageCount = 9, onPageChange = {}, modifier = Modifier.padding(top = 20.dp))
            OinkDivider(inset = 0.dp, modifier = Modifier.padding(vertical = 20.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                var navIndex by remember { mutableStateOf(0) }
                OinkNav(
                    items = listOf(
                        OinkNavItem("Home", OinkIcons.Home),
                        OinkNavItem("People", OinkIcons.Users, badge = "3"),
                        OinkNavItem("Settings", OinkIcons.Settings),
                    ),
                    selectedIndex = navIndex,
                    onSelectedChange = { navIndex = it },
                    modifier = Modifier.width(180.dp),
                    header = "Menu",
                )
                Column(Modifier.weight(1f)) {
                    var tab by remember { mutableStateOf(0) }
                    OinkTabBar(
                        items = listOf(
                            OinkTabBarItem("Home", OinkIcons.Home),
                            OinkTabBarItem("Search", OinkIcons.Search),
                            OinkTabBarItem("Me", OinkIcons.User),
                        ),
                        selectedIndex = tab,
                        onSelectedChange = { tab = it },
                    )
                    OinkSplitPane(
                        modifier = Modifier.fillMaxWidth().height(120.dp).padding(top = 16.dp),
                        first = {
                            OinkCard(Modifier.fillMaxWidth()) {
                                OinkText("Left pane", style = OinkTheme.textStyles.body, modifier = Modifier.padding(12.dp))
                            }
                        },
                        second = {
                            OinkCard(Modifier.fillMaxWidth()) {
                                OinkText("Right pane", style = OinkTheme.textStyles.body, modifier = Modifier.padding(12.dp))
                            }
                        },
                    )
                }
            }
        }
    }
}
