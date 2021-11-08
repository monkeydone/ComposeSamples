package com.a.compose.demo

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.a.compose.component.IconButton
import com.a.compose.component.SampleItem
import com.a.compose.component.SampleList
import com.a.compose.ui.theme.ComposeTheme
import java.util.*


@Preview
@Composable
fun DialogDemo() {
    val loadingState = remember{
        mutableStateOf(false)
    }
    val openDialogState = remember{
        mutableStateOf(false)
    }

    val retryDialogState = remember{
        mutableStateOf(false)
    }

    SampleList(title = "对话框例子") {
        SampleItem("对话框") {
            IconButton("点我显示对话框"){
                loadingState.value = true
            }
        }
        if(loadingState.value) {
            ComponentListDemo(loadingState)
        }

        SampleItem("对话框2") {
            IconButton("点我显示另外一个对话框"){
                openDialogState.value = true
            }
        }
        if(openDialogState.value) {
            oneDialog(openDialogState)
        }
        SampleItem("对话框3") {
            IconButton("点我显示另外一个对话框"){
                retryDialogState.value = true
            }
        }
        if(retryDialogState.value) {
            OfflineDialog(){
                retryDialogState.value = false
            }
        }
    }

}

@Composable
fun ProgressDialog() {
        Dialog(
            onDismissRequest = {  },
            DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {
            Box(
                contentAlignment= Alignment.Center,
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
            ) {
                CircularProgressIndicator()
            }
        }
}

@Composable
fun OfflineDialog(onRetry: () -> Unit) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text(text = "title") },
        text = { Text(text = "message") },
        confirmButton = {
            TextButton(onClick = onRetry) {
                Text("retry")
            }
        }
    )
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

@Composable
fun oneDialog(openDialogState:MutableState<Boolean>){
    val openDialog = remember { openDialogState }
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

@Composable
fun ComponentListDemo(mutableState: MutableState<Boolean>) {
    val alertMessage = "Heads up, you've used up 90% of your Shopping budget for this month."
    RallyAlertDialog(
                    onDismiss = {
                                mutableState.value = false
                    },
                    bodyText = alertMessage,
                    buttonText = "Dismiss".uppercase(Locale.getDefault())
                )

}