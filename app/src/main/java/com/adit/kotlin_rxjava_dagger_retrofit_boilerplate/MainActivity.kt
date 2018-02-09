package com.adit.kotlin_rxjava_dagger_retrofit_boilerplate

import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.adit.kotlin_rxjava_dagger_retrofit_boilerplate.base.App
import com.adit.kotlin_rxjava_dagger_retrofit_boilerplate.commons.GridViewAdapter
import com.adit.kotlin_rxjava_dagger_retrofit_boilerplate.commons.RxBaseActivity
import com.adit.kotlin_rxjava_dagger_retrofit_boilerplate.commons.RxBus
import com.adit.kotlin_rxjava_dagger_retrofit_boilerplate.network.LiveTvService
import com.adit.kotlin_rxjava_dagger_retrofit_boilerplate.network.model.LiveTv
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject



class MainActivity : RxBaseActivity() {

    @Inject
    lateinit var mLiveTvService:LiveTvService

    var player: SimpleExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        inject()
        gridView.setHasFixedSize(true)
        gridView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun initHorizontalGrid(it:List<LiveTv>) {
        if (gridView.adapter == null)
            gridView.adapter = GridViewAdapter()
        (gridView.adapter as GridViewAdapter).addItems(it)

    }

    override fun onResume() {
        super.onResume()
        getLiveTvData()
        manageSubscription()
        initializePlayer()
    }

    private fun initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(
                DefaultRenderersFactory(this),
                DefaultTrackSelector(),
                DefaultLoadControl())

        video_view.player  = player
        player!!.playWhenReady = true
    }

    private fun playVideo(url:String){
        val uri = Uri.parse(url)
        val mediaSource: MediaSource = buildMediaSource(uri)
        player!!.prepare(mediaSource, true, false)
    }

    private fun buildMediaSource(uri: Uri?): MediaSource {
        val dataSoureFactory =  DefaultDataSourceFactory(this, Util.getUserAgent(this, "exoplayer"))
        return HlsMediaSource(uri, dataSoureFactory, 1, null, null)
    }

    private fun getLiveTvData() {
        subscriptions.add(mLiveTvService.liveTvData()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    initHorizontalGrid(it)
                },{
                    error ->
                    Log.e("error", "$error")
                }))
    }

    private fun inject() {
        App.component?.inject(this)
    }

    fun manageSubscription() {
        subscriptions.add(RxBus.get().toObservable().subscribe({
            event -> manageEvent(event)
        },{
            error -> Log.e("dodol", "$error")
        }))
    }

    fun manageEvent(event: Any) {
        when(event){
            event as LiveTv -> {
                Log.d("dodol", "${event.cdn}")
                playVideo(event.cdn)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        releasePlayer()
    }

    private fun releasePlayer() {
        if (player != null) {
            player!!.release()
            player = null
        }
    }
}
