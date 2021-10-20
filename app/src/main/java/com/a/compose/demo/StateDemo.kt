package com.a.compose

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction.Companion.Done
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.a.compose.component.SampleList
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
        repeat(1){
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
    SampleList(title = "State Demo") {
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
        ListTestV1()
        Divider(Modifier.height(1.dp))
        ListTestV2(viewModel = viewModel)
        Divider(Modifier.height(1.dp))
        ListTestV3(viewModel = viewModel)
        Divider(Modifier.height(1.dp))
        ListTestV4(viewModel = viewModel,viewModel::addItem)
    }
}

@Composable
fun ListTestV3(viewModel: TodoViewModel) {
    var list2 = remember {
        viewModel.stateList
    }

    var count by remember {
        mutableStateOf(0)
    }

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp,8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .clickable {
                viewModel.addItem(generateRandomTodoItem())
                count = list2.size
            }
            .background(Color.Red)) {
        items(items = list2) { i ->
            Text("Item ${i.task} ${i.id} $count",modifier= Modifier
                .clip(CircleShape)
                .background(Color.Green))
        }
    }
}

@Composable
fun ListTestV4(viewModel: TodoViewModel,onAddItem:(TodoItem)->Unit) {
    var list2 = remember {
        viewModel.stateList
    }

    var count by remember {
        mutableStateOf(0)
    }

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp,8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .clickable {
//                viewModel.addItem(generateRandomTodoItem())
                onAddItem(generateRandomTodoItem())
                count = list2.size
            }
            .background(Color.Red)) {
        items(items = list2) { i ->
            Text("Item ${i.task} ${i.id} $count",modifier= Modifier
                .clip(CircleShape)
                .background(Color.Green))
        }
    }
}


@Composable
fun ListTestV2(viewModel: TodoViewModel) {
    var list2 = remember {
        mutableStateListOf<TodoItem>().apply {
            addAll(viewModel.lists)
        }
    }

    var count by remember {
        mutableStateOf(0)
    }

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp,8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .clickable {
                list2.add(generateRandomTodoItem())
                count = list2.size
            }
            .background(Color.Blue)) {
        items(items = list2) { i ->
            Text("Item ${i.task} ${i.id} $count",modifier= Modifier.background(Color.Green))
        }
    }
}


@Composable
fun ListTestV1() {

    val l = ArrayList<String>().apply {
        add("Item 1")
        add("Item 2")
    }
    var list = remember {
        mutableListOf("").apply {
            addAll(l)
        }
    }

    var list2 = remember {
        mutableStateListOf("").apply {
            addAll(l)
        }
    }

    var count by remember {
        mutableStateOf(0)
    }

    LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp,8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .clickable {
                list2.add("test")
                count = list2.size
            }
            .background(Color.Blue)) {
        items(items = list2) { i ->
           Text("Item $i $count")
       }
    }
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
fun TodoScreen(viewModel: TodoViewModel) {
    val (tasks,tasksHandler) = remember {
        mutableStateListOf(viewModel.lists)
    }
    Column(modifier = Modifier
        .background(Color.Blue)
        .clickable {
            viewModel.addItem(generateRandomTodoItem())
            viewModel.lists.add(generateRandomTodoItem())
        }) {
       for(i in viewModel.lists) {
           Text(text = "${i.task}",modifier = Modifier.background(Color.Green))
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


enum class TodoIcon(val imageVector: ImageVector, val todoName:String) {
    Square(Icons.Default.AccountBox,"Box"),
    Event(Icons.Default.Edit,"Edit"),
    Done(Icons.Default.Done, "Done");


    companion object {
        val Default = Square
    }
}
