package com.danielmilano.countries.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load

const val PLACEHOLDER = "-"

@BindingAdapter("languages")
fun languages(view: TextView, languages: List<String>?) {
    view.text = languages?.let { languagesList ->
        if (languagesList.isNotEmpty()) {
            languagesList.joinToString(separator = ", ") { it }
        } else {
            PLACEHOLDER
        }
    } ?: run {
        PLACEHOLDER
    }
}

@BindingAdapter("currency")
fun currency(view: TextView, currency: String?) {
    view.text = currency?.let {
        if (currency.isNotEmpty()) {
            currency
        } else {
            PLACEHOLDER
        }
    } ?: run {
        PLACEHOLDER
    }
}

@BindingAdapter("flag")
fun flag(view: ImageView, flag: String?) {
    view.load(flag)
}