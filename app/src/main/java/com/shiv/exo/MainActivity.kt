package com.shiv.exo

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSourceFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

class MainActivity : AppCompatActivity() {


    private lateinit var exoplayerView: PlayerView;
    private lateinit var simpleExoPlayer: SimpleExoPlayer
    private lateinit var mediaDataSourceFactory: DataSource.Factory
    private var scaleGestureDetector: ScaleGestureDetector? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        exoplayerView = findViewById(R.id.mplayerView)
        //initializeExo()
    }

    private fun initializeExo() {


        try {
            mediaDataSourceFactory =
                DefaultDataSourceFactory(this, Util.getUserAgent(this, "mediaPlayerSample"))

            val mediaSource = ProgressiveMediaSource.Factory(mediaDataSourceFactory).createMediaSource(
                MediaItem.fromUri(STREAM_URL)
            )

            val mediaSourceFactory: MediaSourceFactory =
                DefaultMediaSourceFactory(mediaDataSourceFactory)

            simpleExoPlayer = SimpleExoPlayer.Builder(this)
                .setMediaSourceFactory(mediaSourceFactory)
                .setSeekBackIncrementMs(10000)
                .setSeekForwardIncrementMs(10000)
                .build()

            simpleExoPlayer.addMediaSource(mediaSource)

            // simpleExoPlayer.playWhenReady = true  //Play with btton
            simpleExoPlayer.prepare()
            simpleExoPlayer.play()  //Auto Play

            exoplayerView.setShutterBackgroundColor(Color.TRANSPARENT)
            exoplayerView.player = simpleExoPlayer
            scaleGestureDetector =
                ScaleGestureDetector(this, CustomOnScaleGestureListener(exoplayerView))
            exoplayerView.requestFocus()
        } catch (e: Exception) {
            Log.d("shiv","initialize error")
        }
    }

    companion object {
        const val STREAM_URL = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)
        scaleGestureDetector?.onTouchEvent(event)

        return true
    }

    private fun stopplayer() {
        Log.d("zzzz", "called stopplayer")

        try {
            if (simpleExoPlayer != null) {
                simpleExoPlayer.release()
                simpleExoPlayer.clearVideoSurface()

            }
        } catch (e: Exception) {
            Log.d("zzzz", "stop issue")
        }

    }

    override fun onPause() {
        super.onPause()
        Log.d("zzzz", "onPause")
        stopplayer()

    }

    override fun onStop() {
        super.onStop()
        Log.d("zzzz", "onStop")
        stopplayer()

    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT <= 23)   initializeExo()
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) initializeExo()
    }



}