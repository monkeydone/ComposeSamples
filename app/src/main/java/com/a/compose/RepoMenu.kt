package com.a.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview


interface PepoMenu {
    fun getMenuList():List<Menu>
}

class RepoMenuImpl:PepoMenu {
    override fun getMenuList(): List<Menu> {
        return ArrayList<Menu>().apply {
            add(Menu(RouteName.NAME_HOME, RoutePath.PATH_HOME, Icons.Filled.Home) {
                PlaceView(
                    RoutePath.PATH_HOME
                )
            })
            add(Menu(RouteName.NAME_LIST, RoutePath.PATH_LIST, Icons.Filled.List) {
                PlaceView(
                    RoutePath.PATH_LIST
                )
            })
            add(Menu(RouteName.NAME_NETWORK, RoutePath.PATH_NETWORK, Icons.Filled.Phone) {
                PlaceView(
                    RoutePath.PATH_NETWORK
                )
            })
        }
    }

}

data class Menu(val name:String, val routePath:String, val icon: ImageVector,val view:@Composable (String)->Unit)

object RoutePath {
    const val PATH_HOME = "home"
    const val PATH_LIST = "list"
    const val PATH_NETWORK = "network"
}

object RouteName {
    const val NAME_HOME = "主页"
    const val NAME_LIST = "列表"
    const val NAME_NETWORK = "网络"
}

@Preview()
@Composable
fun PlaceView(text:String = "Demo") {
    Box(modifier = Modifier.fillMaxSize().background(Color.Red),contentAlignment = Alignment.Center) {
        Text(text = text)
    }
    
}