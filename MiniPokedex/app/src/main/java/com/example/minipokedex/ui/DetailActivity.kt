package com.example.minipokedex.ui
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.minipokedex.R
import com.example.minipokedex.data.PokemonRepository
import com.example.minipokedex.databinding.ActivityDetailBinding
import com.example.minipokedex.model.Pokemon

class DetailActivity : AppCompatActivity() {
    private lateinit var b: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(b.root)
        setSupportActionBar(b.toolbar)
        b.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        val p = if (Build.VERSION.SDK_INT >= 33)
            intent.getParcelableExtra(EXTRA_POKEMON, Pokemon::class.java)
        else @Suppress("DEPRECATION") intent.getParcelableExtra<Pokemon>(EXTRA_POKEMON)
        if (p == null) { finish(); return }

        b.tvTitle.text = "#${p.id} ${p.name}"
        b.tvOverview.text = p.types.joinToString(" • ") { it.replaceFirstChar(Char::titlecase) }
        b.tvDescription.text = p.description

        val resId = PokemonRepository.drawableIdOrZero(this, p.image)
        if (resId != 0) b.ivPhoto.setImageResource(resId) else b.ivPhoto.load(p.image)

        b.toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.action_share) {
                val text = "#${p.id} ${p.name}\nType: ${p.types.joinToString(", ")}\n\n${p.description}"
                val share = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"; putExtra(Intent.EXTRA_TEXT, text)
                }
                startActivity(Intent.createChooser(share, getString(R.string.app_name)))
                true
            } else false
        }
    }

    companion object { const val EXTRA_POKEMON = "extra_pokemon" }
}
