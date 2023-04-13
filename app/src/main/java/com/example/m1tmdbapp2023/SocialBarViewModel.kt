package com.example.m1tmdbapp2023

import androidx.lifecycle.ViewModel
import com.example.m1tmdbapp2023.db.SocialBarDao

class SocialBarViewModel(private val socialBarDao: SocialBarDao) : ViewModel() {
    var nbLikes = socialBarDao.getAllLikes()
    var isFavorite = socialBarDao.getAllFavorites()
}