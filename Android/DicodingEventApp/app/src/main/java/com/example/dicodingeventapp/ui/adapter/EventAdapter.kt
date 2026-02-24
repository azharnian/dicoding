package com.example.dicodingeventapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingeventapp.data.model.Event
import com.example.dicodingeventapp.databinding.ItemEventBinding

class EventAdapter(
    private val listEvent: List<Event>,
    private val onClick: (Event) -> Unit

) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    class EventViewHolder(
        val binding: ItemEventBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {

        val binding = ItemEventBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {

        val event = listEvent[position]

        holder.binding.apply {

            txtName.text = event.name
            txtOwner.text = event.ownerName

            Glide.with(root.context)
                .load(event.imageLogo)
                .into(imgEvent)

            root.setOnClickListener {
                onClick(event)
            }

        }
    }

    override fun getItemCount(): Int = listEvent.size

}