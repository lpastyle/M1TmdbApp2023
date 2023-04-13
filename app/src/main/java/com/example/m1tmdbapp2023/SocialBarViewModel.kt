package com.example.m1tmdbapp2023

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.m1tmdbapp2023.db.SocialBarDao
import com.example.m1tmdbapp2023.db.SocialBarEntity

class SocialBarViewModel(private val socialBarDao: SocialBarDao) : ViewModel() {
    var nbLikes = socialBarDao.getAllLikes()
    var isFavorite = socialBarDao.getAllFavorites()

    fun insert(socialBarEntity: SocialBarEntity) {
        socialBarDao.insert(socialBarEntity)
    }
}

class SocialBarViewModelFactory(private val socialBarDao: SocialBarDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SocialBarViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SocialBarViewModel(socialBarDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}