package com.example.centerofcat.data.repositories

import com.example.centerofcat.di.App
import com.example.centerofcat.domain.entities.BreedCatInfo
import com.example.centerofcat.domain.entities.CatInfo
import com.example.centerofcat.domain.entities.FavouriteEntity
import com.example.centerofcat.domain.entities.LoadCat
import com.example.centerofcat.domain.repositories.CatModel
import io.reactivex.Single
import java.io.File

class CatModelImpl: CatModel {
    override fun getCatObject(
        page: Int,
        order: String,
        breed_ids: String,
        category: String
    ): Single<List<CatInfo>> {
        val apiService= App.component.provideApi()
        return apiService.getCat(page = page,order = order,breed = breed_ids,category = category)
    }

    override fun getBreedsCatObject(): Single<List<BreedCatInfo>> {
        val apiService= App.component.provideApi()
        return apiService.getBreedsCat()
    }

    override fun postLoadCat(loadCat: LoadCat): Single<LoadCat> {
        val apiService= App.component.provideApi()
        return apiService.postLoadCat(params = loadCat)
    }


    override fun getFavouritesCat(page: Int): Single<List<CatInfo>> {
        val apiService=App.component.provideApi()
        return apiService.getFavouritesCat(page=page)
    }


    override fun postFavouritesCatObject(favouriteEntity: FavouriteEntity): Single<CatInfo> {
        val apiService= App.component.provideApi()
        return apiService.postFavouritesCat(params = favouriteEntity)
    }

    override fun deleteFavouritesCatObject(id: String): Single<CatInfo> {
        val apiService= App.component.provideApi()
        return apiService.deleteFavouritesCat(id = id)
    }

    fun g(){

}}