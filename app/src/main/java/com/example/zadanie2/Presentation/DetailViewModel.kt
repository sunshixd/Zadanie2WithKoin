package com.example.zadanie2.Presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zadanie2.Data.Repository.PokemonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: PokemonRepository) : ViewModel() {

    private val _state = MutableStateFlow(DetailState())
    val state: StateFlow<DetailState> = _state

    fun loadPokemon(id: Int) {
        viewModelScope.launch {
            _state.value = DetailState(isLoading = true)
            try {
                val pokemons = repository.getPokemons().firstOrNull() ?: emptyList()
                val p = pokemons.find { it.id == id }
                _state.value = DetailState(pokemon = p, isLoading = false)
            } catch (e: Exception) {
                _state.value = DetailState(error = e.message ?: "Unknown error", isLoading = false)
            }
        }
    }
}
