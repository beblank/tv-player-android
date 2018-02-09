package com.adit.kotlin_rxjava_dagger_retrofit_boilerplate.commons

import android.content.Context
import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.adit.kotlin_rxjava_dagger_retrofit_boilerplate.R
import com.adit.kotlin_rxjava_dagger_retrofit_boilerplate.network.model.LiveTv
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.tv_grid_list.view.*

class GridViewAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var items:ArrayList<ViewType>? = null
    private var delegateAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()
    private val loadingItem = object : ViewType{
        override fun getViewType() = 0
    }

    init {
        delegateAdapters.put(1, LiveTvDelegateAdapter())

        items = ArrayList()
        items!!.add(loadingItem)
    }

    fun addItems(item:List<ViewType>){
        val initPosition = items!!.size -1
        items!!.removeAt(initPosition)
        notifyItemRemoved(initPosition)

        items!!.addAll(item)
        notifyItemRangeChanged(initPosition, items!!.size)
    }

    override fun getItemCount(): Int = if(items!!.lastIndex == -1) 0 else items!!.lastIndex

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        delegateAdapters.get(getItemViewType(position)).onBindViewHolder(holder!!, items!![position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val delegate = delegateAdapters.get(viewType).onCreateViewHolder(parent!!)
        return delegate
    }

    override fun getItemViewType(position: Int): Int = items!![position].getViewType()

}