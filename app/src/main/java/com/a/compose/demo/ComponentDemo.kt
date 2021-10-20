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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.a.compose.ui.theme.ComposeTheme
import java.util.*


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
    var showDialog by remember { mutableStateOf(false) }
    val alertMessage = "Heads up, you've used up 90% of your Shopping budget for this month."

    LazyColumn() {
        item{
            AnimatedIconRow()
            if (showDialog) {
                RallyAlertDialog(
                    onDismiss = {
                        showDialog = false
                    },
                    bodyText = alertMessage,
                    buttonText = "Dismiss".uppercase(Locale.getDefault())
                )
            }
            Text("显示对话框",modifier = Modifier.clickable { showDialog = true })
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


@Composable
fun RallyAlertDialog(
    onDismiss: () -> Unit,
    bodyText: String,
    buttonText: String
) {
    ComposeTheme {
        AlertDialog(
            onDismissRequest = onDismiss,
            text = { Text(bodyText) },
            buttons = {
                Column {
                    Divider(
                        Modifier.padding(horizontal = 12.dp),
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
                    )
                    TextButton(
                        onClick = onDismiss,
                        shape = RectangleShape,
                        contentPadding = PaddingValues(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(buttonText)
                    }
                }
            }
        )
    }
}
