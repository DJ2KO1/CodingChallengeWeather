package com.example.djmayfieldweatherapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class WeatherResponse(
    val city: City,
    val list: List<Forecast>,
    val data: Forecast? = null
)

@Parcelize
data class City(
    val coord: Coord? = null,
    val country: String? = null,
    val id: Int? = null,
    val name: String? = null,
    val population: Int? = null,
    val sunrise: Int? = null,
    val sunset: Int? = null,
    val timezone: Int?
) : Parcelable

@Parcelize
data class Forecast (
    val clouds: Clouds? = null,
    val dt: Int,
    val dt_txt: String? = null,
    val main: Main? = null,
    val pop: Double? = null,
    val rain: Rain? = null,
    val visibility: Int? = null,
    val weather: List<Weather>? = null,
    val wind: Wind? = null
) : Parcelable

@Parcelize
data class Coord(
    val lat: Double? = null,
    val lon: Double? = null
) : Parcelable

@Parcelize
data class Clouds(
    val all: Int? = null
) : Parcelable

@Parcelize
data class Main(
    val feels_like: Double? = null,
    val grnd_level: Int? = null,
    val humidity: Int? = null,
    val pressure: Int? = null,
    val sea_level: Int? = null,
    val temp: Double? = null,
    val temp_kf: Double? = null,
    val temp_max: Double? = null,
    val temp_min: Double ? = null
) : Parcelable

@Parcelize
data class Rain(
    val `3h`: Double? = null
) : Parcelable

@Parcelize
data class Weather(
    val description: String? = null,
    val icon: String? = null,
    val id: Int? = null,
    val main: String? = null
) : Parcelable

@Parcelize
data class Wind(
    val deg: Int? = null,
    val gust: Double? = null,
    val speed: Double? = null
) : Parcelable