package com.example.m1tmdbapp2023

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


const  val TMDB_API_KEY = "f8c59b73c44d9240c1ded0a07da0d5f5"
interface ITmdbApi {

    // TMDB API call example:
    // https://api.themoviedb.org/3/configuration?api_key=f8c59b73c44d9240c1ded0a07da0d5f5
    // https://api.themoviedb.org/3/person/popular?api_key=f8c59b73c44d9240c1ded0a07da0d5f5&page=1

    @GET("person/popular")
    fun getPopularPerson(
        @Query("api_key") apiKey: String,
        @Query("page") pageNb: Int
    ): Call<PersonPopularResponse>
}