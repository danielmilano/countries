package com.danielmilano.countries.di

import androidx.lifecycle.SavedStateHandle
import com.danielmilano.countries.fragment.countries.CountriesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val countriesModule = module {
    viewModel { (handle: SavedStateHandle) -> CountriesViewModel(handle, get()) }
}