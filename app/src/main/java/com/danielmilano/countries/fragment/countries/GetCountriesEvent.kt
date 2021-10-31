package com.danielmilano.countries.fragment.countries

import com.danielmilano.domain.entities.Country
import java.lang.Exception

sealed class GetCountriesEvent {
    data class GetCountriesSuccess(val countries: List<Country>) : GetCountriesEvent()
    data class GetFilteredCountries(val countries: List<Country>) : GetCountriesEvent()
    data class GetCountriesError(val exception: Exception) : GetCountriesEvent()
    object Loading : GetCountriesEvent()
    object LoadCountries : GetCountriesEvent()
}