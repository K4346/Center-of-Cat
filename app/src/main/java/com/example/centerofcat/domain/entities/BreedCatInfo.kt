package com.example.centerofcat.domain.entities

import com.squareup.moshi.Json

data class BreedCatInfo(
    @Json(name = "weight")
    var weight: WeightCatInfo? = null,

    @Json(name = "id")
    var id: String? = null,

    @Json(name = "name")
    var name: String? = null,

    @Json(name = "cfa_url")
    var cfaUrl: String? = null,

    @Json(name = "vetstreet_url")
    var vetstreetUrl: String? = null,

    @Json(name = "vcahospitals_url")
    var vcahospitalsUrl: String? = null,

    @Json(name = "temperament")
    var temperament: String? = null,

    @Json(name = "origin")
    var origin: String? = null,

    @Json(name = "country_codes")
    var countryCodes: String? = null,

    @Json(name = "country_code")
    var countryCode: String? = null,

    @Json(name = "description")
    var description: String? = null,

    @Json(name = "life_span")
    var lifeSpan: String? = null,

    @Json(name = "indoor")
    var indoor: Int? = null,

    @Json(name = "lap")
    var lap: Int? = null,

    @Json(name = "alt_names")
    var altNames: String? = null,

    @Json(name = "adaptability")
    var adaptability: Int? = null,

    @Json(name = "affection_level")
    var affectionLevel: Int? = null,

    @Json(name = "child_friendly")
    var childFriendly: Int? = null,

    @Json(name = "dog_friendly")
    var dogFriendly: Int? = null,

    @Json(name = "energy_level")
    var energyLevel: Int? = null,

    @Json(name = "grooming")
    var grooming: Int? = null,

    @Json(name = "health_issues")
    var healthIssues: Int? = null,

    @Json(name = "intelligence")
    var intelligence: Int? = null,

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