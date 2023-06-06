package com.example.djmayfieldweatherapp.api

import android.content.ContentValues
import android.content.Context
import android.location.Geocoder
import android.util.Log
import com.example.djmayfieldweatherapp.model.UIState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Locale

interface WeatherRepository {
    suspend fun getForecast(cityName: String): Flow<UIState>

    suspend fun getCityName(lat: Double, lon: Double, context: Context): String?
}

class WeatherRepositoryImpl(private val service: APIService): WeatherRepository{
    override suspend fun getForecast(cityName: String): Flow<UIState> =
        flow {

            try {
                val response = service.getForecast(cityName)
                if (response.code()==200){
                    emit(response.body()?.let {
                        UIState.Success(it)
                    } ?: throw Exception("Empty Response"))
                } else throw Exception ("Failed Network Call")
            } catch (e: java.lang.Exception){
                emit(UIState.Error(e))
            }
        }

    override suspend fun getCityName(lat: Double, lon: Double, context: Context): String? {
        val cityName: String?
        val geocoder = Geocoder(context, Locale.getDefault())
        val address =geocoder.getFromLocation(lat, lon, 3)


        cityName = address?.get(0)?.locality
        Log.d(ContentValues.TAG, "getCityName: cityName= $cityName ")

        return cityName    }

}