package com.example.notiftes

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

data class NotificationData (
    val title: String,
    val message:String
)