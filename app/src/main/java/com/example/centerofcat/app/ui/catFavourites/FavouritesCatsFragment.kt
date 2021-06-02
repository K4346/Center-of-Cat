package com.example.centerofcat.app.ui.catFavourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.paging.PositionalDataSource
import androidx.recyclerview.widget.GridLayoutManager
import com.example.centerofcat.R
import com.example.centerofcat.app.ui.MainCatFragment
import com.example.centerofcat.app.ui.adapters.CatListAdapter
import com.example.centerofcat.databinding.FragmentFavoritesBinding
import com.example.centerofcat.domain.entities.CatInfo

class FavouritesCatsFragment : MainCatFragment() {

    private lateinit var catsFavouritesCatsViewModel: FavouritesCatsViewModel
    private lateinit var binding: FragmentFavoritesBinding

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

    private fun setCatListsObservers() {
        catsFavouritesCatsViewModel.catListInitial.observe(viewLifecycleOwner, {
            callBackInitial.onResult(it, 0)
        })
        catsFavouritesCatsViewModel.catListRange.observe(viewLifecycleOwner, {
            callBackRange.onResult(it)
        })
    }

    private fun actionBarSetText() {
        binding.include8.actionBarTab.text = "Избранное"
    }

    private fun adapterSettings() {
        val layoutManager = GridLayoutManager(context, 2)
        setOnClicksListeners(adapter)
        refreshListForChange(adapter)
        binding.rvCatFavouritesList.layoutManager = layoutManager
        binding.rvCatFavouritesList.adapter = adapter
        catsFavouritesCatsViewModel.refreshView.observe(viewLifecycleOwner, Observer {
            if (catsFavouritesCatsViewModel.pagedCat == null) {
                catsFavouritesCatsViewModel.pagedCat = makeChange(makeDataSource())
            }
            adapter.submitList(catsFavouritesCatsViewModel.pagedCat)
        })
    }

    private fun refreshListForChange(adapter: CatListAdapter) {
        catsFavouritesCatsViewModel.refreshPagedList.observe(viewLifecycleOwner, {
            catsFavouritesCatsViewModel.pagedCat = makeChange(makeDataSource())
            adapter.submitList(catsFavouritesCatsViewModel.pagedCat)
        })
    }

    private fun setClickObservers() {
        catsFavouritesCatsViewModel.bundleForDetailLiveData.observe(viewLifecycleOwner, {
            goToDetailFragment(it)
        })

        catsFavouritesCatsViewModel.dialogLiveData.observe(viewLifecycleOwner, {
            showDialog(it)
        })
    }

    private fun goToDetailFragment(bundle: Bundle) {
        findNavController().navigate(
            R.id.navigation_detail,
            bundle
        )
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
