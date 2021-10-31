package com.danielmilano.countries.repository

import com.danielmilano.countries.datasource.CountriesRemoteDataSourceImplTest
import com.danielmilano.domain.common.Result
import com.danielmilano.domain.entities.Country
import com.danielmilano.domain.repositories.CountriesRepository

class CountriesRepositoryImplTest(private val dataSourceImplTest: CountriesRemoteDataSourceImplTest) : CountriesRepository {

    @ExperimentalStdlibApi
    override suspend fun getCountries(): Result<List<Country>> {
        return dataSourceImplTest.getCountries()
    }
}