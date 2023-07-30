package com.example.weatherappgxx.viewmodel

import androidx.lifecycle.*
import com.example.weatherappgxx.model.BaseResponse
import com.example.weatherappgxx.model.WeatherRequest
import com.example.weatherappgxx.model.WeatherResponse
import com.example.weatherappgxx.network.WeatherRepository
import kotlinx.coroutines.launch

class WeatherViewModel  : ViewModel() {

    val weatherRepo = WeatherRepository()
    val weatherResult: MutableLiveData<BaseResponse<WeatherResponse>> = MutableLiveData()
    fun getWeatherInfo(lat: Double?, lon: Double?) {
        weatherResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {

                val weatherRequest = WeatherRequest(
                     lat,
                    lon,
                    units = "metric"
                )
                val response = weatherRepo.getWeatherInfo(weatherRequest = weatherRequest)
                if (response?.code() == 200) {
                    weatherResult.value = BaseResponse.Success(response.body())
                } else {
                    weatherResult.value = BaseResponse.Error(response?.message())
                }

            } catch (ex: Exception) {
                weatherResult.value = BaseResponse.Error(ex.message)
            }
        }
    }

}