package com.hilmysf.fundamental.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hilmysf.fundamental.data.remote.request.Event
import com.hilmysf.fundamental.databinding.HorizontalEventItemBinding

class HorizontalEventAdapter(
    private val maxItemCount: Int? = null
) :
    ListAdapter<Event, HorizontalEventAdapter.ViewHolder>(DiffCallback) {
    private var onItemClick: ((Event) -> Unit)? = null
    fun setOnItemClick(onClick: (Event) -> Unit) {
        onItemClick = onClick
    }

    inner class ViewHolder(private val binding: HorizontalEventItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Event) {
            binding.apply {
                tvTitle.text = item.name
                Glide.with(itemView.context)
                    .load(item.mediaCover)
                    .into(imgEvent)

            }
            itemView.setOnClickListener {
                onItemClick?.invoke(item)
            }
        }


    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = HorizontalEventItemBinding.inflate(inflater, viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(getItem(position))
    }

    override fun getItemCount(): Int {
        return if (maxItemCount != null) {
            if (currentList.size > maxItemCount) maxItemCount else currentList.size
        } else {
            super.getItemCount()
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<Event>() {

        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem == newItem
        }
    }
}