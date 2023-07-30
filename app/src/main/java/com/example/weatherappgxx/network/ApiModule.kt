package com.example.weatherappgxx.network

import com.example.weatherappgxx.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiModule {

    @GET("onecall?appid=a65877a7e0901b7b8029592f9938c355")
    suspend fun getWeatherInfo(
        @Query("units") unit: String,
        @Query("exclude") exclude: String,
        @Query("lat") lat: Double?,
        @Query("lon") lng: Double?
    ): Response<WeatherResponse>

    companion object {
        fun getApi(): ApiModule? {
            return ApiClient.client?.create(ApiModule::class.java)
        }
    }
}