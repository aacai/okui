package zhiqiu.applewidgets.kmp.basic

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme

/**
 * 列表行原语（对标 miuix `BasicComponent`）。
 * Preference 组件应委托本组件，而不是自行拼 Row。
 */
@Composable
fun BasicComponent(
    modifier: Modifier = Modifier,
    title: String? = null,
    titleColor: BasicComponentColors = BasicComponentDefaults.titleColor(),
    summary: String? = null,
    summaryColor: BasicComponentColors = BasicComponentDefaults.summaryColor(),
    startAction: @Composable (() -> Unit)? = null,
    endActions: @Composable (RowScope.() -> Unit)? = null,
    bottomAction: (@Composable () -> Unit)? = null,
    insideMargin: PaddingValues = BasicComponentDefaults.InsideMargin,
    onClick: (() -> Unit)? = null,
    onClickLabel: String? = null,
    role: Role? = null,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
) {
    BasicComponent(
        modifier = modifier,
        startAction = startAction,
        endActions = endActions,
        bottomAction = bottomAction,
        insideMargin = insideMargin,
        onClick = onClick,
        onClickLabel = onClickLabel,
        role = role,
        enabled = enabled,
        interactionSource = interactionSource,
    ) {
        if (title != null) {
            Text(
                text = title,
                style = AppleWidgetsTheme.textStyles.body,
                color = titleColor.color(enabled),
            )
        }
        if (summary != null) {
            Text(
                text = summary,
                style = AppleWidgetsTheme.textStyles.footnote,
                color = summaryColor.color(enabled),
            )
        }
    }
}

@Composable
fun BasicComponent(
    modifier: Modifier = Modifier,
    startAction: @Composable (() -> Unit)? = null,
    endActions: @Composable (RowScope.() -> Unit)? = null,
    bottomAction: (@Composable () -> Unit)? = null,
    insideMargin: PaddingValues = BasicComponentDefaults.InsideMargin,
    onClick: (() -> Unit)? = null,
    onClickLabel: String? = null,
    role: Role? = null,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    @Suppress("NAME_SHADOWING")
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    val currentOnClick by rememberUpdatedState(onClick)

    val clickableModifier = if (enabled && onClick != null) {
        Modifier.clickable(
            interactionSource = interactionSource,
            indication = LocalIndication.current,
            enabled = true,
            onClickLabel = onClickLabel,
            role = role,
            onClick = { currentOnClick?.invoke() },
        )
    } else {
        Modifier
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .then(clickableModifier)
            .padding(insideMargin),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = BasicComponentDefaults.MinHeight - insideMargin.calculateTopPadding() - insideMargin.calculateBottomPadding()),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (startAction != null) {
                startAction()
                Spacer(Modifier.width(12.dp))
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                content = content,
            )
            if (endActions != null) {
                Spacer(Modifier.width(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    content = endActions,
                )
            }
        }
        bottomAction?.invoke()
    }
}

object BasicComponentDefaults {
    val MinHeight = 44.dp
    val InsideMargin = PaddingValues(horizontal = 16.dp, vertical = 11.dp)

    @Composable
    fun titleColor(
        color: Color = AppleWidgetsTheme.colorScheme.label,
        disabledColor: Color = AppleWidgetsTheme.colorScheme.tertiaryLabel,
    ): BasicComponentColors = BasicComponentColors(color, disabledColor)

    @Composable
    fun summaryColor(
        color: Color = AppleWidgetsTheme.colorScheme.secondaryLabel,
        disabledColor: Color = AppleWidgetsTheme.colorScheme.quaternaryLabel,
    ): BasicComponentColors = BasicComponentColors(color, disabledColor)
}

@Immutable
data class BasicComponentColors(
    val color: Color,
    val disabledColor: Color,
) {
    @Stable
    fun color(enabled: Boolean): Color = if (enabled) color else disabledColor
}
