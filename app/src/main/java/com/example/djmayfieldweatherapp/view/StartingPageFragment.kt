package com.example.djmayfieldweatherapp.view

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.view.doOnAttach
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doBeforeTextChanged
import androidx.navigation.fragment.findNavController
import com.example.djmayfieldweatherapp.databinding.FragmentStartingPageBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Locale
import java.util.jar.Manifest

class StartingPageFragment : ViewModelFragment(

) {
    private lateinit var binding: FragmentStartingPageBinding
    private lateinit var  fusedLocationProviderClient: FusedLocationProviderClient
    private val pref: SharedPreferences? = context?.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
    private val editor = pref?.edit()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentStartingPageBinding.inflate(layoutInflater)
        binding.currentCity.isClickable = false
        binding.etCity.setText(pref?.getString("CITY", ""))


        binding.btnSearch.setOnClickListener {

            if (binding.etCity.text.isBlank()) {
                Toast.makeText(context, "Please Enter a City Name", Toast.LENGTH_SHORT).show()
            } else {
                changeTempScale()
                println(binding.spnTempSelect.selectedItem.toString())
                viewModel.setLoading()
                editor?.putString("CITY", binding.etCity.text.toString())
                findNavController().navigate(
                    StartingPageFragmentDirections.actionStartingToForecast(
                        binding.etCity.text.toString(),
                        binding.spnTempSelect.selectedItem.toString()
                    )
                )

            }
        }

        binding.btnCurrentLocation.setOnClickListener {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
            binding.currentCity.setText(requestLocation())

            changeTempScale()
            viewModel.setLoading()
            if (!binding.currentCity.text.isNullOrBlank())
                editor?.putString("CITY", binding.currentCity.text.toString())

            findNavController().navigate(
                StartingPageFragmentDirections.actionStartingToForecast(
                    binding.currentCity.text.toString(),
                    binding.spnTempSelect.selectedItem.toString()
                )
            )

        }
        binding.currentCity.doOnAttach {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
            binding.currentCity.setText(requestLocation())
        }


        return binding.root
    }

   private fun requestLocation(): String? {
        val task = fusedLocationProviderClient.lastLocation
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                0
            )
            return null
        }
        task.addOnSuccessListener {
            it?.let {

                viewModel.getCityName(it.latitude, it.longitude,requireContext())

            }
        }
       return viewModel.cityName.value
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            0 -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

            }
            else -> {}

        }
    }

    private fun changeTempScale() {
        when (binding.spnTempSelect.selectedItem.toString()) {
            "Celsius" -> {
                viewModel.tempScale = "Celsius"
            }

            "Fahrenheit" -> {
                viewModel.tempScale = "Fahrenheit"
            }

            else -> viewModel.tempScale = "Kelvin"
        }
    }
}