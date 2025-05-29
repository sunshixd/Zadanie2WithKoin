package com.example.zadanie2.Presentation.Main

interface MainScreenContract {
    fun onSearch(query: String)
    fun onSort(byName: Boolean)
    fun onItemClick(pokemonId: Int)
}