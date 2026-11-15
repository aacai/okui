package zhiqiu.app.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.isRuntimeShaderSupported
import zhiqiu.applewidgets.kmp.basic.Card
import zhiqiu.applewidgets.kmp.basic.SmallTitle
import zhiqiu.applewidgets.kmp.basic.Text
import zhiqiu.applewidgets.kmp.layout.TabItem
import zhiqiu.applewidgets.kmp.liquid.LiquidGlassButton
import zhiqiu.applewidgets.kmp.liquid.LiquidGlassCard
import zhiqiu.applewidgets.kmp.liquid.LiquidGlassChip
import zhiqiu.applewidgets.kmp.liquid.LiquidGlassIconButton
import zhiqiu.applewidgets.kmp.liquid.LiquidGlassNavigationBar
import zhiqiu.applewidgets.kmp.liquid.LiquidGlassScene
import zhiqiu.applewidgets.kmp.liquid.LiquidGlassTone
import zhiqiu.applewidgets.kmp.liquid.LiquidGlassToolbar
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

fun LazyListScope.liquidGlassSection() {
    item(key = "glass") {
        var tab by remember { mutableStateOf(0) }
        var chip by remember { mutableStateOf(0) }
        var expanded by remember { mutableStateOf(true) }

        SmallTitle(text = "Glass")
        Card(modifier = Modifier.demoCard()) {
            zhiqiu.applewidgets.kmp.preference.ArrowPreference(
                title = "Liquid Glass Components",
                summary = if (isRuntimeShaderSupported()) {
                    "Kyant Backdrop · RuntimeShader ON"
                } else {
                    "RuntimeShader 不可用 · 半透明降级"
                },
                value = if (expanded) "收起" else "展开",
                onClick = { expanded = !expanded },
            )
        }
        if (expanded) {
            LiquidGlassScene(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp)
                    .fillMaxWidth()
                    .height(360.dp)
                    .clip(RoundedCornerShape(20.dp)),
                background = {
                    Box(
                        Modifier
                            .matchParentSize()
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        Color(0xFF5AC8FA),
                                        Color(0xFF5856D6),
                                        Color(0xFFFF2D55),
                                        Color(0xFFFFCC00),
                                    ),
                                ),
                            ),
                    )
                },
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    LiquidGlassToolbar(
                        title = "Toolbar",
                        leading = {
                            LiquidGlassIconButton(onClick = {}) {
                                Text("‹", style = AppleWidgetsTheme.textStyles.title2)
                            }
                        },
                        trailing = {
                            LiquidGlassIconButton(onClick = {}) {
                                Text("⋯", style = AppleWidgetsTheme.textStyles.title3)
                            }
                        },
                    )
                    LiquidGlassCard(
                        modifier = Modifier.fillMaxWidth().height(72.dp),
                        tone = LiquidGlassTone.Regular,
                    ) {
                        Column(
                            Modifier
                                .align(Alignment.CenterStart)
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            Text("LiquidGlassCard", style = AppleWidgetsTheme.textStyles.headline)
                            Text(
                                "vibrancy · blur · lens",
                                style = AppleWidgetsTheme.textStyles.footnote,
                                color = AppleWidgetsTheme.colorScheme.secondaryLabel,
                            )
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        LiquidGlassButton(text = "Pill", onClick = {})
                        LiquidGlassChip(text = "Thin", selected = chip == 0, onClick = { chip = 0 })
                        LiquidGlassChip(text = "Clear", selected = chip == 1, onClick = { chip = 1 })
                    }
                    LiquidGlassNavigationBar(
                        tabs = listOf(
                            TabItem("主页", "⌂"),
                            TabItem("浏览", "⌕"),
                            TabItem("我的", "☺"),
                        ),
                        selectedIndex = tab,
                        onSelect = { tab = it },
                    )
                }
            }
        }
    }
}
