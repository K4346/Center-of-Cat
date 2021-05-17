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
        onComplete: (List<AnalysisCat>) -> Unit
    ) {
        val disposable: Disposable =
            catRepositoryImpl.getAnalysisAboutLoadCatObject(id = id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    onComplete.invoke(it)
                }, {
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
        val disposable = catRepositoryImpl.postLoadCat(file).subscribeOn(
            Schedulers.io()
        )
            .observeOn(AndroidSchedulers.mainThread()).subscribe({

                liveDataForNotPost.value = true
                catListInfo.value = makeChange()
            }, {
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