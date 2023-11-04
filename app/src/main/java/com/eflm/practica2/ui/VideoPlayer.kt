package com.eflm.practica2.ui

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.MediaController
import com.eflm.practica2.R
import com.eflm.practica2.databinding.ActivityVideoPlayerBinding

class VideoPlayer : AppCompatActivity() {

    private lateinit var binding: ActivityVideoPlayerBinding

    private lateinit var mp: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val url = intent.getStringExtra("EXTRA_URLVIDEO")

        binding.vvVideo.setVideoPath(url)

        val mc = MediaController(this)
        mc.setAnchorView(binding.vvVideo)
        binding.vvVideo.setMediaController(mc)


        binding.vvVideo.start()


    }
}