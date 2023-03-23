package com.example.m1tmdbapp2023

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat

class TmdbNotifications {
    companion object {
        fun createPopularPersonNotification(context : Context, p : Person) {
            // Create notification
            val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(androidx.appcompat.R.drawable.btn_radio_off_mtrl)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(context.getString(R.string.notification_content, p.name.toString(), p.popularity))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()

            // Show notification
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            // TODO generate unique ID
            notificationManager.notify(2023, notification)
        }
    }
}