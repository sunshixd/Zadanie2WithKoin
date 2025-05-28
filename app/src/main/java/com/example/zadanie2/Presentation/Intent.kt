package com.example.zadanie2.Presentation

sealed class MainIntent : UiIntent {
    object LoadPokemons : MainIntent()
    data class Search(val query: String) : MainIntent()
    data class Sort(val sortByName: Boolean) : MainIntent()
}