package com.example.minipokedex.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Pokemon(
    val id: Int,
    val name: String,
    val types: List<String>,
    val overview: String,
    val description: String,
    val image: String
) : Parcelable
