package com.a.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@ExperimentalAnimationApi
@Preview
@Composable
fun ComponentDemo() {
    Column {
        Text("ComponentDemo")
        ComponentListDemo()
    }
}

@ExperimentalAnimationApi
@Composable
fun ComponentListDemo() {
    LazyColumn() {
        item{
            AnimatedIconRow()
        }

    }

}

@ExperimentalAnimationApi
@Composable
fun AnimatedIconRow(
    modifier: Modifier = Modifier,
    visible: Boolean = true,
) {
    var todoIcon by remember {
        mutableStateOf(TodoIcon.Done)
    }

    val enter = remember { fadeIn(animationSpec = TweenSpec(3000, easing = FastOutLinearInEasing)) }
    val exit = remember { fadeOut(animationSpec = TweenSpec(1000, easing = FastOutSlowInEasing)) }
    Box(modifier.size(50.dp).background(Color.Green).clickable {
        if(todoIcon == TodoIcon.Done) TodoIcon.Event else TodoIcon.Done
    }) {
        AnimatedVisibility(
            visible = visible,
            enter = enter,
            exit = exit,
        ) {
            Icon(todoIcon.imageVector,contentDescription = "")
        }
    }
}
