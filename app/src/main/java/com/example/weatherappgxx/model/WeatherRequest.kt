package com.example.weatherappgxx.model

import com.google.gson.annotations.SerializedName

data class WeatherRequest(
    @SerializedName("lat")
    var lat: Double?,
    @SerializedName("lon")
    var lon: Double?,
    @SerializedName("units")
    var units: String
)