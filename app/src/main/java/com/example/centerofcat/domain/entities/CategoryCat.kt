package com.example.centerofcat.domain.entities

import com.squareup.moshi.Json

data class CategoryCat(
    @Json(name = "id")
    var id: Int? = null,

    @Json(name = "name")
    var name: String? = null
)