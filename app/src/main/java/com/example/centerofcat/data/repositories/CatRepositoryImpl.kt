package com.example.centerofcat.data.repositories

import com.example.centerofcat.app.App
import com.example.centerofcat.domain.entities.BreedCatInfo
import com.example.centerofcat.domain.entities.CatInfo
import com.example.centerofcat.domain.entities.FavouriteEntity
import com.example.centerofcat.domain.entities.VoteCat
import com.example.centerofcat.domain.entities.analysis.AnalysisCat
import com.example.centerofcat.domain.repositories.CatRepository
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.ResponseBody

class CatRepositoryImpl : CatRepository {

    override fun getCatObject(
        page: Int,
        order: String,
        breed_ids: String,
        category: String
    ): Single<List<CatInfo>> {
        val apiService = App.component.provideApi()
        return apiService.getCat(page = page, order = order, breed = breed_ids, category = category)
    }

    override fun getLoadsCatObject(page: Int): Single<List<CatInfo>> {
        val apiService = App.component.provideApi()
        return apiService.getLoadsCat(page = page)
    }

    override fun getBreedsCatObject(): Single<List<BreedCatInfo>> {
        val apiService = App.component.provideApi()
        return apiService.getBreedsCat()
    }

    override fun postLoadCat(file: MultipartBody.Part): Single<ResponseBody> {
        val apiService = App.component.provideApi()
        return apiService.postLoadCat(file = file)
    }


    override fun getFavouritesCat(page: Int): Single<List<CatInfo>> {
        val apiService = App.component.provideApi()
        return apiService.getFavouritesCat(page = page)
    }

    override fun postFavouritesCatObject(favouriteEntity: FavouriteEntity): Single<ResponseBody> {
        val apiService = App.component.provideApi()
        return apiService.postFavouritesCat(params = favouriteEntity)
    }

    override fun deleteFavouritesCatObject(id: String): Single<ResponseBody> {
        val apiService = App.component.provideApi()
        return apiService.deleteFavouritesCat(id = id)
    }

    override fun deleteLoadsCatObject(id: String): Single<ResponseBody> {
        val apiService = App.component.provideApi()
        return apiService.deleteLoadsCat(id = id)
    }

    override fun postVoteForCat(voteCat: VoteCat): Single<ResponseBody> {
        val apiService = App.component.provideApi()
        return apiService.postVoteAboutCat(params = voteCat)
    }

    override fun getAnalysisAboutLoadCatObject(id: String): Single<List<AnalysisCat>> {
        val apiService = App.component.provideApi()
        return apiService.getAnalysisAboutLoadCat(imageId = id)
    }
}