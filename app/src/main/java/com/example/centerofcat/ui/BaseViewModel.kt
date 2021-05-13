package com.example.centerofcat.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.centerofcat.data.repositories.CatModelImpl
import com.example.centerofcat.domain.entities.CatInfo
import com.example.centerofcat.domain.entities.FavouriteEntity
import com.example.centerofcat.domain.repositories.CatModel
import com.example.centerofcat.ui.adapters.CatPositionDataSource
import com.example.centerofcat.ui.adapters.MainThreadExecutor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors

abstract class BaseViewModel : ViewModel() {
    var k = 0
    val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val catModelImpl: CatModel = CatModelImpl()
    val catListInfo: MutableLiveData<PagedList<CatInfo>> = MutableLiveData()
    var catList: ArrayList<CatInfo> = ArrayList()
    var breedChoose: String = ""
    var orderr: String = ""
    var categoryy: String = ""

    fun makeChange(): PagedList<CatInfo> {
        val dataSource =
            CatPositionDataSource(this)
        val config: PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(10)
            .setInitialLoadSizeHint(10)
            .build()

        val pagedList: PagedList<CatInfo> = PagedList.Builder(dataSource, config)
            .setNotifyExecutor(MainThreadExecutor())
            .setFetchExecutor(Executors.newSingleThreadExecutor())
            .build()
        return pagedList
    }

    init {
        if (k == 0) {
            catListInfo.value = makeChange()
            k = 1
        }
    }

    abstract fun loadCats(
        page: Int = 1,
        order: String = orderr,
        breed: String = breedChoose,
        category: String = categoryy,
        onComplete: ((List<CatInfo>) -> Unit)
    )

    fun addCatInFavourites(id: String) {
        val favouriteEntity = FavouriteEntity(image_id = id)
        val disposable = catModelImpl.postFavouritesCatObject(favouriteEntity).subscribeOn(
            Schedulers.io()
        )
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
            }, {
            })
        compositeDisposable.add(disposable)
    }

    fun deleteCatInFavourites(id: String) {
        Log.i("kpop", id)
        val disposable = catModelImpl.deleteFavouritesCatObject(id).subscribeOn(
            Schedulers.io()
        )
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                catListInfo.value = makeChange()
                Log.i("kpop", "proizoshel DELETE")
            }, {
                Log.i("kpop2", it.toString())
            })
        compositeDisposable.add(disposable)
    }

    fun deleteCatInLoads(id: String) {
        Log.i("kpop", "delete load $id")
        val disposable = catModelImpl.deleteLoadsCatObject(id).subscribeOn(
            Schedulers.io()
        )
            .observeOn(AndroidSchedulers.mainThread()).subscribe({

                catListInfo.value = makeChange()
            }, {
                catListInfo.value = makeChange()
            })
        compositeDisposable.add(disposable)


    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }


}