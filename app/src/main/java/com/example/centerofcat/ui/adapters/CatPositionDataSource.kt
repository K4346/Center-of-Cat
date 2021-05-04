package com.example.centerofcat.ui.adapters

import android.util.Log
import androidx.paging.PositionalDataSource
import com.example.centerofcat.domain.entities.CatInfo
import com.example.centerofcat.ui.BaseViewModel


class CatPositionDataSource(private val viewModel: BaseViewModel) :
    PositionalDataSource<CatInfo>() {
    private var p = 0
    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<CatInfo>) {
        p = 0
        viewModel.loadCats(page = 0) {
            callback.onResult(it, 0)            // Вызывается несколько раз
        }


        Log.i("kpop", "inLoadInitial" + viewModel.catList.toString())
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<CatInfo>) {
        p += 1
        viewModel.loadCats(page = p) {
            callback.onResult(it)
        }
        Log.i("kpop", "inLoadRange" + viewModel.catList.toString())


    }
}