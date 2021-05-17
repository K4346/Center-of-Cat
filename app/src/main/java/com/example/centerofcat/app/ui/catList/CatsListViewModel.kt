package com.example.centerofcat.app.ui.catList

import com.example.centerofcat.app.ui.BaseViewModel
import com.example.centerofcat.domain.entities.CatInfo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CatsListViewModel : BaseViewModel() {

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


    override fun loadCats(
        page: Int,
        order: String,
        breed: String,
        category: String,
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
                catList.addAll(it)
                onComplete.invoke(it)
            }, {
            }
            )
        compositeDisposable.add(disposable)
    }

    fun loadBreedsCats(
        onComplete: ((ArrayList<ArrayList<String>>) -> Unit)
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
                    onComplete.invoke(allArray)
                }, {
                }
                )
            compositeDisposable.add(disposable)
        } else onComplete.invoke(allArray)
    }
}