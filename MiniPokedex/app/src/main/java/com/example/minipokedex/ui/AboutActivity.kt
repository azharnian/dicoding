package com.example.minipokedex.ui
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.minipokedex.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {
    private lateinit var b: ActivityAboutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(b.root)
        setSupportActionBar(b.toolbar)
        b.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        b.tvName.text = "Anas Azhar"
        b.tvEmail.text = "emailkamu@dicoding.com"
    }
}
