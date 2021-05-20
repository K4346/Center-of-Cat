package com.example.centerofcat.app.ui.catFavourites

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
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
    var catListInitial: MutableLiveData<List<CatInfo>> = MutableLiveData()
    var catListRange: MutableLiveData<List<CatInfo>> = MutableLiveData()
    var listOfCat: List<CatInfo> = arrayListOf()
    var flagInitial: Boolean = false
    var flagRange: Boolean = false
    var flagRefresh: Boolean = true
    var flagForClick: Boolean = false
    val bundleForDetailLiveData: MutableLiveData<Bundle> = MutableLiveData()
    val dialogLiveData: MutableLiveData<CatDialog> = MutableLiveData()

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

    fun loadCats2(
        page: Int,
    ) {
        val disposable = catRepositoryImpl.getFavouritesCat(
            page = page
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (listOfCat != it) {
                    listOfCat = it
                    catListRange.value = it
                }
            }, {
                val p = it.toString()
            }
            )
        compositeDisposable.add(disposable)
    }

    fun deleteCatInFavourites(id: String) {
        val disposable = catRepositoryImpl.deleteFavouritesCatObject(id).subscribeOn(
            Schedulers.io()
        )
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                changeRefreshFlag(true)
                refreshView.value = true
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
        changeFlagForClick(true)
        changeRefreshFlag(false)
        bundleForDetailLiveData.value = idToDetail

    }

    fun onCatLongClick(
        catInfo: CatInfo,
        catsFavouritesCatsViewModel: FavouritesCatsViewModel,
        i: Int
    ) {
        changeFlagForClick(true)
        dialogLiveData.value = CatDialog(catInfo, catsFavouritesCatsViewModel, i)
    }

    fun changeFlagForClick(flag: Boolean) {
        flagForClick = flag
    }

    fun changeInitialFlag(flag: Boolean) {
        flagInitial = flag
    }

    fun changeRangeFlag(flag: Boolean) {
        flagRange = flag
    }

    fun changeRefreshFlag(flag: Boolean) {
        flagRefresh = flag

    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}