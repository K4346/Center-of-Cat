package com.example.centerofcat.domain.entities

import com.squareup.moshi.Json

data class ImageCatInfo (
    @field:Json(name = "id")
    val id: String? = null,

    @field:Json(name = "width")
    val width: Int? = null,

    @field:Json(name = "height")
    val height: Int? = null,

    @field:Json(name = "url")
    val url: String? = null
)