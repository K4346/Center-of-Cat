package com.example.centerofcat.app.ui.loadCat

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.example.centerofcat.domain.entities.CatInfo
import com.example.centerofcat.domain.entities.analysis.AnalysisCat
import com.example.centerofcat.app.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

class LoadCatViewModel : BaseViewModel() {

    val analysisCatLiveData: MutableLiveData<List<AnalysisCat>> = MutableLiveData()
    var liveDataForNotPost: MutableLiveData<Boolean> = MutableLiveData()
    override fun loadCats(
        page: Int,
        order: String,
        breed: String,
        category: String,
        onComplete: ((List<CatInfo>) -> Unit)
    ) {
        val disposable = catRepositoryImpl.getLoadsCatObject(
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

    fun analysisCat(
        id: String,
    ) {
        val disposable: Disposable =
            catRepositoryImpl.getAnalysisAboutLoadCatObject(id = id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                   analysisCatLiveData.value=it
                }, {
                })
        compositeDisposable.add(disposable)
    }

    fun onActivityResult(file: MultipartBody.Part) {
        val disposable = catRepositoryImpl.postLoadCat(file).subscribeOn(
            Schedulers.io()
        )
            .observeOn(AndroidSchedulers.mainThread()).subscribe({

                liveDataForNotPost.value = true
                catPagedListInfo.value = makeChange()
            }, {
                liveDataForNotPost.value = false
            })
        compositeDisposable.add(disposable)
    }
}