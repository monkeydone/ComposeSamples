package com.a.compose

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class LayoutDemo {


}


@Composable
fun ArtistCard() {
    Text("Alfred Sisley")
    Text("3 minutes ago")
}

@Preview("demo")
@Composable
fun LayoutCompose(){
    ArtistCard()
}
