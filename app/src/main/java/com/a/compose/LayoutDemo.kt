package com.a.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp


class LayoutDemo {


}


data class ArtistData(val name:String,val desc:String)

@Composable
fun ArtistCard(artist: ArtistData) {
    Row() {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.weight(2f)
        ) {
            Image(painter = painterResource(id = R.drawable.ic_launcher_foreground),contentDescription = "photo holder",modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .padding(all = 5.dp)
                .border(0.5.dp, Color.Gray, CircleShape)
            )

            Column {
                Text(artist.name,
                    modifier = Modifier.offset(x = 1.dp,y=2.dp)
                )
                Divider()
                Text(artist.desc,
                    modifier = Modifier.offset(x = 1.dp,y=-2.dp)
                )
            }
        }
        Image(painter = painterResource(id = R.drawable.ic_launcher_foreground),contentDescription = "photo holder",modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .padding(all = 5.dp)
            .border(0.5.dp, Color.Blue, CircleShape)
            .weight(1f)
        )
    }

}

@Preview("demo")
@Composable
fun LayoutCompose(){
    ArtistCard(ArtistData("Alfred Sisley","English Man"))
}
