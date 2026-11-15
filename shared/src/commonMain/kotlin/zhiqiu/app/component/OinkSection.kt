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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.basic.Card
import zhiqiu.applewidgets.kmp.basic.SmallTitle

/**
 * 在 Apple 风格主页里挂一块 Oink 预览，并提供进入完整 Oink Gallery 的入口。
 */
fun LazyListScope.oinkSection(onOpen: () -> Unit, dark: Boolean, accent: OinkAccent) {
    item(key = "oink") {
        SmallTitle(text = "Oink UI")
        Card(modifier = Modifier.demoCard()) {
            OinkTheme(darkTheme = dark, accent = accent) {
                Column(Modifier.fillMaxWidth().padding(16.dp)) {
                    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
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
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                    ) {
                        OinkSwitch(checked = true, onCheckedChange = {})
                        OinkButton(text = "Accent", onClick = {}, variant = OinkButtonVariant.Accent)
                        OinkButton(text = "Open Oink Gallery", onClick = onOpen, variant = OinkButtonVariant.Primary, leadingIcon = OinkIcons.External)
                    }
                }
            }
        }
    }
}