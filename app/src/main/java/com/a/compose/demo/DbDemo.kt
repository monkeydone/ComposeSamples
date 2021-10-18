package com.a.compose.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.a.compose.CommonButton
import com.a.compose.Graph
import com.a.compose.TodoItem
import com.a.compose.db.Category
import com.a.compose.generateRandomTodoItem
import kotlinx.coroutines.launch
import java.nio.file.attribute.GroupPrincipal
import kotlin.reflect.KProperty


@Composable
fun DbDemo() {
    val coroutineScope = rememberCoroutineScope()
    var list2 = remember {
        mutableStateListOf<Category>().apply {
            coroutineScope.launch {
                addAll(Graph.categoryStore.listCategory())
            }
        }
    }

    Column {
        Text("DbDemo",textAlign = TextAlign.Center, modifier = Modifier
            .fillMaxWidth()
            .background(Color.Green))
        Divider(Modifier.height(2.dp))
        Text("Add Category",Modifier.clickable {
            val item = generateRandomTodoItem()
            val category = Category(name = item.task)
            coroutineScope.launch {
                Graph.categoryStore.addCategory(category = category)
                list2.addAll(Graph.categoryStore.listCategory())
            }
        })
        Divider(Modifier.height(2.dp))
        for(i in list2) {
            CommonButton("${i.name}  ${i.id}",{})
        }
        Divider(Modifier.height(2.dp))
    }
}

