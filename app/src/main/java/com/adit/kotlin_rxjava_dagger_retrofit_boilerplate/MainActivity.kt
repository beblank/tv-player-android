package com.adit.kotlin_rxjava_dagger_retrofit_boilerplate

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.GridView
import com.adit.kotlin_rxjava_dagger_retrofit_boilerplate.base.App
import com.adit.kotlin_rxjava_dagger_retrofit_boilerplate.commons.GridViewAdapter
import com.adit.kotlin_rxjava_dagger_retrofit_boilerplate.commons.RxBaseActivity
import com.adit.kotlin_rxjava_dagger_retrofit_boilerplate.commons.ViewType
import com.adit.kotlin_rxjava_dagger_retrofit_boilerplate.network.GithubService
import com.adit.kotlin_rxjava_dagger_retrofit_boilerplate.network.model.LiveTv
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject



class MainActivity : RxBaseActivity() {

    @Inject
    lateinit var mGithubService:GithubService

    val tvListData:MutableList<LiveTv> = mutableListOf<LiveTv>()

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
        getGithubData()
    }

    private fun getGithubData() {
        subscriptions.add(mGithubService.liveTvData()!!
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


}
