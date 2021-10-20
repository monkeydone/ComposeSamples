package com.a.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.a.compose.demo.DbDemo
import com.a.compose.demo.LayoutCompose
import com.a.compose.demo.NetworkDemo


interface PepoMenu {
    fun getMenuList():List<Menu>
}

class RepoMenuImpl:PepoMenu {
    @ExperimentalMaterialApi
    override fun getMenuList(): List<Menu> {
        return ArrayList<Menu>().apply {
            add(Menu(RouteName.NAME_HOME, RoutePath.PATH_HOME, Icons.Filled.Home) {
                PlaceView(
                    RoutePath.PATH_HOME
                )
            })
            add(Menu("简单布局", RoutePath.PATH_LIST, Icons.Filled.List) {
                LayoutCompose()
            })
            add(Menu(RouteName.NAME_NETWORK, RoutePath.PATH_NETWORK, Icons.Filled.Phone) {
               NetworkDemo()
            })
            add(Menu(RouteName.NAME_SIMPLE_VIEW, RoutePath.PATH_TEXT, Icons.Filled.Create) {
               TextDemo()
            })
            add(Menu(RouteName.NAME_STATE, RoutePath.PATH_STATE, Icons.Filled.List) {
//                PlaceView(
//                    RoutePath.PATH_STATE
//                )
                StateDemo()
            })
            add(Menu(RouteName.NAME_GESTURE, RoutePath.PATH_GESTURE, Icons.Filled.DateRange) {
                GestureDemo()
            })

            add(Menu("动画", RoutePath.PATH_ANIMATION, Icons.Filled.AddCircle) {
                AnimationDemo()
            })
            add(Menu("绘制", RoutePath.PATH_DRAW, Icons.Filled.LocationOn) {
                ShapeDemo()
            })
            add(Menu("viewModel例子", RoutePath.PATH_VIEW_MODEL, Icons.Filled.Place) {
                ViewModelDemo()
            })
            add(Menu("Room", RoutePath.PATH_DB, Icons.Filled.Build) {
                DbDemo()
            })
            add(Menu(RouteName.NAME_MERGE, RoutePath.PATH_MERGE, Icons.Filled.List) {
                PlaceView(
                    RoutePath.PATH_MERGE
                )
            })

//            add(Menu("混合代码", RoutePath.PATH_MERGE, Icons.Filled.Build) {
//                DbDemo()
//            })
        }
    }

}

data class Menu(val name:String, val routePath:String, val icon: ImageVector,val view:@Composable ()->Unit)

object RoutePath {
    const val PATH_HOME = "home"
    const val PATH_LIST = "list"
    const val PATH_TEXT = "text"
    const val PATH_STATE = "state"
    const val PATH_GESTURE = "gesture"
    const val PATH_NETWORK = "network"
    const val PATH_ANIMATION = "animation"
    const val PATH_DRAW = "draw"
    const val PATH_LAYOUT = "layout"
    const val PATH_VIEW_MODEL = "viewmodel"
    const val PATH_DB = "db"
    const val PATH_MERGE = "merge"
}

object RouteName {
    const val NAME_HOME = "主页"
    const val NAME_LIST = "列表"
    const val NAME_SIMPLE_VIEW = "简单视图"
    const val NAME_STATE = "状态例子"
    const val NAME_GESTURE = "手势"
    const val NAME_NETWORK = "网络"
    const val NAME_MERGE = "混合代码"
}

@Preview()
@Composable
fun PlaceView(text:String = "Demo") {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Red),contentAlignment = Alignment.Center) {
        Text(text = text)
    }
    
}
