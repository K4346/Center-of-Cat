package com.example.centerofcat.app.ui.catList

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.centerofcat.app.ui.CatDialog
import com.example.centerofcat.data.repositories.CatRepositoryImpl
import com.example.centerofcat.domain.entities.CatInfo
import com.example.centerofcat.domain.entities.FavouriteEntity
import com.example.centerofcat.domain.repositories.CatRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CatsListViewModel(application: Application) : AndroidViewModel(application) {
    val bundleForDetailLiveData: MutableLiveData<Bundle> = MutableLiveData()
    val dialogLiveData: MutableLiveData<CatDialog> = MutableLiveData()
    val refreshView: MutableLiveData<Boolean> = MutableLiveData()
    var catListInitial: MutableLiveData<List<CatInfo>> = MutableLiveData()
    var catListRange: MutableLiveData<List<CatInfo>> = MutableLiveData()
    var flagInitial: Boolean = false
    var flagRange: Boolean = false
    var flagRefresh: Boolean = true
    var flagForClick: Boolean = false

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
        changeFlagForClick(true)
        bundleForDetailLiveData.value = idToDetail
    }

    fun setOnCatLongClick(
        catInfo: CatInfo,
        catsListViewModel: CatsListViewModel,
        i: Int
    ) {
        changeFlagForClick(true)
        dialogLiveData.value = CatDialog(catInfo, catsListViewModel, i)
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