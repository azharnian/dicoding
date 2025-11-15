package com.example.minipokedex

import retrofit2.http.GET
import retrofit2.http.Query

data class PokemonListResponse(
    val results: List<PokemonResult>
)

data class PokemonResult(
    val name: String,
    val url: String
)

interface PokeApiService {

    @GET("pokemon")
    suspend fun getPokemons(
        @Query("limit") limit: Int = 20
    ): PokemonListResponse
}