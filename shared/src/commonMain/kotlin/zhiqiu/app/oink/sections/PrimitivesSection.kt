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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 原语补件：表面三档、Box、比例容器、列表、响应式显隐。
 */
@Composable
internal fun PrimitivesSection() {
    Column {
        SectionHeader(
            kicker = "Primitives",
            title = "Surfaces and shells",
            desc = "Surface tones, box, aspect ratio, list, sticky header and responsive show / hide.",
        )
        DemoCard {
            Label("Surface")
            OinkInline(spacing = 12.dp) {
                OinkSurface(variant = OinkSurfaceVariant.Plain, contentPadding = PaddingValues(12.dp)) {
                    OinkText("Plain", color = OinkTheme.colors.text2, style = OinkTheme.textStyles.caption)
                }
                OinkSurface(variant = OinkSurfaceVariant.Raised, contentPadding = PaddingValues(12.dp)) {
                    OinkText("Raised", color = OinkTheme.colors.text2, style = OinkTheme.textStyles.caption)
                }
                OinkSurface(variant = OinkSurfaceVariant.Well, contentPadding = PaddingValues(12.dp)) {
                    OinkText("Well", color = OinkTheme.colors.text2, style = OinkTheme.textStyles.caption)
                }
            }
            Label("Aspect ratio")
            OinkAspectRatio(ratio = 16f / 9f, modifier = Modifier.fillMaxWidth()) {
                OinkBox(Modifier.fillMaxSize(), tone = OinkSurfaceVariant.Well, padding = PaddingValues(12.dp)) {
                    OinkText("16:9", color = OinkTheme.colors.text3, style = OinkTheme.textStyles.caption)
                }
            }
            Label("List")
            val tasks = listOf("Homepage redesign", "Dark theme", "Ship 1.0")
            OinkList(count = tasks.size, maxHeight = 168.dp) { index ->
                OinkRow(title = tasks[index], description = "Owner ${index + 1}")
            }
            Label("Responsive")
            OinkShow(above = OinkBreakpoint.Sm) {
                OinkText("Wide viewport", style = OinkTheme.textStyles.body)
            }
            OinkHide(above = OinkBreakpoint.Sm) {
                OinkText("Narrow viewport", style = OinkTheme.textStyles.body)
            }
        }
    }
}
