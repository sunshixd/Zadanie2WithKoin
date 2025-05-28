package com.example.zadanie2.Presentation.Main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zadanie2.Data.Repository.PokemonRepository
import com.example.zadanie2.Presentation.MainIntent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel(private val repository: PokemonRepository) : ViewModel() {

    private val _state = MutableStateFlow(MainState(isLoading = true))
    val state: StateFlow<MainState> = _state

    init {
        processIntent(MainIntent.LoadPokemons)
    }

    fun processIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.LoadPokemons -> loadPokemons()
            is MainIntent.Search -> {
                _state.value = _state.value.copy(searchQuery = intent.query)
                filterAndSort()
            }
            is MainIntent.Sort -> {
                _state.value = _state.value.copy(sortByName = intent.sortByName)
                filterAndSort()
            }
        }
    }

    private fun loadPokemons() {
        viewModelScope.launch {
            try {
                repository.refreshPokemons()
                repository.getPokemons().collectLatest { pokemons ->
                    _state.value = _state.value.copy(
                        pokemons = pokemons,
                        isLoading = false,
                        error = null
                    )
                    filterAndSort()
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message ?: "Unknown error", isLoading = false)
            }
        }
    }

    private fun filterAndSort() {
        val current = _state.value
        val filtered = current.pokemons.filter {
            it.name.contains(current.searchQuery, ignoreCase = true) ||
                    it.description.contains(current.searchQuery, ignoreCase = true)
        }
        val sorted = if (current.sortByName) filtered.sortedBy { it.name } else filtered
        _state.value = current.copy(filteredPokemons = sorted)
    }
}