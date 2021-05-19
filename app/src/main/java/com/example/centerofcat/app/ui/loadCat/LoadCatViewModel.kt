package com.example.centerofcat.app.ui.loadCat

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.example.centerofcat.app.ui.CatDialog
import com.example.centerofcat.app.ui.adapters.CatPositionDataSource
import com.example.centerofcat.app.ui.adapters.MainThreadExecutor
import com.example.centerofcat.data.repositories.CatRepositoryImpl
import com.example.centerofcat.domain.entities.CatInfo
import com.example.centerofcat.domain.entities.analysis.AnalysisCat
import com.example.centerofcat.domain.repositories.CatRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import java.util.concurrent.Executors

class LoadCatViewModel(application: Application) : AndroidViewModel(application) {
    var k = 0
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val catRepositoryImpl: CatRepository = CatRepositoryImpl()
    val catPagedListInfo: MutableLiveData<PagedList<CatInfo>> = MutableLiveData()
    val analysisCatLiveData: MutableLiveData<List<AnalysisCat>> = MutableLiveData()
    var messageLiveData: MutableLiveData<String> = MutableLiveData()

    var flagForClick: Boolean = false
    val bundleForDetailLiveData: MutableLiveData<Bundle> = MutableLiveData()
    val dialogLiveData: MutableLiveData<CatDialog> = MutableLiveData()


    private fun makeChange(): PagedList<CatInfo> {
        val dataSource =
            CatPositionDataSource(this, 3)
        val config: PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(10)
            .setInitialLoadSizeHint(10)
            .build()

        val pagedList: PagedList<CatInfo> = PagedList.Builder(dataSource, config)
            .setNotifyExecutor(MainThreadExecutor())
            .setFetchExecutor(Executors.newSingleThreadExecutor())
            .build()
        return pagedList
    }

    init {
        if (k == 0) {
            catPagedListInfo.value = makeChange()
            k = 1
        }
    }

    fun loadCats(
        page: Int = 0,
        onComplete: ((List<CatInfo>) -> Unit)
    ) {
        val disposable = catRepositoryImpl.getLoadsCatObject(
            page = page
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

            .subscribe({
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
                catPagedListInfo.value = makeChange()
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

                catPagedListInfo.value = makeChange()
            }, {
                catPagedListInfo.value = makeChange()
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
        flagForClick = true
        bundleForDetailLiveData.value = analysisToDetail
    }

    fun onCatLongClick(
        catInfo: CatInfo,
        loadCatViewModel: LoadCatViewModel,
        i: Int
    ) {
        flagForClick = true
        dialogLiveData.value = CatDialog(catInfo, loadCatViewModel, i)
    }

    fun changeJumpFlag() {
        flagForClick = false
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}