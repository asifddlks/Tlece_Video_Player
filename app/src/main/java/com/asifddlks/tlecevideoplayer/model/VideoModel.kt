package com.asifddlks.tlecevideoplayer.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "videos")
data class VideoModel(
    @PrimaryKey val id: String,
    val title: String,
    val thumbnailUrl: String,
    val duration: String,
    val uploadTime: String,
    val views: String,
    val author: String,
    val videoUrl: String,
    val description: String,
    val subscriber: String,
    val isLive: Boolean
) : Parcelable