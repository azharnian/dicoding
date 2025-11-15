package com.example.minipokedex

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load

class PokemonAdapter(
    private val pokemons: List<Pokemon>
) : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {

    class PokemonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPokemon: ImageView = itemView.findViewById(R.id.img_pokemon)
        val tvName: TextView = itemView.findViewById(R.id.tv_name)
        val tvOverview: TextView = itemView.findViewById(R.id.tv_overview)
        val tvType: TextView = itemView.findViewById(R.id.tv_type)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pokemon, parent, false)
        return PokemonViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val data = pokemons[position]

        holder.tvName.text = data.name
        holder.tvOverview.text = data.overview
        holder.tvType.text = "${data.number} • ${data.type}"
        holder.imgPokemon.load(data.imageUrl)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("EXTRA_POKEMON", pokemons[holder.adapterPosition])
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = pokemons.size
}