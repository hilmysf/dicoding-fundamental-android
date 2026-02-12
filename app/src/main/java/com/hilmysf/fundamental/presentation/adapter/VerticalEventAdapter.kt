package com.hilmysf.fundamental.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hilmysf.fundamental.databinding.EventListItemBinding
import com.hilmysf.fundamental.domain.model.Event

class VerticalEventAdapter(
    private val maxItemCount: Int? = null, private val onEventClickListener: OnEventClickListener
) : ListAdapter<Event, VerticalEventAdapter.ViewHolder>(DiffCallback) {

    inner class ViewHolder(private val binding: EventListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Event) {
            binding.apply {
                tvTitle.text = item.name
                tvDesc.text = item.summary
                val isBookmarked = item.isBookmarked
                btnBookmark.setImageResource(
                    if (isBookmarked) {
                        com.hilmysf.fundamental.R.drawable.ic_bookmark_filled
                    } else {
                        com.hilmysf.fundamental.R.drawable.ic_bookmark_outlined
                    }

                )
                Glide.with(itemView.context).load(item.mediaCover).into(imgEvent)
            }
        }

        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val event = getItem(position)
                    onEventClickListener.onEventClick(event)
                }
            }
            binding.btnBookmark.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val event = getItem(position)
                    onEventClickListener.onBookmarkClick(event)
                }
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = EventListItemBinding.inflate(inflater, viewGroup, false)
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