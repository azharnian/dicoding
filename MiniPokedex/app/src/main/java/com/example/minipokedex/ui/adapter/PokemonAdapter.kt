package com.example.minipokedex.ui.adapter
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.minipokedex.data.PokemonRepository
import com.example.minipokedex.databinding.ItemPokemonBinding
import com.example.minipokedex.model.Pokemon
import com.example.minipokedex.ui.DetailActivity

class PokemonAdapter(private val items: List<Pokemon>) :
    RecyclerView.Adapter<PokemonAdapter.VH>() {

    inner class VH(val b: ItemPokemonBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inf = LayoutInflater.from(parent.context)
        return VH(ItemPokemonBinding.inflate(inf, parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val ctx = holder.itemView.context
        val p = items[position]
        holder.b.tvName.text = p.name
        holder.b.tvOverview.text = p.overview

        val resId = PokemonRepository.drawableIdOrZero(ctx, p.image)
        if (resId != 0) holder.b.imgPhoto.setImageResource(resId) else holder.b.imgPhoto.load(p.image)

        holder.itemView.setOnClickListener {
            ctx.startActivity(Intent(ctx, DetailActivity::class.java).putExtra(DetailActivity.EXTRA_POKEMON, p))
        }
    }

    override fun getItemCount() = items.size
}
