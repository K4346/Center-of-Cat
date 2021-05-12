package com.example.centerofcat.ui.loadCat

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.centerofcat.domain.entities.CatInfo
import com.example.centerofcat.domain.entities.analysis.AnalysisCat
import com.example.centerofcat.ui.BaseViewModel
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

    var liveDataForNotPost: MutableLiveData<Boolean> = MutableLiveData()
    override fun loadCats(
        page: Int,
        order: String,
        breed: String,
        category: String,
        onComplete: ((List<CatInfo>) -> Unit)
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

    fun analysisCat(
        id: String,
        onComplete: (List<AnalysisCat>) -> Unit
    ) {
        val disposable: Disposable =
            catModelImpl.getAnalysisAboutLoadCatObject(id = id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                    Log.i("kpop", "Analysis success $it")
                    onComplete.invoke(it)
                }, {
                    Log.i("kpopQQQQQQQQQQQQQQ", it.toString())
                })
        compositeDisposable.add(disposable)
    }

    fun getBytes(inputStream: InputStream): ByteArray? {
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)
        var len: Int
        while (inputStream.read(buffer).also { len = it } != -1) {
            byteBuffer.write(buffer, 0, len)
        }
        return byteBuffer.toByteArray()
    }

    fun postLoadCat(file: MultipartBody.Part) {
        val disposable = catModelImpl.postLoadCat(file).subscribeOn(
            Schedulers.io()
        )
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                Log.i("kpop", "proizoshel POST CAT")
                Log.i("kpop", it.toString())
                liveDataForNotPost.value = true
                catListInfo.value = makeChange()
            }, {
                Log.i("kpopError", it.toString())
                liveDataForNotPost.value = false
            })
        compositeDisposable.add(disposable)
    }

    fun createImageFile(): String {
        @SuppressLint("SimpleDateFormat") val timeStamp =
            SimpleDateFormat("yyyyMMdd_HHmmss").format(
                Date()
            )
        val imageFileName = "CamPhoto$timeStamp"
        val image = File.createTempFile(
            imageFileName,
            ".jpg"
        )
        return image.absolutePath
    }
}