package com.asifddlks.tlecevideoplayer.view

import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.MediaController
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.asifddlks.tlecevideoplayer.R
import com.asifddlks.tlecevideoplayer.databinding.FragmentDetailsBinding
import com.asifddlks.tlecevideoplayer.utils.htmlText
import com.asifddlks.tlecevideoplayer.viewModel.DetailsViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailsViewModel by viewModels()

    val args: DetailsFragmentArgs by navArgs()

    lateinit var mediaController: MediaController

    private var isVideoViewPrepared = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel.videoItem = args.videoItem

        prepareViews()
        initListeners()
        return view
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // React to orientation changes here
        // You might want to adjust the layout and configuration of your VideoView

        mediaController.setAnchorView(binding.videoView)

        setSystemUIVisibility(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
    }

    private fun prepareViews() {
        viewModel.videoItem?.let { videoItem ->

            binding.textTitle.htmlText(getString(R.string.title_xx,videoItem.title))
            binding.textAuthor.htmlText(getString(R.string.author_xx,videoItem.author))
            binding.textDescription.htmlText(getString(R.string.description_xx,videoItem.description))

            Glide.with(binding.root)
                .load(videoItem.thumbnailUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.imageThumbnail)

            val videoUri = Uri.parse(videoItem.videoUrl)
            binding.videoView.setVideoURI(videoUri)
            mediaController = MediaController(requireContext())

            binding.videoView.setOnPreparedListener {
                // sets the anchor view
                // anchor view for the videoView

                mediaController.setMediaPlayer(binding.videoView)
                binding.videoView.setMediaController(mediaController)
                //binding.videoView.start()
                mediaController.setAnchorView(binding.videoView)
                //mediaController.show(5000)
                isVideoViewPrepared = true
                binding.imagePlayButton.visibility = View.VISIBLE
            }
        }

    }

    private fun initListeners() {
        binding.imagePlayButton.setOnClickListener {
            if(isVideoViewPrepared){
                binding.videoView.start()
                mediaController.show(5000)
                binding.imageThumbnail.visibility = View.GONE
                binding.imagePlayButton.visibility = View.GONE
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setSystemUIVisibility(hide: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val window = requireActivity().window.insetsController!!
            val windows = WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars()
            if (hide) window.hide(windows) else window.show(windows)
            // needed for hide, doesn't do anything in show
            window.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        } else {
            val view = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            requireActivity().window.decorView.systemUiVisibility = if (hide) view else view.inv()
        }
    }

}