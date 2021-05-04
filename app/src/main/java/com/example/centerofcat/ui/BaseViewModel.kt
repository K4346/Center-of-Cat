package com.example.centerofcat.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList

import com.example.centerofcat.data.repositories.CatModelImpl
import com.example.centerofcat.domain.entities.CatInfo
import com.example.centerofcat.domain.entities.FavouriteEntity
import com.example.centerofcat.domain.entities.LoadCat
import com.example.centerofcat.domain.entities.VoteCat
import com.example.centerofcat.domain.repositories.CatModel
import com.example.centerofcat.ui.adapters.CatPositionDataSource
import com.example.centerofcat.ui.adapters.MainThreadExecutor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

abstract class BaseViewModel : ViewModel() {


    val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val catModelImpl: CatModel = CatModelImpl()

    val catListInfo: MutableLiveData<PagedList<CatInfo>> = MutableLiveData()
    var catList: ArrayList<CatInfo> = ArrayList()
    var breedChoose: String = ""
    var orderr: String = ""
    var categoryy: String = ""

    val viewStateLiveData: MutableLiveData<ViewState> = MutableLiveData()

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
        
        catListInfo.value = makeChange()
    }


    abstract fun loadCats(
        page: Int = 1,
        order: String = orderr,
        breed: String = breedChoose,
        category: String = categoryy,
        onComplete: ((List<CatInfo>) -> Unit)
    )


    fun postLoadCat(file: File){
        val loadCat=LoadCat(file)
        val disposable = catModelImpl.postLoadCat(loadCat).subscribeOn(
            Schedulers.io()
        )
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                Log.i("kpop", "proizoshel POST CAT")
            }, {
                Log.i("kpop", it.toString())
            })
        compositeDisposable.add(disposable)

    }

    fun addCatInFavourites(id: String) {
        val favouriteEntity = FavouriteEntity(image_id = id)
        val disposable = catModelImpl.postFavouritesCatObject(favouriteEntity).subscribeOn(
            Schedulers.io()
        )
            .observeOn(AndroidSchedulers.mainThread()).subscribe({

                Log.i("kpop", "proizoshel POST")
            }, {
                Log.i("kpop", it.toString())
            })
        compositeDisposable.add(disposable)
    }


    fun makeVoteForTheCat(id: String,value:Int) {
        val voteCat = VoteCat(image_id = id, value = value)
        val disposable = catModelImpl.postVoteForCat(voteCat = voteCat).subscribeOn(
            Schedulers.io()
        )
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                if (voteCat.value==1)
                Log.i("kpop", "proizoshel Like")
                else
                Log.i("kpop", "proizoshel DisLike")
            }, {
                Log.i("kpop", it.toString())
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
                Log.i("kpop", it.toString())
            })
        compositeDisposable.add(disposable)


    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    sealed class ViewState {
        data class ErrorState(val error: Throwable) : ViewState()
        data class Update(val yourData: List<CatInfo>) : ViewState()
        object EmptyState : ViewState()
    }



}