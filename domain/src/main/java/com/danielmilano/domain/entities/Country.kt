package com.danielmilano.domain.entities

import java.io.Serializable

data class Country(
    val code: String?,
    val name: String?,
    val native: String?,
    val flag: String?,
    val currency: String?,
    val continent: String?,
    val languages: List<String>?
) : Serializable {
    enum class FilterType {
        ALL,
        CONTINENT,
        LANGUAGE
    }
}
