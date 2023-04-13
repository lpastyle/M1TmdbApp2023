package com.example.m1tmdbapp2023.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SocialBarDao {
    @Query("SELECT person_id, is_favorite FROM social_bar_table")
    fun getAllFavorites(): MutableMap<Int,Boolean>

    @Query("SELECT person_id, nb_likes FROM social_bar_table")
    fun getAllLikes(): MutableMap<Int,Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(socialBarEntity: SocialBarEntity)
}