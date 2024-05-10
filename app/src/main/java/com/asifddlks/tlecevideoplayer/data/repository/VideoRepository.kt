package com.asifddlks.tlecevideoplayer.data.repository
import android.util.Log
import com.asifddlks.tlecevideoplayer.VideoPlayerApplication.Companion.apiService
import com.asifddlks.tlecevideoplayer.VideoPlayerApplication.Companion.database
import com.asifddlks.tlecevideoplayer.model.VideoModel

class VideoRepository {

    companion object {
        @Volatile
        private var instance: VideoRepository? = null

        fun getInstance(): VideoRepository {
            return instance ?: synchronized(this) {
                instance ?: VideoRepository().also { instance = it }
            }
        }
    }

   /* private val database = Room.databaseBuilder(
        context.applicationContext,
        VideoDatabase::class.java,
        "video_database"
    ).build()
*/
    private val videoDao = database.videoDao()

    suspend fun refreshVideos() {
        try {
            val videos = apiService.fetchVideos()
            database.videoDao().insertVideos(videos)
        } catch (e: Exception) {
            Log.e("VideoRepository","refreshVideos(): $e")
        }
    }

    suspend fun saveVideos(videos: List<VideoModel>) {
        videoDao.insertVideos(videos)
    }

    suspend fun getVideos(): List<VideoModel> {
        if(videoDao.getAllVideos().isEmpty()){
            refreshVideos()
            return videoDao.getAllVideos()
        }
        else{
            return videoDao.getAllVideos()
        }

    }
}