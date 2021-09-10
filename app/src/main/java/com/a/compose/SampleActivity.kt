package com.a.compose

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.transform.RoundedCornersTransformation
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

class SampleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            PreviewArtistCard()
            ChartDataPreview()
        }
    }
}

@Preview
@Composable
fun TwoTextsPreview() {
    MaterialTheme {
        Surface {
            TwoTexts(text1 = "Hi", text2 = "there")
        }
    }
}

@Composable
fun TwoTexts(
    text1: String,
    text2: String,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.height(IntrinsicSize.Min)) {
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp)
                .wrapContentWidth(Alignment.Start),
            text = text1
        )
        Divider(
            color = Color.Black,
            modifier = Modifier.fillMaxHeight().width(1.dp)
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(end = 4.dp)
                .wrapContentWidth(Alignment.End),
            text = text2
        )
    }
}



val MaxChartValue = HorizontalAlignmentLine(merger = { old, new -> min(old, new) })
val MinChartValue = HorizontalAlignmentLine(merger = { old, new -> max(old, new) })


@Composable
private fun BarChart(
    dataPoints: List<Int>,
    modifier: Modifier = Modifier
) {
    val maxValue: Float = remember(dataPoints) { dataPoints.maxOrNull()!! * 1.2f }
    val maxDataPoint: Int = remember(dataPoints) { dataPoints.maxOrNull()!! }
    val minDataPoint: Int = remember(dataPoints) { dataPoints.minOrNull()!! }

    var maxYBaseline by remember { mutableStateOf(Float.MAX_VALUE) }
    var minYBaseline by remember { mutableStateOf(Float.MIN_VALUE) }

    Layout(
        modifier = modifier,
        content = {
            // TODO: Omit the content block for the code snippets in DAC
            BoxWithConstraints(propagateMinConstraints = true) {
                val density = LocalDensity.current
                with(density) {
                    val yPositionRatio = remember(density, maxHeight, maxValue) {
                        maxHeight.toPx() / maxValue
                    }
                    val xPositionRatio = remember(density, maxWidth, dataPoints) {
                        maxWidth.toPx() / (dataPoints.size + 1)
                    }
                    val xOffset = remember(density) { // center points in the graph
                        xPositionRatio / dataPoints.size
                    }

                    Canvas(Modifier) {
                        dataPoints.forEachIndexed { index, dataPoint ->
                            val rectSize = Size(60f, dataPoint * yPositionRatio)
                            val topLeftOffset = Offset(
                                x = xPositionRatio * (index + 1) - xOffset,
                                y = (maxValue - dataPoint) * yPositionRatio
                            )
                            drawRect(Color(0xFF3DDC84), topLeftOffset, rectSize)

                            if (maxYBaseline == Float.MAX_VALUE && dataPoint == maxDataPoint) {
                                maxYBaseline = topLeftOffset.y
                            }
                            if (minYBaseline == Float.MIN_VALUE && dataPoint == minDataPoint) {
                                minYBaseline = topLeftOffset.y
                            }
                        }
                        drawLine(
                            Color(0xFF073042),
                            start = Offset(0f, 0f),
                            end = Offset(0f, maxHeight.toPx()),
                            strokeWidth = 6f
                        )
                        drawLine(
                            Color(0xFF073042),
                            start = Offset(0f, maxHeight.toPx()),
                            end = Offset(maxWidth.toPx(), maxHeight.toPx()),
                            strokeWidth = 6f
                        )
                    }
                }
            }
        }
    ) { measurables, constraints ->
        // Don't constrain child views further, measure them with given constraints
        // List of measured children
        check(measurables.size == 1)
        val placeable = measurables[0].measure(constraints)

        // Set the size of the layout as big as it can
        layout(
            width = constraints.maxWidth,
            height = constraints.maxHeight,
            alignmentLines = mapOf(
                MinChartValue to minYBaseline.roundToInt(),
                MaxChartValue to maxYBaseline.roundToInt()
            )
        ) {
            placeable.placeRelative(0, 0)
        }
    }
}

@Composable
private fun BarChartMinMax(
    dataPoints: List<Int>,
    maxText: @Composable () -> Unit,
    minText: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Layout(
        content = {
            maxText()
            minText()
            // Set a fixed size to make the example easier to follow
            BarChart(dataPoints, Modifier.size(200.dp))
        },
        modifier = modifier
    ) { measurables, constraints ->
        check(measurables.size == 3)
        val placeables = measurables.map {
            it.measure(constraints.copy(minWidth = 0, minHeight = 0))
        }

        val maxTextPlaceable = placeables[0]
        val minTextPlaceable = placeables[1]
        val barChartPlaceable = placeables[2]

        // Obtain the alignment lines from BarChart to position the Text
        val minValueBaseline = barChartPlaceable[MinChartValue]
        val maxValueBaseline = barChartPlaceable[MaxChartValue]
        layout(constraints.maxWidth, constraints.maxHeight) {
            maxTextPlaceable.placeRelative(
                x = 0,
                y = maxValueBaseline - (maxTextPlaceable.height / 2)
            )
            minTextPlaceable.placeRelative(
                x = 0,
                y = minValueBaseline - (minTextPlaceable.height / 2)
            )
            barChartPlaceable.placeRelative(
                x = max(maxTextPlaceable.width, minTextPlaceable.width) + 20,
                y = 0
            )
        }
    }
}

@Preview
@Composable
private fun ChartDataPreview() {
    MaterialTheme {
        BarChartMinMax(
            dataPoints = listOf(4, 24, 15,40),
            maxText = { Text("Max") },
            minText = { Text("Min") },
            modifier = Modifier.padding(24.dp)
        )
    }
}
@Composable
fun MyBasicColumn(
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



fun Modifier.firstBaselineToTop(
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
    layout(placeable.width, height) {
        // Where the composable gets placed
        placeable.placeRelative(0, placeableY)
    }
}

@Preview
@Composable
fun PaddingExample() {
    Column() {



        Text("Hi there!", Modifier.firstBaselineToTop(32.dp).background(MaterialTheme.colors.background))

        Text("Hi there!", Modifier.padding(top = 32.dp).background(MaterialTheme.colors.secondary))
        Text(
            text = "Hello World!",
            color = MaterialTheme.colors.primary,
            modifier = Modifier
                .padding(8.dp) // margin
                .border(2.dp, MaterialTheme.colors.secondary) // outer border
                .padding(8.dp) // space between the borders
                .border(2.dp, MaterialTheme.colors.error) // inner border
                .padding(8.dp) // padding
        )
    }


}


@Preview
@Composable
fun PreviewArtistCard() {
    val context = LocalContext.current

    val list = ArrayList<Artist>().apply {
        val l = this
        repeat(10){
            val a = Artist("https://picsum.photos/300/300","${it} Alfred Sisley","${it} 3 minutes ago","https://picsum.photos/300/300")
            l.add(a)
        }
    }

    LazyColumn {
        item() {
            MyBasicColumn(Modifier.padding(8.dp)) {
                Text("MyBasicColumn")
                Text("places items")
                Text("vertically.")
                Text("We've done it by hand!")
            }
        }
        item() {
            PaddingExample()
        }

        items(list) {
            ArtistCard(artist = it) {
                Toast.makeText(context,it.name,Toast.LENGTH_LONG).show()
            }
        }

    }

}


data class Artist(val logoUrl:String,val name:String ,val desc:String,val workUrl:String)

@Composable
fun ArtistCard(artist: Artist,onClick:()->Unit) {
    val padding = 16.dp
    Column(
        Modifier
            .padding(all = padding)
            .clickable(onClick = onClick)
        ,
        horizontalAlignment = Alignment.CenterHorizontally
       ) {
        Spacer(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.secondary))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .padding(all = 4.dp)
        ) {
            Image(painter = painterResource(id = R.drawable.ic_launcher_foreground),contentDescription = "photo holder",modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .padding(end = 5.dp)
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
                modifier = Modifier.size(400.dp)
            )
        }

    }

}


