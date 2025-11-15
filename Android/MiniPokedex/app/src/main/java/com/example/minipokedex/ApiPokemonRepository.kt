package com.example.minipokedex

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiPokemonRepository : PokemonRepository {

    private val api: PokeApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokeApiService::class.java)
    }

    override suspend fun getPokemons(limit: Int): List<Pokemon> {
        val response = api.getPokemons(limit)

        return response.results.map { result ->
            // ambil ID dari URL, misal .../pokemon/1/ -> "1"
            val id = result.url.trimEnd('/').split("/").last()

            val imageUrl =
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"

            Pokemon(
                name = result.name.replaceFirstChar { it.uppercase() },
                overview = "Pokémon #$id",
                description = "Ini adalah Pokémon bernama ${result.name} dari PokeAPI.",
                imageUrl = imageUrl,
                type = "Unknown type",    // kalau mau, nanti bisa fetch detail lagi
                number = "#$id"
            )
        }
    }
}