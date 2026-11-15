package zhiqiu.applewidgets.kmp.oink.basic

import zhiqiu.applewidgets.kmp.oink.data.*
import zhiqiu.applewidgets.kmp.oink.form.*
import zhiqiu.applewidgets.kmp.oink.layout.*
import zhiqiu.applewidgets.kmp.oink.nav.*
import zhiqiu.applewidgets.kmp.oink.overlay.*
import zhiqiu.applewidgets.kmp.oink.primitives.*
import zhiqiu.applewidgets.kmp.oink.responsive.*
import zhiqiu.applewidgets.kmp.oink.theme.*

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.Dp

internal const val OinkCheckPath = "M4.5 10.5l3.5 3.5 7.5-8"
internal const val OinkDashPath = "M5.5 10h9"

/** 按 progress（0→1）描线绘制 20×20 视口内的路径，调用方负责驱动 progress。 */
@Composable
internal fun OinkDrawnPath(
    progress: Float,
    color: Color,
    size: Dp,
    vararg paths: String,
    strokeWidth: Float = 2.4f,
) {
    val parsed = remember(paths) { paths.map { PathParser().parsePathString(it).toPath(Path()) } }
    Canvas(Modifier.size(size)) {
        val s = this.size.minDimension / 20f
        val measure = PathMeasure()
        scale(s, s, pivot = Offset.Zero) {
            for (p in parsed) {
                if (progress <= 0f) continue
                measure.setPath(p, false)
                val dst = Path()
                measure.getSegment(0f, progress.coerceIn(0f, 1f) * measure.length, dst, true)
                drawPath(dst, color = color, style = Stroke(strokeWidth, cap = StrokeCap.Round, join = StrokeJoin.Round))
            }
        }
    }
}