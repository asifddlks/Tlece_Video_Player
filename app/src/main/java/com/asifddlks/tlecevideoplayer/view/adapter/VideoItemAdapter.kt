package com.asifddlks.tlecevideoplayer.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.asifddlks.tlecevideoplayer.databinding.ItemVideoBinding
import com.asifddlks.tlecevideoplayer.model.VideoModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class VideoItemAdapter(val viewItemInterface: VideoItemInterface) : RecyclerView.Adapter<VideoItemAdapter.ViewHolder>() {

    private var videoList = listOf<VideoModel>()

    // ViewHolder class to hold the views for each item in the RecyclerView
    class ViewHolder(private val binding: ItemVideoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(video: VideoModel) {
            binding.textTitle.text = video.title
            binding.textTitle.isSelected = true

            Glide.with(binding.root)
                .load(video.thumbnailUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.image)
        }
    }

    // Set data to update RecyclerView
    fun updateData(newVideoList: List<VideoModel>) {
        val diffCallback = VideoDiffCallback(videoList, newVideoList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        videoList = newVideoList
        diffResult.dispatchUpdatesTo(this)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view binding
        val binding = ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Bind data to views

        val item = videoList[position]
        holder.bind(item)

        holder.itemView.setOnClickListener {
            viewItemInterface.clickedOnVideoItem(item)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return videoList.size
    }

    // DiffUtil Callback
    class VideoDiffCallback(private val oldList: List<VideoModel>, private val newList: List<VideoModel>) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

    interface VideoItemInterface {
        fun clickedOnVideoItem(item: VideoModel)

    }
}