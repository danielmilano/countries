package com.danielmilano.data.repositories

import com.danielmilano.data.repositories.remote.countries.CountriesRemoteDataSource
import com.danielmilano.domain.entities.Country
import com.danielmilano.domain.common.Result
import com.danielmilano.domain.repositories.CountriesRepository

class CountriesRepositoryImpl(
    private val remoteDataSource: CountriesRemoteDataSource
) : CountriesRepository {

    override suspend fun getCountries(): Result<List<Country>> {
        return remoteDataSource.getCountries()
    }
}