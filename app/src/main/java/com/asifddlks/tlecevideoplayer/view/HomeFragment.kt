package com.asifddlks.tlecevideoplayer.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.asifddlks.tlecevideoplayer.R
import com.asifddlks.tlecevideoplayer.databinding.FragmentHomeBinding
import com.asifddlks.tlecevideoplayer.model.VideoModel
import com.asifddlks.tlecevideoplayer.view.adapter.VideoItemAdapter
import com.asifddlks.tlecevideoplayer.viewModel.HomeViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var adapter: VideoItemAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        initViews()
        initListeners()

        subscribeToObservables()

        viewModel.getVideos()

        return view
    }

    private fun subscribeToObservables() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.videoListStateFlow.collectLatest { videoList ->
                    adapter.updateData(videoList)
                }
            }
        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.isLoading.collectLatest { isLoading->
                    updateShimmerView(isLoading)
                }
            }
        }
    }

    private fun updateShimmerView(isLoading: Boolean) {
        if (isLoading) {
            //Start Shimmer
            binding.layoutShimmerView.startShimmer()
            binding.layoutShimmerView.visibility = View.VISIBLE
            binding.textEmptyList.visibility = View.GONE
        } else {
            //Stop Shimmer
            binding.layoutSwipe.isRefreshing = false
            binding.layoutShimmerView.stopShimmer()
            binding.layoutShimmerView.visibility = View.GONE

            if(viewModel.videoListStateFlow.value.isEmpty()){
                binding.textEmptyList.visibility = View.VISIBLE
            }
            else{
                binding.textEmptyList.visibility = View.GONE
            }
        }
    }

    private fun initViews() {
        adapter = VideoItemAdapter(object :
            VideoItemAdapter.VideoItemInterface {
            override fun clickedOnVideoItem(item: VideoModel) {
                findNavController().navigate(R.id.action_homeFragment_to_detailsFragment)
            }
        })
        binding.videoList.layoutManager = GridLayoutManager(requireContext(),2)
        binding.videoList.adapter = adapter
    }

    private fun initListeners() {
        binding.layoutSwipe.setOnRefreshListener {
            viewModel.getVideos()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}