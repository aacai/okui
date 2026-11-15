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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun PeopleSection() {
    SectionHeader("Identity", "Avatars & rating", "Initials avatars with status dots, overlapping groups, and interactive stars.")
    DemoCard {
        Label("Avatars")
        Row(horizontalArrangement = Arrangement.spacedBy(14.dp), verticalAlignment = Alignment.CenterVertically) {
            OinkAvatar(name = "Ada Lovelace", size = OinkAvatarSize.Xl, status = OinkAvatarStatus.Online)
            OinkAvatar(name = "Grace Hopper", size = OinkAvatarSize.Lg, status = OinkAvatarStatus.Away)
            OinkAvatar(name = "Alan Turing", size = OinkAvatarSize.Md, status = OinkAvatarStatus.Busy)
            OinkAvatar(name = "Katherine Johnson", size = OinkAvatarSize.Sm)
        }
        Spacer(Modifier.height(20.dp))
        Label("Group")
        OinkAvatarGroup(names = listOf("Ada Lovelace", "Grace Hopper", "Alan Turing", "Katherine Johnson", "Margaret Hamilton", "Edsger Dijkstra"), max = 4)
        Spacer(Modifier.height(20.dp))
        Label("Rating")
        var rating by remember { mutableStateOf(4) }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OinkRating(value = rating, onValueChange = { rating = it })
            OinkText("$rating / 5", style = OinkTheme.textStyles.caption, color = OinkTheme.colors.text2)
        }
    }
}
