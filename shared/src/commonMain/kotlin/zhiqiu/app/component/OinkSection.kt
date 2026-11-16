package zhiqiu.app.component

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.basic.Button
import zhiqiu.applewidgets.kmp.basic.ButtonStyle
import zhiqiu.applewidgets.kmp.basic.Card
import zhiqiu.applewidgets.kmp.basic.Chip
import zhiqiu.applewidgets.kmp.basic.SmallTitle
import zhiqiu.applewidgets.kmp.basic.Switch
import zhiqiu.applewidgets.kmp.basic.Text
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

/**
 * 在主页挂一块 Oink 预览，并提供进入完整 Oink Gallery 的入口。
 * 预览卡跟随当前主题：Oink 主题下用 Oink 组件，Apple 主题下用 Apple 组件。
 */
fun LazyListScope.oinkSection(
    onOpen: () -> Unit,
    dark: Boolean,
    accent: OinkAccent,
    oinkTheme: Boolean,
) {
    item(key = "oink") {
        SmallTitle(text = "Oink UI")
        if (oinkTheme) {
            Card(modifier = Modifier.demoCard()) {
                OinkTheme(darkTheme = dark, accent = accent) {
                    OinkPreviewBody(onOpen = onOpen)
                }
            }
        } else {
            Card(modifier = Modifier.demoCard()) {
                ApplePreviewBody(onOpen = onOpen, dark = dark, accent = accent)
            }
        }
    }
}

@Composable
private fun OinkPreviewBody(onOpen: () -> Unit) {
    Column(Modifier.fillMaxWidth().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            OinkLogo(size = 36.dp, animated = true)
            Column(Modifier.padding(start = 12.dp).weight(1f)) {
                OinkText("oink-ui for Compose", style = OinkTheme.textStyles.h4, color = OinkTheme.colors.text)
                OinkText(
                    "Soft, flat, quiet — superellipse corners, hairline surfaces, one accent.",
                    style = OinkTheme.textStyles.caption,
                    color = OinkTheme.colors.text2,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
            OinkBadge(text = "new", variant = OinkBadgeVariant.Solid)
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OinkSwitch(checked = true, onCheckedChange = {})
            OinkButton(text = "Accent", onClick = {}, variant = OinkButtonVariant.Accent)
            OinkButton(text = "Open Oink Gallery", onClick = onOpen, variant = OinkButtonVariant.Primary, leadingIcon = OinkIcons.External)
        }
    }
}

@Composable
private fun ApplePreviewBody(onOpen: () -> Unit, dark: Boolean, accent: OinkAccent) {
    val c = AppleWidgetsTheme.colorScheme
    Column(Modifier.fillMaxWidth().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            OinkTheme(darkTheme = dark, accent = accent) {
                OinkLogo(size = 36.dp, animated = true)
            }
            Column(Modifier.padding(start = 12.dp).weight(1f)) {
                Text(
                    text = "oink-ui for Compose",
                    style = AppleWidgetsTheme.textStyles.title3,
                    color = c.label,
                )
                Text(
                    text = "Soft, flat, quiet — superellipse corners, hairline surfaces, one accent.",
                    style = AppleWidgetsTheme.textStyles.footnote,
                    color = c.secondaryLabel,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
            Chip(text = "new", onClick = {}, selected = true)
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Switch(checked = true, onCheckedChange = {})
            Button(text = "Accent", onClick = {}, style = ButtonStyle.Filled)
            Button(text = "Open Oink Gallery", onClick = onOpen, style = ButtonStyle.Plain)
        }
    }
}
