package com.a.compose.demo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
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

    Column() {
       Text("DbDemo")

        Text("Add Category",Modifier.clickable {
            val item = generateRandomTodoItem()
            val category = Category(name = item.task)
            coroutineScope.launch {
                Graph.categoryStore.addCategory(category = category)
                list2.addAll(Graph.categoryStore.listCategory())
            }
        })

        for(i in list2) {
            CommonButton("${i.name}  ${i.id}",{})
        }



    }
}

