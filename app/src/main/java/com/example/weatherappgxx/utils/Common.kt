package com.example.weatherappgxx.utils

import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.example.weatherappgxx.R
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


object Common {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getTime(date: Long): String {
        try {
            val formatter = DateTimeFormatter.ofPattern("hh:mm a")
            val formattedDtm: String =
                Instant.ofEpochSecond(date).atZone(ZoneId.of("GMT+05:30")).format(formatter)
            return formattedDtm

        } catch (e: Exception) {
            return e.toString()

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDate(date: Long): String {
        try {
            val formatter = DateTimeFormatter.ofPattern("dd-MMM")
            val formattedDtm: String =
                Instant.ofEpochSecond(date).atZone(ZoneId.of("GMT+05:30")).format(formatter)
            return formattedDtm

        } catch (e: Exception) {
            return e.toString()
        }
    }

    fun getWeatherData(weather: String, cloudIV: ImageView) {
        if (weather.equals("Rain")) return cloudIV.setImageDrawable(cloudIV.context.getDrawable(R.drawable.rain))
        else return cloudIV.setImageDrawable(cloudIV.context.getDrawable(R.drawable.cloud))

    }

    private fun isLocationPermissionGranted(context: Activity): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                context, android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context, android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context, arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ), Constant.LOCATION_REQUEST_CODE
            )
            false
        } else {
            true
        }
    }
}