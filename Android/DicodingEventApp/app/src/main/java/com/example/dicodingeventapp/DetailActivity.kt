package com.example.dicodingeventapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Html
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.dicodingeventapp.databinding.ActivityDetailBinding
import androidx.core.net.toUri
import com.example.dicodingeventapp.extensions.loadImage
import com.example.dicodingeventapp.viewmodel.FavoriteViewModel
import com.example.dicodingeventapp.viewmodel.ViewModelFactory
import com.example.dicodingeventapp.data.local.entity.FavoriteEventEntity

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val favoriteViewModel: FavoriteViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private var isFavorite = false

    companion object {
        const val EXTRA_ID = "id"
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

        val id = intent.getIntExtra(EXTRA_ID, 0)
        val name = intent.getStringExtra(EXTRA_NAME)
        val image = intent.getStringExtra(EXTRA_IMAGE)
        val owner = intent.getStringExtra(EXTRA_OWNER)
        val time = intent.getStringExtra(EXTRA_TIME)
        val quota = intent.getIntExtra(EXTRA_QUOTA, 0)
        val desc = intent.getStringExtra(EXTRA_DESC)
        val link = intent.getStringExtra(EXTRA_LINK)

        binding.apply {
            txtName.text = name ?: ""
            txtOwner.text = "Organizer: ${owner ?: ""}"
            txtTime.text = "Time: ${time ?: ""}"
            txtQuota.text = "Sisa Kuota: $quota"
            txtDesc.text = Html.fromHtml(desc ?: "", Html.FROM_HTML_MODE_LEGACY)
            txtDesc.movementMethod = android.text.method.LinkMovementMethod.getInstance()

            imgDetail.loadImage(url = image)
        }

        favoriteViewModel.getFavoriteById(id).observe(this) { favorite ->
            if (favorite != null) {
                isFavorite = true
                binding.btnFavorite.setImageResource(R.drawable.ic_favorite)
            } else {
                isFavorite = false
                binding.btnFavorite.setImageResource(R.drawable.ic_favorite_border)
            }
        }

        binding.btnFavorite.setOnClickListener {

            val favoriteEntity = FavoriteEventEntity(
                id = id,
                name = name ?: "",
                mediaCover = image,
                ownerName = owner,
                beginTime = time,
                description = desc,
                quota = quota,
                registrants = 0,
                link = link
            )

            if (isFavorite) {
                favoriteViewModel.removeFavorite(favoriteEntity)
            } else {
                favoriteViewModel.addFavorite(favoriteEntity)
            }
        }

        binding.btnRegister.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, link?.toUri())
            startActivity(intent)
        }
    }


}