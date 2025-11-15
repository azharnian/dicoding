package com.example.minipokedex

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pokemon(
    val name: String,
    val overview: String,
    val description: String,
    val imageUrl: String,   // dari internet
    val type: String,
    val number: String
) : Parcelable