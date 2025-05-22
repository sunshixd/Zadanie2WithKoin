package com.example.zadanie2.Data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PokemonEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
}
