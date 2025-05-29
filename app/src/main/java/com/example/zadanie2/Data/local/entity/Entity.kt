package com.example.zadanie2.Data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.zadanie2.Data.Repository.PokemonRepository.FlavorTextEntry
import com.example.zadanie2.Domain.Pokemon

@Entity(tableName = "pokemon")
data class PokemonEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val imageUrl: String,
    val description: String
)

@kotlinx.serialization.Serializable
data class SpeciesDetail(
    val flavor_text_entries: List<FlavorTextEntry>
)
