package com.example.centerofcat.app.ui

import androidx.fragment.app.Fragment
import androidx.paging.PagedList
import androidx.paging.PositionalDataSource
import androidx.recyclerview.widget.GridLayoutManager
import com.example.centerofcat.app.App
import com.example.centerofcat.app.ui.adapters.CatListAdapter
import com.example.centerofcat.app.ui.adapters.MainThreadExecutor
import com.example.centerofcat.domain.entities.CatInfo
import java.util.concurrent.Executors
import javax.inject.Inject

open class MainCatFragment: Fragment() {

    lateinit var callBackInitial: PositionalDataSource.LoadInitialCallback<CatInfo>
    lateinit var callBackRange: PositionalDataSource.LoadRangeCallback<CatInfo>

    @Inject
    lateinit var adapter: CatListAdapter
    val app = App()

    fun makeChange(dataSource: PositionalDataSource<CatInfo>): PagedList<CatInfo> {

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

     fun showDialog(catDialog: CatDialog) {
        activity?.supportFragmentManager.let {
            if (it != null) {
                catDialog.show(it, "dialog")
            }
        }
    }



}