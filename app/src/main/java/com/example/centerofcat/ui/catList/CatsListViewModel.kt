package com.example.centerofcat.ui.catList

import android.util.Log
import com.example.centerofcat.domain.entities.CatInfo
import com.example.centerofcat.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CatsListViewModel : BaseViewModel() {

    private var nameArray = ArrayList<String>()
    private var idArray = ArrayList<String>()
    private var allArray = ArrayList<ArrayList<String>>()

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
                onComplete.invoke(it)
            }, {
                Log.i("kpop", it.toString())
            }
            )
        compositeDisposable.add(disposable)
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
            }
            )
        compositeDisposable.add(disposable)
    }
}