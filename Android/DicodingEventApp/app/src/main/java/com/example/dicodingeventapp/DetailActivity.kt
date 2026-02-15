package com.example.dicodingeventapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import com.example.dicodingeventapp.databinding.ActivityDetailBinding
import androidx.core.net.toUri
import com.example.dicodingeventapp.extensions.loadImage

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    companion object {
        const val EXTRA_NAME = "name"
        const val EXTRA_IMAGE = "image"
        const val EXTRA_OWNER = "owner"
        const val EXTRA_TIME = "time"
        const val EXTRA_QUOTA = "quota"
        const val EXTRA_DESC = "desc"
        const val EXTRA_LINK = "link"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showDetail()
    }

    @SuppressLint("SetTextI18n")
    private fun showDetail() {
        val name = intent.getStringExtra(EXTRA_NAME)
        val image = intent.getStringExtra(EXTRA_IMAGE)
        val owner = intent.getStringExtra(EXTRA_OWNER)
        val time = intent.getStringExtra(EXTRA_TIME)
        val quota = intent.getIntExtra(EXTRA_QUOTA, 0)
        val desc = intent.getStringExtra(EXTRA_DESC)
        val link = intent.getStringExtra(EXTRA_LINK)

        binding.apply {
            txtName.text = name
            txtOwner.text = "Organizer: $owner"
            txtTime.text = "Time: $time"
            txtQuota.text = "Sisa Kuota: $quota"
            txtDesc.text = Html.fromHtml(desc, Html.FROM_HTML_MODE_LEGACY)
            txtDesc.movementMethod = android.text.method.LinkMovementMethod.getInstance()

            // Menggunakan extension function
            imgDetail.loadImage(url = image)
        }

        binding.btnRegister.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, link?.toUri())
            startActivity(intent)
        }
    }
}