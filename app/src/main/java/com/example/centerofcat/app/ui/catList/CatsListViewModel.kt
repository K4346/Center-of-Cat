package com.example.centerofcat.app.ui.catList

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

class CatsListViewModel : ViewModel() {

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

    fun onCatClick(catInfo: CatInfo): Bundle {
        val idToDetail = Bundle()
        val infoAboutCat = arrayListOf<String>(catInfo.url, catInfo.id, "")
        idToDetail.putStringArrayList("infoAboutCat", infoAboutCat)
        return idToDetail
    }

    fun setOnCatLongClick(
        catInfo: CatInfo,
        catsListViewModel: CatsListViewModel,
        i: Int
    ): CatDialog {
        return CatDialog(catInfo, catsListViewModel, i)

    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}