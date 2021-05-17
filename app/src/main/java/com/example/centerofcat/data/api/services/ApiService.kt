package com.example.centerofcat.data.api.services

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

    @GET(value = "v1/images/search")
    fun getCat(
        @Query("limit") limit: Int = 10,
        @Query("page") page: Int,
        @Query("order") order: String,
        @Query("breed_ids") breed: String,
        @Query("category_ids") category: String
    ): Single<List<CatInfo>>


    @GET(value = "v1/images")
    fun getLoadsCat(
        @Query("limit") limit: Int = 10,
        @Query("page") page: Int
    ): Single<List<CatInfo>>


    @GET(value = "v1/breeds")
    fun getBreedsCat(
    ): Single<List<BreedCatInfo>>


    @GET(value = "v1/favourites")
    fun getFavouritesCat(
        @Query("limit") limit: Int = 10,
        @Query("page") page: Int
    ): Single<List<CatInfo>>


    @POST(value = "v1/favourites")
    fun postFavouritesCat(
        @Body params: FavouriteEntity
    ): Single<ResponseBody>


    @Multipart
    @POST(value = "v1/images/upload")
    fun postLoadCat(
        @Part file: MultipartBody.Part
    ): Single<ResponseBody>


    @POST(value = "v1/votes")
    fun postVoteAboutCat(
        @Body params: VoteCat
    ): Single<ResponseBody>


    @GET(value = "v1/images/{image_id}/analysis/")
    fun getAnalysisAboutLoadCat(
        @Path("image_id") imageId: String,
    ): Single<List<AnalysisCat>>


    @DELETE(value = "v1/favourites/{favourite_id}/")
    fun deleteFavouritesCat(
        @Path("favourite_id") id: String
    ): Single<ResponseBody>


    @DELETE(value = "v1/images/{image_id}/")
    fun deleteLoadsCat(
        @Path("image_id") id: String
    ): Single<ResponseBody>
}