package com.a.compose.demo

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.a.compose.LeftMenu
import com.a.compose.R
import com.a.compose.ShapeDemo
import com.a.compose.component.SampleItem
import com.a.compose.component.SampleList
import kotlinx.coroutines.delay

@Preview
@Composable
fun ComposeLifeDemo() {
    SampleList(title = "生命周期和高级状态的例子") {
        SampleItem("生命周期测试2") {
            LifeDemoTest2()
        }

        SampleItem("CompositionLocal的例子") {
            CompositionLocalDemo()
        }
        SampleItem("使用Saver保存数据"){
            CraneEditableUserInput()
        }
        SampleItem("Loading屏幕") {
            MainScreen()
        }
        SampleItem("Compose生命周期") {
            LifeDemo()
        }


    }

}


@Composable
fun LifeDemoTest2() {
    var hide by remember {
        mutableStateOf(false)
    }
    Column() {
        EffectCountV2(text = "点击") {
            hide = !hide
        }
        if(!hide){
            EffectCountV2(text = "隐藏")
        }

    }
    
}


@Composable
fun rememberEditableUserInputState(hint: String): EditableUserInputState =
    rememberSaveable(hint, saver = EditableUserInputState.Saver) {
        EditableUserInputState(hint, hint)
    }

class EditableUserInputState(private val hint: String, initialText: String) {

    var text by mutableStateOf(initialText)

    val isHint: Boolean
        get() = text == hint

    companion object {
        var m = Pair("init","init2")
        val Saver: Saver<EditableUserInputState, *> = listSaver(
            save = {
                m=Pair(it.hint,it.text)
                listOf(it.hint, it.text) },
            restore = {
                EditableUserInputState(
                    hint = m.first,
                    initialText = m.second,
                )
            }
        )
    }
}

@Composable
fun CraneEditableUserInput(
    state: EditableUserInputState = rememberEditableUserInputState(""),
    caption: String? = null,
    @DrawableRes vectorImageId: Int? = null
) {
    CraneBaseUserInput(
        caption = caption,
        tintIcon = { !state.isHint },
        showCaption = { !state.isHint },
        vectorImageId = vectorImageId
    ) {
        BasicTextField(
            value = state.text,
            onValueChange = { state.text = it },
            textStyle = MaterialTheme.typography.body1.copy(color = LocalContentColor.current),
            cursorBrush = SolidColor(LocalContentColor.current)
        )
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CraneBaseUserInput(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { },
    caption: String? = null,
    @DrawableRes vectorImageId: Int? = null,
    showCaption: () -> Boolean = { true },
    tintIcon: () -> Boolean,
    tint: Color = LocalContentColor.current,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier,
        onClick = onClick,
        color = MaterialTheme.colors.primaryVariant
    ) {
        Row(Modifier.padding(all = 12.dp)) {
            if (vectorImageId != null) {
                Icon(
                    modifier = Modifier.size(24.dp, 24.dp),
                    painter = painterResource(id = vectorImageId),
                    tint = if (tintIcon()) tint else Color(0x80FFFFFF),
                    contentDescription = null
                )
                Spacer(Modifier.width(8.dp))
            }
            if (caption != null && showCaption()) {
                Text(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    text = caption,

                )
                Spacer(Modifier.width(8.dp))
            }
            Row(
                Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                content()
            }
        }
    }
}



data class Elevations(val card: Dp = 3.dp, val default: Dp = 0.dp)

// Define a CompositionLocal global object with a default
// This instance can be accessed by all composables in the app
val LocalElevations = compositionLocalOf { Elevations() }


@Composable
fun CompositionLocalDemo() {
    Surface(color = MaterialTheme.colors.primary,modifier = Modifier.fillMaxWidth(),shape = RoundedCornerShape(8.dp)) {
        Column() {
            Text(text = "一段文字 ${LocalElevations.current.card}",modifier = Modifier.padding(3.dp))
            CompositionLocalProvider(LocalElevations provides Elevations(card = 1.dp, default = 1.dp)) {
                Text(text = "使用全局变量的文字一段文字 ${LocalElevations.current.card}")
            }
            Text(text = "最后一段文字 ${LocalElevations.current.card}",modifier = Modifier.padding(3.dp))

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
//            text += "Box Enter\n"
            println("Life: Box LaunchedEffect")

        }

        DisposableEffect(true) {
            onDispose {
//               text += "Boxox Disposable\n"
                println("Life:${text}")
                println("Life: Box DisposableEffect")
            }
        }

        SideEffect {
//            text += "Box update\n"
            println("Life: Box SideEffect")

        }

        LazyColumn {

            item{
                val scope = rememberCoroutineScope()

                EffectCount(mutableState = text)
            }
        }


    }
}

@Composable
fun EffectCountV2(text:String,onClick:()->Unit = {}) {
    
    LaunchedEffect(true) {
       println("LaunchedEffect ${text}")
    }

    DisposableEffect(true ) {
        onDispose {
           println("DisposableEffect ${text}")
        }
    }
    
    SideEffect {
        println("SideEffect ${text}")
    }
    com.a.compose.component.IconButton(text = text,onClick = onClick)

}


@Composable
fun EffectCount(mutableState:String) {
    var text by remember {
        mutableStateOf(mutableState)
    }

    LaunchedEffect(true) {
        text += "EffectCount Enter\n"

    }

    DisposableEffect(true ) {
        onDispose {
            text += "EffectCount Disposable\n"
            println("Effect Life:${text}")
        }
    }
//
    SideEffect {
        text += "EffectCount update\n"
    }


    Text(text = text,modifier = Modifier.clickable {
        text+="Text Click\n"
        println("Click Life:${text}")
    })

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
