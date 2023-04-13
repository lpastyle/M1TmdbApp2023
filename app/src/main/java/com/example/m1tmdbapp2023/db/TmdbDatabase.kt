package com.example.m1tmdbapp2023.db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Annotates class to be a Room Database with a table (entity) of the SocialBarEntity class
@Database(entities = [SocialBarEntity::class], version = 1)
abstract class TmdbDatabase : RoomDatabase() {

    abstract fun socialBarDao(): SocialBarDao

    companion object {
        private val LOGTAG = TmdbDatabase::class.simpleName
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: TmdbDatabase? = null

        fun getDatabase(
            context: Context
        ): TmdbDatabase {
            Log.d(LOGTAG,"getDatabase()")
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TmdbDatabase::class.java,
                    "tmdb_database"
                )
                    //.allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}