package com.example.m1tmdbapp2023.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

abstract class TmdbDatabase : RoomDatabase() {

    abstract fun socialBarDao(): SocialBarDao

    companion object {

        @Volatile
        private var INSTANCE: TmdbDatabase? = null

        fun getDatabase(
            context: Context
        ) : TmdbDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TmdbDatabase::class.java,
                    "tmdb_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}