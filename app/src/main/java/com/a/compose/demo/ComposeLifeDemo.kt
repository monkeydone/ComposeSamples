package com.a.compose.demo

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.a.compose.LeftMenu
import com.a.compose.R
import com.a.compose.ShapeDemo
import com.a.compose.component.SampleItem
import com.a.compose.component.SampleList
import kotlinx.coroutines.delay

@Composable
fun ComposeLifeDemo() {
    SampleList(title = "生命周期和高级状态的例子") {
        SampleItem("Loading屏幕") {
            MainScreen()
        }
        SampleItem("Compose生命周期") {
            LifeDemo()
        }

    }

}


@Composable
fun MainScreen() {
    Surface(color = MaterialTheme.colors.primary,modifier = Modifier.size(300.dp)) {
        var showLandingScreen by remember { mutableStateOf(true) }
        if (showLandingScreen) {
            LandingScreen(onTimeout = { showLandingScreen = false })
        } else {
           ShapeDemo()
        }
    }
}

@Composable
fun LifeDemo() {
    var text by remember {
        mutableStateOf("")
    }
    Box(modifier = Modifier.size(100.dp)){
        LaunchedEffect(true) {
            text += "Box Enter\n"

        }

        DisposableEffect(true ) {
            onDispose {
               text += "Box Disposable\n"
                println("Life:${text}")
            }
        }

        SideEffect {
            text += "Box update\n"
        }

        LazyColumn {
            item{
                Text(text = text,modifier = Modifier.clickable {
                    text+="Text Click\n"
                })
            }
        }


    }
}

private const val SplashWaitTime: Long = 2000

@Composable
fun LandingScreen(modifier: Modifier = Modifier, onTimeout: () -> Unit) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        // Adds composition consistency. Use the value when LaunchedEffect is first called
        val currentOnTimeout by rememberUpdatedState(onTimeout)

        LaunchedEffect(true) {
            delay(SplashWaitTime)
            currentOnTimeout()
        }
        Image(painterResource(id = R.drawable.ic_crane_drawer), contentDescription = null)
    }
}
