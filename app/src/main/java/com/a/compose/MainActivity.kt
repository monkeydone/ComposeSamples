package com.a.compose

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.a.compose.ui.theme.BaseTheme
import com.a.compose.ui.theme.ComposeTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.jar.Attributes

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            Conversation(messages = messages)
            HelloContent()

//            TodoList()
//            LandingScreen {
//            }
//            MoviesScreen()
//            HelloScreen()
//            MessageCard(msg = Message("Colleague","Hi it body"))
        }
    }
}


@Composable
fun TodoList(
    highPriorityKeywords: List<String> = listOf("Review", "Unblock", "Compose")
) {
    val todoTasks = remember { mutableStateListOf<String>() }

    // Calculate high priority tasks only when the todoTasks or
    // highPriorityKeywords change, not on every recomposition
    val highPriorityTasks by remember(todoTasks, highPriorityKeywords) {
        derivedStateOf {
            todoTasks
//            todoTasks.filter { it.containsWord(highPriorityKeywords) }
        }
    }

    Box(Modifier.fillMaxSize()) {
        LazyColumn {
            items(highPriorityTasks) {
                Text(it)
            }
            items(todoTasks) {
                Text(it,color=MaterialTheme.colors.primary)
            }
        }
        /* Rest of the UI where users can add elements to the list */
    }
}


sealed class WelcomeEvent {
    data class Event1(val text:String):WelcomeEvent()
    object Event2:WelcomeEvent()
}

data class Message(val author:String,val body:String)

@Composable
fun MessageCard(msg:Message) {

    Row() {
//        Image(painter = painterResource(id = R.drawable.ic_launcher_background), contentDescription = "Contact Profile picture")
    }

    Row(modifier = Modifier.padding(all = 8.dp)){

        Image(painter = painterResource(id = R.drawable.ic_launcher_foreground),contentDescription = "photo holder",modifier = Modifier
            .size(20.dp)
            .clip(CircleShape)
            .border(1.5.dp, MaterialTheme.colors.secondary, CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        var isExpanded by remember {
            mutableStateOf(false)
        }
        val surfaceColor2  by animateColorAsState(targetValue = if(isExpanded) MaterialTheme.colors.primary else MaterialTheme.colors.surface)

        Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
            Text(text = msg.author,
            color=MaterialTheme.colors.secondaryVariant)
            Spacer(modifier = Modifier.height(2.dp))
            androidx.compose.material.Surface(shape = MaterialTheme.shapes.medium,color = surfaceColor2,elevation = 10.dp) {
                Text(text = msg.body,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(all = 4.dp),
                    maxLines = if(isExpanded)Int.MAX_VALUE else 1,
                )
            }

        }
    }
}

/**
 * Display a list of names the user can click with a header
 */
@Composable
fun NamePicker(
    header: String,
    names: List<String>,
    onNameClicked: (String) -> Unit
) {
    Column {
        // this will recompose when [header] changes, but not when [names] changes
        Text(header, style = MaterialTheme.typography.h5)
        Divider()

        // LazyColumn is the Compose version of a RecyclerView.
        // The lambda passed to items() is similar to a RecyclerView.ViewHolder.
        LazyColumn {
            items(names) { name ->
                // When an item's [name] updates, the adapter for that item
                // will recompose. This will not recompose when [header] changes
                NamePickerItem(name, onNameClicked)
            }
        }
    }
}

class HelloViewModel : ViewModel() {

    // LiveData holds state which is observed by the UI
    // (state flows down from ViewModel)
    private val _name = MutableLiveData("")
    val name: LiveData<String> = _name

    // onNameChange is an event we're defining that the UI can invoke
    // (events flow up from UI)
    fun onNameChange(newName: String) {
        _name.value = newName
    }
}

data class City(val name: String, val country: String)
@Composable
fun CityScreen() {
    var selectedCity = rememberSaveable {
        mutableStateOf(City("Madrid", "Spain"))
    }
}

val SplashWaitTimeMillis = 3000L
@Composable
fun LandingScreen(onTimeout: () -> Unit) {

    // This will always refer to the latest onTimeout function that
    // LandingScreen was recomposed with
    val currentOnTimeout by rememberUpdatedState(onTimeout)

    // Create an effect that matches the lifecycle of LandingScreen.
    // If LandingScreen recomposes, the delay shouldn't start again.
    LaunchedEffect(true) {
        delay(SplashWaitTimeMillis)
        currentOnTimeout()
    }

    /* Landing screen content */
}

@Composable
fun MoviesScreen(scaffoldState: ScaffoldState = rememberScaffoldState()) {

    // Creates a CoroutineScope bound to the MoviesScreen's lifecycle
    val scope = rememberCoroutineScope()

    Scaffold(scaffoldState = scaffoldState) {
        Column {
            /* ... */
            Button(
                onClick = {
                    // Create a new coroutine in the event handler
                    // to show a snackbar
                    scope.launch {
                        scaffoldState.snackbarHostState
                            .showSnackbar("Something happened!")
                    }
                }
            ) {
                Text("Press me")
            }
        }
    }
}

@Composable
fun HelloScreen(helloViewModel: HelloViewModel = viewModel()) {
    // by default, viewModel() follows the Lifecycle as the Activity or Fragment
    // that calls HelloScreen(). This lifecycle can be modified by callers of HelloScreen.

    // name is the current value of [helloViewModel.name]
    // with an initial value of ""
    val name: String by helloViewModel.name.observeAsState("")
    HelloContent(name = name, onNameChange = { helloViewModel.onNameChange(it) })
}


@Composable
fun HelloContent(name: String, onNameChange: (String) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Hello, $name",
            modifier = Modifier.padding(bottom = 8.dp),
            style = MaterialTheme.typography.h5
        )
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Name") }
        )
    }
}

@Composable
fun HelloContent() {
    var  name by remember {
        mutableStateOf("")
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Hello, $name",
            modifier = Modifier.padding(bottom = 8.dp),
            style = MaterialTheme.typography.h5
        )
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") }
        )
    }
}


@Preview
@Composable
fun Test1() {
    HelloContent()
}


@Preview
@Composable
fun PreviewNamePicker() {
    val names = ArrayList<String>().apply {

    }
    repeat(10) {
        names.add("name ${it}")
    }

    NamePicker(header = "Header View", names =names , onNameClicked ={
    })
}

/**
 * Display a single name the user can click.
 */
@Composable
private fun NamePickerItem(name: String, onClicked: (String) -> Unit) {
    Text(name, Modifier.clickable(onClick = { onClicked(name) }))
}

@Preview(name="Dark Mode",
    showBackground = true,
uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewMessageCard() {
    MessageCard(msg = Message("Colleague","Hi it body2"))
}

@Preview
@Composable
fun PreviewMessageCardLightMode() {
    MessageCard(msg = Message("Colleague","Hi it body2"))
}

@Composable
fun Conversation(messages:List<Message>) {
    LazyColumn {
        items(messages) {
            MessageCard(msg = it)
        }
      
    }
}

@Preview
@Composable
fun PreviewConversation() {
    Conversation(messages = messages)
}

val messages = ArrayList<Message>().apply {
    add(Message("1.message title","1.message body1.message body1.message body1.message body1.message body1.message body"))
    add(Message("2.message title","2.message body2.message body2.message body2.message body2.message body2.message body2.message body"))
    add(Message("3.message title","3.message body"))
    add(Message("4.message title","4.message body"))
    add(Message("5.message title","5.message body"))
}

@Composable
fun welcome() {
    BaseTheme {
        Greeting(name = "Android3")
    }
}

@Composable
fun welcome2(){
    ComposeTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
            Greeting("Android")
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = false)
@Composable
fun DefaultPreview() {
    ComposeTheme {
        Greeting("Android2")
    }
}