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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 数据补件：表格、图片、统计、徽标计数、描述列表、时间线、树、代码与表情反馈。
 */
@Composable
internal fun DataSection() {
    Column {
        SectionHeader(
            kicker = "Data",
            title = "Tables and records",
            desc = "Table, image frame, stat with sparkline, count, description list, timeline, tree, code and reactions.",
        )
        DemoCard {
            OinkTable(
                headers = listOf("Task", "Owner", "Status"),
                rows = listOf(
                    listOf("Homepage redesign", "Lena Hart", "In progress"),
                    listOf("Dark theme", "Owen Brooks", "Done"),
                    listOf("Accessibility review", "Mia Chen", "To do"),
                ),
            )
            Label("Image")
            OinkImage(modifier = Modifier.fillMaxWidth())
            Label("Stat")
            OinkStat(
                label = "Net revenue",
                value = "$94,500",
                delta = "+12.4%",
                deltaPositive = true,
                trend = listOf(0.2f, 0.35f, 0.28f, 0.5f, 0.44f, 0.7f, 0.86f),
            )
            Label("Description list")
            OinkDescriptionList(
                items = listOf(
                    OinkDescriptionItem("Plan", "Team"),
                    OinkDescriptionItem("Seats", "12 of 50"),
                    OinkDescriptionItem("Renews", "2026-12-01"),
                ),
            )
            Label("Timeline")
            OinkTimeline(
                items = listOf(
                    OinkTimelineItem("Order placed", time = "09:12", done = true),
                    OinkTimelineItem("Packed", time = "11:40", description = "Warehouse 2", done = true),
                    OinkTimelineItem("Shipped", time = "—"),
                ),
            )
            Label("Tree")
            OinkTree(
                nodes = listOf(
                    OinkTreeNode(
                        label = "src",
                        children = listOf(
                            OinkTreeNode("components", children = listOf(OinkTreeNode("Button.tsx"), OinkTreeNode("Input.tsx"))),
                            OinkTreeNode("index.ts"),
                        ),
                    ),
                ),
            )
            Label("Code")
            OinkCode(code = "val greeting = \"soft, flat, quiet\"", language = "kotlin")
            Label("Reactions")
            OinkInline(spacing = 8.dp) {
                OinkReaction(emoji = "👍", count = 24, selected = true, onClick = {})
                OinkReaction(emoji = "🎉", count = 8, selected = false, onClick = {})
                OinkCount(value = 12)
            }
        }
    }
}
