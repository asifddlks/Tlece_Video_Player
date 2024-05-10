package com.asifddlks.tlecevideoplayer.data.repository

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.asifddlks.tlecevideoplayer.VideoPlayerApplication
import com.asifddlks.tlecevideoplayer.data.remote.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DataUpdateWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        try {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://gist.githubusercontent.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiService = retrofit.create(ApiService::class.java)

            val response = apiService.fetchVideos()

            if (response.isNotEmpty()) {
                /*val videoEntities = response.map { video ->
                    VideoEntity(video.id, video.title, video.description, video.thumbnailUrl)
                }*/
                val videoEntities = response
                val database = VideoPlayerApplication.database
                val dao = database.videoDao()
                dao.insertVideos(videoEntities)
            }

            return Result.success()
        } catch (e: Exception) {
            return Result.failure()
        }
    }
}