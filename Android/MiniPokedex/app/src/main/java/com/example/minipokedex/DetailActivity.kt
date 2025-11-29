package com.example.minipokedex

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.google.android.material.appbar.MaterialToolbar

class DetailActivity : AppCompatActivity() {

    private var pokemon: Pokemon? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val toolbar: MaterialToolbar = findViewById(R.id.detailToolbar)

        val detailProgressBar: ProgressBar = findViewById(R.id.detailProgressBar)

        pokemon = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("EXTRA_POKEMON", Pokemon::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("EXTRA_POKEMON")
        }

        val titleText = pokemon?.name ?: getString(R.string.app_name)
        toolbar.title = titleText

        toolbar.setNavigationOnClickListener { finish() }

        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_share -> {
                    sharePokemon()
                    true
                }
                else -> false
            }
        }

        val ivPhoto: ImageView = findViewById(R.id.iv_detail_photo)
        val tvName: TextView = findViewById(R.id.tv_detail_name)
        val tvType: TextView = findViewById(R.id.tv_detail_type)
        val tvNumber: TextView = findViewById(R.id.tv_detail_number)
        val tvBasicInfo: TextView = findViewById(R.id.tv_detail_basic_info)
        val tvAbilities: TextView = findViewById(R.id.tv_detail_abilities)
        val tvStats: TextView = findViewById(R.id.tv_detail_stats)
        val tvDescription: TextView = findViewById(R.id.tv_detail_description)

        detailProgressBar.visibility = View.VISIBLE

        pokemon?.let { data ->
            ivPhoto.load(data.imageUrl) {
                listener(
                    onSuccess = { _, _ ->
                        detailProgressBar.visibility = View.GONE
                    },
                    onError = { _, _ ->
                        detailProgressBar.visibility = View.GONE
                    }
                )
            }

            tvName.text = data.name
            tvType.text = data.types.joinToString(" / ")
            tvNumber.text = data.number

            val heightMeters = data.height / 10.0
            val weightKg = data.weight / 10.0
            tvBasicInfo.text = """
                Height : ${heightMeters} m
                Weight : ${weightKg} kg
                Base Exp : ${data.baseExperience}
            """.trimIndent()

            tvAbilities.text = data.abilities.joinToString(", ") {
                it.replaceFirstChar { c -> c.uppercase() }
            }

            val statsText = data.stats.joinToString("\n") { stat ->
                "${stat.name.uppercase()}: ${stat.value}"
            }
            tvStats.text = statsText

            tvDescription.text = data.description
        } ?: run {
            detailProgressBar.visibility = View.GONE
        }
    }

    private fun sharePokemon() {
        val data = pokemon ?: return
        val shareText =
            "Cek Pokémon ${data.name} (${data.number}) - tipe ${data.types.joinToString("/")} di Mini Pokedex-ku!"

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        startActivity(Intent.createChooser(intent, "Bagikan via"))
    }
}