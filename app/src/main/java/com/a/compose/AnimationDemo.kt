package com.a.compose

import androidx.compose.animation.*
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

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
    val backgroundColor by animateColorAsState(targetValue = if(colorFlag) Color.Black else Color.Red)

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


@ExperimentalMaterialApi
@OptIn(ExperimentalAnimationApi::class)
@Preview
@Composable
fun AnimationDemo() {
    LazyColumn {
        item {
            Text("Animation Demo", modifier = Modifier
                .fillMaxWidth()
                .background(Color.Green))
        }
        item{
            var extended by remember { mutableStateOf(true)}
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
