package com.example.centerofcat.domain.entities

import com.squareup.moshi.Json

data class BreedCatInfo(
    @Json(name = "weight")
    val weight: WeightCatInfo? = null,

    @Json(name = "id")
    val id: String? = null,

    @Json(name = "name")
    val name: String? = null,

    @Json(name = "cfa_url")
    val cfaUrl: String? = null,

    @Json(name = "vetstreet_url")
    val vetstreetUrl: String? = null,

    @Json(name = "vcahospitals_url")
    val vcahospitalsUrl: String? = null,

    @Json(name = "temperament")
    val temperament: String? = null,

    @Json(name = "origin")
    val origin: String? = null,

    @Json(name = "country_codes")
    val countryCodes: String? = null,

    @Json(name = "country_code")
    val countryCode: String? = null,

    @Json(name = "description")
    val description: String? = null,

    @Json(name = "life_span")
    val lifeSpan: String? = null,

    @Json(name = "indoor")
    val indoor: Int? = null,

    @Json(name = "lap")
    val lap: Int? = null,

    @Json(name = "alt_names")
    val altNames: String? = null,

    @Json(name = "adaptability")
    val adaptability: Int? = null,

    @Json(name = "affection_level")
    val affectionLevel: Int? = null,

    @Json(name = "child_friendly")
    val childFriendly: Int? = null,

    @Json(name = "dog_friendly")
    val dogFriendly: Int? = null,

    @Json(name = "energy_level")
    val energyLevel: Int? = null,

    @Json(name = "grooming")
    val grooming: Int? = null,

    @Json(name = "health_issues")
    val healthIssues: Int? = null,

    @Json(name = "intelligence")
    val intelligence: Int? = null,

    @Json(name = "shedding_level")
    var sheddingLevel: Int? = null,

    @Json(name = "social_needs")
    var socialNeeds: Int? = null,

    @Json(name = "stranger_friendly")
    var strangerFriendly: Int? = null,

    @Json(name = "vocalisation")
    var vocalisation: Int? = null,

    @Json(name = "experimental")
    var experimental: Int? = null,

    @Json(name = "hairless")
    var hairless: Int? = null,

    @Json(name = "natural")
    var natural: Int? = null,

    @Json(name = "rare")
    var rare: Int? = null,

    @Json(name = "rex")
    var rex: Int? = null,

    @Json(name = "suppressed_tail")
    var suppressedTail: Int? = null,

    @Json(name = "short_legs")
    var shortLegs: Int? = null,

    @Json(name = "wikipedia_url")
    var wikipediaUrl: String? = null,

    @Json(name = "hypoallergenic")
    var hypoallergenic: Int? = null,

    @Json(name = "reference_image_id")
    var referenceImageId: String? = null,

    @Json(name = "image")
    var image: ImageCatInfo? = null,

    @Json(name = "cat_friendly")
    var catFriendly: Int? = null,

    @Json(name = "bidability")
    var bidability: Int? = null
)