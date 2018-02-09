package com.adit.kotlin_rxjava_dagger_retrofit_boilerplate.network.model

import com.adit.kotlin_rxjava_dagger_retrofit_boilerplate.commons.ViewType

/**
 * Created by adit on 9/3/2017.
 */
class LiveTv (
    val id:String,
    val logo:String,
   val cdn:String
):ViewType{
    override fun getViewType(): Int = 1
}