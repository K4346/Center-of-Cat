package com.example.centerofcat.ui.loadCat

import android.util.Log
import com.example.centerofcat.domain.entities.CatInfo
import com.example.centerofcat.domain.entities.analysis.AnalysisCat
import com.example.centerofcat.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class LoadCatViewModel : BaseViewModel() {


    override fun loadCats(
        page: Int,
        order: String,
        breed: String,
        category: String,
        onComplete: (List<CatInfo>) -> Unit
    ) {
        val disposable = catModelImpl.getLoadsCatObject(
            page = page
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

            .subscribe({
                Log.i("kpop", "tra ta ta ta$it")
                catList.addAll(it)
//                catListInfo.value = pagedList
                Log.i("kpop", "load $it")
                onComplete.invoke(it)
            }, {
                Log.i("kpop", it.toString())

            }
            )


        compositeDisposable.add(disposable)
    }


    fun analysisCat(id: String,
                    onComplete: (List<AnalysisCat>) -> Unit)
    {
        val disposable: Disposable =
            catModelImpl.getAnalysisAboutLoadCatObject(id = id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    onComplete.invoke(it)
                    Log.i("kpop", "Analysis success $it")
                }, {
                    Log.i("kpop", it.toString())
                })
    }

}