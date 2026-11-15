package zhiqiu.applewidgets.kmp.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * iOS Dynamic Type 风格字号体系。
 */
@Stable
class TextStyles(
    largeTitle: TextStyle,
    title1: TextStyle,
    title2: TextStyle,
    title3: TextStyle,
    headline: TextStyle,
    body: TextStyle,
    callout: TextStyle,
    subheadline: TextStyle,
    footnote: TextStyle,
    caption1: TextStyle,
    caption2: TextStyle,
) {
    var largeTitle by mutableStateOf(largeTitle, structuralEqualityPolicy())
        internal set
    var title1 by mutableStateOf(title1, structuralEqualityPolicy())
        internal set
    var title2 by mutableStateOf(title2, structuralEqualityPolicy())
        internal set
    var title3 by mutableStateOf(title3, structuralEqualityPolicy())
        internal set
    var headline by mutableStateOf(headline, structuralEqualityPolicy())
        internal set
    var body by mutableStateOf(body, structuralEqualityPolicy())
        internal set
    var callout by mutableStateOf(callout, structuralEqualityPolicy())
        internal set
    var subheadline by mutableStateOf(subheadline, structuralEqualityPolicy())
        internal set
    var footnote by mutableStateOf(footnote, structuralEqualityPolicy())
        internal set
    var caption1 by mutableStateOf(caption1, structuralEqualityPolicy())
        internal set
    var caption2 by mutableStateOf(caption2, structuralEqualityPolicy())
        internal set

    fun copy(
        largeTitle: TextStyle = this.largeTitle,
        title1: TextStyle = this.title1,
        title2: TextStyle = this.title2,
        title3: TextStyle = this.title3,
        headline: TextStyle = this.headline,
        body: TextStyle = this.body,
        callout: TextStyle = this.callout,
        subheadline: TextStyle = this.subheadline,
        footnote: TextStyle = this.footnote,
        caption1: TextStyle = this.caption1,
        caption2: TextStyle = this.caption2,
    ): TextStyles = TextStyles(
        largeTitle, title1, title2, title3, headline, body, callout, subheadline, footnote, caption1, caption2,
    )

    fun updateTextStylesFrom(other: TextStyles) {
        largeTitle = other.largeTitle
        title1 = other.title1
        title2 = other.title2
        title3 = other.title3
        headline = other.headline
        body = other.body
        callout = other.callout
        subheadline = other.subheadline
        footnote = other.footnote
        caption1 = other.caption1
        caption2 = other.caption2
    }
}

fun defaultTextStyles(
    largeTitle: TextStyle = TextStyle(fontSize = 34.sp, fontWeight = FontWeight.Bold, lineHeight = 41.sp),
    title1: TextStyle = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold, lineHeight = 34.sp),
    title2: TextStyle = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold, lineHeight = 28.sp),
    title3: TextStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold, lineHeight = 25.sp),
    headline: TextStyle = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.SemiBold, lineHeight = 22.sp),
    body: TextStyle = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Normal, lineHeight = 22.sp),
    callout: TextStyle = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Normal, lineHeight = 21.sp),
    subheadline: TextStyle = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Normal, lineHeight = 20.sp),
    footnote: TextStyle = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Normal, lineHeight = 18.sp),
    caption1: TextStyle = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Normal, lineHeight = 16.sp),
    caption2: TextStyle = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Normal, lineHeight = 13.sp),
): TextStyles = TextStyles(
    largeTitle, title1, title2, title3, headline, body, callout, subheadline, footnote, caption1, caption2,
)

internal val LocalTextStyles = staticCompositionLocalOf { defaultTextStyles() }
