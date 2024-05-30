package com.md29.husein.myappsstory.data.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.md29.husein.myappsstory.data.networking.ListStories
import com.md29.husein.myappsstory.databinding.ItemRowStoriesBinding
import com.md29.husein.myappsstory.utils.DateFormatting
import com.md29.husein.myappsstory.utils.loadImage
import java.util.TimeZone


class StoriesAdapter(
    private val onClick: (ListStories) -> Unit,
) :
    PagingDataAdapter<ListStories, StoriesAdapter.ListViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemRowStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    inner class ListViewHolder(private var binding: ItemRowStoriesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NewApi")
        fun bind(listStories: ListStories) {
            binding.apply {
                tvName.text = listStories.name
                imageStories.loadImage(listStories.photoUrl)
                tvCreateAt.text = listStories.createAt?.let {
                    DateFormatting.formatDate(
                        it,
                        TimeZone.getDefault().id
                    )
                }
            }
            itemView.setOnClickListener {
                onClick(listStories)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStories>() {
            override fun areItemsTheSame(oldItem: ListStories, newItem: ListStories): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStories,
                newItem: ListStories,
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}