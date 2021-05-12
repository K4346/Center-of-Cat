package com.example.centerofcat.domain.repositories

import com.example.centerofcat.domain.entities.BreedCatInfo
import com.example.centerofcat.domain.entities.CatInfo
import com.example.centerofcat.domain.entities.FavouriteEntity
import com.example.centerofcat.domain.entities.VoteCat
import com.example.centerofcat.domain.entities.analysis.AnalysisCat
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.ResponseBody

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
        file: MultipartBody.Part
    ): Single<ResponseBody>

    fun getFavouritesCat(
        page: Int,
    ): Single<List<CatInfo>>

    fun postFavouritesCatObject(
        favouriteEntity: FavouriteEntity
    ): Single<ResponseBody>

    fun deleteFavouritesCatObject(
        id: String
    ): Single<ResponseBody>

    fun deleteLoadsCatObject(
        id: String
    ): Single<ResponseBody>

    fun postVoteForCat(
        voteCat: VoteCat
    ): Single<ResponseBody>

    fun getAnalysisAboutLoadCatObject(
        id: String
    ): Single<List<AnalysisCat>>
}