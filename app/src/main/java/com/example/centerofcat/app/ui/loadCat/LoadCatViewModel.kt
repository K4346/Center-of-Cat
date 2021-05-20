package com.example.centerofcat.app.ui.loadCat

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
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
    var k = 0
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val catRepositoryImpl: CatRepository = CatRepositoryImpl()
    val analysisCatLiveData: MutableLiveData<List<AnalysisCat>> = MutableLiveData()
    var messageLiveData: MutableLiveData<String> = MutableLiveData()
    var flagToast: Boolean = false
    var flagForClick: Boolean = false
    val bundleForDetailLiveData: MutableLiveData<Bundle> = MutableLiveData()
    val dialogLiveData: MutableLiveData<CatDialog> = MutableLiveData()
    val refreshView: MutableLiveData<Boolean> = MutableLiveData()
    var catListInitial: MutableLiveData<List<CatInfo>> = MutableLiveData()
    var catListRange: MutableLiveData<List<CatInfo>> = MutableLiveData()
    var flagInitial: Boolean = false
    var flagRange: Boolean = false
    var flagRefresh: Boolean = true

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
                changeToastFlag(true)
                messageLiveData.value = "Кот загрузился"
                refreshView.value = true
            }, {
                changeToastFlag(true)
                messageLiveData.value = "На фото нет кота"
            })
        compositeDisposable.add(disposable)
    }

    fun deleteCatInLoads(id: String) {
        val disposable = catRepositoryImpl.deleteLoadsCatObject(id).subscribeOn(
            Schedulers.io()
        )
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                refreshView.value = true
            }, {
                refreshView.value = true
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
        changeFlagForClick(true)
        changeRefreshFlag(false)
        bundleForDetailLiveData.value = analysisToDetail
    }

    fun onCatLongClick(
        catInfo: CatInfo,
        loadCatViewModel: LoadCatViewModel,
        i: Int
    ) {
        changeFlagForClick(true)
        dialogLiveData.value = CatDialog(catInfo, loadCatViewModel, i)
    }

    fun changeFlagForClick(flag: Boolean) {
        flagForClick = flag
    }

    fun changeInitialFlag(flag: Boolean) {
        flagInitial = flag
    }

    fun changeRangeFlag(flag: Boolean) {
        flagRange = flag
    }

    fun changeRefreshFlag(flag: Boolean) {
        flagRefresh = flag

    }

    fun changeToastFlag(flag: Boolean) {
        flagToast = flag

    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}