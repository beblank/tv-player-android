package com.adit.kotlin_rxjava_dagger_retrofit_boilerplate

import com.adit.kotlin_rxjava_dagger_retrofit_boilerplate.commons.Utils
import com.adit.kotlin_rxjava_dagger_retrofit_boilerplate.network.GithubService
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by adit on 9/3/2017.
 */
@Module
class MainModule {

    @Provides
    @Singleton
    fun provideGithubUserData():GithubService{
        val url = "http://10.0.2.2:9000"

        val clientBuilder: OkHttpClient.Builder = Utils.buildClient()

        val retrofit = Retrofit.Builder()
                .baseUrl(url)
                .client(clientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()

        return retrofit.create(GithubService::class.java)
    }

}