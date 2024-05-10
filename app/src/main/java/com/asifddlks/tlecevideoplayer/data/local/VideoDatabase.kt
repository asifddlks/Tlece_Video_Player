package com.asifddlks.tlecevideoplayer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.asifddlks.tlecevideoplayer.model.VideoModel

@Database(entities = [VideoModel::class], version = 1)
abstract class VideoDatabase : RoomDatabase() {
    abstract fun videoDao(): VideoDao

    /*companion object {
        @Volatile
        private var INSTANCE: VideoDatabase? = null

        fun getInstance(context: Context): VideoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VideoDatabase::class.java,
                    "video_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }*/
}