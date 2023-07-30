package com.example.weatherappgxx.network

import com.example.weatherappgxx.model.WeatherRequest
import com.example.weatherappgxx.model.WeatherResponse
import retrofit2.Response

class WeatherRepository {
    suspend fun getWeatherInfo(weatherRequest: WeatherRequest): Response<WeatherResponse>? {
        return ApiModule.getApi()?.getWeatherInfo("metric","minutely",
            weatherRequest.lat,weatherRequest.lon)
    }
}