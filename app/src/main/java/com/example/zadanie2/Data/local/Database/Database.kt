package com.example.zadanie2.Data.local.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.zadanie2.Data.local.dao.PokemonDao
import com.example.zadanie2.Data.local.entity.PokemonEntity

@Database(entities = [PokemonEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
}
