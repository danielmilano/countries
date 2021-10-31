package com.danielmilano.countries.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.danielmilano.countries.datasource.CountriesRemoteDataSourceImplTest
import com.danielmilano.countries.fragment.countries.CountriesViewModel
import com.danielmilano.countries.fragment.countries.GetCountriesEvent
import com.danielmilano.countries.repository.CountriesRepositoryImplTest
import com.danielmilano.domain.usecases.GetCountriesUseCase
import com.google.common.truth.Truth.assertThat
import com.danielmilano.countries.helpers.MainCoroutineRule
import com.danielmilano.domain.entities.Country
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CountriesViewModelUnitTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val savedStateHandle = mockk<SavedStateHandle>(relaxed = true)
    private lateinit var viewModel: CountriesViewModel

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        viewModel = CountriesViewModel(
            savedStateHandle,
            GetCountriesUseCase(CountriesRepositoryImplTest(CountriesRemoteDataSourceImplTest())),
            mainCoroutineRule.testDispatcher
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `get list countries , returns success`() = mainCoroutineRule.testDispatcher.runBlockingTest {
        val events = mutableListOf<GetCountriesEvent>()
        val job = launch {
            viewModel.getCountriesEvent.toList(events)
        }
        viewModel.getCountries()
        assertThat(events[0] is GetCountriesEvent.LoadCountries)
        assertThat(events[1] is GetCountriesEvent.Loading)
        assertThat(events[2] is GetCountriesEvent.GetCountriesSuccess)
        val countries = (events[2] as GetCountriesEvent.GetCountriesSuccess).countries
        assertThat(countries).isNotEmpty()
        assertThat(countries.size).isEqualTo(250)
        job.cancel()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `get list countries then get country filtered by name , returns success`() = mainCoroutineRule.testDispatcher.runBlockingTest {
        val events = mutableListOf<GetCountriesEvent>()
        val job = launch {
            viewModel.getCountriesEvent.toList(events)
        }
        viewModel.getCountries()
        viewModel.getFilteredCountries("Italy")
        assertThat(events[0] is GetCountriesEvent.LoadCountries)
        assertThat(events[1] is GetCountriesEvent.Loading)
        assertThat(events[2] is GetCountriesEvent.GetCountriesSuccess)
        assertThat(events[3] is GetCountriesEvent.Loading)
        assertThat(events[4] is GetCountriesEvent.GetFilteredCountries)
        val countries = (events[4] as GetCountriesEvent.GetFilteredCountries).countries
        assertThat(countries).isNotEmpty()
        assertThat(countries.size).isEqualTo(1)
        assertThat(countries[0].name).isEqualTo("Italy")
        job.cancel()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `get list countries then get country filtered by language , returns success`() = mainCoroutineRule.testDispatcher.runBlockingTest {
        val events = mutableListOf<GetCountriesEvent>()
        val job = launch {
            viewModel.getCountriesEvent.toList(events)
        }
        viewModel.getCountries()
        viewModel.filterBy = Country.FilterType.LANGUAGE
        viewModel.getFilteredCountries("italian")
        assertThat(events[0] is GetCountriesEvent.LoadCountries)
        assertThat(events[1] is GetCountriesEvent.Loading)
        assertThat(events[2] is GetCountriesEvent.GetCountriesSuccess)
        assertThat(events[3] is GetCountriesEvent.Loading)
        assertThat(events[4] is GetCountriesEvent.GetFilteredCountries)
        val countries = (events[4] as GetCountriesEvent.GetFilteredCountries).countries
        assertThat(countries).isNotEmpty()
        assertThat(countries.size).isGreaterThan(1)
        assertThat(countries.find { it.name == "Italy" }).isNotNull()
        job.cancel()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `get list countries then get country filtered by continent , returns success`() = mainCoroutineRule.testDispatcher.runBlockingTest {
        val events = mutableListOf<GetCountriesEvent>()
        val job = launch {
            viewModel.getCountriesEvent.toList(events)
        }
        viewModel.getCountries()
        viewModel.filterBy = Country.FilterType.CONTINENT
        viewModel.getFilteredCountries("europe")
        assertThat(events[0] is GetCountriesEvent.LoadCountries)
        assertThat(events[1] is GetCountriesEvent.Loading)
        assertThat(events[2] is GetCountriesEvent.GetCountriesSuccess)
        assertThat(events[3] is GetCountriesEvent.Loading)
        assertThat(events[4] is GetCountriesEvent.GetFilteredCountries)
        val countries = (events[4] as GetCountriesEvent.GetFilteredCountries).countries
        assertThat(countries).isNotEmpty()
        assertThat(countries.size).isGreaterThan(20)
        assertThat(countries.find { it.name == "Italy" }).isNotNull()
        assertThat(countries.find { it.name == "India" }).isNull()
        job.cancel()
    }
}