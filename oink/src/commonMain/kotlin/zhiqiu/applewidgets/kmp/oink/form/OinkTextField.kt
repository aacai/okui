package zhiqiu.applewidgets.kmp.oink.form

import zhiqiu.applewidgets.kmp.oink.basic.*
import zhiqiu.applewidgets.kmp.oink.data.*
import zhiqiu.applewidgets.kmp.oink.layout.*
import zhiqiu.applewidgets.kmp.oink.nav.*
import zhiqiu.applewidgets.kmp.oink.overlay.*
import zhiqiu.applewidgets.kmp.oink.primitives.*
import zhiqiu.applewidgets.kmp.oink.responsive.*
import zhiqiu.applewidgets.kmp.oink.theme.*

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

/**
 * Oink 输入框：凹槽底色，聚焦时升起为 raised 并点亮 accent 描边 + 3px 光环。
 */
@Composable
fun OinkTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    placeholder: String? = null,
    leadingIcon: ImageVector? = null,
    trailing: (@Composable () -> Unit)? = null,
    showClear: Boolean = false,
    singleLine: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    colors: OinkTextFieldColors = OinkTextFieldDefaults.colors(),
) {
    val shape = OinkShape(OinkTextFieldDefaults.CornerRadius)
    var focused by remember { mutableStateOf(false) }
    val interaction = remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()

    val container by animateColorAsState(
        if (focused) colors.focusedContainer else colors.container,
        tween(OinkMotion.Dur2, easing = OinkMotion.EaseOut),
        label = "fieldBg",
    )
    val borderColor by animateColorAsState(
        when {
            focused -> colors.focusedBorder
            hovered && enabled -> colors.hoverBorder
            else -> colors.border
        },
        tween(OinkMotion.Dur2, easing = OinkMotion.EaseOut),
        label = "fieldBorder",
    )
    val ringAlpha by animateFloatAsState(
        if (focused) 1f else 0f,
        tween(OinkMotion.Dur2, easing = OinkMotion.EaseOut),
        label = "fieldRing",
    )

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .alpha(if (enabled) 1f else 0.5f)
            .onFocusChanged { focused = it.isFocused }
            .hoverable(interaction),
        enabled = enabled,
        textStyle = OinkTheme.textStyles.body.copy(color = colors.text),
        cursorBrush = SolidColor(colors.cursor),
        singleLine = singleLine,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .heightIn(min = OinkTextFieldDefaults.MinHeight)
                    .then(if (singleLine) Modifier.height(OinkTextFieldDefaults.MinHeight) else Modifier)
                    .clip(shape)
                    .background(container)
                    .border(3.dp, colors.ring.copy(alpha = ringAlpha), shape)
                    .border(OinkMetrics.Hairline, borderColor, shape)
                    .padding(horizontal = 14.dp)
                    .padding(vertical = if (singleLine) 0.dp else 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (leadingIcon != null) {
                    OinkIcon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        tint = if (focused) colors.focusedIcon else colors.icon,
                        modifier = Modifier.padding(end = 9.dp).size(18.dp),
                    )
                }
                Box(Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                    if (value.isEmpty() && placeholder != null) {
                        OinkText(text = placeholder, color = colors.placeholder, style = OinkTheme.textStyles.body)
                    }
                    innerTextField()
                }
                AnimatedVisibility(
                    visible = showClear && value.isNotEmpty(),
                    enter = fadeIn(tween(OinkMotion.Dur1)) + scaleIn(tween(OinkMotion.Dur2), initialScale = 0.6f),
                    exit = fadeOut(tween(OinkMotion.Dur1)) + scaleOut(tween(OinkMotion.Dur1), targetScale = 0.6f),
                ) {
                    val clearInteraction = remember { MutableInteractionSource() }
                    val clearHovered by clearInteraction.collectIsHoveredAsState()
                    Box(
                        modifier = Modifier
                            .padding(start = 6.dp)
                            .size(22.dp)
                            .clip(androidx.compose.foundation.shape.CircleShape)
                            .background(if (clearHovered) colors.clearHover else androidx.compose.ui.graphics.Color.Transparent)
                            .hoverable(clearInteraction)
                            .clickable(interactionSource = clearInteraction, indication = null) { onValueChange("") },
                        contentAlignment = Alignment.Center,
                    ) {
                        OinkIcon(OinkIcons.X, contentDescription = "Clear", tint = colors.icon, modifier = Modifier.size(13.dp))
                    }
                }
                if (trailing != null) {
                    Box(Modifier.padding(start = 6.dp)) { trailing() }
                }
            }
        },
    )
}

/**
 * Oink 搜索框：带放大镜与一键清空。
 */
@Composable
fun OinkSearchInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    placeholder: String = "Search",
    colors: OinkTextFieldColors = OinkTextFieldDefaults.colors(),
) {
    OinkTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        placeholder = placeholder,
        leadingIcon = OinkIcons.Search,
        showClear = true,
        colors = colors,
    )
}

object OinkTextFieldDefaults {
    val MinHeight = 40.dp
    val CornerRadius = 14.dp

    @Composable
    fun colors(): OinkTextFieldColors {
        val c = OinkTheme.colors
        return OinkTextFieldColors(
            container = c.well,
            focusedContainer = c.raised,
            border = c.line,
            hoverBorder = c.line2,
            focusedBorder = c.accent,
            ring = c.accentRing,
            text = c.text,
            placeholder = c.text3,
            icon = c.text3,
            focusedIcon = c.accent,
            clearHover = c.fillHover,
            cursor = c.accent,
        )
    }
}

@Immutable
data class OinkTextFieldColors(
    val container: Color,
    val focusedContainer: Color,
    val border: Color,
    val hoverBorder: Color,
    val focusedBorder: Color,
    val ring: Color,
    val text: Color,
    val placeholder: Color,
    val icon: Color,
    val focusedIcon: Color,
    val clearHover: Color,
    val cursor: Color,
)