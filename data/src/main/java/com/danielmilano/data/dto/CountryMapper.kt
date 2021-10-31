package com.danielmilano.data.dto

import com.danielmilano.countries.CountriesQuery
import com.danielmilano.domain.entities.Country

private val IMAGE_HOST = "https://flagcdn.com/"
private val IMAGE_EXT = ".svg"

fun CountriesQuery.Country.toDomain(): Country {
    return Country(
        code = code,
        name = name,
        native = native_,
        flag = IMAGE_HOST + code?.lowercase() + IMAGE_EXT,
        currency = currency,
        continent = continent?.name,
        languages = languages?.filterNotNull()?.filter { !it.name.isNullOrEmpty() }?.map { it.name!! }
    )
}