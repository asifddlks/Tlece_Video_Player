package com.asifddlks.tlecevideoplayer

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.room.Room
import com.asifddlks.tlecevideoplayer.data.local.VideoDatabase
import com.asifddlks.tlecevideoplayer.data.remote.ApiService
import com.asifddlks.tlecevideoplayer.data.remote.NetworkConnectionInterceptor
import com.asifddlks.tlecevideoplayer.data.remote.NetworkConnectivityObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class VideoPlayerApplication:Application() {
    companion object {
        private lateinit var _database: VideoDatabase
        val database: VideoDatabase
            get() = _database

        private lateinit var _apiService: ApiService
        val apiService: ApiService
            get() = _apiService
    }

    override fun onCreate() {
        super.onCreate()
        _database = Room.databaseBuilder(
            applicationContext,
            VideoDatabase::class.java,
            "video_database"
        ).build()

        val oktHttpClient: OkHttpClient.Builder = OkHttpClient.Builder()
            .addInterceptor(NetworkConnectionInterceptor(applicationContext))

        val retrofit = Retrofit.Builder()
            .baseUrl("https://gist.githubusercontent.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(oktHttpClient.build())
            .build()

        _apiService = retrofit.create(ApiService::class.java)

        networkConnectivityObserver()
    }

    private fun networkConnectivityObserver() {
        CoroutineScope(Dispatchers.IO).launch {
            NetworkConnectivityObserver(applicationContext).observe().collectLatest {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(this@VideoPlayerApplication, "Connection: $it", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}