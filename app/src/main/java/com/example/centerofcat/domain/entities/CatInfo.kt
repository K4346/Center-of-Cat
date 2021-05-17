package com.example.centerofcat.domain.entities
import com.example.centerofcat.domain.entities.BreedCatInfo
import com.example.centerofcat.domain.entities.CategoryCat
import com.squareup.moshi.Json
import java.io.Serializable

data class CatInfo (

    val breeds: List<BreedCatInfo>? = null,

    val width: Int? = null,

    val height: Int? = null,

    val url: String,

    val id: String="",

    val categories: List<CategoryCat>? = null,

    val image_id:String?=null,

    val userId:String?=null,

    val imageId:String?=null,

   //Должен быть только у favorites
    val created_at:String="",

    val image:ImageCatInfo?=null

)