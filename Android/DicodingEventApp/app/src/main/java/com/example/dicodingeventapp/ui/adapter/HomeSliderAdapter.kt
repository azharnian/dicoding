package com.example.dicodingeventapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dicodingeventapp.data.model.Event
import com.example.dicodingeventapp.databinding.ItemEventHomeBinding
import com.example.dicodingeventapp.utils.loadImage

class HomeSliderAdapter(
    private val onClick: (Event) -> Unit
) : ListAdapter<Event, HomeSliderAdapter.HomeViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = ItemEventHomeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val event = getItem(position)
        holder.binding.apply {
            txtHomeTitle.text = event.name
            imgHomeEvent.loadImage(event.imageLogo)
            root.setOnClickListener {
                onClick(event)
            }
        }
    }

    class HomeViewHolder(val binding: ItemEventHomeBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Event>() {
            override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
                return oldItem == newItem
            }
        }
    }
}