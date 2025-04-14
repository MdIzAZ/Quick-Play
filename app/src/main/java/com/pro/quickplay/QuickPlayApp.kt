package com.pro.quickplay

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


const val CHANNEL_ID = "channel_id"
const val CHANNEL_NAME = "channel_name"

@HiltAndroidApp
class QuickPlayApp: Application(){

    override fun onCreate() {
        super.onCreate()

//        val channel = NotificationChannel(
//            CHANNEL_ID,
//            CHANNEL_NAME,
//            NotificationManager.IMPORTANCE_LOW
//        )
//
//        val notificationManager =
//            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.createNotificationChannel(channel)
    }
}