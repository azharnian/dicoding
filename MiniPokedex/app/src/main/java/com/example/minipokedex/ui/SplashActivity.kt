package com.example.minipokedex.ui
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.minipokedex.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val b = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(b.root)
        b.root.postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 800)
    }
}
