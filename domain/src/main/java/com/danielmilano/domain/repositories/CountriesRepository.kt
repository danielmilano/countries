package com.danielmilano.domain.repositories

import com.danielmilano.domain.common.Result
import com.danielmilano.domain.entities.Country

interface CountriesRepository {
    suspend fun getCountries(): Result<List<Country>>
}