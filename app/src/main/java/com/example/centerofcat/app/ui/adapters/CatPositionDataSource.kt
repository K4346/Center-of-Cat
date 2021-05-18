package com.example.centerofcat.app.ui.adapters

import androidx.lifecycle.ViewModel
import androidx.paging.PositionalDataSource
import com.example.centerofcat.app.ui.catFavourites.FavouritesCatsViewModel
import com.example.centerofcat.app.ui.catList.CatsListViewModel
import com.example.centerofcat.app.ui.loadCat.LoadCatViewModel
import com.example.centerofcat.domain.entities.CatInfo

class CatPositionDataSource(val viewModel: ViewModel, val tap: Int) :
    PositionalDataSource<CatInfo>() {
    private var p = 0
    override fun loadInitial(
        params: LoadInitialParams,
        callback: LoadInitialCallback<CatInfo>
    ) {
        p = 0
        when (tap) {
            1 -> {
                viewModel as CatsListViewModel
                viewModel.loadCats(page = 0) {
                    callback.onResult(it, p)
                }
            }
            2 -> {
                viewModel as FavouritesCatsViewModel
                viewModel.loadCats(page = 0) {
                    callback.onResult(it, p)
                }
            }
            3 -> {
                viewModel as LoadCatViewModel
                viewModel.loadCats(page = 0) {
                    callback.onResult(it, p)
                }
            }
        }
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<CatInfo>) {
        p += 1
        when (tap) {
            1 -> {
                viewModel as CatsListViewModel
                viewModel.loadCats(page = p) {
                    callback.onResult(it)
                }
            }
            2 -> {
                viewModel as FavouritesCatsViewModel
                viewModel.loadCats(page = p) {
                    callback.onResult(it)
                }
            }
            3 -> {
                viewModel as LoadCatViewModel
                viewModel.loadCats(page = p) {
                    callback.onResult(it)
                }
            }
        }
    }
}