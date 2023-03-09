package com.example.m1tmdbapp2023

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {

    /* companion is used to get the JAVA "static" behavior we need to implement a singleton pattern
      * see https://kotlinlang.org/docs/object-declarations.html#companion-objects
      */
   companion object {
       const val TMDBAPI_BASE_URL = "https://api.themoviedb.org/3/"
        const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w45"

       // keep singleton instance
       val instance = Build()

       private fun Build(): Retrofit {

           // GSON converter creation
           val converter = GsonConverterFactory.create()

           val httpLoggingInterceptor = HttpLoggingInterceptor()
           httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC)
           // HTTP Client

           val okHttpClient = OkHttpClient().newBuilder()
               .readTimeout(30, TimeUnit.SECONDS)
               .writeTimeout(30, TimeUnit.SECONDS)
               .addInterceptor(httpLoggingInterceptor)
               .build()

           return Retrofit.Builder()
               .baseUrl(TMDBAPI_BASE_URL)
               .addConverterFactory(converter)
               .client(okHttpClient)
               .build()
       }
   }
}