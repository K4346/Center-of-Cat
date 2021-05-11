package com.example.centerofcat.data.repositories

import com.example.centerofcat.di.App
import com.example.centerofcat.domain.entities.*
import com.example.centerofcat.domain.entities.analysis.AnalysisCat
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

    override fun getLoadsCatObject(page: Int): Single<List<CatInfo>> {
        val apiService= App.component.provideApi()
        return apiService.getLoadsCat(page = page)
    }

    override fun getBreedsCatObject(): Single<List<BreedCatInfo>> {
        val apiService= App.component.provideApi()
        return apiService.getBreedsCat()
    }

    override fun postLoadCat(loadCat: LoadCat): Single<LoadCat> {
        val apiService= App.component.provideApi()
        return apiService.postLoadCat(file = loadCat.file)
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

    override fun deleteLoadsCatObject(id: String): Single<CatInfo> {
        val apiService= App.component.provideApi()
        return apiService.deleteLoadsCat(id = id)
    }

    override fun postVoteForCat(voteCat: VoteCat): Single<CatInfo> {
        val apiService= App.component.provideApi()
        return apiService.postVoteAboutCat(params = voteCat)
    }

    override fun getAnalysisAboutLoadCatObject(id: String): Single<List<AnalysisCat>> {
        val apiService= App.component.provideApi()
        return apiService.getAnalysisAboutLoadCat(imageId = id)
    }


}