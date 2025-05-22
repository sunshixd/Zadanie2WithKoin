package com.example.zadanie2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.example.zadanie2.Data.AppDatabase
import com.example.zadanie2.Data.PokemonApi
import com.example.zadanie2.Data.Repository.PokemonRepository
import com.example.zadanie2.Presentation.DetailViewModel
import com.example.zadanie2.Presentation.DetailViewModelFactory
import com.example.zadanie2.Presentation.MainViewModel
import com.example.zadanie2.Presentation.MainViewModelFactory
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class MainActivity : ComponentActivity() {

    private lateinit var repository: PokemonRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val httpClient = HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    explicitNulls = false
                })
            }
        }

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "pokemon_db"
        ).build()

        val api = PokemonApi(httpClient)
        repository = PokemonRepository(api, db.pokemonDao(), httpClient)

        setContent {
            MaterialTheme {
                val mainViewModel: MainViewModel = viewModel(
                    factory = MainViewModelFactory(repository)
                )
                val detailViewModel: DetailViewModel = viewModel(
                    factory = DetailViewModelFactory(repository)
                )

                NavGraph(
                    mainViewModel = mainViewModel,
                    detailViewModel = detailViewModel,
                    onSaveImage = { imageUrl ->

                    }
                )
            }
        }
    }
}