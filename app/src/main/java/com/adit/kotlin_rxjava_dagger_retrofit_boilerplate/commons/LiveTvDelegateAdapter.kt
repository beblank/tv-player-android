package com.adit.kotlin_rxjava_dagger_retrofit_boilerplate.commons

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.adit.kotlin_rxjava_dagger_retrofit_boilerplate.R
import com.adit.kotlin_rxjava_dagger_retrofit_boilerplate.extensions.inflate
import com.adit.kotlin_rxjava_dagger_retrofit_boilerplate.network.model.LiveTv
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.tv_grid_list.view.*

class LiveTvDelegateAdapter : ViewTypeDelegateAdapter {
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return  LiveTvViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        holder as LiveTvViewHolder
        holder.bind(item as LiveTv)
    }

    inner class LiveTvViewHolder(parent:ViewGroup):RecyclerView.ViewHolder(
            parent.inflate(R.layout.tv_grid_list)){

        fun bind(item:LiveTv) =
                with(itemView){
                    Picasso.with(tv_logo.context).load(item.logo).into(tv_logo);

                    super.itemView.setOnClickListener{
                        RxBus.get().send(item)
                    }
                }

    }
}