package com.example.minipokedex

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

data class PokemonListResponse(
    val results: List<PokemonListResult>
)

data class PokemonListResult(
    val name: String,
    val url: String
)

data class PokemonDetailDto(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val base_experience: Int,
    val sprites: SpritesDto,
    val types: List<PokemonTypeDto>,
    val abilities: List<PokemonAbilityDto>,
    val stats: List<PokemonStatDto>
)

data class SpritesDto(
    val front_default: String?,
    val other: OtherSpritesDto?
)

data class OtherSpritesDto(
    val official_artwork: OfficialArtworkDto?
)

data class OfficialArtworkDto(
    val front_default: String?
)

data class PokemonTypeDto(
    val type: NamedApiResourceDto
)

data class PokemonAbilityDto(
    val ability: NamedApiResourceDto
)

data class PokemonStatDto(
    val base_stat: Int,
    val stat: NamedApiResourceDto
)

data class NamedApiResourceDto(
    val name: String,
    val url: String
)

data class PokemonSpeciesDto(
    val flavor_text_entries: List<FlavorTextDto>
)

data class FlavorTextDto(
    val flavor_text: String,
    val language: NamedApiResourceDto
)

interface PokeApiService {

    @GET("pokemon")
    suspend fun getPokemons(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int = 0
    ): PokemonListResponse

    @GET("pokemon/{id}")
    suspend fun getPokemonDetail(
        @Path("id") id: Int
    ): PokemonDetailDto

    @GET("pokemon-species/{id}")
    suspend fun getPokemonSpecies(
        @Path("id") id: Int
    ): PokemonSpeciesDto
}