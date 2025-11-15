package com.example.minipokedex

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var rvPokemon: RecyclerView
    private val repository: PokemonRepository = ApiPokemonRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: MaterialToolbar = findViewById(R.id.topAppBar)
        setSupportActionBar(toolbar)

        toolbar.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.about_page -> {
                    startActivity(Intent(this, AboutActivity::class.java))
                    true
                }
                else -> false
            }
        }

        rvPokemon = findViewById(R.id.rvPokemon)
        rvPokemon.layoutManager = LinearLayoutManager(this)
        rvPokemon.setHasFixedSize(true)

        // Ambil data dari API di background thread
        lifecycleScope.launch {
            try {
                val pokemons = repository.getPokemons(20)
                rvPokemon.adapter = PokemonAdapter(pokemons)
            } catch (e: Exception) {
                e.printStackTrace()
                // TODO: tampilkan Toast error kalau mau
            }
        }
    }
}