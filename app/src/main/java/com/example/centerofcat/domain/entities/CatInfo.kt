package com.example.centerofcat.domain.entities
import com.example.centerofcat.domain.entities.BreedCatInfo
import com.example.centerofcat.domain.entities.CategoryCat
import com.squareup.moshi.Json
import java.io.Serializable

data class CatInfo (
    @Json(name = "breeds")
    val breeds: List<BreedCatInfo>? = null,

    @Json(name = "width")
    val width: Int? = null,

    @Json(name = "height")
    val height: Int? = null,

    @Json(name = "url")
    val url: String,

    @Json(name = "id")
    val id: String="",

    @Json(name = "categories")
    val categories: List<CategoryCat>? = null,

    //Favourites без id
    @Json(name = "image_id")
    val image_id:String?=null,

    @Json(name = "userId")
    val userId:String?=null,

    @Json(name = "imageId")
    val imageId:String?=null,

    @Json(name = "createdAt")
    val createdAt:String="",

    @Json(name = "image")
    val image:ImageCatInfo?=null

)