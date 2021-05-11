package com.example.centerofcat.domain.entities.analysis

import com.squareup.moshi.Json

data class AnalysisCat(
    @field:Json( name = "labels")
    val labels: List<LabelInAnalysisCat>? = null,

    @field:Json( name = "vendor")
    val vendor: String? = null,

    @field:Json( name = "approved")
    val approved: Int? = null,

    @field:Json( name = "rejected")
    val rejected: Int? = null,

    @field:Json( name = "image_id")
    val image_id: String? = null
    )