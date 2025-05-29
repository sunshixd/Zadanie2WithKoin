package com.example.zadanie2.Data.Remote.Dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonListResponse(
    val results: List<PokemonDto>
)

@Serializable
data class PokemonDto(
    val name: String,
    val url: String
)

@Serializable
data class PokemonDetailDto(
    val id: Int,
    val name: String,
    val sprites: Sprites,
    val species: Species
)

@Serializable
data class Sprites(
    @SerialName("front_default") val frontDefault: String? = null,
    val other: OtherSprites? = null
)

@Serializable
data class OtherSprites(
    @SerialName("official-artwork") val officialArtwork: Artwork? = null
)

@Serializable
data class Artwork(
    @SerialName("front_default") val frontDefault: String? = null
)

@Serializable
data class Species(
    val url: String
)

@Serializable
data class SpeciesDetail(
    val flavor_text_entries: List<FlavorTextEntry>
)

@Serializable
data class FlavorTextEntry(
    val flavor_text: String,
    val language: Language
)

@Serializable
data class Language(
    val name: String
)

