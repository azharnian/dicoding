package com.example.minipokedex

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val toolbar: MaterialToolbar = findViewById(R.id.aboutToolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.title = getString(R.string.about_title)

        toolbar.setNavigationOnClickListener {
            finish()
        }
    }
}