package zhiqiu.applewidgets.kmp.macos

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.basic.Text
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

data class OutlineNode(
    val id: String,
    val title: String,
    val children: List<OutlineNode> = emptyList(),
)

@Composable
fun OutlineView(
    roots: List<OutlineNode>,
    selectedId: String?,
    onSelect: (OutlineNode) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        roots.forEach { node ->
            OutlineNodeRow(
                node = node,
                depth = 0,
                selectedId = selectedId,
                onSelect = onSelect,
            )
        }
    }
}

@Composable
private fun OutlineNodeRow(
    node: OutlineNode,
    depth: Int,
    selectedId: String?,
    onSelect: (OutlineNode) -> Unit,
) {
    var expanded by remember(node.id) { mutableStateOf(true) }
    val hasChildren = node.children.isNotEmpty()
    val colors = AppleWidgetsTheme.colorScheme
    val selected = node.id == selectedId

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect(node) }
            .padding(start = (12 + depth * 14).dp, end = 8.dp, top = 4.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (hasChildren) {
            Text(
                text = if (expanded) "▼" else "▶",
                style = AppleWidgetsTheme.textStyles.caption2,
                color = colors.secondaryLabel,
                modifier = Modifier.clickable { expanded = !expanded },
            )
        } else {
            Spacer(Modifier.width(12.dp))
        }
        Spacer(Modifier.width(4.dp))
        Text(
            text = node.title,
            style = AppleWidgetsTheme.textStyles.callout,
            color = if (selected) colors.accent else colors.label,
        )
    }
    if (hasChildren && expanded) {
        node.children.forEach { child ->
            OutlineNodeRow(
                node = child,
                depth = depth + 1,
                selectedId = selectedId,
                onSelect = onSelect,
            )
        }
    }
}
