package com.example.centerofcat.domain.entities

import com.squareup.moshi.Json

data class ImageCatInfo (
    @Json(name = "id")
    var id: String? = null,

    @Json(name = "width")
    var width: Int? = null,

    @Json(name = "height")
    var height: Int? = null,

    @Json(name = "url")
    var url: String? = null
)