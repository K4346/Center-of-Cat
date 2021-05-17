package com.example.centerofcat.domain.entities

import com.squareup.moshi.Json

data class BreedCatInfo(


    val id: String? = null,


    val name: String? = null,


    val cfaUrl: String? = null,


    val vetstreetUrl: String? = null,


    val vcahospitalsUrl: String? = null,


    val temperament: String? = null,


    val origin: String? = null,


    val countryCodes: String? = null,

    val countryCode: String? = null,

    val description: String? = null,

    val lifeSpan: String? = null,

    val indoor: Int? = null,

    val lap: Int? = null,

    val altNames: String? = null,


    val adaptability: Int? = null,


)