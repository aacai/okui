package zhiqiu.applewidgets.kmp.macos

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.basic.Text
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

@Composable
fun RadioButton(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val colors = AppleWidgetsTheme.colorScheme
    val border = if (selected) colors.accent else colors.opaqueSeparator
    val selectable = if (onClick != null) {
        Modifier.selectable(
            selected = selected,
            enabled = enabled,
            role = Role.RadioButton,
            onClick = onClick,
        )
    } else {
        Modifier
    }
    Box(
        modifier = modifier
            .then(selectable)
            .size(18.dp)
            .clip(CircleShape)
            .border(1.5.dp, if (enabled) border else colors.tertiaryLabel, CircleShape)
            .background(colors.systemBackground),
        contentAlignment = Alignment.Center,
    ) {
        if (selected) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(if (enabled) colors.accent else colors.tertiaryLabel),
            )
        }
    }
}

@Composable
fun RadioGroup(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        options.forEachIndexed { index, label ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.selectable(
                    selected = index == selectedIndex,
                    enabled = enabled,
                    role = Role.RadioButton,
                    onClick = { onSelect(index) },
                ),
            ) {
                RadioButton(
                    selected = index == selectedIndex,
                    onClick = { onSelect(index) },
                    enabled = enabled,
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = label,
                    style = AppleWidgetsTheme.textStyles.body,
                    color = if (enabled) {
                        AppleWidgetsTheme.colorScheme.label
                    } else {
                        AppleWidgetsTheme.colorScheme.tertiaryLabel
                    },
                )
            }
        }
    }
}
