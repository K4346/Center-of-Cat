package com.example.centerofcat.ui.detailCatInfo

import android.util.Log
import com.example.centerofcat.domain.entities.CatInfo
import com.example.centerofcat.domain.entities.VoteCat
import com.example.centerofcat.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DetailViewModel : BaseViewModel() {
    override fun loadCats(
        page: Int,
        order: String,
        breed: String,
        category: String,
        onComplete: (List<CatInfo>) -> Unit
    ) {
    }

    fun makeVoteForTheCat(id: String, value: Int) {
        val voteCat = VoteCat(image_id = id, value = value)
        val disposable = catModelImpl.postVoteForCat(voteCat = voteCat).subscribeOn(
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
}