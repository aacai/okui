@file:OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)

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

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

private val sections = listOf(
    "Buttons", "Toggles", "Sliders", "Segments", "Inputs",
    "Badges", "Cards", "Progress", "People", "Accordion",
    "Feedback", "Loaders", "Overlays", "Layout", "Forms", "Data", "Overlays+", "Primitives",
)

/**
 * Oink UI 完整展示页：固定顶栏（主题 / 强调色切换）+ 侧栏导航 + 全组件实时演示。
 */
@Composable
fun OinkShowcase(
    onBack: (() -> Unit)? = null,
    initialDark: Boolean = false,
    initialAccent: OinkAccent = OinkAccent.Pink,
) {
    val controller = rememberOinkThemeController(
        colorSchemeMode = if (initialDark) OinkColorSchemeMode.Dark else OinkColorSchemeMode.Light,
        accent = initialAccent,
    )
    OinkTheme(controller) {
        val c = OinkTheme.colors
        val listState = rememberLazyListState()
        val scope = rememberCoroutineScope()
        val toastState = rememberOinkToastState()

        BoxWithConstraints(Modifier.fillMaxSize().background(c.bg)) {
            val wide = maxWidth >= 920.dp
            Column(Modifier.fillMaxSize()) {
                TopBar(
                    controller = controller,
                    onBack = onBack,
                )
                Row(Modifier.fillMaxSize()) {
                    if (wide) {
                        SideNav(
                            active = listState.firstVisibleItemIndex - 1,
                            onSelect = { scope.launch { listState.animateScrollToItem(it + 1) } },
                        )
                    }
                    Box(Modifier.weight(1f).fillMaxHeight()) {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(start = if (wide) 8.dp else 16.dp, end = 28.dp, top = 8.dp, bottom = 80.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            item(key = "hero") { Hero() }
                            item(key = "buttons") { ButtonsSection() }
                            item(key = "toggles") { TogglesSection() }
                            item(key = "sliders") { SlidersSection() }
                            item(key = "segments") { SegmentsSection() }
                            item(key = "inputs") { InputsSection() }
                            item(key = "badges") { BadgesSection() }
                            item(key = "cards") { CardsSection() }
                            item(key = "progress") { ProgressSection() }
                            item(key = "people") { PeopleSection() }
                            item(key = "accordion") { AccordionSection() }
                            item(key = "feedback") { FeedbackSection() }
                            item(key = "loaders") { LoadersSection() }
                            item(key = "overlays") { OverlaysSection(toastState) }
                            item(key = "layout") { LayoutSection() }
                            item(key = "forms") { FormsSection() }
                            item(key = "data") { DataSection() }
                            item(key = "overlaysExtra") { OverlaysExtraSection() }
                            item(key = "primitives") { PrimitivesSection() }
                        }
                        OinkVerticalScrollbar(
                            state = listState,
                            modifier = Modifier.align(Alignment.TopEnd).fillMaxHeight().padding(vertical = 8.dp, horizontal = 2.dp),
                        )
                    }
                }
            }
            OinkToastHost(state = toastState, modifier = Modifier.fillMaxSize().padding(bottom = 12.dp))
        }
    }
}

@Composable
private fun TopBar(
    controller: OinkThemeController,
    onBack: (() -> Unit)?,
) {
    val c = OinkTheme.colors
    val dark = controller.colorSchemeMode == OinkColorSchemeMode.Dark
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(c.bg)
            .border(width = 1.dp, color = c.line, shape = androidx.compose.ui.graphics.RectangleShape)
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (onBack != null) {
            OinkIconButton(icon = OinkIcons.ArrowLeft, onClick = onBack, contentDescription = "Back", variant = OinkButtonVariant.Ghost, size = OinkButtonSize.Sm)
            Spacer(Modifier.width(8.dp))
        }
        OinkLogo(size = 28.dp, animated = true)
        Spacer(Modifier.width(10.dp))
        OinkText(text = "oink", style = OinkTheme.textStyles.h4.copy(fontWeight = FontWeight.W700), color = c.text)
        Spacer(Modifier.width(8.dp))
        OinkBadge(text = "ui", variant = OinkBadgeVariant.Accent)
        Spacer(Modifier.weight(1f))
        // 强调色切换
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            OinkAccent.entries.forEach { a ->
                val swatch = oinkLightColors(a).accent
                val selected = a == controller.accent
                Box(
                    modifier = Modifier
                        .size(if (selected) 24.dp else 20.dp)
                        .clip(CircleShape)
                        .background(swatch)
                        .border(if (selected) 2.dp else 0.dp, if (selected) c.text else Color.Transparent, CircleShape)
                        .clickable { controller.accent = a },
                )
            }
        }
        Spacer(Modifier.width(16.dp))
        OinkTooltip(tooltip = if (dark) "Light mode" else "Dark mode") {
            OinkIconButton(
                icon = if (dark) OinkIcons.Sun else OinkIcons.Moon,
                onClick = {
                    controller.colorSchemeMode =
                        if (controller.colorSchemeMode == OinkColorSchemeMode.Dark) OinkColorSchemeMode.Light else OinkColorSchemeMode.Dark
                },
                contentDescription = "Toggle theme",
                variant = OinkButtonVariant.Soft,
                size = OinkButtonSize.Sm,
            )
        }
    }
}

@Composable
private fun SideNav(active: Int, onSelect: (Int) -> Unit) {
    val c = OinkTheme.colors
    Column(
        modifier = Modifier
            .width(210.dp)
            .fillMaxHeight()
            .border(width = 1.dp, color = c.line, shape = androidx.compose.ui.graphics.RectangleShape)
            .padding(horizontal = 14.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        OinkText(text = "COMPONENTS", style = OinkTheme.textStyles.micro, color = c.text3, modifier = Modifier.padding(start = 12.dp, bottom = 10.dp))
        sections.forEachIndexed { index, label ->
            val selected = index == active
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(OinkShapeNav)
                    .background(if (selected) c.accentSoft else Color.Transparent)
                    .clickable { onSelect(index) }
                    .padding(horizontal = 12.dp, vertical = 9.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OinkText(
                    text = label,
                    style = OinkTheme.textStyles.menuItem.copy(fontWeight = if (selected) FontWeight.W600 else FontWeight.W400),
                    color = if (selected) c.accentText else c.text2,
                )
            }
        }
    }
}

private val OinkShapeNav = OinkShape(OinkRadii.r2)

@Composable
private fun Hero() {
    val c = OinkTheme.colors
    Column(Modifier.fillMaxWidth().padding(vertical = 28.dp, horizontal = 8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            OinkLogo(size = 64.dp, animated = true)
            Spacer(Modifier.width(18.dp))
            Column {
                OinkText(text = "Soft, flat, quiet.", style = OinkTheme.textStyles.h1, color = c.text)
                OinkText(
                    text = "A Compose Multiplatform port of oink-ui — superellipse corners, hairline surfaces, one accent, quick springs.",
                    style = OinkTheme.textStyles.bodyLarge,
                    color = c.text2,
                    modifier = Modifier.padding(top = 6.dp).widthIn(max = 560.dp),
                )
            }
        }
        Spacer(Modifier.height(20.dp))
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OinkBadge(text = "${sections.size} families", variant = OinkBadgeVariant.Solid)
            OinkBadge(text = "light + dark", variant = OinkBadgeVariant.Neutral, dot = true)
            OinkBadge(text = "5 accents", variant = OinkBadgeVariant.Neutral, dot = true)
            OinkBadge(text = "superellipse 1.75", variant = OinkBadgeVariant.Neutral, dot = true)
        }
    }
}

@Composable
internal fun SectionHeader(kicker: String, title: String, desc: String) {
    val c = OinkTheme.colors
    Column(Modifier.fillMaxWidth().padding(top = 24.dp, bottom = 12.dp, start = 8.dp, end = 8.dp)) {
        OinkText(text = kicker.uppercase(), style = OinkTheme.textStyles.micro, color = c.accentText)
        OinkText(text = title, style = OinkTheme.textStyles.h2, color = c.text, modifier = Modifier.padding(top = 4.dp))
        OinkText(text = desc, style = OinkTheme.textStyles.body, color = c.text2, modifier = Modifier.padding(top = 4.dp).widthIn(max = 620.dp))
    }
}

@Composable
internal fun DemoCard(content: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit) {
    OinkCard(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
        contentPadding = PaddingValues(20.dp),
        content = content,
    )
}

@Composable
internal fun Label(text: String) {
    OinkText(text = text, style = OinkTheme.textStyles.caption, color = OinkTheme.colors.text3, modifier = Modifier.padding(bottom = 10.dp))
}
