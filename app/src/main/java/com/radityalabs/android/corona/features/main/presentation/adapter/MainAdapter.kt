package com.radityalabs.android.corona.features.main.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.radityalabs.android.corona.R
import com.radityalabs.android.corona.features.main.presentation.uimodel.FeedModel
import com.squareup.picasso.Picasso

class MainAdapter(): ListAdapter<FeedModel, MainAdapter.MainViewHolder>(object : DiffUtil.ItemCallback<FeedModel>() {
    override fun areItemsTheSame(oldItem: FeedModel, newItem: FeedModel) = oldItem == newItem
    override fun areContentsTheSame(oldItem: FeedModel, newItem: FeedModel) = oldItem == newItem
}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_bottom_sheet_feed,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(vh: MainViewHolder, position: Int) {
        vh.bind(getItem(position))
    }

    class MainViewHolder(
        private val view: View
    ) : RecyclerView.ViewHolder(view) {

        private val ivPhoto by lazy { view.findViewById<ImageView>(R.id.ivPhoto) }
        private val tvDescription by lazy { view.findViewById<TextView>(R.id.tvDescription) }

        fun bind(feed: FeedModel) {
            Picasso.get()
                .load(feed.image)
                .fit()
                .tag(view.context)
                .into(ivPhoto)
            tvDescription.text = feed.description
        }
    }
}