package com.a.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.a.compose.ui.theme.ComposeTheme
import kotlinx.coroutines.launch

class SampleListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposeTheme {
                val scaffoldState = rememberScaffoldState()

                val navController = rememberNavController()
                val coroutineScope = rememberCoroutineScope()

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route ?: MainDestinations.HOME_ROUTE


                Scaffold(
                    scaffoldState = scaffoldState,
                    drawerContent = {
                        LeftMenu(
                        currentRoute = currentRoute,
                        navController = navController,
                        closeDrawer = { coroutineScope.launch { scaffoldState.drawerState.close() } }
                    )
                    }
                ) {
                    AndroidNavGraph(navController = navController,scaffoldState = scaffoldState)
                }
            }
        }
    }
}


@Composable
fun AndroidNavGraph(
    navController: NavHostController = rememberNavController(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    startDestination: String = MainDestinations.HOME_ROUTE
) {
    val container = AppContainerImpl(LocalContext.current)

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        for(i in container.menu.getMenuList()) {
            composable(i.routePath) {
                i.view()
            }
        }
        composable(RoutePath.PATH_HOME) {
            LeftMenu(
                currentRoute = RoutePath.PATH_HOME,
                navController = navController,
                closeDrawer = {  }
            )
        }
        composable(RoutePath.PATH_TEXT) {
            TextDemo()
        }
        composable(RoutePath.PATH_STATE) {
            StateDemo()
        }
        composable(MainDestinations.INTERESTS_ROUTE) {
            Text(MainDestinations.INTERESTS_ROUTE)
        }
        composable("${MainDestinations.ARTICLE_ROUTE}") { backStackEntry ->
            Text(MainDestinations.ARTICLE_ROUTE)
        }
    }
}


@Preview()
@Composable
fun PlaceContent(text:String = "Demo") {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Red),contentAlignment = Alignment.Center) {
        Text(text = text)
    }
}



@Composable
fun LeftMenu(
    currentRoute: String,
    navController: NavController,
    closeDrawer: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(Modifier.height(24.dp))
        Divider(color = MaterialTheme.colors.onSurface.copy(alpha = .2f))
        val container = AppContainerImpl(LocalContext.current)
        for(i in container.menu.getMenuList()) {
            DrawerButton(
                icon = i.icon,
                label = i.name,
                isSelected = currentRoute == i.routePath,
                action = {
                    navController.navigate(i.routePath)
                    closeDrawer()
                }
            )
        }
    }
}

@Composable
private fun DrawerButton(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    action: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colors
    val imageAlpha = if (isSelected) {
        1f
    } else {
        0.6f
    }
    val textIconColor = if (isSelected) {
        colors.primary
    } else {
        colors.onSurface.copy(alpha = 0.6f)
    }
    val backgroundColor = if (isSelected) {
        colors.primary.copy(alpha = 0.12f)
    } else {
        Color.Transparent
    }

    val surfaceModifier = modifier
        .padding(start = 8.dp, top = 8.dp, end = 8.dp)
        .fillMaxWidth()
    Surface(
        modifier = surfaceModifier,
        color = backgroundColor,
        shape = MaterialTheme.shapes.small
    ) {
        TextButton(
            onClick = action,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    imageVector = icon,
                    contentDescription = null, // decorative
                    colorFilter = ColorFilter.tint(textIconColor),
                    alpha = imageAlpha
                )
                Spacer(Modifier.width(16.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.body2,
                    color = textIconColor
                )
            }
        }
    }
}
