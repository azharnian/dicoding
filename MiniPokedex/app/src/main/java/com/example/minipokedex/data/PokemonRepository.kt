package com.example.minipokedex.data
import android.content.Context
import com.example.minipokedex.model.Pokemon
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

object PokemonRepository {
    private val json = Json { ignoreUnknownKeys = true }

    fun load(context: Context): List<Pokemon> {
        val text = context.assets.open("pokemon.json").bufferedReader().use { it.readText() }
        return json.decodeFromString(ListSerializer(Pokemon.serializer()), text)
    }

    fun drawableIdOrZero(context: Context, nameOrUrl: String): Int =
        if (nameOrUrl.startsWith("http")) 0
        else context.resources.getIdentifier(nameOrUrl, "drawable", context.packageName)
}
