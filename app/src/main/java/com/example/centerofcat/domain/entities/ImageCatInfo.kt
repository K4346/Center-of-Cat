package com.example.centerofcat.domain.entities

import com.squareup.moshi.Json

data class ImageCatInfo (
    @Json(name = "id")
    val id: String? = null,

    @Json(name = "width")
    val width: Int? = null,

    @Json(name = "height")
    val height: Int? = null,

    @Json(name = "url")
    val url: String? = null
)