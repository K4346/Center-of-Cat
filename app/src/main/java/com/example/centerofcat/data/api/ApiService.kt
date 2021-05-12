package com.example.centerofcat.data.api

import com.example.centerofcat.domain.entities.BreedCatInfo
import com.example.centerofcat.domain.entities.CatInfo
import com.example.centerofcat.domain.entities.FavouriteEntity
import com.example.centerofcat.domain.entities.VoteCat
import com.example.centerofcat.domain.entities.analysis.AnalysisCat
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface ApiService {

    companion object {
        const val apiKey: String = "2d63512c-1c5f-496b-8250-71b91514da66"
    }

    @GET(value = "v1/images/search")
    fun getCat(
        @Query("api_key") _apiKey: String = apiKey,
        @Query("limit") limit: Int = 10,
        @Query("page") page: Int,
        @Query("order") order: String,
        @Query("breed_ids") breed: String,
        @Query("category_ids") category: String
    ): Single<List<CatInfo>>


    @GET(value = "v1/images")
    fun getLoadsCat(
        @Header("x-api-key") _apiKey: String = apiKey,
        @Query("limit") limit: Int = 10,
        @Query("page") page: Int
    ): Single<List<CatInfo>>


    @GET(value = "v1/breeds")
    fun getBreedsCat(
        @Query("api_key") _apiKey: String = apiKey,
    ): Single<List<BreedCatInfo>>


    @GET(value = "v1/favourites")
    fun getFavouritesCat(
        @Query("api_key") _apiKey: String = apiKey,
        @Query("limit") limit: Int = 10,
        @Query("page") page: Int
    ): Single<List<CatInfo>>


    @POST(value = "v1/favourites")
    fun postFavouritesCat(
        @Header("x-api-key") _apiKey: String = apiKey,
        @Body params: FavouriteEntity
    ): Single<ResponseBody>


    @Multipart
    @POST(value = "v1/images/upload")
    fun postLoadCat(
        @Header("x-api-key") _apiKey: String = apiKey,
        @Part file: MultipartBody.Part
    ): Single<ResponseBody>


    @POST(value = "v1/votes")
    fun postVoteAboutCat(
        @Header("x-api-key") _apiKey: String = apiKey,
        @Body params: VoteCat
    ): Single<ResponseBody>


    @GET(value = "v1/images/{image_id}/analysis/")
    fun getAnalysisAboutLoadCat(
        @Header("x-api-key") _apiKey: String = apiKey,
        @Path("image_id") imageId: String,
    ): Single<List<AnalysisCat>>


    @DELETE(value = "v1/favourites/{favourite_id}/")
    fun deleteFavouritesCat(
        @Header("x-api-key") _apiKey: String = apiKey,
        @Path("favourite_id") id: String
    ): Single<ResponseBody>


    @DELETE(value = "v1/images/{image_id}/")
    fun deleteLoadsCat(
        @Header("x-api-key") _apiKey: String = apiKey,
        @Path("image_id") id: String
    ): Single<ResponseBody>
}