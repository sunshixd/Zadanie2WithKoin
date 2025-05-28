package com.example.zadanie2.Presentation.Main

import com.example.zadanie2.Domain.Pokemon
import com.example.zadanie2.Presentation.UiState

data class MainState(
    val pokemons: List<Pokemon> = emptyList(),
    val filteredPokemons: List<Pokemon> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val sortByName: Boolean = true
) : UiState