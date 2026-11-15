package zhiqiu.applewidgets.kmp.oink.data

import zhiqiu.applewidgets.kmp.oink.basic.*
import zhiqiu.applewidgets.kmp.oink.form.*
import zhiqiu.applewidgets.kmp.oink.layout.*
import zhiqiu.applewidgets.kmp.oink.nav.*
import zhiqiu.applewidgets.kmp.oink.overlay.*
import zhiqiu.applewidgets.kmp.oink.primitives.*
import zhiqiu.applewidgets.kmp.oink.responsive.*
import zhiqiu.applewidgets.kmp.oink.theme.*

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs

enum class OinkAvatarSize(val dp: Dp, val font: Int) { Sm(28.dp, 11), Md(36.dp, 14), Lg(48.dp, 18), Xl(64.dp, 24) }
enum class OinkAvatarStatus { Online, Away, Busy, Offline }

/**
 * Oink 头像：无图时用姓名首字母 + 由名字散列决定的柔和色底，可带状态点。
 */
@Composable
fun OinkAvatar(
    name: String,
    modifier: Modifier = Modifier,
    size: OinkAvatarSize = OinkAvatarSize.Md,
    shape: Shape = CircleShape,
    status: OinkAvatarStatus? = null,
    colors: OinkAvatarColors = OinkAvatarDefaults.colors(),
) {
    val palette = colors.palettes
    val (bg, fg) = palette[abs(name.hashCode()) % palette.size]
    val initials = remember(name) {
        name.trim().split(Regex("\\s+")).filter { it.isNotEmpty() }.take(2)
            .joinToString("") { it.first().uppercase() }
            .ifEmpty { "?" }
    }
    Box(modifier = modifier.size(size.dp)) {
        Box(
            Modifier.size(size.dp).clip(shape).background(bg),
            contentAlignment = Alignment.Center,
        ) {
            OinkText(
                text = initials,
                color = fg,
                style = OinkTheme.textStyles.label.copy(fontSize = size.font.sp, fontWeight = FontWeight.SemiBold),
            )
        }
        if (status != null) {
            val dotColor = when (status) {
                OinkAvatarStatus.Online -> colors.online
                OinkAvatarStatus.Away -> colors.away
                OinkAvatarStatus.Busy -> colors.busy
                OinkAvatarStatus.Offline -> colors.offline
            }
            val dotSize = (size.dp.value * 0.28f).dp
            Box(
                Modifier
                    .align(Alignment.BottomEnd)
                    .size(dotSize + 4.dp)
                    .clip(CircleShape)
                    .background(colors.ring),
                contentAlignment = Alignment.Center,
            ) {
                Box(Modifier.size(dotSize).clip(CircleShape).background(dotColor))
            }
        }
    }
}

/**
 * Oink 头像组：重叠排列 + 纸面描边，超出部分显示 +N。
 */
@Composable
fun OinkAvatarGroup(
    names: List<String>,
    modifier: Modifier = Modifier,
    max: Int = 4,
    size: OinkAvatarSize = OinkAvatarSize.Md,
    colors: OinkAvatarGroupColors = OinkAvatarGroupDefaults.colors(),
) {
    val shown = names.take(max)
    val extra = names.size - shown.size
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(-(size.dp.value * 0.28f).dp)) {
        shown.forEachIndexed { index, name ->
            Box(Modifier.graphicsLayer { }) {
                Box(
                    Modifier
                        .size(size.dp + 4.dp)
                        .clip(CircleShape)
                        .background(colors.ring),
                    contentAlignment = Alignment.Center,
                ) {
                    OinkAvatar(name = name, size = size)
                }
            }
        }
        if (extra > 0) {
            Box(
                Modifier
                    .size(size.dp + 4.dp)
                    .clip(CircleShape)
                    .background(colors.ring),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    Modifier.size(size.dp).clip(CircleShape).background(colors.extraContainer),
                    contentAlignment = Alignment.Center,
                ) {
                    OinkText(
                        text = "+$extra",
                        color = colors.extraContent,
                        style = OinkTheme.textStyles.caption.copy(fontSize = (size.font - 1).sp, fontWeight = FontWeight.SemiBold),
                    )
                }
            }
        }
    }
}

/**
 * Oink 评分：可交互五星，hover 预览、按下回弹；[onValueChange] 为 null 时只读。
 */
@Composable
fun OinkRating(
    value: Int,
    modifier: Modifier = Modifier,
    count: Int = 5,
    starSize: Dp = OinkRatingDefaults.StarSize,
    onValueChange: ((Int) -> Unit)? = null,
    colors: OinkRatingColors = OinkRatingDefaults.colors(),
) {
    var hovered by remember { mutableStateOf(-1) }
    val interactive = onValueChange != null
    val preview = if (interactive && hovered >= 0) hovered + 1 else value
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        for (i in 0 until count) {
            val filled = i < preview
            val interaction = remember { MutableInteractionSource() }
            val pressed by interaction.collectIsPressedAsState()
            val isHovered by interaction.collectIsHoveredAsState()
            LaunchedEffect(isHovered) {
                if (isHovered) hovered = i else if (hovered == i) hovered = -1
            }
            val scale by animateFloatAsState(if (pressed) 0.82f else 1f, OinkMotion.bouncy(), label = "starScale")
            Box(
                modifier = Modifier
                    .graphicsLayer { scaleX = scale; scaleY = scale }
                    .size(starSize)
                    .then(
                        if (interactive) {
                            Modifier
                                .hoverable(interaction)
                                .clickable(interactionSource = interaction, indication = null) {
                                    onValueChange?.invoke(i + 1)
                                }
                        } else {
                            Modifier
                        },
                    ),
                contentAlignment = Alignment.Center,
            ) {
                OinkIcon(
                    imageVector = if (filled) OinkIcons.StarFilled else OinkIcons.Star,
                    contentDescription = null,
                    tint = if (filled) colors.filled else colors.empty,
                    size = starSize,
                )
            }
        }
    }
}

object OinkAvatarDefaults {
    /** 首字母底色按姓名散列从调色板取一组；ring 用于状态点 / 头像组的纸面描边。 */
    @Composable
    fun colors(): OinkAvatarColors {
        val c = OinkTheme.colors
        return remember(c) {
            OinkAvatarColors(
                palettes = listOf(
                    c.accentSoft to c.accentText,
                    c.infoSoft to c.infoText,
                    c.successSoft to c.successText,
                    c.warningSoft to c.warningText,
                    c.accentTint to c.accentDeep,
                ),
                ring = c.card,
                online = c.success,
                away = c.warning,
                busy = c.danger,
                offline = c.text3,
            )
        }
    }
}

@Immutable
data class OinkAvatarColors(
    val palettes: List<Pair<Color, Color>>,
    val ring: Color,
    val online: Color,
    val away: Color,
    val busy: Color,
    val offline: Color,
)

object OinkAvatarGroupDefaults {
    @Composable
    fun colors(): OinkAvatarGroupColors {
        val c = OinkTheme.colors
        return OinkAvatarGroupColors(ring = c.card, extraContainer = c.fill, extraContent = c.text2)
    }
}

@Immutable
data class OinkAvatarGroupColors(
    val ring: Color,
    val extraContainer: Color,
    val extraContent: Color,
)

object OinkRatingDefaults {
    val StarSize = 24.dp

    @Composable
    fun colors(): OinkRatingColors {
        val c = OinkTheme.colors
        return OinkRatingColors(filled = c.accent, empty = c.well2)
    }
}

@Immutable
data class OinkRatingColors(
    val filled: Color,
    val empty: Color,
)