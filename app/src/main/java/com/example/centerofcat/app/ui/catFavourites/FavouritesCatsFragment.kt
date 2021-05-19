package com.example.centerofcat.app.ui.catFavourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedList
import androidx.paging.PositionalDataSource
import androidx.recyclerview.widget.GridLayoutManager
import com.example.centerofcat.R
import com.example.centerofcat.app.App
import com.example.centerofcat.app.ui.CatDialog
import com.example.centerofcat.app.ui.adapters.CatListAdapter
import com.example.centerofcat.app.ui.adapters.MainThreadExecutor
import com.example.centerofcat.databinding.FragmentFavoritesBinding
import com.example.centerofcat.domain.entities.CatInfo
import java.util.concurrent.Executors
import javax.inject.Inject

class FavouritesCatsFragment : Fragment() {

    private lateinit var catsFavouritesCatsViewModel: FavouritesCatsViewModel
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var callBackInitial: PositionalDataSource.LoadInitialCallback<CatInfo>
    private lateinit var callBackRange: PositionalDataSource.LoadRangeCallback<CatInfo>

    @Inject
    lateinit var adapter: CatListAdapter
    val app = App()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        actionBarSetText()
        catsFavouritesCatsViewModel =
            ViewModelProvider(this).get(FavouritesCatsViewModel::class.java)
        app.component.injectAdapter(this)
        setCatListsObservers()
        catsFavouritesCatsViewModel.firstOn()
        setClickObservers()
        adapterSettings()
    }

    private fun makeDataSource(): PositionalDataSource<CatInfo> {
        val dataSource = object : PositionalDataSource<CatInfo>() {
            private var p = 0
            override fun loadInitial(
                params: LoadInitialParams,
                callback: LoadInitialCallback<CatInfo>
            ) {
                p = 0
                callBackInitial = callback
                catsFavouritesCatsViewModel.loadCats(page = 0)

            }

            override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<CatInfo>) {
                p += 1
                callBackRange = callback
                catsFavouritesCatsViewModel.loadCats(page = p)
            }
        }
        return dataSource
    }

    private fun makeChange(dataSource: PositionalDataSource<CatInfo>): PagedList<CatInfo> {
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

    private fun setCatListsObservers() {
        catsFavouritesCatsViewModel.catListInitial.observe(viewLifecycleOwner, {
            if (catsFavouritesCatsViewModel.flagInitial) {
                callBackInitial.onResult(it, 0)
            } else {
                catsFavouritesCatsViewModel.changeInitialFlag(true)
            }

        })
        catsFavouritesCatsViewModel.catListRange.observe(viewLifecycleOwner, {
            if (catsFavouritesCatsViewModel.flagRange) {
                callBackRange.onResult(it)
            } else {
                catsFavouritesCatsViewModel.changeRangeFlag(true)
            }
        })
    }

    private fun actionBarSetText() {
        binding.include8.actionBarTab.text = "Избранное"
    }

    private fun adapterSettings() {
        setOnClicksListeners(adapter)
        val layoutManager = GridLayoutManager(context, 2)
        binding.rvCatFavouritesList.layoutManager = layoutManager
        binding.rvCatFavouritesList.adapter = adapter
        catsFavouritesCatsViewModel.refreshView.observe(viewLifecycleOwner, Observer {
            if (catsFavouritesCatsViewModel.flagRefresh) {
                adapter.submitList(makeChange(makeDataSource()))
            } else {
                catsFavouritesCatsViewModel.changeRefreshFlag(true)
            }

        })
    }

    private fun setClickObservers() {
        catsFavouritesCatsViewModel.bundleForDetailLiveData.observe(viewLifecycleOwner, {
            if (catsFavouritesCatsViewModel.flagForClick) {
                goToDetailFragment(it)
            }
            catsFavouritesCatsViewModel.changeJumpFlag(false)
        })

        catsFavouritesCatsViewModel.dialogLiveData.observe(viewLifecycleOwner, {
            if (catsFavouritesCatsViewModel.flagForClick) {
                showDialog(it)
            }
            catsFavouritesCatsViewModel.changeJumpFlag(false)

        })
    }


    private fun goToDetailFragment(bundle: Bundle) {

        findNavController().navigate(
            R.id.navigation_detail,
            bundle
        )
    }

    private fun showDialog(catDialog: CatDialog) {
        activity?.supportFragmentManager.let {
            if (it != null) {
                catDialog.show(it, "dialog")
            }
        }
    }

    private fun setOnClicksListeners(adapter: CatListAdapter) {
        adapter.onCatClickListener = object : CatListAdapter.OnCatClickListener {
            override fun onCatClick(catInfo: CatInfo) {
                catsFavouritesCatsViewModel.onCatClick(catInfo)
            }

            override fun onCatLongClick(catInfo: CatInfo) {
                catsFavouritesCatsViewModel.onCatLongClick(
                    catInfo,
                    catsFavouritesCatsViewModel,
                    2
                )
            }
        }
    }
}
