package com.a.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import java.util.*


data class TodoItem(
    val task: String,
    val id: UUID = UUID.randomUUID()
)
fun generateRandomTodoItem(): TodoItem {
    val message = listOf(
        "Learn compose",
        "Learn state",
        "Build dynamic UIs",
        "Learn Unidirectional Data Flow",
        "Integrate LiveData",
        "Integrate ViewModel",
        "Remember to savedState!",
        "Build stateless composables",
        "Use state from stateless composables"
    ).random()
    return TodoItem(message)
}



class TodoViewModel:ViewModel(){
    var count = 0
    var data = CountData(0,"data")
    val lists = ArrayList<TodoItem>().apply {
        repeat(5){
            add(generateRandomTodoItem())
        }
    }

    var stateList = mutableStateListOf<TodoItem>().apply {
        addAll(lists)
    }

    fun addItem(item:TodoItem) {
        stateList.add(item)
    }


}

@Preview
@Composable
fun StateDemo(viewModel: TodoViewModel = TodoViewModel()) {
    LazyColumn()  {
        item{
            Text("State Demo",textAlign = TextAlign.Center,modifier = Modifier
                .fillMaxWidth()

            )
        }
        item{
            Divider(Modifier.height(1.dp))
            CountDemo(viewModel = viewModel)
            Divider(Modifier.height(1.dp))
            CountDemoV2()
            Divider(Modifier.height(1.dp))
            CountDemoV3(viewModel = viewModel)
            Divider(Modifier.height(1.dp))
            DataClassDemo()
            Divider(Modifier.height(1.dp))
            CountDemoV4(viewModel = viewModel)
            Divider(Modifier.height(1.dp))

        }
    }

//    TodoScreen(viewModel = viewModel,viewModel.lists)
//    OfflineDialog {  }

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

data class CountData(var count:Int,var name:String)

@Composable
fun CountDemo(viewModel: TodoViewModel) {
    var count by remember {
        mutableStateOf(viewModel.count)
    }

    Text(text="$count",
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Blue)
            .padding(10.dp)
            .clickable {
                viewModel.count += 1
                count = viewModel.count
            })

}

@Composable
fun CountDemoV3(viewModel: TodoViewModel) {
    var (model,modelSetting) = remember {
        mutableStateOf(CountData(0,"name2"))
    }

    Text(text="${model.name} ${model.count}",
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Green)
            .padding(10.dp)
            .clickable {
                viewModel.data.count += 1
                modelSetting(
                    model.copy(
                        name = "${viewModel.data.name} ${viewModel.data.count}",
                        count = viewModel.data.count
                    )
                )

            })

}

@Composable
fun CountDemoV4(viewModel: TodoViewModel) {
    var (model,modelSetting) = remember {
        mutableStateOf(viewModel.data)
    }

    Text(text="${model.name} ${model.count}",
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Green)
            .padding(10.dp)
            .clickable {
                viewModel.data.count += 1
                modelSetting(
                    model.copy(
                        name = "${viewModel.data.name} ${viewModel.data.count}",
                        count = viewModel.data.count
                    )
                )

            })

}

data class MyThing(var name: String = "Ma", var age: Int = 0)

@Composable
fun DataClassDemo() {
    val (myThing, myThingSetter) = remember { mutableStateOf(MyThing()) }

    Column {
        Text(text = "${myThing.name} ${myThing.age}")
        // button to add "a" to the end of the name
        Button(onClick = { myThingSetter(myThing.copy(name = myThing.name + "a")) }) {
            Text(text = "新增A")
        }
        // button to increment the new "age" field by 1
        Button(onClick = { myThingSetter(myThing.copy(age = myThing.age + 1)) }) {
            Text(text = "新增年龄")
        }
    }
}


@Composable
fun TodoScreen(viewModel: TodoViewModel,todoList:ArrayList<TodoItem>) {
    var list by remember {
        mutableStateOf(todoList)
    }
    LazyColumn(contentPadding = PaddingValues(top= 8.dp),modifier = Modifier
        .background(Color.Blue)
        .clickable {
            viewModel.addItem(generateRandomTodoItem())
            viewModel.lists.add(generateRandomTodoItem())
            list = viewModel.lists
        }) {
       for(i in todoList) {
           item {
               Text(text = "${i.task}",modifier = Modifier.background(Color.Green))
           }
       }
    }

}


@Composable
fun CountDemoV2() {
    var count by remember {
        mutableStateOf(0)
    }
    Text(
        text = "Editing item $count",
        style = MaterialTheme.typography.h6,
        textAlign = TextAlign.Center,
        modifier = Modifier
//            .align(Alignment.CenterVertically)
            .padding(16.dp)
            .clickable { count += 1 }
            .fillMaxWidth()
    )
}