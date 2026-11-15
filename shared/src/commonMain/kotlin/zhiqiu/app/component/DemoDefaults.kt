package zhiqiu.app.component

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** Gallery 卡片外边距，对齐 miuix example（12.dp）。 */
fun Modifier.demoCard(): Modifier =
    this
        .padding(horizontal = 12.dp)
        .padding(bottom = 12.dp)
