package com.danielmilano.data.repositories.remote.countries

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.await
import com.danielmilano.countries.CountriesQuery
import com.danielmilano.data.api.CountriesService
import com.danielmilano.data.dto.toDomain
import com.danielmilano.domain.common.Result
import com.danielmilano.domain.entities.Country
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class CountriesRemoteDataSourceImpl(private val apolloClient: ApolloClient) : CountriesRemoteDataSource {

    override suspend fun getCountries(): Result<List<Country>> {
        return try {
            val countryList = mutableListOf<Country>()
            apolloClient.query(CountriesQuery()).await().data?.countries?.forEach { it != null && countryList.add(it.toDomain()) }
            Result.Success(countryList)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}