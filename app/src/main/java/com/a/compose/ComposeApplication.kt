package com.a.compose

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.a.compose.db.CategoryStore
import com.a.compose.db.ComposeDB
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.LoggingEventListener
import java.io.File

class ComposeApplication:Application() {

    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
    }
}

object Graph {
    lateinit var okHttpClient: OkHttpClient

    lateinit var database: ComposeDB
        private set

    val categoryStore by lazy {
        CategoryStore(
            categoriesDao = database.categoriesDao(),
        )
    }

    val mainDispatcher: CoroutineDispatcher
        get() = Dispatchers.Main

    val ioDispatcher: CoroutineDispatcher
        get() = Dispatchers.IO

    fun provide(context: Context) {
        okHttpClient = OkHttpClient.Builder()
            .cache(Cache(File(context.cacheDir, "http_cache"), 20 * 1024 * 1024))
            .apply {
                if (BuildConfig.DEBUG) eventListenerFactory(LoggingEventListener.Factory())
            }
            .build()

        database = Room.databaseBuilder(context, ComposeDB::class.java, "data.db")
            // This is not recommended for normal apps, but the goal of this sample isn't to
            // showcase all of Room.
            .fallbackToDestructiveMigration()
            .build()
    }
}
