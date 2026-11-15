package zhiqiu.applewidgets.kmp.oink.basic

import zhiqiu.applewidgets.kmp.oink.data.*
import zhiqiu.applewidgets.kmp.oink.form.*
import zhiqiu.applewidgets.kmp.oink.layout.*
import zhiqiu.applewidgets.kmp.oink.nav.*
import zhiqiu.applewidgets.kmp.oink.overlay.*
import zhiqiu.applewidgets.kmp.oink.primitives.*
import zhiqiu.applewidgets.kmp.oink.responsive.*
import zhiqiu.applewidgets.kmp.oink.theme.*

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.dp

/**
 * Oink 内置描线图标：20×20 视口、1.8 描线、圆头圆角，路径数据取自 oink-ui icons.ts。
 * 用 Icon(imageVector, tint = ...) 上色。
 */
object OinkIcons {
    val Search by lazy { icon("M13.2 13.2L17 17", circle(9f, 9f, 5.5f)) }
    val Check by lazy { icon("M4.5 10.5l3.5 3.5 7.5-8") }
    val X by lazy { icon("M5.5 5.5l9 9M14.5 5.5l-9 9") }
    val Plus by lazy { icon("M10 4.5v11M4.5 10h11") }
    val Minus by lazy { icon("M4.5 10h11") }
    val ChevronRight by lazy { icon("M7.5 4.5L13 10l-5.5 5.5") }
    val ChevronDown by lazy { icon("M4.5 7.5L10 13l5.5-5.5") }
    val ChevronLeft by lazy { icon("M12.5 4.5L7 10l5.5 5.5") }
    val ChevronUp by lazy { icon("M4.5 12.5L10 7l5.5 5.5") }
    val ArrowLeft by lazy { icon("M16 10H4M8.5 5.5L4 10l4.5 4.5") }
    val ArrowRight by lazy { icon("M4 10h12M11.5 5.5L16 10l-4.5 4.5") }
    val Sun by lazy {
        icon(
            "M10 2.5v1.8M10 15.7v1.8M2.5 10h1.8M15.7 10h1.8M4.7 4.7L6 6M14 14l1.3 1.3M4.7 15.3L6 14M14 6l1.3-1.3",
            circle(10f, 10f, 3.3f),
        )
    }
    val Moon by lazy { icon("M16.5 11.8A7 7 0 1 1 8.2 3.5a5.5 5.5 0 0 0 8.3 8.3z") }
    val Monitor by lazy { icon("M7.5 16.5h5M10 13.5v3", roundRect(3f, 4f, 14f, 9.5f, 2.2f)) }
    val Copy by lazy {
        icon(
            "M13 7V5.5A2 2 0 0 0 11 3.5H5.5a2 2 0 0 0-2 2V11a2 2 0 0 0 2 2H7",
            roundRect(7f, 7f, 9.5f, 9.5f, 2.5f),
        )
    }
    val Play by lazy { icon("M7 4.6v10.8a.7.7 0 0 0 1.1.6l8-5.4a.7.7 0 0 0 0-1.2l-8-5.4A.7.7 0 0 0 7 4.6z", fill = true) }
    val Replay by lazy { icon("M4.3 10a5.7 5.7 0 1 0 1.9-4.2M4 3.5v3h3") }
    val Info by lazy { icon("M10 9.3v4.2M10 6.5v.1", circle(10f, 10f, 7f)) }
    val Alert by lazy { icon("M10 3.5l7 12.5H3l7-12.5zM10 8.5v3.2M10 14v.1") }
    val Bell by lazy { icon("M5.5 8.5a4.5 4.5 0 0 1 9 0c0 4 1.5 5 1.5 5H4s1.5-1 1.5-5zM8.3 16.2a1.8 1.8 0 0 0 3.4 0") }
    val Trash by lazy { icon("M4.5 6h11M8 6V4.5h4V6M6 6l.7 8.8a1.6 1.6 0 0 0 1.6 1.5h3.4a1.6 1.6 0 0 0 1.6-1.5L14 6") }
    val More by lazy { iconStroke(2.6f, "M5 10v.1M10 10v.1M15 10v.1") }
    val Download by lazy { icon("M10 3.5v9M6.5 9.5L10 13l3.5-3.5M4.5 16.5h11") }
    val Upload by lazy { icon("M10 13V4M6.5 7.5L10 4l3.5 3.5M4.5 16.5h11") }
    val Edit by lazy { icon("M4 16l.8-3.3L13.4 4a1.7 1.7 0 0 1 2.4 0l.2.2a1.7 1.7 0 0 1 0 2.4L7.3 15.2 4 16z") }
    val Share by lazy { icon("M10 12.5V3.5M6.8 6.5L10 3.5l3.2 3M5 10v5a1.5 1.5 0 0 0 1.5 1.5h7A1.5 1.5 0 0 0 15 15v-5") }
    val Mail by lazy { icon("M3.8 7l6.2 4.3L16.2 7", roundRect(3f, 4.8f, 14f, 10.5f, 2.5f)) }
    val Sparkle by lazy { icon("M10 3.5l1.7 4.8 4.8 1.7-4.8 1.7L10 16.5l-1.7-4.8L3.5 10l4.8-1.7z") }
    val Sliders by lazy {
        icon(
            "M3.5 6.5h6M13.5 6.5h3M3.5 13.5h3M10.5 13.5h6",
            circle(11.5f, 6.5f, 2f),
            circle(8.5f, 13.5f, 2f),
        )
    }
    val Home by lazy { icon("M3.5 9.2L10 3.5l6.5 5.7V15a1.5 1.5 0 0 1-1.5 1.5H5A1.5 1.5 0 0 1 3.5 15V9.2z", "M8 16.5V12h4v4.5") }
    val User by lazy { icon("M4 16.5c.7-2.8 3-4.3 6-4.3s5.3 1.5 6 4.3", circle(10f, 7f, 3.2f)) }
    val Users by lazy {
        icon(
            "M2.5 16c.6-2.5 2.5-3.8 5-3.8s4.4 1.3 5 3.8M12.8 4.6a2.8 2.8 0 0 1 0 5.2M14.8 12.5c1.4.5 2.3 1.7 2.7 3.5",
            circle(7.5f, 7.2f, 2.8f),
        )
    }
    val Settings by lazy {
        icon(
            "M8.6 3h2.8l.5 2 1.8 1 2-.6 1.4 2.4-1.5 1.5v1.4l1.5 1.5-1.4 2.4-2-.6-1.8 1-.5 2H8.6l-.5-2-1.8-1-2 .6-1.4-2.4 1.5-1.5V9.3L2.9 7.8l1.4-2.4 2 .6 1.8-1z",
            circle(10f, 10f, 2.5f),
        )
    }
    val Heart by lazy { icon("M10 16.5S3.5 12.7 3.5 8.2A3.7 3.7 0 0 1 10 5.9a3.7 3.7 0 0 1 6.5 2.3c0 4.5-6.5 8.3-6.5 8.3z") }
    val Star by lazy { icon("M10 3.5l1.8 4.5 4.9.4-3.7 3.1 1.1 4.7L10 13.6l-4.1 2.6 1.1-4.7-3.7-3.1 4.9-.4z") }
    val StarFilled by lazy { icon("M10 3.5l1.8 4.5 4.9.4-3.7 3.1 1.1 4.7L10 13.6l-4.1 2.6 1.1-4.7-3.7-3.1 4.9-.4z", fill = true) }
    val Grid by lazy {
        icon(
            roundRect(3.5f, 3.5f, 5.5f, 5.5f, 1.5f),
            roundRect(11f, 3.5f, 5.5f, 5.5f, 1.5f),
            roundRect(3.5f, 11f, 5.5f, 5.5f, 1.5f),
            roundRect(11f, 11f, 5.5f, 5.5f, 1.5f),
        )
    }
    val List by lazy { icon("M7.5 5.5h9M7.5 10h9M7.5 14.5h9", "M3.8 5.5v.1M3.8 10v.1M3.8 14.5v.1") }
    val Filter by lazy { icon("M3.5 4.5h13l-5 6V15l-3 1.5v-6l-5-6z") }
    val Calendar by lazy { icon("M3.5 8.5h13M7 3v3M13 3v3", roundRect(3.5f, 4.5f, 13f, 12f, 2.5f)) }
    val Clock by lazy { icon("M10 6v4l2.5 1.5", circle(10f, 10f, 7f)) }
    val Inbox by lazy {
        icon(
            "M3.5 11l2-6a1.5 1.5 0 0 1 1.4-1h6.2a1.5 1.5 0 0 1 1.4 1l2 6v3.5A1.5 1.5 0 0 1 15 16H5a1.5 1.5 0 0 1-1.5-1.5V11z",
            "M3.5 11H7l1 2h4l1-2h3.5",
        )
    }
    val Send by lazy { icon("M16.5 3.5L3.5 8.7l5.3 2.5 2.5 5.3 5.2-13zM8.8 11.2L12 8") }
    val Folder by lazy { icon("M3.5 6A1.5 1.5 0 0 1 5 4.5h3l1.8 2H15A1.5 1.5 0 0 1 16.5 8v6.5A1.5 1.5 0 0 1 15 16H5a1.5 1.5 0 0 1-1.5-1.5V6z") }
    val File by lazy { icon("M11.5 3.5h-5A1.5 1.5 0 0 0 5 5v10a1.5 1.5 0 0 0 1.5 1.5h7A1.5 1.5 0 0 0 15 15V7l-3.5-3.5z", "M11.5 3.5V7H15") }
    val Image by lazy { icon("M4.2 13.8l3.3-3.3 3 3 2-2 3.3 3.3", circle(7.5f, 8f, 1.3f), roundRect(3.5f, 4f, 13f, 12f, 2.5f)) }
    val Link by lazy {
        icon(
            "M8.5 11.5a3.2 3.2 0 0 0 4.5 0L15.5 9A3.2 3.2 0 0 0 11 4.5l-.8.8",
            "M11.5 8.5a3.2 3.2 0 0 0-4.5 0L4.5 11A3.2 3.2 0 0 0 9 15.5l.8-.8",
        )
    }
    val External by lazy { icon("M11.5 4H16v4.5M16 4l-7 7M14 11.5V15a1.5 1.5 0 0 1-1.5 1.5H5A1.5 1.5 0 0 1 3.5 15V7.5A1.5 1.5 0 0 1 5 6h3.5") }
    val Eye by lazy { icon("M2.5 10S5.2 5 10 5s7.5 5 7.5 5-2.7 5-7.5 5-7.5-5-7.5-5z", circle(10f, 10f, 2.3f)) }
    val EyeOff by lazy {
        icon(
            "M8.3 5.2A7 7 0 0 1 10 5c4.8 0 7.5 5 7.5 5a12.5 12.5 0 0 1-1.9 2.5M12.9 14.3A6.5 6.5 0 0 1 10 15c-4.8 0-7.5-5-7.5-5a12.3 12.3 0 0 1 3.3-3.7M3.5 3.5l13 13M8.4 8.4a2.3 2.3 0 0 0 3.2 3.2",
        )
    }
    val Lock by lazy { icon("M7 9V6.5a3 3 0 0 1 6 0V9", roundRect(4.5f, 9f, 11f, 7.5f, 2f)) }
    val Tag by lazy { icon("M3.5 4.5v5.2a1.5 1.5 0 0 0 .4 1l5.7 5.7a1.5 1.5 0 0 0 2.1 0l4.7-4.7a1.5 1.5 0 0 0 0-2.1l-5.7-5.7a1.5 1.5 0 0 0-1-.4H4.5a1 1 0 0 0-1 1z", "M7 7v.1") }
    val Bookmark by lazy { icon("M5.5 4.5A1.5 1.5 0 0 1 7 3h6a1.5 1.5 0 0 1 1.5 1.5v12L10 13.5l-4.5 3v-12z") }
    val Refresh by lazy { icon("M16 10a6 6 0 0 1-10.6 3.8M4 10a6 6 0 0 1 10.6-3.8M14.5 3v3.2h-3.2M5.5 17v-3.2h3.2") }
    val Menu by lazy { icon("M3.5 6h13M3.5 10h13M3.5 14h13") }
    val CheckCircle by lazy { icon("M6.8 10.3l2.2 2.2 4.2-4.6", circle(10f, 10f, 7f)) }
    val XCircle by lazy { icon("M7.5 7.5l5 5M12.5 7.5l-5 5", circle(10f, 10f, 7f)) }
    val Help by lazy { icon("M7.9 8a2.2 2.2 0 1 1 3.2 2c-.7.4-1.1.9-1.1 1.6M10 14v.1", circle(10f, 10f, 7f)) }
    val Globe by lazy { icon("M3 10h14M10 3c2 2.2 3 4.5 3 7s-1 4.8-3 7c-2-2.2-3-4.5-3-7s1-4.8 3-7z", circle(10f, 10f, 7f)) }
    val Zap by lazy { icon("M11 2.5l-6.5 9H10l-1 6 6.5-9H10l1-6z") }
    val Command by lazy { icon("M7.5 7.5h5v5h-5zM7.5 7.5h-2a2 2 0 1 1 2-2zM12.5 7.5v-2a2 2 0 1 1 2 2zM12.5 12.5h2a2 2 0 1 1-2 2zM7.5 12.5v2a2 2 0 1 1-2-2z") }
    val Chart by lazy { icon("M3.5 16.5h13M6 13.5V10M10 13.5V5M14 13.5V8") }
    val TrendingUp by lazy { icon("M3 14l4.5-4.5 3 3L17 6M12.5 6H17v4.5") }
    val Layout by lazy { icon("M3 8h14M8 8v8.5", roundRect(3f, 3.5f, 14f, 13f, 2.5f)) }
    val Sidebar by lazy { icon("M8 3.5v13", roundRect(3f, 3.5f, 14f, 13f, 2.5f)) }
    val Cart by lazy { icon("M2.5 3.5h2l1.8 8.5h8.2l1.5-6H5", circle(7.5f, 15.5f, 1.2f), circle(13.5f, 15.5f, 1.2f)) }
    val Bag by lazy { icon("M4.5 7h11l-.7 8.1a1.5 1.5 0 0 1-1.5 1.4H6.7a1.5 1.5 0 0 1-1.5-1.4L4.5 7z", "M7.5 9V6.5a2.5 2.5 0 0 1 5 0V9") }
    val CreditCard by lazy { icon("M2.5 8.5h15M5.5 12.5h3", roundRect(2.5f, 4.5f, 15f, 11f, 2.2f)) }
    val Archive by lazy { icon("M4.5 8v6.5A1.5 1.5 0 0 0 6 16h8a1.5 1.5 0 0 0 1.5-1.5V8M8.3 11.2h3.4", roundRect(3f, 4f, 14f, 4f, 1.2f)) }
    val Paperclip by lazy { icon("M15.5 9.5l-5.7 5.7a3.54 3.54 0 0 1-5-5l6-6a2.34 2.34 0 0 1 3.3 3.3l-5.9 5.9a1.2 1.2 0 0 1-1.7-1.7l5.3-5.3") }
}

private fun circle(cx: Float, cy: Float, r: Float): String =
    "M ${cx - r} $cy a $r $r 0 1 0 ${2 * r} 0 a $r $r 0 1 0 ${-2 * r} 0"

private fun roundRect(x: Float, y: Float, w: Float, h: Float, rx: Float): String =
    "M ${x + rx} $y h ${w - 2 * rx} a $rx $rx 0 0 1 $rx $rx v ${h - 2 * rx} " +
        "a $rx $rx 0 0 1 ${-rx} $rx h ${-(w - 2 * rx)} a $rx $rx 0 0 1 ${-rx} ${-rx} " +
        "v ${-(h - 2 * rx)} a $rx $rx 0 0 1 $rx ${-rx} z"

private fun icon(vararg paths: String, fill: Boolean = false): ImageVector = iconStroke(1.8f, *paths, fill = fill)

private fun iconStroke(width: Float, vararg paths: String, fill: Boolean = false): ImageVector {
    val builder = ImageVector.Builder(
        defaultWidth = 20.dp,
        defaultHeight = 20.dp,
        viewportWidth = 20f,
        viewportHeight = 20f,
    )
    for (d in paths) {
        val nodes = PathParser().parsePathString(d).toNodes()
        builder.addPath(
            pathData = nodes,
            fill = if (fill) SolidColor(Color.Black) else null,
            stroke = if (fill) null else SolidColor(Color.Black),
            strokeLineWidth = width,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        )
    }
    return builder.build()
}