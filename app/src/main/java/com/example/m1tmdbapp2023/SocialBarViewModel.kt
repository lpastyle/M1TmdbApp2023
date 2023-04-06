package com.example.m1tmdbapp2023

import androidx.lifecycle.ViewModel

class SocialBarViewModel : ViewModel() {
    var nbLikes = mutableMapOf<Int,Int>()
    var isFavorite = mutableMapOf<Int,Boolean>()
}