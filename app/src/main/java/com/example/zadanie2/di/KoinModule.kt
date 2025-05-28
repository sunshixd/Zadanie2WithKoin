package com.example.zadanie2.di

import android.content.Context
import androidx.room.Room
import com.example.zadanie2.Data.local.Database.AppDatabase
import com.example.zadanie2.Data.Remote.Api.PokemonApi
import com.example.zadanie2.Data.Repository.PokemonRepository
import com.example.zadanie2.Presentation.Detail.DetailViewModel
import com.example.zadanie2.Presentation.Main.MainViewModel
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<HttpClient> {
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    explicitNulls = false
                })
            }
        }
    }

    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "pokemon_db"
        ).build()
    }

    single { get<AppDatabase>().pokemonDao() }

    single { PokemonApi(get()) }

    single { PokemonRepository(get(), get(), get()) }
}

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { DetailViewModel(get()) }
}

fun provideKoinModules(context: Context) = listOf(appModule, viewModelModule)