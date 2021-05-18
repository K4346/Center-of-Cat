package com.example.centerofcat.app.ui.detailCatInfo

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.centerofcat.data.repositories.CatRepositoryImpl
import com.example.centerofcat.domain.entities.VoteCat
import com.example.centerofcat.domain.repositories.CatRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val catRepositoryImpl: CatRepository = CatRepositoryImpl()

    fun onActivityResult(id: String, value: Int) {
        val voteCat = VoteCat(image_id = id, value = value)
        val disposable = catRepositoryImpl.postVoteForCat(voteCat = voteCat).subscribeOn(
            Schedulers.io()
        )
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                if (voteCat.value == 1)
                    Log.i("kpop", "proizoshel Like")
                else
                    Log.i("kpop", "proizoshel DisLike")
            }, {
                Log.i("kpop", it.toString())
            })
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }


}