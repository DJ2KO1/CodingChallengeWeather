package com.example.djmayfieldweatherapp.model


// group of related objects
sealed class UIState {
    object Loading: UIState()
    class Error(val error: Exception): UIState()
    class Success<T>(val response: T): UIState()
    class Success2<Forecast>(val response: Forecast): UIState()
}
