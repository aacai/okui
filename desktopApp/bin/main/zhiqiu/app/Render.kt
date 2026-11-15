package zhiqiu.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.oink.OinkAccent
import zhiqiu.applewidgets.kmp.oink.OinkBadge
import zhiqiu.applewidgets.kmp.oink.OinkBadgeVariant
import zhiqiu.applewidgets.kmp.oink.OinkButton
import zhiqiu.applewidgets.kmp.oink.OinkButtonState
import zhiqiu.applewidgets.kmp.oink.OinkButtonVariant
import zhiqiu.applewidgets.kmp.oink.OinkCard
import zhiqiu.applewidgets.kmp.oink.OinkCheckbox
import zhiqiu.applewidgets.kmp.oink.OinkChip
import zhiqiu.applewidgets.kmp.oink.OinkDialog
import zhiqiu.applewidgets.kmp.oink.OinkIcons
import zhiqiu.applewidgets.kmp.oink.OinkIcon
import zhiqiu.applewidgets.kmp.oink.OinkLogo
import zhiqiu.applewidgets.kmp.oink.OinkProgress
import zhiqiu.applewidgets.kmp.oink.OinkRadio
import zhiqiu.applewidgets.kmp.oink.OinkRing
import zhiqiu.applewidgets.kmp.oink.OinkSegmented
import zhiqiu.applewidgets.kmp.oink.OinkSlider
import zhiqiu.applewidgets.kmp.oink.OinkSwitch
import zhiqiu.applewidgets.kmp.oink.OinkTabs
import zhiqiu.applewidgets.kmp.oink.OinkText
import zhiqiu.applewidgets.kmp.oink.OinkTextField
import zhiqiu.applewidgets.kmp.oink.OinkTheme
import zhiqiu.app.oink.OinkShowcase
import java.io.File

private val outDir = File("/tmp/oink-render").apply { mkdirs() }

private fun snap(name: String, width: Int, height: Int, content: @Composable () -> Unit) {
    val scene = ImageComposeScene(width, height, Density(2f), kotlinx.coroutines.Dispatchers.Unconfined) {
        content()
    }
    try {
        var t = 0L
        repeat(48) {
            scene.render(t)
            t += 16_000_000L
        }
        val image = scene.render(2_000_000_000L)
        val data = image.encodeToData() ?: error("encode failed for $name")
        val file = File(outDir, "$name.png")
        file.writeBytes(data.bytes)
        println("wrote ${file.absolutePath} (${width}x$height)")
    } finally {
        scene.close()
    }
}

fun main() {
    System.setProperty("java.awt.headless", "true")

    snap("showcase-light", 2560, 3400) { OinkShowcase() }
    snap("showcase-narrow", 1600, 1100) { OinkShowcase() }
    snap("showcase-dark", 2560, 3400) { OinkShowcase(initialDark = true, initialAccent = OinkAccent.Violet) }

    snap("components-light", 1520, 3000) { OinkTheme(darkTheme = false, accent = OinkAccent.Pink) { ComponentBoard() } }
    snap("components-dark", 1520, 3000) { OinkTheme(darkTheme = true, accent = OinkAccent.Violet) { ComponentBoard() } }
    snap("components-blue", 1520, 3000) { OinkTheme(darkTheme = false, accent = OinkAccent.Blue) { ComponentBoard() } }

    snap("icons-light", 1600, 1400) { OinkTheme(darkTheme = false) { IconSheet() } }

    snap("logo", 900, 400) {
        OinkTheme(darkTheme = false) {
            Row(
                Modifier.fillMaxSize().background(OinkTheme.colors.bg).padding(40.dp),
                horizontalArrangement = Arrangement.spacedBy(60.dp),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            ) {
                OinkLogo(size = 140.dp, animated = true)
                OinkLogo(size = 64.dp, animated = true)
                OinkLogo(size = 28.dp, animated = true)
                OinkLogo(size = 64.dp, animated = false)
            }
        }
    }

    snap("dialog-light", 1240, 1040) {
        OinkTheme(darkTheme = false) {
            Box(Modifier.fillMaxSize().background(OinkTheme.colors.bg)) {
                OinkDialog(onDismissRequest = {}, title = "Save changes?", text = "Your profile will be updated with the new details.", icon = OinkIcons.Sparkle, confirmLabel = "Save")
            }
        }
    }
    snap("dialog-dark", 1240, 1040) {
        OinkTheme(darkTheme = true) {
            Box(Modifier.fillMaxSize().background(OinkTheme.colors.bg)) {
                OinkDialog(onDismissRequest = {}, title = "Delete project?", text = "This permanently removes the project and all of its files.", icon = OinkIcons.Trash, destructive = true, confirmLabel = "Delete")
            }
        }
    }
    println("done")
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun IconSheet() {
    val c = OinkTheme.colors
    val icons = listOf(
        "Search" to OinkIcons.Search, "Check" to OinkIcons.Check, "X" to OinkIcons.X, "Plus" to OinkIcons.Plus,
        "Sun" to OinkIcons.Sun, "Moon" to OinkIcons.Moon, "Mail" to OinkIcons.Mail, "Heart" to OinkIcons.Heart,
        "Share" to OinkIcons.Share, "Info" to OinkIcons.Info, "Bell" to OinkIcons.Bell, "Trash" to OinkIcons.Trash,
        "Copy" to OinkIcons.Copy, "User" to OinkIcons.User, "Globe" to OinkIcons.Globe, "Zap" to OinkIcons.Zap,
        "Archive" to OinkIcons.Archive, "Layout" to OinkIcons.Layout, "Menu" to OinkIcons.Menu, "CheckCircle" to OinkIcons.CheckCircle,
        "XCircle" to OinkIcons.XCircle, "Inbox" to OinkIcons.Inbox, "Star" to OinkIcons.Star, "Download" to OinkIcons.Download,
        "Edit" to OinkIcons.Edit, "Settings" to OinkIcons.Settings, "Clock" to OinkIcons.Clock, "Calendar" to OinkIcons.Calendar,
        "Lock" to OinkIcons.Lock, "Tag" to OinkIcons.Tag, "Link" to OinkIcons.Link, "External" to OinkIcons.External,
    )
    FlowRow(
        Modifier.fillMaxSize().background(c.bg).padding(24.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        icons.forEach { (name, vec) ->
            Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                OinkIcon(vec, name, tint = c.text, size = 36.dp)
                OinkText(name, style = OinkTheme.textStyles.micro, color = c.text3, modifier = Modifier.padding(top = 6.dp))
            }
        }
    }
}

@Composable
private fun ComponentBoard() {
    val c = OinkTheme.colors
    Column(
        Modifier.fillMaxSize().background(c.bg).padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OinkButton("Soft", {}, variant = OinkButtonVariant.Soft)
            OinkButton("Primary", {}, variant = OinkButtonVariant.Primary)
            OinkButton("Accent", {}, variant = OinkButtonVariant.Accent)
            OinkButton("Danger", {}, variant = OinkButtonVariant.Danger)
            OinkButton("Loading", {}, variant = OinkButtonVariant.Accent, state = OinkButtonState.Loading)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            OinkSwitch(checked = true, onCheckedChange = {})
            OinkSwitch(checked = false, onCheckedChange = {})
            OinkCheckbox(checked = true, onCheckedChange = {})
            OinkCheckbox(checked = false, onCheckedChange = {}, indeterminate = true)
            OinkRadio(selected = true, onClick = {})
            OinkRadio(selected = false, onClick = {})
        }
        OinkTextField(value = "piglet@oink.dev", onValueChange = {}, leadingIcon = OinkIcons.Mail, showClear = true)
        OinkSlider(value = 0.6f, onValueChange = {})
        OinkSegmented(options = listOf("Overview", "Activity", "Settings"), selectedIndex = 1, onSelectedChange = {})
        OinkTabs(tabs = listOf("For you", "Following", "Trending"), selectedIndex = 0, onSelectedChange = {})
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OinkBadge("Neutral")
            OinkBadge("Accent", variant = OinkBadgeVariant.Accent)
            OinkBadge("Solid", variant = OinkBadgeVariant.Solid)
            OinkBadge("Success", variant = OinkBadgeVariant.Success, dot = true)
            OinkBadge("Danger", variant = OinkBadgeVariant.Danger, dot = true)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OinkChip("Music", selected = true, onClick = {})
            OinkChip("Films", selected = false, onClick = {})
            OinkChip("Books", selected = false, onClick = {})
        }
        OinkProgress(progress = 0.68f)
        Row(horizontalArrangement = Arrangement.spacedBy(20.dp), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            OinkRing(progress = 0.68f, size = 64.dp) { OinkText("68%", style = OinkTheme.textStyles.caption, color = c.text) }
            OinkCard(Modifier.width(220.dp), contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp)) {
                OinkText("Paper card", style = OinkTheme.textStyles.h4, color = c.text)
                OinkText("Hairline border, top highlight, no shadow.", style = OinkTheme.textStyles.caption, color = c.text2, modifier = Modifier.padding(top = 4.dp))
            }
        }
    }
}