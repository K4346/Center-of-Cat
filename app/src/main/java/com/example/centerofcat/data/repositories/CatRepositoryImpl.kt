package com.example.centerofcat.data.repositories

import com.example.centerofcat.app.App
import com.example.centerofcat.data.api.services.ApiService
import com.example.centerofcat.domain.entities.BreedCatInfo
import com.example.centerofcat.domain.entities.CatInfo
import com.example.centerofcat.domain.entities.FavouriteEntity
import com.example.centerofcat.domain.entities.VoteCat
import com.example.centerofcat.domain.entities.analysis.AnalysisCat
import com.example.centerofcat.domain.repositories.CatRepository
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import javax.inject.Inject

class CatRepositoryImpl : CatRepository {
    @Inject
    lateinit var apiService: ApiService
 val app=App()
    override fun getCatObject(
        page: Int,
        order: String,
        breed_ids: String,
        category: String
    ): Single<List<CatInfo>> {

//        val apiService = App.component.provideApi()
        app.component.injectCatRepository(this)
        return apiService.getCat(page = page, order = order, breed = breed_ids, category = category)
    }

    override fun getLoadsCatObject(page: Int): Single<List<CatInfo>> {
        app.component.injectCatRepository(this)
        return apiService.getLoadsCat(page = page)
    }

    override fun getBreedsCatObject(): Single<List<BreedCatInfo>> {
        app.component.injectCatRepository(this)
        return apiService.getBreedsCat()
    }

    override fun postLoadCat(file: MultipartBody.Part): Single<ResponseBody> {
        app.component.injectCatRepository(this)
        return apiService.postLoadCat(file = file)
    }


    override fun getFavouritesCat(page: Int): Single<List<CatInfo>> {
        app.component.injectCatRepository(this)
        return apiService.getFavouritesCat(page = page)
    }

    override fun postFavouritesCatObject(favouriteEntity: FavouriteEntity): Single<ResponseBody> {
        app.component.injectCatRepository(this)
        return apiService.postFavouritesCat(params = favouriteEntity)
    }

    override fun deleteFavouritesCatObject(id: String): Single<ResponseBody> {
        app.component.injectCatRepository(this)
        return apiService.deleteFavouritesCat(id = id)
    }

    override fun deleteLoadsCatObject(id: String): Single<ResponseBody> {
        app.component.injectCatRepository(this)
        return apiService.deleteLoadsCat(id = id)
    }

    override fun postVoteForCat(voteCat: VoteCat): Single<ResponseBody> {
        app.component.injectCatRepository(this)
        return apiService.postVoteAboutCat(params = voteCat)
    }

    override fun getAnalysisAboutLoadCatObject(id: String): Single<List<AnalysisCat>> {
        app.component.injectCatRepository(this)
        return apiService.getAnalysisAboutLoadCat(imageId = id)
    }
}