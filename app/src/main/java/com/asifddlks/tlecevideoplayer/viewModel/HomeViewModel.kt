package com.asifddlks.tlecevideoplayer.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asifddlks.tlecevideoplayer.data.repository.VideoRepository
import com.asifddlks.tlecevideoplayer.model.VideoModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel:ViewModel() {
    private val _videoListStateFlow:MutableStateFlow<List<VideoModel>> = MutableStateFlow(listOf())
    val videoListStateFlow = _videoListStateFlow.asStateFlow()

    private val _isLoading= MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun getVideos(){
        viewModelScope.launch {
            _isLoading.value = true
            val repository = VideoRepository.getInstance()
            _videoListStateFlow.value = repository.getVideos()
            //_videoListStateFlow.value = listOf(VideoModel("11","qew","fsdf","sdfs","sdfs","sad","asda","sdfs","asda","asdad",true))
            _isLoading.value = false
        }
    }

}