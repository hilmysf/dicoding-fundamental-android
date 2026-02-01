package com.hilmysf.fundamental.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hilmysf.fundamental.databinding.ItemSettingBinding
import com.hilmysf.fundamental.presentation.setting.SettingItem


class SettingAdapter :
    ListAdapter<SettingItem, SettingAdapter.ViewHolder>(DiffCallback) {
    private var onCheckChanged: ((SettingItem) -> Unit)? = null
    fun setOnCheckChanged(onClick: (SettingItem) -> Unit) {
        onCheckChanged = onClick
    }

    inner class ViewHolder(private val binding: ItemSettingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SettingItem) {
            binding.apply {
                tvTitle.text = item.title
                tvDesc.text = item.description

                switcher.setOnCheckedChangeListener(null)
                switcher.isChecked = item.isChecked

                switcher.setOnCheckedChangeListener { _, isChecked ->
                    onCheckChanged?.invoke(item.copy(isChecked = isChecked))
                }
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = ItemSettingBinding.inflate(inflater, viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(getItem(position))
    }

    object DiffCallback : DiffUtil.ItemCallback<SettingItem>() {

        override fun areItemsTheSame(oldItem: SettingItem, newItem: SettingItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SettingItem, newItem: SettingItem): Boolean {
            return oldItem == newItem
        }
    }
}