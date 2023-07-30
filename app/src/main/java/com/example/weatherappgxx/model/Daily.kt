package com.example.weatherappgxx.model

import com.google.gson.annotations.SerializedName


data class Daily(

  @SerializedName("dt") var dt: Long? = null,
  @SerializedName("sunrise") var sunrise: Double? = null,
  @SerializedName("sunset") var sunset: Double? = null,
  @SerializedName("moonrise") var moonrise: Double? = null,
  @SerializedName("moonset") var moonset: Double? = null,
  @SerializedName("moon_phase") var moonPhase: Double? = null,
  @SerializedName("summary") var summary: String? = null,
  @SerializedName("temp") var temp: Temp? = Temp(),
  @SerializedName("feels_like") var feelsLike: FeelsLike? = FeelsLike(),
  @SerializedName("pressure") var pressure: Double? = null,
  @SerializedName("humidity") var humidity: Double? = null,
  @SerializedName("dew_point") var dewPoint: Double? = null,
  @SerializedName("wind_speed") var windSpeed: Double? = null,
  @SerializedName("wind_deg") var windDeg: Double? = null,
  @SerializedName("wind_gust") var windGust: Double? = null,
  @SerializedName("weather") var weather: ArrayList<Weather> = arrayListOf(),
  @SerializedName("clouds") var clouds: Double? = null,
  @SerializedName("pop") var pop: Double? = null,
  @SerializedName("rain") var rain: Double? = null,
  @SerializedName("uvi") var uvi: Double? = null,

  )