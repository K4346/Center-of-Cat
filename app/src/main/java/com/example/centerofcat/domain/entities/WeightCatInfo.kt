package com.example.centerofcat.domain.entities

import com.squareup.moshi.Json

data class WeightCatInfo(
    @Json(name = "imperial")
    var imperial: String? = null,

    @Json(name = "metric")
    var metric: String? = null
)