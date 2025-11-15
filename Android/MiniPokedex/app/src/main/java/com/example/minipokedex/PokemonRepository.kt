package com.example.minipokedex

interface PokemonRepository {
    suspend fun getPokemons(limit: Int = 20): List<Pokemon>
}