package com.example.centerofcat.data.api

import com.example.centerofcat.domain.entities.*

import io.reactivex.Single
import retrofit2.http.*
import java.io.File

interface ApiService {
    @GET (value = "v1/images/search")
    fun getCat(
        @Query ("api_key") apiKey:String="2d63512c-1c5f-496b-8250-71b91514da66",
        @Query ("limit") limit:Int=10,
        @Query ("page") page:Int,
        @Query ("order") order:String,
        @Query ("breed_ids") breed:String,
        @Query ("category_ids") category:String
    ):Single<List<CatInfo>>

    @GET (value = "v1/breeds")
    fun getBreedsCat(
        @Query ("api_key") apiKey:String="2d63512c-1c5f-496b-8250-71b91514da66",
    ):Single<List<BreedCatInfo>>





    @GET(value = "v1/favourites")
    fun getFavouritesCat(
        @Query ("api_key") apiKey:String="2d63512c-1c5f-496b-8250-71b91514da66",
        @Query ("limit") limit: Int=10,
        @Query ("page") page: Int
    ):Single<List<CatInfo>>


    @POST(value = "v1/favourites")
    fun postFavouritesCat(
        @Header ("x-api-key") apiKey:String="2d63512c-1c5f-496b-8250-71b91514da66",
        @Body params: FavouriteEntity
    ):Single<CatInfo>

    @POST(value = "v1/favourites")
    fun postLoadCat(
        @Header ("x-api-key") apiKey:String="2d63512c-1c5f-496b-8250-71b91514da66",
        @Body params: LoadCat
    ):Single<LoadCat>


    @POST(value = "v1/votes")
    fun postVoteAboutCat(
        @Header ("x-api-key") apiKey:String="2d63512c-1c5f-496b-8250-71b91514da66",
        @Body params: VoteCat
    ):Single<CatInfo>






    @DELETE(value = "v1/favourites/{favourite_id}/")
    fun deleteFavouritesCat(
        @Header ("x-api-key") apiKey:String="2d63512c-1c5f-496b-8250-71b91514da66",
        @Path ("favourite_id") id: String
    ):Single<CatInfo>



}