package com.example.storyapp.view.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.api.response.ListStoryItem
import com.example.storyapp.databinding.ItemStoryBinding
import com.example.storyapp.view.detailstory.DetailStoryActivity

class MainAdapter: PagingDataAdapter<ListStoryItem, MainAdapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder(val binding: ItemStoryBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(story: ListStoryItem){
            Glide.with(binding.imgStory)
                .load(story.photoUrl)
                .into(binding.imgStory)
            binding.tvName.text = story.name
            binding.tvDescription.text = story.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):MyViewHolder{
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int){
        val story = getItem(position)
        if (story != null) {
            holder.bind(story)
            holder.itemView.setOnClickListener{
                val context: Context = holder.itemView.context
                val intent = Intent(context, DetailStoryActivity::class.java)
                intent.putExtra(DetailStoryActivity.EXTRA_ID, story.id)
                holder.itemView.context.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(holder.itemView.context as Activity).toBundle())
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}