package com.example.centerofcat.app.ui.catFavourites

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.centerofcat.app.ui.CatDialog
import com.example.centerofcat.app.ui.adapters.CatPositionDataSource
import com.example.centerofcat.app.ui.adapters.MainThreadExecutor
import com.example.centerofcat.data.repositories.CatRepositoryImpl
import com.example.centerofcat.domain.entities.CatInfo
import com.example.centerofcat.domain.repositories.CatRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors

class FavouritesCatsViewModel(application: Application) : AndroidViewModel(application) {
    var k = 0
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val catRepositoryImpl: CatRepository = CatRepositoryImpl()
    val catPagedListInfo: MutableLiveData<PagedList<CatInfo>> = MutableLiveData()

    private fun makeChange(): PagedList<CatInfo> {
        val dataSource =
            CatPositionDataSource(this, 2)
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
//
//    inner class CatListPositionDataSource(): PositionalDataSource<CatInfo>(){
//        private var p = 0
//        override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<CatInfo>) {
//            p = 0
//            loadCats(page = 0){
//                callback.onResult(it, p)
//            }
//        }
//        override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<CatInfo>) {
//            p += 1
//            loadCats(page = p) {
//                callback.onResult(it)
//            }
//        }
//
//    }

    init {
        if (k == 0) {
            catPagedListInfo.value = makeChange()
            k = 1
        }
    }

    fun loadCats(
        page: Int,
        onComplete: ((List<CatInfo>) -> Unit)
    ) {
        val disposable = catRepositoryImpl.getFavouritesCat(
            page = page
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onComplete.invoke(it)
            }, {
            }
            )
        compositeDisposable.add(disposable)
    }

    fun deleteCatInFavourites(id: String) {
        val disposable = catRepositoryImpl.deleteFavouritesCatObject(id).subscribeOn(
            Schedulers.io()
        )
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                catPagedListInfo.value = makeChange()
            }, {
            })
        compositeDisposable.add(disposable)
    }

    fun onCatClick(catInfo: CatInfo): Bundle {
        val idToDetail = Bundle()
        val infoAboutCat = arrayListOf<String>(
            catInfo.image?.url.toString(),
            catInfo.image?.id.toString(),
            catInfo.created_at
        )
        idToDetail.putStringArrayList("infoAboutCat", infoAboutCat)
        return idToDetail
    }

    fun onCatLongClick(
        catInfo: CatInfo,
        catsFavouritesCatsViewModel: FavouritesCatsViewModel,
        i: Int
    ): CatDialog {
        return CatDialog(catInfo, catsFavouritesCatsViewModel, i)

    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}