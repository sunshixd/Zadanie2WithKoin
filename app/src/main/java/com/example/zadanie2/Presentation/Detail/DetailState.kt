package com.example.zadanie2.Presentation.Detail

import com.example.zadanie2.Domain.Pokemon
import com.example.zadanie2.Presentation.UiState

data class DetailState(
    val pokemon: Pokemon? = null,
    val isLoading: Boolean = false,
    val error: String? = null
) : UiState