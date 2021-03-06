package com.example.centerofcat.app.ui.catList

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.example.centerofcat.app.SingleLiveEvent
import com.example.centerofcat.app.ui.CatDialog
import com.example.centerofcat.data.repositories.CatRepositoryImpl
import com.example.centerofcat.domain.entities.CatInfo
import com.example.centerofcat.domain.entities.FavouriteEntity
import com.example.centerofcat.domain.repositories.CatRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CatsListViewModel(application: Application) : AndroidViewModel(application) {
    var pagedCat: PagedList<CatInfo>? = null
    val bundleForDetailLiveData: SingleLiveEvent<Bundle> =
        SingleLiveEvent()
    val dialogLiveData: SingleLiveEvent<CatDialog> =
        SingleLiveEvent()
    val refreshView: MutableLiveData<Boolean> = MutableLiveData()
    val refreshPagedList: SingleLiveEvent<Boolean> =
        SingleLiveEvent()
    var catListInitial: SingleLiveEvent<List<CatInfo>> =
        SingleLiveEvent()
    var catListRange: SingleLiveEvent<List<CatInfo>> =
        SingleLiveEvent()

    private var k = 0
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val catRepositoryImpl: CatRepository = CatRepositoryImpl()
    var breedChoose: String = ""
    var orderr: String = ""
    var categoryy: String = ""
    val breedsCatLiveData: MutableLiveData<ArrayList<ArrayList<String>>> = MutableLiveData()
    private var nameArray = ArrayList<String>()
    private var idArray = ArrayList<String>()
    var allArray = ArrayList<ArrayList<String>>()
    val idsCats = arrayListOf<String>("")
    val breedsCats = arrayListOf<String>("")
    val order = arrayOf("", "ASC", "DESC", "RAND")

    val categoriesCats = arrayListOf<String>(
        "",
        "boxes",
        "clothes",
        "hats",
        "sinks",
        "space",
        "sunglasses",
        "ties"
    )
    val categoriesIdCats = arrayListOf<String>("", "5", "15", "1", "14", "2", "4", "7")

    fun firstOn() {
        if (k == 0) {
            refreshView.value = true
            k = 1
        }
    }

    fun loadCats(
        page: Int = 0,
        order: String = orderr,
        breed: String = breedChoose,
        category: String = categoryy,
    ) {
        val disposable = catRepositoryImpl.getCatObject(
            page = page,
            order = order,
            breed_ids = breed,
            category = category
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

    fun loadBreedsCats(
    ) {
        if (allArray == ArrayList<ArrayList<String>>()) {
            val disposable = catRepositoryImpl.getBreedsCatObject(
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe({
                    it.forEach {
                        it.name?.let { it1 -> nameArray.add(it1) }
                        it.id?.let { it1 -> idArray.add(it1) }
                    }
                    allArray.add(nameArray)
                    allArray.add(idArray)
                    breedsCatLiveData.value = allArray
                }, {
                }
                )
            compositeDisposable.add(disposable)
        }
    }

    fun refreshPagedList() {
        refreshPagedList.value = true
    }

    fun addCatInFavourites(id: String) {
        val favouriteEntity = FavouriteEntity(image_id = id)
        val disposable = catRepositoryImpl
            .postFavouritesCatObject(favouriteEntity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
            }, {
            })
        compositeDisposable.add(disposable)
    }

    fun onCatClick(catInfo: CatInfo) {
        val idToDetail = Bundle()
        val infoAboutCat = arrayListOf<String>(catInfo.url, catInfo.id, "")
        idToDetail.putStringArrayList("infoAboutCat", infoAboutCat)
        bundleForDetailLiveData.value = idToDetail
    }

    fun setOnCatLongClick(
        catInfo: CatInfo,
        catsListViewModel: CatsListViewModel,
        i: Int
    ) {
        dialogLiveData.value = CatDialog(catInfo, catsListViewModel, i)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}