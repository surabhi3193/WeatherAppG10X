package com.example.weatherappgxx.network

import android.app.Application
import android.content.Context
import com.example.weatherappgxx.utils.Common

class WeatherApplication : Application() {
    init {
        instance = this
    }

    companion object {
        private var instance: WeatherApplication? = null
        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
    }
}