package com.example.weatherappgxx.model

import com.google.gson.annotations.SerializedName


data class Hourly(

  @SerializedName("dt") var dt: Long? = null,
  @SerializedName("temp") var temp: Double? = null,
  @SerializedName("feels_like") var feelsLike: Double? = null,
  @SerializedName("pressure") var pressure: Double? = null,
  @SerializedName("humidity") var humidity: Double? = null,
  @SerializedName("dew_point") var dewPoint: Double? = null,
  @SerializedName("uvi") var uvi: Double? = null,
  @SerializedName("clouds") var clouds: Double? = null,
  @SerializedName("visibility") var visibility: Double? = null,
  @SerializedName("wind_speed") var windSpeed: Double? = null,
  @SerializedName("wind_deg") var windDeg: Double? = null,
  @SerializedName("wind_gust") var windGust: Double? = null,
  @SerializedName("weather") var weather: ArrayList<Weather> = arrayListOf(),
  @SerializedName("pop") var pop: Double? = null,
  @SerializedName("rain") var rain: Rain? = Rain(),

  )