package com.example.centerofcat.ui.catFavourites

import android.util.Log
import com.example.centerofcat.domain.entities.CatInfo
import com.example.centerofcat.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class FavouritesCatsViewModel : BaseViewModel() {

    override fun loadCats(
        page: Int,
        order: String,
        breed: String,
        category: String,
        onComplete: ((List<CatInfo>) -> Unit)
    ) {
        val disposable = catModelImpl.getFavouritesCat(
            page = page
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



}