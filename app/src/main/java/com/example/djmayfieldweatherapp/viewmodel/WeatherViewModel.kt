package com.example.djmayfieldweatherapp.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.djmayfieldweatherapp.api.WeatherRepository
import com.example.djmayfieldweatherapp.model.Forecast
import com.example.djmayfieldweatherapp.model.UIState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

const val TAG = "WeatherViewModel"

class WeatherViewModel(
    private val repository: WeatherRepository,
    private val dispatcher: CoroutineDispatcher
): ViewModel() {
    private val _weatherLiveData = MutableLiveData<UIState>()
    val weatherLiveData: LiveData<UIState> get() = _weatherLiveData

    var tempScale = "Kelvin"
    var cityName: MutableLiveData<String> = MutableLiveData()

    private val coroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.e(TAG, "Context: $coroutineContext\nMessage: ${throwable.localizedMessage}",throwable)
        }
    }
     val viewModelSafeScope by lazy {
        viewModelScope + coroutineExceptionHandler
    }

    fun getWeather(cityName: String){
        viewModelSafeScope.launch {
            repository.getForecast(cityName).collect{
                _weatherLiveData.postValue(it)

            }
        }
    }

     fun getCityName(lat: Double, lon: Double, context: Context){
        viewModelSafeScope.launch {
            cityName.value = repository.getCityName(lat, lon, context)

        }
    }



    fun setLoading(){
        _weatherLiveData.value = UIState.Loading
    }
    fun setLoadingForDetails() { _weatherLiveData.value = UIState.Loading }

    fun setSuccessForDetails(item: Forecast) { _weatherLiveData.value = UIState.Success2(item) }

}
