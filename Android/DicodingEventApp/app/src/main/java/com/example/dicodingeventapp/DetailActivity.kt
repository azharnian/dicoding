package com.example.dicodingeventapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.dicodingeventapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showDetail()
    }

    private fun showDetail() {

        val name = intent.getStringExtra("name")
        val image = intent.getStringExtra("image")
        val owner = intent.getStringExtra("owner")
        val time = intent.getStringExtra("time")
        val quota = intent.getStringExtra("quota")
        val desc = intent.getStringExtra("desc")
        val link = intent.getStringExtra("link")

        binding.txtName.text = name
        binding.txtOwner.text = "Organizer: $owner"
        binding.txtTime.text = "Time: $time"
        binding.txtQuota.text = "Quota: $quota"
        binding.txtDesc.text =
            Html.fromHtml(desc, Html.FROM_HTML_MODE_LEGACY)

        binding.txtDesc.movementMethod =
            android.text.method.LinkMovementMethod.getInstance()


        Glide.with(this)
            .load(image)
            .into(binding.imgDetail)

        binding.btnRegister.setOnClickListener {

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            startActivity(intent)
        }
    }
}
