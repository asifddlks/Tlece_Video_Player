package com.asifddlks.tlecevideoplayer.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.asifddlks.tlecevideoplayer.model.VideoModel

@Dao
interface VideoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideos(videos: List<VideoModel>)

    @Query("SELECT * FROM videos")
    suspend fun getAllVideos(): List<VideoModel>
}