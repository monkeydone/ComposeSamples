package com.a.compose

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import coil.compose.rememberImagePainter
import coil.transform.RoundedCornersTransformation
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
import androidx.compose.foundation.gestures.detectDragGestures as detectDragGestures1

class SampleActivity : ComponentActivity() {
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            TestAnimation()
//            ScrollBoxes2()
//            DragBoxes()
//            TransformableSample()
//            SwipeableSample()
//            PreviewArtistCard()
//            ChartDataPreview()
//            ChartDataPreview()
//            ScrollBoxes()
//            LayoutCompose()
//            TextDemo()
//            ShapeDemo()
//            AnimationDemo()
//            GestureDemo()
            StateDemo()
        }

    }
}


@Composable
fun TransformableSample() {
    // set up all transformation states
    var scale by remember { mutableStateOf(1f) }
    var rotation by remember { mutableStateOf(0f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
        scale *= zoomChange
        rotation += rotationChange
        offset += offsetChange
    }
    Box(
        Modifier
            // apply other transformations like rotation and zoom
            // on the pizza slice emoji
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                rotationZ = rotation,
                translationX = offset.x,
                translationY = offset.y
            )
            // add transformable to listen to multitouch transformation events
            // after offset
            .transformable(state = state)
            .background(Color.Blue)
            .fillMaxSize()
    )
}

@Composable
fun DragBoxes(){
//    var offsetX by remember { mutableStateOf(0f) }
//    Text(
//        modifier = Modifier
//            .offset { IntOffset(offsetX.roundToInt(), 0) }
//            .draggable(
//                orientation = Orientation.Horizontal,
//                state = rememberDraggableState { delta ->
//                    offsetX += delta
//                }
//            ),
//        text = "Drag me!"
//    )

    Box(modifier = Modifier.fillMaxSize()) {
        var offsetX by remember { mutableStateOf(0f) }
        var offsetY by remember { mutableStateOf(0f) }

        Box(
            Modifier
                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                .background(Color.Blue)
                .size(50.dp)
                .pointerInput(Unit) {
                    detectDragGestures1 { change, dragAmount ->
                        change.consumeAllChanges()
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                    }
                }
        )
    }

}


@ExperimentalMaterialApi
@Composable
fun SwipeableSample() {
    val width = 96.dp
    val squareSize = 48.dp

    val swipeableState = rememberSwipeableState(0)
    val sizePx = with(LocalDensity.current) { squareSize.toPx() }
    val anchors = mapOf(0f to 0, sizePx to 1) // Maps anchor points (in px) to states

    Box(
        modifier = Modifier
            .width(width)
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.3f) },
                orientation = Orientation.Horizontal
            )
            .background(Color.LightGray)
    ) {
        Box(
            Modifier
                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                .size(squareSize)
                .background(Color.DarkGray)
        )
    }
}

@Preview
@Composable
fun ScrollBoxes2() {
    Column(
    ) {
        var offset by remember { mutableStateOf(0f) }
        Box(
            Modifier
                .size(150.dp)
                .scrollable(
                    orientation = Orientation.Vertical,
                    // Scrollable state: describes how to consume
                    // scrolling delta and update offset
                    state = rememberScrollableState { delta ->
                        offset += delta
                        delta
                    }
                )
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Text(offset.toString())
        }

        val gradient = Brush.verticalGradient(0f to Color.Gray, 1000f to Color.White)
        Box(
            modifier = Modifier
                .background(Color.LightGray)
                .verticalScroll(rememberScrollState())
                .padding(32.dp)
        ) {
            Column {
                repeat(6) {
                    Box(
                        modifier = Modifier
                            .height(128.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            "Scroll here",
                            modifier = Modifier
                                .border(12.dp, Color.DarkGray)
                                .background(brush = gradient)
                                .padding(24.dp)
                                .height(150.dp)
                        )
                    }
                }
            }
        }
    }
}


@ExperimentalMaterialApi
@OptIn(ExperimentalAnimationApi::class)
@Preview
@Composable
fun TestAnimation() {
    var visible by remember { mutableStateOf(true) }
//    val density = LocalDensity.current
//    AnimatedVisibility(
//        visible = visible,
//        enter = slideInVertically(
//            // Slide in from 40 dp from the top.
//            initialOffsetY = { with(density) { -40.dp.roundToPx() } }
//        ) + expandVertically(
//            // Expand from the top.
//            expandFrom = Alignment.Top
//        ) + fadeIn(
//            // Fade in with the initial alpha of 0.3f.
//            initialAlpha = 0.3f
//        ),
//        exit = slideOutVertically() + shrinkVertically() + fadeOut()
//    ) {
//        Text("Hello", Modifier.fillMaxWidth().height(200.dp))
//    }

//    AnimatedVisibility(
//        visible = visible,
//        // Fade in/out the background and the foreground.
//        enter = fadeIn(),
//        exit = fadeOut()
//    ) {
//        Box(
//            Modifier
//                .fillMaxSize()
//                .background(Color.DarkGray)) {
//            Box(
//                Modifier
//                    .align(Alignment.Center)
//                    .animateEnterExit(
//                        // Slide in/out the inner box.
//                        enter = slideInVertically(),
//                        exit = slideOutVertically()
//                    )
//                    .sizeIn(minWidth = 256.dp, minHeight = 64.dp)
//                    .background(Color.Red)
//            ) {
//                Column() {
//                    Text("Hello",
//                        Modifier
//                            .fillMaxWidth()
//                            .height(200.dp))
//                    Spacer(modifier = Modifier.fillMaxWidth().height(5.dp).background(Color.Blue))
//                    Text("Click",
//                        Modifier
//                            .fillMaxWidth()
//                            .height(200.dp)
//                            .clickable {
//                                visible = !visible
//                            })
//                }
//
//            }
//        }
//    }

    var count by remember { mutableStateOf(1) }

    AnimatedContent(
        targetState = count,
        transitionSpec = {
            // Compare the incoming number with the previous number.
            if (targetState > initialState) {
                // If the target number is larger, it slides up and fades in
                // while the initial (smaller) number slides up and fades out.
                slideInVertically({ height -> height }) + fadeIn() with
                        slideOutVertically({ height -> -height }) + fadeOut()
            } else {
                // If the target number is smaller, it slides down and fades in
                // while the initial number slides down and fades out.
                slideInVertically({ height -> -height }) + fadeIn() with
                        slideOutVertically({ height -> height }) + fadeOut()
            }.using(
                // Disable clipping since the faded slide-in/out should
                // be displayed out of bounds.
                SizeTransform(clip = false)
            )
        }
    ) { targetCount ->
        Text(text = "$targetCount",modifier = Modifier.clickable {
            count++
        })
    }

    var expanded by remember { mutableStateOf(false) }

    Surface(
        color = MaterialTheme.colors.primary,
        onClick = { expanded = !expanded }
    ) {
        AnimatedContent(
            targetState = expanded,
            transitionSpec = {
                fadeIn(animationSpec = tween(150, 150)) with
                        fadeOut(animationSpec = tween(150)) using
                        SizeTransform { initialSize, targetSize ->
                            if (targetState) {
                                keyframes {
                                    // Expand horizontally first.
                                    IntSize(targetSize.width, initialSize.height) at 150
                                    durationMillis = 300
                                }
                            } else {
                                keyframes {
                                    // Shrink vertically first.
                                    IntSize(initialSize.width, targetSize.height) at 150
                                    durationMillis = 300
                                }
                            }
                        }
            }
        ) { targetExpanded ->
//            if (targetExpanded) {
//                Expanded()
//            } else {
//                ContentIcon()
//            }
        }
    }
}


@Preview
@Composable
fun ShapeTest(){
    Column {
        Canvas(modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            drawLine(
                start = Offset(x = canvasWidth, y = 0f),
                end = Offset(x = 0f, y = canvasHeight),
                color = Color.Blue,
                strokeWidth = 5F
            )
            drawCircle(
                color = Color.Blue,
                center = Offset(x = canvasWidth / 2, y = canvasHeight / 2),
                radius = size.minDimension / 4
            )

            val canvasQuadrantSize = size / 2F
            drawRect(
                color = Color.Green,
                size = canvasQuadrantSize
            )

            inset(50F, 30F) {
                drawRect(
                    color = Color.Green,
                    size = canvasQuadrantSize
                )
            }

            val canvasSize = size
            drawRect(
                color = Color.Gray,
                topLeft = Offset(x = canvasWidth / 3F, y = canvasHeight / 3F),
                size = canvasSize / 3F
            )

            rotate(degrees = 45F) {
                drawRect(
                    color = Color.Gray,
                    topLeft = Offset(x = canvasWidth / 3F, y = canvasHeight / 3F),
                    size = canvasSize / 3F
                )
            }

            withTransform({
                translate(left = canvasWidth / 5F)
                rotate(degrees = 45F)
            }) {
                drawRect(
                    color = Color.Gray,
                    topLeft = Offset(x = canvasWidth / 3F, y = canvasHeight / 3F),
                    size = canvasSize / 3F
                )
            }
        }


    }

}

@Preview
@Composable
fun ScrollBoxes() {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .background(Color.LightGray)
            .size(1000.dp)
            .verticalScroll(rememberScrollState())
    ) {
        repeat(10) {
            Text("Item $it", modifier = Modifier.padding(2.dp))
        }

        Text("Hello World")
        Text(stringResource(R.string.app_name))
        Text("Hello World", color = Color.Blue)
        Text("Hello World", fontSize = 30.sp)
        Text("Hello World", fontStyle = FontStyle.Italic)
        Text("Hello World", fontWeight = FontWeight.Bold)

        Text("Hello World", textAlign = TextAlign.Center,
            modifier = Modifier.width(150.dp))

        Text("Hello World", fontFamily = FontFamily.Serif)
        Text("Hello World", fontFamily = FontFamily.SansSerif)

        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.Blue)) {
                    append("H")
                }
                append("ello ")

                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Red)) {
                    append("W")
                }
                append("orld")
            }
        )

        Text(
            buildAnnotatedString {
                withStyle(style = ParagraphStyle(lineHeight = 30.sp)) {
                    withStyle(style = SpanStyle(color = Color.Blue)) {
                        append("Hello\n")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = Color.Red
                        )
                    ) {
                        append("World\n")
                    }
                    append("Compose")
                }
            }
        )

        Text("hello ".repeat(50), maxLines = 2)

        Text("Hello Compose ".repeat(50), maxLines = 2, overflow = TextOverflow.Ellipsis)

        SelectionContainer {
            Column {
                Text("This text is selectable")
                Text("This one too")
                Text("This one as well")
                DisableSelection {
                    Text("But not this one")
                    Text("Neither this one")
                }
                Text("But again, you can select this one")
                Text("And this one too")
            }
        }

        ClickableText(
            text = AnnotatedString("Click Me"),
            onClick = { offset ->
                Toast.makeText(context,"$offset -th character is clicked.",Toast.LENGTH_LONG).show()
            }
        )

        val annotatedText = buildAnnotatedString {
            append("Click ")

            // We attach this *URL* annotation to the following content
            // until `pop()` is called
            pushStringAnnotation(tag = "URL",
                annotation = "https://developer.android.com")
            withStyle(style = SpanStyle(color = Color.Blue,
                fontWeight = FontWeight.Bold)) {
                append("here")
            }

            pop()
        }

        ClickableText(
            text = annotatedText,
            onClick = { offset ->
                // We check if there is an *URL* annotation attached to the text
                // at the clicked position
                annotatedText.getStringAnnotations(tag = "URL", start = offset,
                    end = offset)
                    .firstOrNull()?.let { annotation ->
                        // If yes, we log its value
                        Toast.makeText(context,"${annotation.item}",Toast.LENGTH_LONG).show()

                        Log.d("Clicked URL", annotation.item)
                    }
            }
        )


        var text by remember { mutableStateOf("Hello") }

        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Label") }
        )


        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Label") }
        )


        var value by remember { mutableStateOf("Hello\nWorld\nInvisible") }

        TextField(
            value = value,
            onValueChange = { value = it },
            label = { Text("Enter text") },
            maxLines = 2,
            textStyle = TextStyle(color = Color.Blue, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(20.dp)
        )


        var password by rememberSaveable { mutableStateOf("") }

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Enter password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            drawLine(
                start = Offset(x = canvasWidth, y = 0f),
                end = Offset(x = 0f, y = canvasHeight),
                color = Color.Blue,
                strokeWidth = 5F
            )
        }

    }
}

@Preview
@Composable
fun DecoupledConstraintLayout() {
    BoxWithConstraints {
        val constraints = if (minWidth < 600.dp) {
            decoupledConstraints(margin = 16.dp) // Portrait constraints
        } else {
            decoupledConstraints(margin = 32.dp) // Landscape constraints
        }

        ConstraintLayout(constraints) {
            Button(
                onClick = { /* Do something */ },
                modifier = Modifier.layoutId("button")
            ) {
                Text("Button")
            }

            Text("Text", Modifier.layoutId("text"))
        }
    }
}

private fun decoupledConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val button = createRefFor("button")
        val text = createRefFor("text")

        constrain(button) {
            top.linkTo(parent.top, margin = margin)
        }
        constrain(text) {
            top.linkTo(button.bottom, margin)
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
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
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



        Text("Hi there!",
            Modifier
                .firstBaselineToTop(32.dp)
                .background(MaterialTheme.colors.background))

        Text("Hi there!",
            Modifier
                .padding(top = 32.dp)
                .background(MaterialTheme.colors.secondary))
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

    LazyColumn(contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)) {
        item(){
            ScrollBoxes()
        }
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
    val padding = 4.dp
    Column(
        Modifier
            .padding(all = padding)
            .background(MaterialTheme.colors.primary)
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


