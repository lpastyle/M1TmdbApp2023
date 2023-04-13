package com.example.m1tmdbapp2023.db

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SocialBarDao {
    @MapInfo(keyColumn = "person_id", valueColumn = "is_favorite")
    @Query("SELECT person_id, is_favorite FROM social_bar_table")
    fun getAllFavorites(): Flow<MutableMap<Int, Boolean>>

    @MapInfo(keyColumn = "person_id", valueColumn = "nb_likes")
    @Query("SELECT person_id, nb_likes FROM social_bar_table")
    fun getAllLikes(): Flow<MutableMap<Int,Int>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(socialBarEntity: SocialBarEntity)
}