package com.example.zadanie2.Data.Repository

import com.example.zadanie2.Data.local.dao.PokemonDao
import com.example.zadanie2.Data.local.entity.PokemonEntity
import com.example.zadanie2.Data.Remote.Api.PokemonApi
import com.example.zadanie2.Domain.Pokemon
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PokemonRepository(
    private val api: PokemonApi,
    private val dao: PokemonDao,
    private val httpClient: HttpClient
) {

    fun getPokemons(): Flow<List<Pokemon>> =
        dao.getAllPokemons().map { list -> list.map { it.toDomain() } }

    suspend fun refreshPokemons() {
        val pokemonDtos = api.getPokemonList()
        val entities = pokemonDtos.mapIndexed { index, dto ->
            val detail = api.getPokemonDetail(dto.url)
            val description = fetchDescription(detail.species.url)
            PokemonEntity(
                id = detail.id,
                name = detail.name.replaceFirstChar { it.uppercase() },
                imageUrl = detail.sprites.other?.officialArtwork?.frontDefault
                    ?: detail.sprites.frontDefault
                    ?: "",
                description = description
            )
        }
        dao.insertPokemons(entities)
    }

    private suspend fun fetchDescription(speciesUrl: String): String {
        val speciesDetail: SpeciesDetail = httpClient.get(speciesUrl).body()
        return speciesDetail.flavor_text_entries
            .firstOrNull { it.language.name == "en" }
            ?.flavor_text
            ?.replace("\n", " ")
            ?.replace("\u000c", " ")
            ?: "No description"
    }

private fun PokemonEntity.toDomain() = Pokemon(id, name, imageUrl, description)
@kotlinx.serialization.Serializable
data class SpeciesDetail(
    val flavor_text_entries: List<FlavorTextEntry>
)

@kotlinx.serialization.Serializable
data class FlavorTextEntry(
    val flavor_text: String,
    val language: Language
)

@kotlinx.serialization.Serializable
data class Language(
    val name: String
)}
