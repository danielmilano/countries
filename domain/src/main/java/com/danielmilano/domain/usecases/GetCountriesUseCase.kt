package com.danielmilano.domain.usecases

import com.danielmilano.domain.repositories.CountriesRepository

class GetCountriesUseCase(private val countriesRepository: CountriesRepository) {
    suspend operator fun invoke() = countriesRepository.getCountries()
}