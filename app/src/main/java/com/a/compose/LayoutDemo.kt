package com.a.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.transform.RoundedCornersTransformation



data class ArtistData(val name:String,val desc:String,val workUrl:String)

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
        Image(painter = rememberImagePainter(
            data = artist.workUrl,
            builder = {
                transformations(RoundedCornersTransformation(topLeft = 2.0f,topRight = 2.0f,bottomLeft = 2.0f,bottomRight = 2.0f))
            }
        ),contentDescription = "photo holder",modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .padding(all = 5.dp)
            .border(0.5.dp, Color.Blue, CircleShape)
            .weight(1f)
        )
    }

}

@Composable
fun LikeButton(onClick:()->Unit) {

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
            Icons.Filled.Favorite,
            contentDescription = "Favorite",
            modifier = Modifier.size(ButtonDefaults.IconSize)
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text("Like")
    }
}

@Composable
fun CallingComposable(modifier: Modifier = Modifier) {
    MyBasicColumnV2(modifier.padding(8.dp)) {
        Text("MyBasicColumn")
        Text("places items")
        Text("vertically.")
        Text("We've done it by hand!")
    }
}
@Composable
fun CallingComposableV3(modifier: Modifier = Modifier) {
    MyBasicColumnV3(modifier.padding(8.dp)) {
        Text("MyBasicColumn")
        Text("places items")
        Text("vertically.")
        Text("We've done it by hand!")
        Text("We've done it by hand!")
        Text("We've done it by hand!")
    }
}

@Composable
fun MyBasicColumnV3(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        // Don't constrain child views further, measure them with given constraints
        // List of measured children
        val placeables = measurables.map { measurable ->
            // Measure each children
            measurable.measure(constraints)
        }

        // Set the size of the layout as big as it can
        layout(constraints.maxWidth, constraints.maxHeight) {
            // Track the y co-ord we have placed children up to
            var yPosition = 0
            var xPosition = 0

            // Place children in the parent layout
            placeables.forEach { placeable ->
                // Position item on the screen
                placeable.placeRelative(x = xPosition, y = yPosition)

                // Record the y co-ord placed up to
                if(xPosition+placeable.width>constraints.maxWidth) {
                    xPosition = 0
                    yPosition += placeable.height
                }else {
                    xPosition += placeable.width
                }
//                yPosition += placeable.height
            }
        }
    }
}

@Composable
fun MyBasicColumnV2(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        // Don't constrain child views further, measure them with given constraints
        // List of measured children
        val placeables = measurables.map { measurable ->
            // Measure each children
            measurable.measure(constraints)
        }

        // Set the size of the layout as big as it can
        val finalHeight = placeables[0].height*placeables.size
        layout(constraints.maxWidth, finalHeight) {
            // Track the y co-ord we have placed children up to
            var yPosition = 0

            // Place children in the parent layout
            placeables.forEach { placeable ->
                // Position item on the screen
                placeable.placeRelative(x = 0, y = yPosition)

                // Record the y co-ord placed up to
                yPosition += placeable.height
            }
        }
    }
}


@Composable
fun CustomText() {
    Divider(modifier = Modifier.height(16.dp))
    Text("Modify Hi there!",
        Modifier
            .background(Color.Green)
            .firstBaselineToTopV2(32.dp)
            .background(Color.Blue))
    Divider(modifier = Modifier.height(16.dp))
    Text("Normal Hi there!",
        Modifier
            .background(Color.Red)
            .padding(top = 32.dp)
            .background(Color.Gray))
}

fun Modifier.firstBaselineToTopV2(
    firstBaselineToTop: Dp
) = layout { measurable, constraints ->
    // Measure the composable
    val placeable = measurable.measure(constraints)

    // Check the composable has a first baseline
    check(placeable[FirstBaseline] != AlignmentLine.Unspecified)
    val firstBaseline = placeable[FirstBaseline]

    // Height of the composable with padding - first baseline
    val placeableY = firstBaselineToTop.roundToPx() - firstBaseline
    val height = placeable.height + placeableY
    layout(placeable.width, 200) {
        // Where the composable gets placed
        placeable.placeRelative(20, 20)
    }
    layout(placeable.width, height) {
        // Where the composable gets placed
        placeable.placeRelative(20, placeableY)
    }
}

@Preview("demo")
@Composable
fun LayoutCompose(){
    Column {
        ArtistCard(ArtistData("Alfred Sisley","English Man","https://picsum.photos/300/300"))
        Divider(modifier = Modifier.height(16.dp))
        LikeButton {
//            oneDialog()
        }
        CustomText()
        Divider(modifier = Modifier.height(16.dp))
        CallingComposable()
        Divider(modifier = Modifier.height(16.dp))
        ArtistCard(ArtistData("Alfred Sisley","English Man","https://picsum.photos/300/300"))
        Divider(modifier = Modifier.height(16.dp))
        CallingComposableV3()

    }
}


@Composable
fun oneDialog(){
    val openDialog = remember { mutableStateOf(true) }
    var text by remember { mutableStateOf("") }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(text = "Title")
            },
            text = {
                Column() {
                    TextField(
                        value = text,
                        onValueChange = { text = it }
                    )
                    Text("Custom Text")
                    Checkbox(checked = false, onCheckedChange = {})
                }
            },
            buttons = {
                Row(
                    modifier = Modifier.padding(all = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { openDialog.value = false }
                    ) {
                        Text("Dismiss")
                    }
                }
            }
        )
    }
}