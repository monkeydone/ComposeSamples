package com.a.compose.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.a.compose.ArcTextExample
import com.a.compose.CanvasDemoV1


@Preview("IconButton")
@Composable
fun IconButton(text:String="",imageVector: ImageVector = Icons.Filled.Favorite,onClick:()->Unit = {}) {

    Button(
        onClick = onClick,
        // Uses ButtonDefaults.ContentPadding by default
        contentPadding = PaddingValues(
            start = 20.dp,
            top = 12.dp,
            end = 20.dp,
            bottom = 12.dp
        )
    ) {
        // Inner content including an icon and a text label
        Icon(
            imageVector = imageVector,
            contentDescription = text,
            modifier = Modifier.size(ButtonDefaults.IconSize)
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(text)
    }
}

@Composable
fun SampleList(title:String,content:@Composable ()->Unit) {
    Column {
        Text("${title}",textAlign = TextAlign.Center, modifier = Modifier
            .fillMaxWidth().padding(8.dp),
            style = MaterialTheme.typography.h5,
            )
        Divider(Modifier.height(2.dp))
        content()
    }
}

@Composable
fun SampleItem(desc:String="",content: @Composable () -> Unit) {
    if(desc.isNotEmpty()) {
        Divider(Modifier.height(8.dp))
        Text("${desc}",textAlign = TextAlign.Center, modifier = Modifier
            .fillMaxWidth().padding(8.dp),
            style = MaterialTheme.typography.h6,
        )
    }
    Divider(Modifier.height(2.dp))
    content()
}