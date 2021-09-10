package com.a.compose

import android.content.Context
import android.os.Bundle
import android.text.Layout
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation

class SampleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PreviewArtistCard()
        }
    }
}


@Preview
@Composable
fun PreviewArtistCard() {
    val list = ArrayList<Artist>().apply {
        val l = this
        repeat(10){
            val a = Artist("https://picsum.photos/300/300","${it} Alfred Sisley","${it} 3 minutes ago","https://picsum.photos/300/300")
            l.add(a)
        }
    }

    LazyColumn {
        items(list) {
            ArtistCard(artist = it) {
//                Toast.makeText()
            }
        }

    }

}


data class Artist(val logoUrl:String,val name:String ,val desc:String,val workUrl:String)

@Composable
fun ArtistCard(artist: Artist,onClick:()->Unit) {
    val padding = 16.dp
    Column(Modifier.padding(all=padding),
        horizontalAlignment = Alignment.CenterHorizontally
       ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .padding(all = 4.dp)
                .clickable(onClick = onClick)
        ) {
            Image(painter = painterResource(id = R.drawable.ic_launcher_foreground),contentDescription = "photo holder",modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .padding(all = 4.dp)
                .border(1.5.dp, MaterialTheme.colors.secondary, CircleShape)
            )

            Column {
                Text(artist.name)
                Text(artist.desc)
            }


        }
        Spacer(Modifier.size(padding))
        Card(elevation = 4.dp){
            Image(
                painter = rememberImagePainter(
                    data = artist.workUrl,
                    builder = {
                        transformations(RoundedCornersTransformation(topLeft = 2.0f,topRight = 2.0f,bottomLeft = 2.0f,bottomRight = 2.0f))
                    }
                ),
                contentDescription = null,
                modifier = Modifier.size(128.dp)
            )
        }

    }

}