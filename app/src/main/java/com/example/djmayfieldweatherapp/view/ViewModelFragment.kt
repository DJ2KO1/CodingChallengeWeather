package com.example.djmayfieldweatherapp.view

import androidx.fragment.app.Fragment
import com.example.djmayfieldweatherapp.di.DI

open class ViewModelFragment: Fragment() {

    protected val viewModel by lazy {
        DI.provideViewModel(requireActivity())
    }
}