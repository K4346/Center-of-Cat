package com.example.centerofcat.domain.repositories

import com.example.centerofcat.domain.entities.*
import io.reactivex.Single
import java.io.File

interface CatModel {

    fun getCatObject(
        page: Int,
        order: String,
        breed_ids: String,
        category: String
    ): Single<List<CatInfo>>


    fun getLoadsCatObject(
        page: Int
    ): Single<List<CatInfo>>

    fun getBreedsCatObject(
    ): Single<List<BreedCatInfo>>

    fun postLoadCat(
        loadCat: LoadCat
    ): Single<LoadCat>

    fun getFavouritesCat(
        page: Int,
    ): Single<List<CatInfo>>

    fun postFavouritesCatObject(
        favouriteEntity: FavouriteEntity
    ): Single<CatInfo>

    fun deleteFavouritesCatObject(
        id: String
    ): Single<CatInfo>

    fun deleteLoadsCatObject(
        id: String
    ): Single<CatInfo>

    fun postVoteForCat(
        voteCat: VoteCat
    ): Single<CatInfo>
}