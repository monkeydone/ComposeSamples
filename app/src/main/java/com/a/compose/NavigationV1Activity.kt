package com.a.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.a.compose.ui.theme.ComposeTheme
import kotlinx.coroutines.delay

class NavigationV1 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    LandingScreen() {
                        
                    }
                    MainApp()
                }
            }
        }
    }
}

private const val SplashWaitTime: Long = 2000*5

@Composable
fun LandingScreen(modifier: Modifier = Modifier, onTimeout: () -> Unit) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        // Adds composition consistency. Use the value when LaunchedEffect is first called
        val currentOnTimeout by rememberUpdatedState(onTimeout)

        LaunchedEffect(Unit) {
            delay(SplashWaitTime)
            currentOnTimeout()
        }
        Image(painterResource(id = R.drawable.ic_crane_drawer), contentDescription = null)
    }
}


@Composable
fun MainApp() {
    val allScreens = RallyScreen.values().toList()
    var currentScreen by remember { mutableStateOf(RallyScreen.Overview) }
    Scaffold(
        topBar = {
            RallyTabRow(
                allScreens = allScreens,
                onTabSelected = { screen -> currentScreen = screen },
                currentScreen = currentScreen
            )
        }
    ) { innerPadding ->
        Box(
            Modifier
                .padding(innerPadding)
                .background(Color.Blue)
                .fillMaxSize(),contentAlignment = Alignment.Center) {
            currentScreen.content(
                onScreenChange = { screen ->
                    currentScreen = RallyScreen.valueOf(screen)
                }
            )
        }
    }
}

@Composable
fun RallyTabRow(
    allScreens: List<RallyScreen>,
    onTabSelected: (RallyScreen) -> Unit,
    currentScreen: RallyScreen
) {
    Surface(
        Modifier
            .height(56.dp)
            .fillMaxWidth()
    ) {
        Row(Modifier.selectableGroup()) {
            allScreens.forEach { screen ->
                val bg = if(currentScreen == screen) Color.Blue else Color.Red
                Text(text = screen.name,modifier = Modifier
                    .background(bg)
                    .clickable {
                        onTabSelected(screen)
                    })
            }
        }
    }
}

@Composable
fun EmptyView(name: String) {
    Text(text = name)
}


enum class RallyScreen(
    name: String,
    val body: @Composable ((String) -> Unit) -> Unit
) {
    Overview(
        name = "Overview",
        body = { com.a.compose.EmptyView(name = "Overview")}
    ),
    Accounts(
        name = "Account",
        body = { com.a.compose.EmptyView(name = "Account") }
    ),
    Bills(
        name = "Bills",
        body = { com.a.compose.EmptyView(name = "Bills") }
    );

    @Composable
    fun content(onScreenChange: (String) -> Unit) {
        body(onScreenChange)
    }

    companion object {
        fun fromRoute(route: String?): RallyScreen =
            when (route?.substringBefore("/")) {
                Accounts.name -> Accounts
                Bills.name -> Bills
                Overview.name -> Overview
                null -> Overview
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
    }
}
