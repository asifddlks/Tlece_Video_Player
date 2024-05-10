package com.asifddlks.tlecevideoplayer

import android.app.Application
import androidx.room.Room
import com.asifddlks.tlecevideoplayer.data.local.VideoDatabase
import com.asifddlks.tlecevideoplayer.data.remote.ApiService
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

        val retrofit = Retrofit.Builder()
            .baseUrl("https://gist.githubusercontent.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        _apiService = retrofit.create(ApiService::class.java)
    }
}