package com.example.minipokedex.ui
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.minipokedex.R
import com.example.minipokedex.data.PokemonRepository
import com.example.minipokedex.databinding.ActivityMainBinding
import com.example.minipokedex.ui.adapter.PokemonAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var b: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        setSupportActionBar(b.topAppBar)
        val data = PokemonRepository.load(this)

        b.rvList.layoutManager = LinearLayoutManager(this)
        b.rvList.adapter = PokemonAdapter(data)

        b.topAppBar.setOnMenuItemClickListener {
            if (it.itemId == R.id.about_page) {
                startActivity(Intent(this, AboutActivity::class.java)); true
            } else false
        }
    }
}
