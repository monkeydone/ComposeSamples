package com.a.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Preview
@Composable
fun ViewModelDemo() {
    val coroutineScope = rememberCoroutineScope()
    val viewModel = viewModel(ComposeViewModel::class.java)

    val flowCount by viewModel.flowCount.collectAsState()
    val flowObjectState by viewModel._flowState.collectAsState()

    var count by remember {
        viewModel.composeCount
    }

    var (composeState,stateSet) = remember {
        viewModel.composeObjectState
    }

    var composeList   =  remember {
        viewModel.composeListState
    }


    Column() {
        Text("ViewModelDemo",textAlign = TextAlign.Center, modifier = Modifier
            .fillMaxWidth()
            .background(Color.Green))

        Divider(Modifier.height(2.dp))
        Text("viewModel点击响应",modifier = Modifier
            .padding(all = 8.dp)
            .clickable {
                count += 1
            })
        Divider(Modifier.height(2.dp))
        Text("$count")

        Divider(Modifier.height(2.dp))
        Text("compose状态同步点击",modifier = Modifier
            .padding(all = 8.dp)
            .clickable {
                stateSet(composeState.copy(count = composeState.count + 1))
            })
        Divider(Modifier.height(2.dp))
        Text("${composeState.name} ${composeState.count}")

        Divider(Modifier.height(2.dp))
        Text("compose list 点击",modifier = Modifier
            .padding(all = 8.dp)
            .clickable {
                composeList.add(ComposeViewState("List1", count = 0))
            })
        Divider(Modifier.height(2.dp))
        var index = 0
        for(i in composeList) {
            index++
            Text("${index} ${i.name} ${i.count}")
        }



        Divider(Modifier.height(2.dp))
        Text("Flow点击响应",modifier = Modifier
            .padding(all = 8.dp)
            .clickable {
                viewModel.flowCount.value += 1
            })
        Divider(Modifier.height(2.dp))
        Text(text = "Flow点击响应 ${flowCount}")


        Divider(Modifier.height(2.dp))
        Text("Flow对象同步点击",modifier = Modifier
            .padding(all = 8.dp)
            .clickable {
//                viewModel._flowState.value = ComposeViewState("点击响应",count = viewModel._flowState.value.count+1)
                viewModel._flowState.value = viewModel._flowState.value.copy(name="另外一种同步方式",count = viewModel._flowState.value.count+1)
            })
        Divider(Modifier.height(2.dp))
        Text("${flowObjectState.name} ${flowObjectState.count}")

    }
}


data class ComposeViewState(val name:String="",var count:Int = 0)

class ComposeViewModel:ViewModel() {


    var composeCount = mutableStateOf(0)
    var composeObjectState = mutableStateOf(ComposeViewState(name="compose状态同步点击"))
    var composeListState = mutableStateListOf<ComposeViewState>()



    var flowCount = MutableStateFlow(0)
    val _flowState = MutableStateFlow(ComposeViewState(name="Flow点击响应"))
    val flowState :StateFlow<ComposeViewState>
         get() = _flowState
    fun updateFlowCount() {
        _flowState.value = ComposeViewState("Flow对象点击响应",count = _flowState.value.count+1)
    }

    val list = ArrayList<String>().apply {
        add("list1")
    }
}