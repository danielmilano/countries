package com.danielmilano.countries.datasource

import com.danielmilano.countries.CountriesQuery
import com.danielmilano.countries.helpers.TestUtils
import com.danielmilano.data.dto.toDomain
import com.danielmilano.data.repositories.remote.countries.CountriesRemoteDataSource
import com.danielmilano.domain.common.Result
import com.danielmilano.domain.entities.Country
import com.google.gson.Gson
import com.google.gson.JsonElement
import kotlin.reflect.javaType
import kotlin.reflect.typeOf

class CountriesRemoteDataSourceImplTest : CountriesRemoteDataSource {

    @ExperimentalStdlibApi
    override suspend fun getCountries(): Result<List<Country>> {
        val response : JsonElement = TestUtils.readJsonFile("countries_list.json")
        val result = Gson().fromJsonToObjectType<CountriesQuery.Data>(response.asJsonObject.get("data"))
        return Result.Success(result.countries!!.map { it!!.toDomain() })
    }
}

@ExperimentalStdlibApi
inline fun <reified T> Gson.fromJsonToObjectType(json: JsonElement): T =
    fromJson(json, typeOf<T>().javaType)