package com.example.centerofcat.app.ui.catFavourites

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.centerofcat.app.SingleLiveEvent
import com.example.centerofcat.app.ui.CatDialog
import com.example.centerofcat.data.repositories.CatRepositoryImpl
import com.example.centerofcat.domain.entities.CatInfo
import com.example.centerofcat.domain.repositories.CatRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class FavouritesCatsViewModel(application: Application) : AndroidViewModel(application) {
    var k = 0
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val catRepositoryImpl: CatRepository = CatRepositoryImpl()
    val refreshView: MutableLiveData<Boolean> = MutableLiveData()
    val refreshPagedList: SingleLiveEvent<Boolean> =
        SingleLiveEvent()
    var catListInitial: SingleLiveEvent<List<CatInfo>> =
        SingleLiveEvent()
    var catListRange: SingleLiveEvent<List<CatInfo>> =
        SingleLiveEvent()
    val bundleForDetailLiveData: SingleLiveEvent<Bundle> =
        SingleLiveEvent()
    val dialogLiveData: SingleLiveEvent<CatDialog> =
        SingleLiveEvent()

    fun firstOn() {
        if (k == 0) {
            refreshView.value = true
            k = 1
        }
    }

    fun loadCats(
        page: Int,
    ) {
        val disposable = catRepositoryImpl.getFavouritesCat(
            page = page
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (page == 0) {
                    catListInitial.value = it
                } else {
                    catListRange.value = it
                }
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
                refreshPagedList.value = true
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
        bundleForDetailLiveData.value = idToDetail
    }

    fun onCatLongClick(
        catInfo: CatInfo,
        catsFavouritesCatsViewModel: FavouritesCatsViewModel,
        i: Int
    ) {
        dialogLiveData.value = CatDialog(catInfo, catsFavouritesCatsViewModel, i)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}