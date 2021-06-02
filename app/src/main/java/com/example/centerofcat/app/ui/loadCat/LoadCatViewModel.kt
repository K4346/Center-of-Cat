package com.example.centerofcat.app.ui.loadCat

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.example.centerofcat.app.SingleLiveEvent
import com.example.centerofcat.app.ui.CatDialog
import com.example.centerofcat.data.repositories.CatRepositoryImpl
import com.example.centerofcat.domain.entities.CatInfo
import com.example.centerofcat.domain.entities.analysis.AnalysisCat
import com.example.centerofcat.domain.repositories.CatRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody

class LoadCatViewModel(application: Application) : AndroidViewModel(application) {
    var pagedCat: PagedList<CatInfo>? = null
    var k = 0
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val catRepositoryImpl: CatRepository = CatRepositoryImpl()
    val analysisCatLiveData: SingleLiveEvent<List<AnalysisCat>> =
        SingleLiveEvent()
    var messageLiveData: SingleLiveEvent<String> =
        SingleLiveEvent()
    val bundleForDetailLiveData: SingleLiveEvent<Bundle> =
        SingleLiveEvent()
    val dialogLiveData: SingleLiveEvent<CatDialog> =
        SingleLiveEvent()
    val refreshView: MutableLiveData<Boolean> = MutableLiveData()
    val refreshPagedList: SingleLiveEvent<Boolean> =
        SingleLiveEvent()
    var catListInitial: SingleLiveEvent<List<CatInfo>> =
        SingleLiveEvent()
    var catListRange: SingleLiveEvent<List<CatInfo>> =
        SingleLiveEvent()

    fun firstOn() {
        if (k == 0) {
            refreshView.value = true
            k = 1
        }
    }

    fun loadCats(
        page: Int = 0,
    ) {
        val disposable = catRepositoryImpl.getLoadsCatObject(
            page = page
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

            .subscribe({
                if (page == 0) {
                    catListInitial.value = it
                } else {
                    catListRange.value = it
                }
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
                    analysisCatLiveData.value = it
                }, {
                })
        compositeDisposable.add(disposable)
    }

    fun onActivityResult(file: MultipartBody.Part) {
        val disposable = catRepositoryImpl.postLoadCat(file).subscribeOn(
            Schedulers.io()
        )
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                messageLiveData.value = "Кот загрузился"
                refreshPagedList.value = true
            }, {
                messageLiveData.value = "На фото нет кота"
            })
        compositeDisposable.add(disposable)
    }


    fun deleteCatInLoads(id: String) {
        val disposable = catRepositoryImpl.deleteLoadsCatObject(id).subscribeOn(
            Schedulers.io()
        )
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                refreshPagedList.value = true
            }, {
                refreshPagedList.value = true
            })
        compositeDisposable.add(disposable)
    }

    fun onCatClick(catInfo: CatInfo, listAnalysisCat: List<AnalysisCat>) {
        val analysisToDetail = Bundle()
        val infoAboutAnalysis = arrayListOf<String>()
        listAnalysisCat[0].labels?.forEach {
            infoAboutAnalysis.add(it.name + " - С уверенностью в " + it.confidence + "%")
        }
        analysisToDetail.putStringArrayList("infoAnalysis", infoAboutAnalysis)
        val infoAboutCat = arrayListOf<String>(
            catInfo.url,
            catInfo.id,
            ""
        )
        analysisToDetail.putStringArrayList("infoAboutCat", infoAboutCat)
        bundleForDetailLiveData.value = analysisToDetail
    }

    fun onCatLongClick(
        catInfo: CatInfo,
        loadCatViewModel: LoadCatViewModel,
        i: Int
    ) {
        dialogLiveData.value = CatDialog(catInfo, loadCatViewModel, i)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}