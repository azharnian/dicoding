package com.example.minipokedex

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import coil.load

class DetailActivity : AppCompatActivity() {

    private var pokemon: Pokemon? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Detail Pokémon"

        pokemon = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("EXTRA_POKEMON", Pokemon::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("EXTRA_POKEMON")
        }

        pokemon?.let { data ->
            findViewById<ImageView>(R.id.iv_detail_photo).load(data.imageUrl)
            findViewById<TextView>(R.id.tv_detail_name).text = data.name
            findViewById<TextView>(R.id.tv_detail_type).text = "${data.number} • ${data.type}"
            findViewById<TextView>(R.id.tv_detail_description).text = data.description
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_share -> {          // <-- PENTING: pakai R.id.action_share
                sharePokemon()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun sharePokemon() {
        val data = pokemon ?: return
        val shareText = "Cek Pokémon ${data.name} (${data.number}) - tipe ${data.type} di Mini Pokedex-ku!"
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        startActivity(Intent.createChooser(intent, "Bagikan via"))
    }
}