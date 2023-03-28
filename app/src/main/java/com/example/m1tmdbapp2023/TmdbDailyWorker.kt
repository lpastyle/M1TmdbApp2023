package com.example.m1tmdbapp2023

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.io.IOException

class TmdbDailyWorker  (context: Context, params: WorkerParameters) : Worker(context, params) {
    private val LOGTAG = TmdbDailyWorker::class.simpleName

    override fun doWork(): Result {
        Log.d(LOGTAG,"doWork()")
        val tmdbapi = ApiClient.instance.create(ITmdbApi::class.java)
        val call = tmdbapi.getPopularPerson(TMDB_API_KEY, 1)
        try {
            val response = call.execute() // Synchronous call
            if (response.isSuccessful) {
                TmdbNotifications.createPopularPersonNotification(
                    applicationContext,
                    response.body()?.results!![0] // assume first array element has the best popularity
                )
                return Result.success()
            } else {
                return Result.failure()
            }
        } catch (e : IOException) {
            Log.e(LOGTAG,"call to getPopularPerson() failed with ${e.message}")
            return Result.retry()
        }
    }
}