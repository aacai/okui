package zhiqiu.app.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zhiqiu.applewidgets.kmp.basic.SearchBar
import zhiqiu.applewidgets.kmp.basic.SmallTitle

fun LazyListScope.searchBarSection() {
    item(key = "searchbar") {
        var search by remember { mutableStateOf("") }
        SmallTitle(text = "SearchBar")
        SearchBar(
            value = search,
            onValueChange = { search = it },
            cancelLabel = "Cancel",
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .padding(bottom = 12.dp),
        )
    }
}
