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
            val id = result.url.trimEnd('/').split("/").last().toInt()

            val detail = api.getPokemonDetail(id)

            val species = api.getPokemonSpecies(id)
            val flavor = species.flavor_text_entries
                .firstOrNull { it.language.name == "en" }
                ?.flavor_text
                ?.replace("\n", " ")
                ?.replace("\u000c", " ")
                ?: "No description available."

            val imageUrl =
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"

            val types = detail.types.map {
                it.type.name.replaceFirstChar { c -> c.uppercase() }
            }

            val abilities = detail.abilities.map {
                it.ability.name.replaceFirstChar { c -> c.uppercase() }
            }

            val stats = detail.stats.map {
                PokemonStat(
                    name = it.stat.name,
                    value = it.base_stat
                )
            }

            Pokemon(
                id = detail.id,
                name = detail.name.replaceFirstChar { it.uppercase() },
                number = "#${detail.id.toString().padStart(3, '0')}",
                imageUrl = imageUrl,
                types = types,
                height = detail.height,
                weight = detail.weight,
                baseExperience = detail.base_experience,
                abilities = abilities,
                stats = stats,
                description = flavor
            )
        }
    }
}