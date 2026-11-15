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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun FeedbackSection() {
    SectionHeader("Messaging", "Alerts, empty states & skeletons", "Semantic alerts, a friendly empty state, shimmering placeholders, and key caps.")
    Column(Modifier.fillMaxWidth().padding(horizontal = 8.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OinkAlert(title = "Heads up", text = "A new version of oink-ui is available.", variant = OinkAlertVariant.Info)
        OinkAlert(title = "Saved", text = "Your changes were stored successfully.", variant = OinkAlertVariant.Success)
        OinkAlert(title = "Careful", text = "You are approaching your storage limit.", variant = OinkAlertVariant.Warning)
        OinkAlert(title = "Failed", text = "We couldn’t reach the server. Try again.", variant = OinkAlertVariant.Danger)
        OinkCard(Modifier.fillMaxWidth()) {
            OinkEmptyState(
                title = "Nothing here yet",
                text = "When you add items, they’ll show up in this space.",
                icon = OinkIcons.Inbox,
                action = { OinkButton(text = "Add item", onClick = {}, variant = OinkButtonVariant.Accent, leadingIcon = OinkIcons.Plus) },
            )
        }
        OinkCard(Modifier.fillMaxWidth(), contentPadding = PaddingValues(20.dp)) {
            Label("Skeleton")
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp), verticalAlignment = Alignment.CenterVertically) {
                OinkSkeleton(Modifier.size(48.dp), shape = CircleShape)
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OinkSkeleton(Modifier.fillMaxWidth(0.6f).height(14.dp))
                    OinkSkeleton(Modifier.fillMaxWidth(0.9f).height(14.dp))
                }
            }
            Spacer(Modifier.height(20.dp))
            Label("Keyboard")
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                OinkKbd("⌘")
                OinkKbd("K")
                OinkText("to search", style = OinkTheme.textStyles.caption, color = OinkTheme.colors.text3, modifier = Modifier.padding(start = 6.dp))
            }
        }
    }
}
