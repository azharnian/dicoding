package com.example.minipokedex

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var rvPokemon: RecyclerView
    private lateinit var mainProgressBar: ProgressBar
    private val repository: PokemonRepository = ApiPokemonRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: MaterialToolbar = findViewById(R.id.topAppBar)
        setSupportActionBar(toolbar)

        val aboutAvatar: ShapeableImageView = findViewById(R.id.about_page)
        aboutAvatar.setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }

        rvPokemon = findViewById(R.id.rvPokemon)
        mainProgressBar = findViewById(R.id.mainProgressBar)

        rvPokemon.layoutManager = LinearLayoutManager(this)
        rvPokemon.setHasFixedSize(true)

        mainProgressBar.visibility = View.VISIBLE
        rvPokemon.visibility = View.GONE

        lifecycleScope.launch {
            try {
                val pokemons = repository.getPokemons(20)
                rvPokemon.adapter = PokemonAdapter(pokemons)

                mainProgressBar.visibility = View.GONE
                rvPokemon.visibility = View.VISIBLE
            } catch (e: Exception) {
                e.printStackTrace()
                mainProgressBar.visibility = View.GONE
                // optional: Toast error
            }
        }
    }
}