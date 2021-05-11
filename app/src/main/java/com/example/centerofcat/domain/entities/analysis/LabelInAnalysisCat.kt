package com.example.centerofcat.domain.entities.analysis

import com.squareup.moshi.Json

data class LabelInAnalysisCat (
    @field:Json( name = "Name")
    val name: String? = null,

    @field:Json( name = "Confidence")
    val confidence: Float? = null,
    )