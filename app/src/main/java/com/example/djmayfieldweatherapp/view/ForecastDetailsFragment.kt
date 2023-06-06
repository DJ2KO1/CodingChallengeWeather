package com.example.djmayfieldweatherapp.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.djmayfieldweatherapp.databinding.FragmentForecastDetailsBinding
import com.example.djmayfieldweatherapp.model.Forecast
import com.example.djmayfieldweatherapp.model.UIState
import kotlin.math.roundToInt

class ForecastDetailsFragment : ViewModelFragment(){
    private lateinit var binding: FragmentForecastDetailsBinding
    private val args: ForecastDetailsFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForecastDetailsBinding.inflate(layoutInflater)
        configureObserver()
            return binding.root
    }


    private fun configureObserver() {
        viewModel.weatherLiveData.observe(viewLifecycleOwner) { uiState->
            when (uiState){
                is UIState.Loading -> {
                    viewModel.setSuccessForDetails(args.item!!)
                    println(args.item?.main?.temp.toString())
                }

                is UIState.Error -> {

                }

                is UIState.Success<*> -> {

                }

                is UIState.Success2<*> -> {
                    val item = uiState.response as Forecast


                    binding.apply {
                        when (viewModel.tempScale) {
                            "Celsius" -> {
                                tvDetailsTemp.text = item.main?.temp?.roundToInt()?.minus(273).toString()
                                tvDetailsFeel.text= item.main?.feels_like?.roundToInt()?.minus(273).toString()
                                tvHighTem.text = item.main?.temp_max?.roundToInt()?.minus(273).toString()
                                tvLowTem.text = item.main?.temp_min?.roundToInt()?.minus(273).toString()
                            }
                            "Fahrenheit" -> {
                                tvDetailsTemp.text = item.main?.temp?.minus(273)?.times(1.8)?.plus(32)?.roundToInt().toString()
                                tvDetailsFeel.text= item.main?.feels_like?.minus(273)?.times(1.8)?.plus(32)?.roundToInt().toString()
                                tvHighTem.text = item.main?.temp_max?.minus(273)?.times(1.8)?.plus(32)?.roundToInt().toString()
                                tvLowTem.text = item.main?.temp_min?.minus(273)?.times(1.8)?.plus(32)?.roundToInt().toString()
                            }
                            else -> {
                                tvDetailsTemp.text  = item.main?.temp?.roundToInt()?.toString()
                                tvDetailsFeel.text= item.main?.feels_like?.roundToInt()?.toString()
                                tvHighTem.text = item.main?.temp_max?.roundToInt().toString()
                                tvLowTem.text = item.main?.temp_min?.roundToInt().toString()

                            }
                        }
                            tvDetailsWeather.text =  item.weather?.get(0)?.main?.uppercase() + " with " + item.weather?.get(0)?.description?.uppercase()
                        tvHumidity.text = item.main?.humidity?.toString()


                    }
                }

            }
        }
    }

}