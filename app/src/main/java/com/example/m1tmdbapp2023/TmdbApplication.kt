package com.example.m1tmdbapp2023

import android.app.Application
import com.example.m1tmdbapp2023.db.TmdbDatabase

class TmdbApplication : Application() {
    // Using by lazy so the database and the DAO are only created when they're needed
    // rather than when the application starts
    val database by lazy { TmdbDatabase.getDatabase(this) }
    val socialBarDao by lazy {  database.socialBarDao() }
}