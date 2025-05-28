package com.example.zadanie2.Data.Remote.Api

import com.example.zadanie2.Data.Remote.Dto.PokemonDetailDto
import com.example.zadanie2.Data.Remote.Dto.PokemonDto
import com.example.zadanie2.Data.Remote.Dto.PokemonListResponse
import com.example.zadanie2.Data.Remote.Dto.SpeciesDetail
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class PokemonApi(private val client: HttpClient) {
    suspend fun getPokemonList(limit: Int = 50, offset: Int = 0): List<PokemonDto> {
        val response: PokemonListResponse = client.get(
            "https://pokeapi.co/api/v2/pokemon?limit=$limit&offset=$offset"
        ).body()
        return response.results
    }

    suspend fun getPokemonDetail(url: String): PokemonDetailDto {
        return client.get(url).body()
    }

    suspend fun getPokemonSpecies(url: String): SpeciesDetail {
        return client.get(url).body()
    }
}