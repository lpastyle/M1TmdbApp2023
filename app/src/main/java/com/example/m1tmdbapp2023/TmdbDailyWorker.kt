package com.example.m1tmdbapp2023

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class TmdbDailyWorker  (context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        TODO("Not yet implemented")

    }
}