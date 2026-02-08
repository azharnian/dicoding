package com.example.dicodingeventapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingeventapp.data.model.Event
import com.example.dicodingeventapp.databinding.ItemEventHomeBinding

class HomeSliderAdapter(
    private val events: List<Event>,
    private val onClick: (Event) -> Unit
) : RecyclerView.Adapter<HomeSliderAdapter.HomeViewHolder>() {

    inner class HomeViewHolder(
        val binding: ItemEventHomeBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {

        val binding = ItemEventHomeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {

        val event = events[position]

        holder.binding.apply {

            txtHomeTitle.text = event.name

            Glide.with(root.context)
                .load(event.imageLogo)
                .into(imgHomeEvent)

            root.setOnClickListener {
                onClick(event)
            }
        }
    }

    override fun getItemCount(): Int = events.size
}