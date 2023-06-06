package com.example.djmayfieldweatherapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.djmayfieldweatherapp.databinding.FragmentForecastListBinding
import com.example.djmayfieldweatherapp.model.Forecast
import com.example.djmayfieldweatherapp.model.UIState
import com.example.djmayfieldweatherapp.model.WeatherResponse

class ForecastListFragment: ViewModelFragment() {
    private lateinit var binding: FragmentForecastListBinding
    private val args: ForecastListFragmentArgs by navArgs()

    private val weatherForecastAdapter by lazy {
        WeatherForecastAdapter(openDetails = ::openDetails)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForecastListBinding.inflate(layoutInflater)
        configureObserver()
        return binding.root
    }

    private fun configureObserver() {
        viewModel.weatherLiveData.observe(viewLifecycleOwner) { uiState->
            when (uiState){
                is UIState.Loading -> {
                    viewModel.getWeather(args.cityName)
                    if (args.tempScale == "Celsius" ) { weatherForecastAdapter.celsius()}
                    else if(args.tempScale == "Fahrenheit") {weatherForecastAdapter.fahrenheit()}
                }

                is UIState.Error -> {
                    binding.pbLoading.visibility = View.GONE
                    binding.tvLoadingText.text = "City Not Found"
                    binding.tvLoadingText.textSize = 40F
                    Toast.makeText(context, "Please Try Again", Toast.LENGTH_SHORT).show()
                    binding.tvCityName.text = args.cityName.uppercase()
                    }

                is UIState.Success<*> -> {
                    binding.apply {
                        pbLoading.visibility = View.GONE
                        binding.tvLoadingText.visibility = View.GONE
                        tvCityName.text = args.cityName.uppercase()
                        weatherForecastAdapter.setForecastList((uiState.response as WeatherResponse).list)
                        rvForecast.adapter = weatherForecastAdapter
                    }
                }

                else -> {}
            }
        }
    }




    private fun openDetails(item: Forecast){
        viewModel.setLoadingForDetails()
        findNavController().navigate(
            ForecastListFragmentDirections.actionForecastToDetails(item)

        )
    }
}
