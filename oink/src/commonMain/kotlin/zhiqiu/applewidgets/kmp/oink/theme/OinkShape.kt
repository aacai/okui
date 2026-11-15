package zhiqiu.applewidgets.kmp.oink.theme

import zhiqiu.applewidgets.kmp.oink.basic.*
import zhiqiu.applewidgets.kmp.oink.data.*
import zhiqiu.applewidgets.kmp.oink.form.*
import zhiqiu.applewidgets.kmp.oink.layout.*
import zhiqiu.applewidgets.kmp.oink.nav.*
import zhiqiu.applewidgets.kmp.oink.overlay.*
import zhiqiu.applewidgets.kmp.oink.primitives.*
import zhiqiu.applewidgets.kmp.oink.responsive.*

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Oink 超椭圆圆角，和 Chromium `corner-shape: superellipse(K)` 同一条曲线：
 * 每个角拆成两段三次贝塞尔（直边起点 → 45° 点 → 直边终点），把手长度取自
 * oink-ui 从 Chromium 149 拟合的 HANDLES 表。
 *
 * [nominalRadius] 是标称半径（CSS 里写的值）；超椭圆同半径下显方，
 * 内部乘 [RadiusScale]（--ok-rk = 1.45）外扩曲线起点，视觉圆角与标称一致。
 * 全圆（胶囊、圆）请直接用 CircleShape / RoundedCornerShape(percent = 50)。
 */
data class OinkShape(
    val topStart: Dp,
    val topEnd: Dp,
    val bottomEnd: Dp,
    val bottomStart: Dp,
    val k: Float = DefaultK,
) : Shape {

    constructor(nominalRadius: Dp, k: Float = DefaultK) : this(
        nominalRadius, nominalRadius, nominalRadius, nominalRadius, k,
    )

    override fun createOutline(size: androidx.compose.ui.geometry.Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val w = size.width
        val h = size.height
        if (w <= 0f || h <= 0f) return Outline.Rectangle(Rect(0f, 0f, w.coerceAtLeast(0f), h.coerceAtLeast(0f)))
        val rtl = layoutDirection == LayoutDirection.Rtl
        var tl = (if (rtl) topEnd else topStart).value * density.density * RadiusScale
        var tr = (if (rtl) topStart else topEnd).value * density.density * RadiusScale
        var br = (if (rtl) bottomStart else bottomEnd).value * density.density * RadiusScale
        var bl = (if (rtl) bottomEnd else bottomStart).value * density.density * RadiusScale
        // border-radius 同款缩放：相邻两角半径和超过边长时整体按比例缩小
        var f = 1f
        if (tl + tr > w) f = min(f, w / (tl + tr))
        if (bl + br > w) f = min(f, w / (bl + br))
        if (tl + bl > h) f = min(f, h / (tl + bl))
        if (tr + br > h) f = min(f, h / (tr + br))
        tl *= f; tr *= f; br *= f; bl *= f

        val path = Path()
        val unit = unitCorner(k)
        // 顺时针：右上 → 右下 → 左下 → 左上
        val trPts = mapCorner(unit, tr, w, h, Corner.TopEnd, reversed = true)
        val brPts = mapCorner(unit, br, w, h, Corner.BottomEnd, reversed = false)
        val blPts = mapCorner(unit, bl, w, h, Corner.BottomStart, reversed = true)
        val tlPts = mapCorner(unit, tl, w, h, Corner.TopStart, reversed = false)
        val seq = arrayOf(trPts, brPts, blPts, tlPts)
        path.moveTo(seq[0][0].x, seq[0][0].y)
        for (i in seq.indices) {
            val pts = seq[i]
            var j = 1
            while (j + 2 < pts.size) {
                path.cubicTo(pts[j].x, pts[j].y, pts[j + 1].x, pts[j + 1].y, pts[j + 2].x, pts[j + 2].y)
                j += 3
            }
            val next = seq[(i + 1) % 4][0]
            path.lineTo(next.x, next.y)
        }
        path.close()
        return Outline.Generic(path)
    }

    companion object {
        const val DefaultK = 1.75f
        const val RadiusScale = 1.45f
    }
}

private enum class Corner { TopStart, TopEnd, BottomEnd, BottomStart }

private fun mapCorner(
    unit: List<Pair<Float, Float>>,
    r: Float,
    w: Float,
    h: Float,
    corner: Corner,
    reversed: Boolean,
): List<androidx.compose.ui.geometry.Offset> {
    if (r <= 0f) {
        val p = when (corner) {
            Corner.TopStart -> 0f to 0f
            Corner.TopEnd -> w to 0f
            Corner.BottomEnd -> w to h
            Corner.BottomStart -> 0f to h
        }
        return listOf(androidx.compose.ui.geometry.Offset(p.first, p.second))
    }
    val pts = if (reversed) unit.asReversed() else unit
    return pts.map { (u, v) ->
        when (corner) {
            Corner.TopStart -> androidx.compose.ui.geometry.Offset(u * r, v * r)
            Corner.TopEnd -> androidx.compose.ui.geometry.Offset(w - u * r, v * r)
            Corner.BottomEnd -> androidx.compose.ui.geometry.Offset(w - u * r, h - v * r)
            Corner.BottomStart -> androidx.compose.ui.geometry.Offset(u * r, h - v * r)
        }
    }
}

// K → [c1, c2] 把手长（半径单位），oink-ui 从 Chromium 149 拟合
private val Handles = arrayOf(
    floatArrayOf(0f, 0f, 0.4714f), floatArrayOf(0.25f, 0.0266f, 0.447f), floatArrayOf(0.5f, 0.061f, 0.4174f),
    floatArrayOf(0.75f, 0.1043f, 0.3848f), floatArrayOf(1.1f, 0.1846f, 0.3359f), floatArrayOf(1.25f, 0.2171f, 0.3201f),
    floatArrayOf(1.375f, 0.2551f, 0.302f), floatArrayOf(1.5f, 0.2917f, 0.2864f), floatArrayOf(1.625f, 0.3294f, 0.2714f),
    floatArrayOf(1.75f, 0.3673f, 0.2565f), floatArrayOf(1.875f, 0.4047f, 0.2418f), floatArrayOf(2f, 0.4393f, 0.2274f),
    floatArrayOf(2.25f, 0.4946f, 0.2011f), floatArrayOf(2.5f, 0.5519f, 0.1767f), floatArrayOf(2.75f, 0.6129f, 0.1541f),
    floatArrayOf(3f, 0.6749f, 0.1334f), floatArrayOf(3.5f, 0.7875f, 0.0985f), floatArrayOf(4f, 0.8701f, 0.0718f),
    floatArrayOf(5f, 0.9667f, 0.0372f), floatArrayOf(8f, 1f, 0f),
)

private const val CircleHandle = 0.26521f // (4/3)·tan(π/16)

private fun cornerHandles(k: Float): Pair<Float, Float> {
    if (k == 1f) return CircleHandle to CircleHandle
    if (k >= 8f) return 1f to 0f
    for (i in 1 until Handles.size) {
        if (k <= Handles[i][0]) {
            val (k0, a0, b0) = Triple(Handles[i - 1][0], Handles[i - 1][1], Handles[i - 1][2])
            val (k1, a1, b1) = Triple(Handles[i][0], Handles[i][1], Handles[i][2])
            val f = (k - k0) / (k1 - k0)
            return (a0 + (a1 - a0) * f) to (b0 + (b1 - b0) * f)
        }
    }
    return 1f to 0f
}

/** 单位角：从左边上的 (0,1) 走到上边上的 (1,0)，两段三次贝塞尔共 7 个点。 */
private fun unitCorner(k: Float): List<Pair<Float, Float>> {
    if (k <= 0f) return listOf(0f to 1f, 0f to 1f, 0f to 0f, 0f to 0f, 0f to 0f, 1f to 0f, 1f to 0f)
    val n = 2f.pow(k)
    val m = 1f - 2f.pow(-1f / n)
    val (c1, c2) = cornerHandles(k)
    val d = c2 * sqrt(0.5f)
    return listOf(
        0f to 1f,
        0f to (1f - c1),
        (m - d) to (m + d),
        m to m,
        (m + d) to (m - d),
        (1f - c1) to 0f,
        1f to 0f,
    )
}

/** Oink 圆角令牌（标称值，Shape 内部乘 1.45）。 */
object OinkRadii {
    val r1 = 8.dp
    val r2 = 12.dp
    val r3 = 16.dp
    val r4 = 24.dp
    val r5 = 32.dp
    val r6 = 44.dp
}

/** Oink 尺寸令牌：控件高度三档并排等高，4pt 间距栅格。 */
object OinkMetrics {
    val HeightSm = 32.dp
    val HeightMd = 40.dp
    val HeightLg = 48.dp
    val ItemHeight = 36.dp
    val RowMinHeight = 56.dp

    val Space1 = 4.dp
    val Space2 = 8.dp
    val Space3 = 12.dp
    val Space4 = 16.dp
    val Space5 = 20.dp
    val Space6 = 24.dp
    val Space8 = 32.dp
    val Space10 = 40.dp
    val Space12 = 48.dp
    val Space16 = 64.dp

    val Hairline = 1.dp
    val ControlStroke = 1.5.dp
}