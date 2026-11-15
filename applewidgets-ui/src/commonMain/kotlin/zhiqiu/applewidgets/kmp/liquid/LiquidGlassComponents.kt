package zhiqiu.applewidgets.kmp.liquid

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.basic.Text
import zhiqiu.applewidgets.kmp.layout.TabItem
import zhiqiu.applewidgets.kmp.platform.safeNavigationBarsPadding
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme
import zhiqiu.applewidgets.kmp.theme.LocalContentColor

@Composable
fun LiquidGlassButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    tone: LiquidGlassTone = LiquidGlassTone.Thin,
    content: @Composable RowScope.() -> Unit,
) {
    val interaction = remember { MutableInteractionSource() }
    LiquidGlassPill(
        modifier = modifier
            .defaultMinSize(minHeight = 44.dp)
            .graphicsLayer { alpha = if (enabled) 1f else 0.45f }
            .clickable(
                interactionSource = interaction,
                indication = null,
                enabled = enabled,
                role = Role.Button,
                onClick = onClick,
            )
            .padding(horizontal = 16.dp, vertical = 10.dp),
        tone = tone,
    ) {
        Row(
            modifier = Modifier.align(Alignment.Center),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            content = content,
        )
    }
}

@Composable
fun LiquidGlassButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    LiquidGlassButton(onClick = onClick, modifier = modifier, enabled = enabled) {
        Text(text = text, style = AppleWidgetsTheme.textStyles.headline, color = LocalContentColor.current)
    }
}

@Composable
fun LiquidGlassIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    LiquidGlass(
        modifier = modifier
            .size(44.dp)
            .clickable(role = Role.Button, onClick = onClick),
        style = LiquidGlassDefaults.style(tone = LiquidGlassTone.Thin, cornerRadius = 22.dp),
    ) {
        Box(Modifier.align(Alignment.Center), contentAlignment = Alignment.Center) {
            content()
        }
    }
}

@Composable
fun LiquidGlassToolbar(
    modifier: Modifier = Modifier,
    title: String? = null,
    leading: @Composable RowScope.() -> Unit = {},
    trailing: @Composable RowScope.() -> Unit = {},
) {
    LiquidGlass(
        modifier = modifier.fillMaxWidth(),
        style = LiquidGlassDefaults.style(tone = LiquidGlassTone.Regular, cornerRadius = 0.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), content = leading)
            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                if (title != null) {
                    Text(title, style = AppleWidgetsTheme.textStyles.headline)
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), content = trailing)
        }
    }
}

@Composable
fun LiquidGlassNavigationBar(
    tabs: List<TabItem>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .safeNavigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp),
    ) {
        LiquidGlassPill(
            modifier = Modifier.fillMaxWidth(),
            tone = LiquidGlassTone.Regular,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                tabs.forEachIndexed { index, tab ->
                    val selected = index == selectedIndex
                    val tint = if (selected) {
                        AppleWidgetsTheme.colorScheme.accent
                    } else {
                        AppleWidgetsTheme.colorScheme.secondaryLabel
                    }
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .selectable(
                                selected = selected,
                                onClick = { onSelect(index) },
                                role = Role.Tab,
                            )
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        // selected capsule
                        if (selected) {
                            LiquidGlass(
                                modifier = Modifier.padding(horizontal = 4.dp),
                                style = LiquidGlassDefaults.style(
                                    tone = LiquidGlassTone.Thin,
                                    cornerRadius = 18.dp,
                                ),
                            ) {
                                Row(
                                    Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(tab.icon, color = tint, style = AppleWidgetsTheme.textStyles.callout)
                                    Text(tab.title, color = tint, style = AppleWidgetsTheme.textStyles.caption1)
                                }
                            }
                        } else {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(tab.icon, color = tint, style = AppleWidgetsTheme.textStyles.callout)
                                Text(tab.title, color = tint, style = AppleWidgetsTheme.textStyles.caption1)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LiquidGlassChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LiquidGlassButton(
        onClick = onClick,
        modifier = modifier,
        tone = if (selected) LiquidGlassTone.Regular else LiquidGlassTone.Clear,
    ) {
        Text(
            text = text,
            style = AppleWidgetsTheme.textStyles.footnote,
            color = if (selected) {
                AppleWidgetsTheme.colorScheme.accent
            } else {
                LocalContentColor.current
            },
        )
    }
}
