package com.a.compose.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.network.HttpException
import com.a.compose.ComposeApplication
import com.a.compose.Graph
import com.a.compose.MainDestinations
import com.a.compose.component.SampleItem
import com.a.compose.component.SampleList
import com.a.compose.repo.NetworkService
import com.a.compose.repo.Plant
import com.a.compose.utils.await
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.Utf8
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

@Preview
@Composable
fun NetworkDemo() {
    val viewModel = PlantViewModel()

    val progressState = viewModel.liveProgress.observeAsState(false)
    SampleList(title = "网络测试") {
        FetchData()
        ListObjectFetchUrl(viewModel)
    }
    if(progressState.value) {
       ProgressDialog()
    }
}

@Composable
fun ListObjectFetchUrl(viewModel: PlantViewModel) {

    val liveList = viewModel.livePlants.observeAsState()
    SampleItem("点击从网络获取对象",onClick = {
        viewModel.fetchPlants()
    }) {
        Column() {
            liveList.value?.let {
                for(i in it) {
                    Text("name:${i.name}")
                }
            }
        }

    }

}



@Composable
fun FetchData() {
    val coroutineScope = rememberCoroutineScope()
    var text by remember {
        mutableStateOf("")
    }

    SampleItem("点击获取内容",onClick = {
        coroutineScope.launch {
            val result = fetch("https://fragmentedpodcast.com/feed/")
            if(result is PodcastRssResponse.Success){
                text = result.podcast
            }else {
                if(result is PodcastRssResponse.Error) {
                    text = result.throwable.toString()
                }else {
                    text = "error"
                }
            }
        }
    }) {
        Text(text = text)
    }
}


private val cacheControl by lazy {
    CacheControl.Builder().maxStale(8, TimeUnit.HOURS).build()
}

sealed class PodcastRssResponse {
    data class Error(
        val throwable: Throwable?,
    ) : PodcastRssResponse()

    data class Success(
        val podcast: String,
    ) : PodcastRssResponse()
}

private suspend fun fetch(url: String): PodcastRssResponse {
    val request = Request.Builder()
        .url(url)
        .cacheControl(cacheControl)
        .build()

    val response = Graph.okHttpClient.newCall(request).await()

    // If the network request wasn't successful, throw an exception
    if (!response.isSuccessful) throw HttpException(response)

    // Otherwise we can parse the response using a Rome SyndFeedInput, then map it
    // to a Podcast instance. We run this on the IO dispatcher since the parser is reading
    // from a stream.
    return withContext(Graph.ioDispatcher) {
        response.body!!.use { body ->
            val b = body.byteString().string(Charset.defaultCharset())
            PodcastRssResponse.Success(b)
        }
    }
}


class PlantViewModel:ViewModel() {
    val livePlants = MutableLiveData<List<Plant>>()
    val liveProgress = MutableLiveData<Boolean>()


    fun fetchPlants() {
        viewModelScope.launch {
            withContext(Graph.mainDispatcher) {
//                liveProgress.value = true
            }
            withContext(Graph.ioDispatcher) {
                val n = NetworkService()
                val list = n.allPlants()
                withContext(Graph.mainDispatcher) {
                    livePlants.value = list
//                    liveProgress.value = false

                }
            }

        }
    }


}