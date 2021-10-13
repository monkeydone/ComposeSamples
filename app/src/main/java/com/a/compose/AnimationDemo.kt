package com.a.compose

import android.graphics.Paint
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun HomeFloatingActionButton(
    extended: Boolean,
    onClick: () -> Unit
) {
    // Use `FloatingActionButton` rather than `ExtendedFloatingActionButton` for full control on
    // how it should animate.
    FloatingActionButton(onClick = onClick) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null
            )
            // Toggle the visibility of the content with animation.
            AnimatedVisibility(visible = extended,
                enter = slideInHorizontally(),
                exit = slideOutHorizontally()
            ) {
                Text(
                    text = stringResource(R.string.edit),
                    modifier = Modifier
                        .padding(start = 8.dp, top = 3.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun HomeFloatingActionButtonV2(
    extended: Boolean,
    onClick: () -> Unit
) {
    // Use `FloatingActionButton` rather than `ExtendedFloatingActionButton` for full control on
    // how it should animate.
    FloatingActionButton(onClick = onClick) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null
            )
            // Toggle the visibility of the content with animation.
            AnimatedVisibility(visible = extended,
                enter = slideInHorizontally() + fadeIn(initialAlpha = 0.3f),
                exit = slideOutHorizontally() + fadeOut()
            ) {
                Text(
                    text = stringResource(R.string.edit),
                    modifier = Modifier
                        .padding(start = 8.dp, top = 3.dp)
                )
            }
        }
    }
}

@Composable
fun ColorDemo() {
    var colorFlag by remember {
        mutableStateOf(true)
    }
    val backgroundColor by animateColorAsState(targetValue = if(colorFlag) Color.Black else Color.Red,
    keyframes {
        durationMillis = 5000
    })

    Box(modifier = Modifier
        .size(100.dp)
        .background(backgroundColor)
        .clickable {
            colorFlag = !colorFlag
        })

}



@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FullScreenNotification(visible: Boolean) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(), exit = fadeOut()
    ) {
        // Fade in/out the background and foreground
        Box(
            Modifier
                .fillMaxSize()
                .background(Color(0x88000000))) {
            Box(
                Modifier
                    .align(Alignment.TopStart)
                    .animateEnterExit(
                        // Slide in/out the rounded rect
                        enter = slideInVertically(),
                        exit = slideOutVertically()
                    )
                    .clip(RoundedCornerShape(10.dp))
                    .requiredHeight(100.dp)
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                // Content of the notification goes here
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun AnimatedContentDemo() {
    Row(modifier = Modifier.size(200.dp,100.dp),
    verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        var count by remember { mutableStateOf(0) }
        Button(onClick = { count++ }) {
            Text("Add")
        }
        AnimatedContent(targetState = count,
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
            }) { targetCount ->
            // Make sure to use `targetCount`, not `count`.
            Text(text = "Count: $targetCount",textAlign = TextAlign.Center,modifier = Modifier.size(50.dp))
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@ExperimentalMaterialApi
@Composable
fun AnimatedContentDemoV2() {
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
                                    durationMillis = 3000
                                }
                            } else {
                                keyframes {
                                    // Shrink vertically first.
                                    IntSize(initialSize.width, targetSize.height) at 150
                                    durationMillis = 3000
                                }
                            }
                        }
            }
        ) { targetExpanded ->
            if(targetExpanded) {
               Text("Hello".repeat(5))
            }else {
                Text("Hello".repeat(30))
            }
        }
    }
}

@Composable
fun ContentSizeDemo() {
    var message by remember {
        mutableStateOf("Hello")
    }

    Box(
        modifier = Modifier
            .background(Color.Blue)
            .animateContentSize()
            .clickable {
                message += "Hello".repeat(10)
            }
    ) {
        Text(text = message)
    }

}

@Composable
fun CrossfadeDemo() {
    var currentPage by remember {
        mutableStateOf("A")
    }

    Crossfade(targetState = currentPage,modifier = Modifier.clickable {
        currentPage = if(currentPage == "A") "B" else "A"
    }) {
        when(it) {
            "A" -> Text("Page A".repeat(20))
            "B" -> Text("Page B".repeat(30))
        }
    }

}

@Composable
fun GestureDemo() {
    val offset = remember { Animatable(Offset(0f, 0f), Offset.VectorConverter) }
    Box(
        modifier = Modifier
            .size(200.dp)
            .pointerInput(Unit) {
                coroutineScope {
                    while (true) {
                        // Detect a tap event and obtain its position.
                        val position = awaitPointerEventScope {
                            awaitFirstDown().position
                        }
                        launch {
                            // Animate to the tap position.
                            offset.animateTo(position)
                        }
                    }
                }
            }
    ) {
        Text(text="Text",modifier = Modifier.border(BorderStroke(2.dp,Color.Blue)).offset { offset.value.toIntOffset() })
    }
}

private fun Offset.toIntOffset() = IntOffset(x.roundToInt(), y.roundToInt())


@Composable
fun AnimatableDemo() {
    var enabled by remember {
        mutableStateOf(true)
    }
    val color = remember { Animatable(Color.Gray) }
    LaunchedEffect(enabled) {
        color.animateTo(if (enabled) Color.Green else Color.Red)
    }
    Box(
        Modifier
            .size(100.dp)
            .background(color.value))
    
}

enum class BoxState { Collapsed, Expanded }

@Composable
fun AnimatingBoxDemo() {
    var boxState by remember{
        mutableStateOf(BoxState.Collapsed)
    }
    var transitionData =  updateTransitionData(boxState)
    // UI tree
    Box(
        modifier = Modifier
            .background(transitionData.color)
            .size(transitionData.size)
            .clickable {
                boxState = if (boxState == BoxState.Collapsed) BoxState.Expanded else BoxState.Collapsed
            }
    ){
       Text("color:${transitionData.size},dp:${transitionData.color.toArgb()}")
    }
}

// Holds the animation values.
private class TransitionData(
    color: State<Color>,
    size: State<Dp>
) {
    val color by color
    val size by size
}

// Create a Transition and return its animation values.
@Composable
private fun updateTransitionData(boxState: BoxState): TransitionData {
    val transition = updateTransition(boxState)
    val color = transition.animateColor { state ->
        when (state) {
            BoxState.Collapsed -> Color.Gray
            BoxState.Expanded -> Color.Red
        }
    }
    val size = transition.animateDp { state ->
        when (state) {
            BoxState.Collapsed -> 64.dp
            BoxState.Expanded -> 128.dp
        }
    }
    return remember(transition) { TransitionData(color, size) }
}

@Composable
fun CircleAnimDemo() {

    val infiniteTransition = rememberInfiniteTransition()
    val size by infiniteTransition.animateFloat(
        initialValue = 100f,
        targetValue = 200f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000,easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val color by infiniteTransition.animateColor(
        initialValue = Color.Red,
        targetValue = Color.Green,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        Modifier
            .size(size.dp)
            .background(color))
}
enum class SurfaceState { Pressed, Released }

@Preview
@Composable
fun PressedSurface() {

    val (pressed, onPress) = remember { mutableStateOf(false) }
    val transition = updateTransition(
        targetState = if (pressed) SurfaceState.Pressed else SurfaceState.Released
    )

    val width by transition.animateDp { state ->
        when (state) {
            SurfaceState.Released -> 20.dp
            SurfaceState.Pressed -> 50.dp
        }
    }
    val surfaceColor by transition.animateColor { state ->
        when (state) {
            SurfaceState.Released -> Color.Blue
            SurfaceState.Pressed -> Color.Red
        }
    }
    val selectedAlpha by transition.animateFloat { state ->
        when (state) {
            SurfaceState.Released -> 0.5f
            SurfaceState.Pressed -> 1f
        }
    }

    Surface(
        color = surfaceColor.copy(alpha = selectedAlpha),
        modifier = Modifier
            .toggleable(value = pressed, onValueChange = onPress)
            .size(height = 200.dp, width = width)
    ){}
}

@Preview
@OptIn(ExperimentalAnimationApi::class)
@ExperimentalMaterialApi
@Composable
fun TransitionDemo() {
    var selected by remember { mutableStateOf(false) }
// Animates changes when `selected` is changed.
    val transition = updateTransition(selected)
    val borderColor by transition.animateColor { isSelected ->
        if (isSelected) Color.Magenta else Color.White
    }
    val elevation by transition.animateDp { isSelected ->
        if (isSelected) 10.dp else 2.dp
    }
    Surface(
        onClick = { selected = !selected },
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(2.dp, borderColor),
        elevation = elevation
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
            Text(text = "Hello, world!")
            // AnimatedVisibility as a part of the transition.
            transition.AnimatedVisibility(
                visible = { targetSelected -> targetSelected },
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Text(text = "It is fine today.")
            }
            // AnimatedContent as a part of the transition.
            transition.AnimatedContent { targetState ->
                if (targetState) {
                    Text(text = "Selected")
                } else {
                    Icon(imageVector = Icons.Default.Phone, contentDescription = "Phone")
                }
            }
        }
    }
}
@Composable
fun FloatValueDemo() {
    var enabled by remember {
        mutableStateOf(true)
    }
    val alpha: Float by animateFloatAsState(if (enabled) 1f else 0.5f,
    keyframes {
        durationMillis = 4000
    })
    Box(
        Modifier
            .fillMaxSize()
            .graphicsLayer(alpha = alpha)

            .background(Color.Red)
            .clickable {
                enabled = !enabled
            }

    ) {
        Text(text = "${alpha}")
    }

}

@ExperimentalMaterialApi
@OptIn(ExperimentalAnimationApi::class)
@Preview
@Composable
fun AnimationDemo() {
    LazyColumn {
        item {
            Text("Animation Demo",textAlign = TextAlign.Center, modifier = Modifier
                .fillMaxWidth()
                .background(Color.Green))
        }
        item{
            var extended by remember { mutableStateOf(true)}
            GestureDemo()
            Divider(Modifier.height(2.dp))
            AnimatingBoxDemo()
            Divider(Modifier.height(2.dp))
            CircleAnimDemo()
            Divider(Modifier.height(2.dp))
            PressedSurface()
            Divider(Modifier.height(2.dp))
            TransitionDemo()
            Divider(Modifier.height(2.dp))
            AnimatableDemo()
            Divider(Modifier.height(2.dp))
            FloatValueDemo()
            Divider(Modifier.height(2.dp))
            CrossfadeDemo()
            Divider(Modifier.height(2.dp))
            ContentSizeDemo()
            Divider(Modifier.height(2.dp))
            AnimatedContentDemoV2()
            Divider(Modifier.height(2.dp))
            AnimatedContentDemo()
            Divider(Modifier.height(2.dp))
            HomeFloatingActionButton(extended = extended){
                extended = !extended
            }
            Divider(Modifier.height(2.dp))
            HomeFloatingActionButton(false){

            }
            Divider(Modifier.height(2.dp))
            HomeFloatingActionButtonV2(extended = extended) {

            }
            Divider(Modifier.height(2.dp))
            ColorDemo()
            Divider(Modifier.height(2.dp))
            FullScreenNotification(visible = extended)
        }
    }

}
