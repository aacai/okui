package zhiqiu.app.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.basic.SecureField
import zhiqiu.applewidgets.kmp.basic.SmallTitle
import zhiqiu.applewidgets.kmp.basic.TextEditor
import zhiqiu.applewidgets.kmp.basic.TextField

fun LazyListScope.textFieldSection() {
    item(key = "textField") {
        var text1 by remember { mutableStateOf("") }
        var text2 by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var notes by remember { mutableStateOf("") }

        SmallTitle(text = "TextField")
        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .padding(bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            TextField(value = text1, onValueChange = { text1 = it }, placeholder = "Search")
            TextField(value = text2, onValueChange = { text2 = it }, placeholder = "With placeholder")
            SecureField(value = password, onValueChange = { password = it }, placeholder = "Password")
            TextEditor(value = notes, onValueChange = { notes = it }, placeholder = "多行文本")
        }
    }
}
