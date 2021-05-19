package com.example.centerofcat.app.ui.catFavourites

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
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

    var flagForClick: Boolean = false
    val bundleForDetailLiveData: MutableLiveData<Bundle> = MutableLiveData()
    val dialogLiveData: MutableLiveData<CatDialog> = MutableLiveData()

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

    fun onCatClick(catInfo: CatInfo) {
        val idToDetail = Bundle()
        val infoAboutCat = arrayListOf<String>(
            catInfo.image?.url.toString(),
            catInfo.image?.id.toString(),
            catInfo.created_at
        )
        idToDetail.putStringArrayList("infoAboutCat", infoAboutCat)
        flagForClick = true
        bundleForDetailLiveData.value = idToDetail

    }

    fun onCatLongClick(
        catInfo: CatInfo,
        catsFavouritesCatsViewModel: FavouritesCatsViewModel,
        i: Int
    ) {
        flagForClick = true
        dialogLiveData.value = CatDialog(catInfo, catsFavouritesCatsViewModel, i)
    }

    fun changeJumpFlag() {
        flagForClick = false
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}