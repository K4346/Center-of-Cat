package com.example.centerofcat.app.ui.catFavourites

import com.example.centerofcat.domain.entities.CatInfo
import com.example.centerofcat.app.ui.BaseViewModel
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
        val disposable = catRepositoryImpl.getFavouritesCat(
            page = page
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



}