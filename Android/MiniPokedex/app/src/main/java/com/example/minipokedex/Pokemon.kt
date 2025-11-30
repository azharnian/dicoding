package com.example.minipokedex

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pokemon(
    val id: Int,
    val name: String,
    val number: String,
    val imageUrl: String,
    val types: List<String>,
    val height: Int,
    val weight: Int,
    val baseExperience: Int,
    val abilities: List<String>,
    val stats: List<PokemonStat>,
    val description: String
) : Parcelable

@Parcelize
data class PokemonStat(
    val name: String,
    val value: Int
) : Parcelable