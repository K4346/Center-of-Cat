package com.example.centerofcat.domain.entities

import com.squareup.moshi.Json

data class CategoryCat(
    @Json(name = "id")
    val id: Int? = null,

    @Json(name = "name")
    val name: String? = null
)