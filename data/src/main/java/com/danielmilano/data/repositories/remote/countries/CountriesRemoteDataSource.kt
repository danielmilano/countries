package com.danielmilano.data.repositories.remote.countries

import com.danielmilano.domain.common.Result
import com.danielmilano.domain.entities.Country

interface CountriesRemoteDataSource {
   suspend fun getCountries(): Result<List<Country>>
}