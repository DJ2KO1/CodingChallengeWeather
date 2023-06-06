package com.example.djmayfieldweatherapp.api

import com.example.djmayfieldweatherapp.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

//API Key: 0954d7668747eef95a3bf8ba29b29a11
//default temp is kelvin
//https://api.openweathermap.org/data/2.5/forecast?q={city}&appid={api key}

interface APIService {
    @GET("data/2.5/forecast")
    suspend fun getForecast(
        @Query("q") cityName: String,
        @Query("appid") appid: String = "0954d7668747eef95a3bf8ba29b29a11"
    ):Response<WeatherResponse>

}