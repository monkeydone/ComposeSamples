package com.a.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel


class TodoViewModel:ViewModel(){
    var count = 0
    var data = CountData(0,"data")

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
                modelSetting(model.copy(count=viewModel.data.count))
            })

}

data class MyThing(var name: String = "Ma", var age: Int = 0)

@Composable
fun DataClassDemo() {
    val (myThing, myThingSetter) = remember { mutableStateOf(MyThing()) }

    Column {
        Text(text = myThing.name)
        // button to add "a" to the end of the name
        Button(onClick = { myThingSetter(myThing.copy(name = myThing.name + "a")) }) {
            Text(text = "Add an 'a'")
        }
        // button to increment the new "age" field by 1
        Button(onClick = { myThingSetter(myThing.copy(age = myThing.age + 1)) }) {
            Text(text = "Increment age")
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