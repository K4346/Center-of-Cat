package com.example.centerofcat.ui.catList

import android.util.Log
import com.example.centerofcat.domain.entities.BreedCatInfo
import com.example.centerofcat.domain.entities.CatInfo
import com.example.centerofcat.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CatsListViewModel : BaseViewModel() {

    private var nameArray=ArrayList<String>()
    private var idArray=ArrayList<String>()
    private var allArray=ArrayList<ArrayList<String>>()

    override fun loadCats(
        page: Int,
        order: String,
        breed: String,
        category: String,
        onComplete: ((List<CatInfo>) -> Unit)
    ) {
        val disposable = catModelImpl.getCatObject(
            page = page,
            order = order,
            breed_ids = breed,
            category = category
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

            .subscribe({
                catList.addAll(it)
//                catListInfo.value = pagedList
                onComplete.invoke(it)
            }, {
                Log.i("kpop", it.toString())

            }
            )


        compositeDisposable.add(disposable)
    }

    init {

        loadBreedsCats { allArray.addAll(it) }
    }

    fun loadBreedsCats(
        onComplete: ((ArrayList<ArrayList<String>>) -> Unit)
    ) {
        val disposable = catModelImpl.getBreedsCatObject(
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
//                catListInfo.value = pagedList
                onComplete.invoke(allArray)
            }, {
                Log.i("kpop", it.toString())

            }
            )


        compositeDisposable.add(disposable)
    }

//
//    val compositeDisposable: CompositeDisposable = CompositeDisposable()
//    val catModelImpl: CatModelImpl = CatModelImpl()
//
//    val catListInfo:MutableLiveData<PagedList<CatInfo>> =MutableLiveData()
//    var catList:ArrayList<CatInfo> = ArrayList()
//
//
//
////    val dataSource: CatPositionDataSource = CatPositionDataSource(this)
////    val config: PagedList.Config= PagedList.Config.Builder()
////        .setEnablePlaceholders(false)
////        .setPageSize(10)
////        .build();
////    val pagedList: PagedList<CatInfo> = PagedList.Builder(dataSource, config)
////        .setNotifyExecutor(MainThreadExecutor())
////        .setFetchExecutor(Executors.newSingleThreadExecutor())
////        .build();
////
////
//
//
//
//
//
//
//
//
//
//
//    fun loadCats(
//        page: Int = 1,
//        order: String = "ASC",
//        breed: String = "",
//        category: String = "",
//        onComplete: (List<CatInfo>) -> Unit
//    ) {
//        val disposable = catModelImpl.getCatObject(
//            page = page,
//            order = order,
//            breed = breed,
//            category = category
//        )
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//
//            .subscribe({
//                catList=it as ArrayList<CatInfo>
////                catListInfo.value = pagedList
//                onComplete.invoke(it)
//            }, {
//                Log.i("kpop", it.toString())
//
//            }
//            )
//
//
//        compositeDisposable.add(disposable)
//    }
//
//
//    fun addCatInFavourites(id:String){
//        val favouriteEntity=FavouriteEntity(image_id = id)
//        val disposable = catModelImpl.postFavouritesCatObject(favouriteEntity).subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread()).subscribe({
//
//                Log.i("kpop","proizoshel POST")
//            },{
//                Log.i("kpop",it.toString())
//            })
//    compositeDisposable.add(disposable)
//    }

//    override fun onCleared() {
//        super.onCleared()
//        compositeDisposable.dispose()
//    }
}