package com.example.centerofcat.app.ui.loadCat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
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

class LoadCatViewModel : ViewModel() {
    var k = 0
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val catRepositoryImpl: CatRepository = CatRepositoryImpl()
    val catPagedListInfo: MutableLiveData<PagedList<CatInfo>> = MutableLiveData()
    val analysisCatLiveData: MutableLiveData<List<AnalysisCat>> = MutableLiveData()
    var liveDataForNotPost: MutableLiveData<Boolean> = MutableLiveData()

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
//    inner class CatListPositionDataSource() : PositionalDataSource<CatInfo>() {
//        private var p = 0
//        override fun loadInitial(
//            params: LoadInitialParams,
//            callback: LoadInitialCallback<CatInfo>
//        ) {
//            p = 0
//            loadCats(page = 0) {
//                callback.onResult(it, p)
//            }
//        }
//
//        override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<CatInfo>) {
//            p += 1
//            loadCats(page = p) {
//                callback.onResult(it)
//            }
//        }
//
//    }

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

                liveDataForNotPost.value = true
                catPagedListInfo.value = makeChange()
            }, {
                liveDataForNotPost.value = false
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

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}