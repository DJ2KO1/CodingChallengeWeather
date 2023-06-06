package com.example.djmayfieldweatherapp.view


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.djmayfieldweatherapp.databinding.ForecastListItemBinding
import com.example.djmayfieldweatherapp.model.Forecast
import kotlin.math.roundToInt


open class WeatherForecastAdapter(
    private val list: MutableList<Forecast> = mutableListOf(),
    private val openDetails: (Forecast) -> Unit
): RecyclerView.Adapter<WeatherForecastAdapter.WeatherViewHolder>() {
    var tempScale: String = "Kelvin"



    fun setForecastList(newList: List<Forecast>){
        list.addAll(newList)
        notifyDataSetChanged()
    }

    inner class WeatherViewHolder(private val binding: ForecastListItemBinding)
        :RecyclerView.ViewHolder(binding.root)
        {
            fun onBind(item: Forecast) {
                binding.apply {
                    when (tempScale) {
                        "Celsius" -> {
                            tvTemp.text= item.main?.temp?.roundToInt()?.minus(273).toString()
                        }
                        "Fahrenheit" -> {
                            tvTemp.text= item.main?.temp?.minus(273)?.times(1.8)?.plus(32)?.roundToInt().toString()
                        }
                        else -> tvTemp.text = item.main?.temp?.roundToInt()?.toString()
                    }
                    tvWeatherDescription.text = item.weather?.get(0)?.description?.uppercase()
                    tvDt.text = item.dt_txt
                }
                binding.root.setOnClickListener {

                    openDetails(item)
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        return WeatherViewHolder(
            ForecastListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun fahrenheit(): String {
        tempScale = "Fahrenheit"

        return tempScale
    }
    fun celsius():String {
        tempScale = "Celsius"
        return tempScale
    }
}