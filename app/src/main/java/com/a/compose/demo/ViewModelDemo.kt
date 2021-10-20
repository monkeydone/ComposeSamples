package com.a.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.a.compose.component.IconButton
import com.a.compose.component.SampleItem
import com.a.compose.component.SampleList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@Preview
@Composable
fun ViewModelDemo() {
    val coroutineScope = rememberCoroutineScope()
    val viewModel = viewModel(ComposeViewModel::class.java)



    SampleList(title = "ViewModel中的状态变化同步实例") {
        LazyColumn {
            item{
                Column() {

                    ComposeStateDemo(viewModel = viewModel)

                    ComposeFlowStateDemo(viewModel = viewModel)

//
                }
            }
        }


    }


}

@Composable
fun ComposeFlowStateDemo(viewModel: ComposeViewModel) {
    
    val flowCount by viewModel.flowCount.collectAsState()
    val flowObjectState by viewModel.flowState.collectAsState()
    val flowListState by viewModel.flowListState.collectAsState()
    val coroutineScope = rememberCoroutineScope()



    Column {
        SampleItem("Compose中的flow的例子",style= MaterialTheme.typography.h5) {
        }

        SampleItem("基础类型") {
            IconButton("点我") {
                viewModel.flowCount.value += 1
            }
            Text("Flow点击响应 ${flowCount}",modifier = Modifier
                .padding(all = 8.dp))
        }

        SampleItem("对象类型") {
            Row(horizontalArrangement = Arrangement.SpaceBetween,modifier = Modifier.fillMaxWidth().padding(2.dp)){
                IconButton("同步方式1") {
                    viewModel._flowState.value = viewModel._flowState.value.copy(
                        name = "另外一种同步方式",
                        count = viewModel._flowState.value.count + 1
                    )
                }
                IconButton("同步方式2") {
                    viewModel._flowState.value = ComposeViewState("点击响应",count = viewModel._flowState.value.count+1)
                }

            }
            IconButton("同步方式3") {
                viewModel.updateFlowCount()
            }
            Text("${flowObjectState.name} ${flowObjectState.count}",modifier = Modifier
                .padding(all = 8.dp))
        }




        SampleItem("列表类型") {
            for(j in flowListState.toList().indices) {
                val i = flowListState[j]
                Text("${j} ${i.name} ${i.count}",modifier = Modifier
                    .padding(all = 8.dp))
            }
            IconButton("点我") {
                viewModel.updateFlowList(ComposeViewState("列表Item"))
            }
        }

    }

}

@Composable
fun ComposeStateDemo(viewModel: ComposeViewModel) {
    var count by remember {
        viewModel.composeCount
    }

    var (composeState,stateSet) = remember {
        viewModel.composeObjectState
    }

    var composeList = remember {
        viewModel.composeListState
    }
    
    Column() {
        SampleItem("Compose中的remember例子",style= MaterialTheme.typography.h5) {
        }

        SampleItem("基础类型") {
            IconButton("点我") {
                count += 1
            }
            Text("$count",modifier = Modifier
                .padding(all = 8.dp))
        }

        SampleItem("对象类型") {
            IconButton("点我") {
                stateSet(composeState.copy(count = composeState.count + 1))
            }
            Text("${composeState.name} ${composeState.count}",modifier = Modifier
                .padding(all = 8.dp))
        }



        SampleItem("列表类型") {
            IconButton("点我") {
                composeList.add(ComposeViewState("Compose List1", count = 0))
            }
            for(j in composeList.toList().indices) {
                val i = composeList[j]
                Text("${j} ${i.name} ${i.count}",modifier = Modifier
                    .padding(all = 8.dp))
            }

        }
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
        _flowState.value =  ComposeViewState("点击响应",count = flowState.value.count+1)
    }
    val flowListState = MutableStateFlow(ArrayList<ComposeViewState>())


    fun updateFlowList(data:ComposeViewState) {
        val list = ArrayList<ComposeViewState>()
        list.addAll(flowListState.value)
        list.add(data)
        flowListState.value = list
    }

    val list = ArrayList<String>().apply {
        add("list1")
    }
}