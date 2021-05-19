package com.example.centerofcat.app.ui.catList

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
import com.example.centerofcat.domain.entities.FavouriteEntity
import com.example.centerofcat.domain.repositories.CatRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors

class CatsListViewModel(application: Application) : AndroidViewModel(application) {
    var flagForClick: Boolean = false
    val bundleForDetailLiveData: MutableLiveData<Bundle> = MutableLiveData()
    val dialogLiveData: MutableLiveData<CatDialog> = MutableLiveData()

    private var k = 0
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val catRepositoryImpl: CatRepository = CatRepositoryImpl()
    val catPagedListInfo: MutableLiveData<PagedList<CatInfo>> = MutableLiveData()
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

    fun makeChange(): PagedList<CatInfo> {
        val dataSource =
            CatPositionDataSource(this, 1)
        val config: PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(10)
            .setInitialLoadSizeHint(10)
            .build()

        return PagedList.Builder(dataSource, config)
            .setNotifyExecutor(MainThreadExecutor())
            .setFetchExecutor(Executors.newSingleThreadExecutor())
            .build()
    }

    init {
        if (k == 0) {
            catPagedListInfo.value = makeChange()
            k = 1
        }
    }

    fun loadCats(
        page: Int = 0,
        order: String = orderr,
        breed: String = breedChoose,
        category: String = categoryy,
        onComplete: ((List<CatInfo>) -> Unit)
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
                onComplete.invoke(it)
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
        val disposable = catRepositoryImpl.postFavouritesCatObject(favouriteEntity).subscribeOn(
            Schedulers.io()
        )
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
            }, {
            })
        compositeDisposable.add(disposable)
    }

    fun onCatClick(catInfo: CatInfo) {
        val idToDetail = Bundle()
        val infoAboutCat = arrayListOf<String>(catInfo.url, catInfo.id, "")
        idToDetail.putStringArrayList("infoAboutCat", infoAboutCat)
        flagForClick = true
        bundleForDetailLiveData.value = idToDetail

    }

    fun setOnCatLongClick(
        catInfo: CatInfo,
        catsListViewModel: CatsListViewModel,
        i: Int
    ) {
        flagForClick = true
        dialogLiveData.value = CatDialog(catInfo, catsListViewModel, i)
    }

    fun changeJumpFlag() {
        flagForClick = false
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}