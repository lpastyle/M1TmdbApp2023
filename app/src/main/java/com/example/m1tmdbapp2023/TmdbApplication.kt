package com.example.m1tmdbapp2023

import android.app.Application
import com.example.m1tmdbapp2023.db.TmdbDatabase

class TmdbApplication : Application() {
    val database by lazy { TmdbDatabase.getDatabase(this) }
    val socialBarDao by lazy {database.socialBarDao()}
}