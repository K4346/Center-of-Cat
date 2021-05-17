package com.example.centerofcat.app.ui.adapters

import androidx.paging.PositionalDataSource
import com.example.centerofcat.app.ui.BaseViewModel
import com.example.centerofcat.domain.entities.CatInfo


class CatPositionDataSource(private val viewModel: BaseViewModel) :
    PositionalDataSource<CatInfo>() {
    private var p = 0

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<CatInfo>) {
        p = 0
        viewModel.loadCats(page = 0) {
            callback.onResult(it, p)
        }
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<CatInfo>) {
        p += 1
        viewModel.loadCats(page = p) {
            callback.onResult(it)
        }
    }
}