package com.danielmilano.countries.fragment.countries

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danielmilano.domain.common.Result
import com.danielmilano.domain.entities.Country
import com.danielmilano.domain.usecases.GetCountriesUseCase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CountriesViewModel(
    private val mState: SavedStateHandle,
    private val getCountriesUseCase: GetCountriesUseCase,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    companion object {
        private const val QUERY_KEY = "QUERY_KEY"
    }

    private val _getCountriesEvent: MutableStateFlow<GetCountriesEvent> = MutableStateFlow(GetCountriesEvent.LoadCountries)
    val getCountriesEvent: StateFlow<GetCountriesEvent> = _getCountriesEvent

    private var countries = listOf<Country>()
    private var filteredCountries = mutableListOf<Country>()

    var filterBy: Country.FilterType = Country.FilterType.ALL

    private val _query = mState.getLiveData<String>(QUERY_KEY)
    val query: LiveData<String>
        get() = _query

    fun setQuery(query: String?) {
        mState.set(QUERY_KEY, query)
    }

    fun getCountries() {
        viewModelScope.launch(coroutineDispatcher) {
            _getCountriesEvent.value = GetCountriesEvent.Loading
            when (val result = getCountriesUseCase.invoke()) {
                is Result.Success -> {
                    countries = result.data.sortedBy { it.name }
                    _getCountriesEvent.value = GetCountriesEvent.GetCountriesSuccess(countries)
                }
                is Result.Error -> {
                    _getCountriesEvent.value = GetCountriesEvent.GetCountriesError(result.exception)
                }
            }
        }
    }

    fun getFilteredCountries(query: String? = null) {
        viewModelScope.launch(coroutineDispatcher) {
            _getCountriesEvent.value = GetCountriesEvent.Loading
            val result = async(coroutineContext) {
                filteredCountries.clear()
                if (query.isNullOrEmpty()) {
                    filteredCountries.addAll(countries)
                } else {
                    when (filterBy) {
                        Country.FilterType.ALL -> {
                            filteredCountries.addAll(countries.filter { it.name?.contains(query, ignoreCase = true) == true })
                        }
                        Country.FilterType.CONTINENT -> {
                            filteredCountries.addAll(countries.filter { it.continent?.contains(query, ignoreCase = true) == true })
                        }
                        Country.FilterType.LANGUAGE -> {
                            filteredCountries.addAll(countries.filter {
                                it.languages?.any { language ->
                                    language.contains(
                                        query,
                                        ignoreCase = true
                                    )
                                } == true
                            })
                        }
                    }
                }
                filteredCountries.sortedBy { it.name }
            }
            result.await().let { _getCountriesEvent.value = GetCountriesEvent.GetFilteredCountries(ArrayList(it))}
        }
    }

}