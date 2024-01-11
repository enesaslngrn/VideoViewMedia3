package com.enesas.videoviewmedia3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import com.enesas.videoviewmedia3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var player : ExoPlayer? = null
    private var playbackPosition = 0L
    private var playWhenReady = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preparePlayer()

    }

    companion object{
        // ÇOK ÖNEMLİ -> Media3 kullanırken url'leri hep https ile başlat http ile değil.
        const val mpdURL = "https://bitmovin-a.akamaihd.net/content/MI201109210084_1/mpds/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.mpd"
        const val mp4URL = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
    }

    @OptIn(UnstableApi::class) private fun preparePlayer(){
        player = ExoPlayer.Builder(this).build()
        player?.playWhenReady = true
        binding.playerView.player = player
        val mediaItem = MediaItem.fromUri(mp4URL)
        val mediaSource = DefaultMediaSourceFactory(this).createMediaSource(mediaItem) // ProggressiveMediaSource veya DashMediaSource diye belirtmeye gerek kalmadı.
        player?.setMediaSource(mediaSource)
        player?.seekTo(playbackPosition)
        player?.playWhenReady = playWhenReady
        player?.prepare()
    }

    private fun releasePlayer(){
        player?.let {
            playbackPosition = it.currentPosition
            playWhenReady = it.playWhenReady
            it.release()
            player = null
        }
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    override fun onPause() {
        super.onPause()
        releasePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }
}