package com.example.centerofcat.app.ui.catList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.paging.PositionalDataSource
import androidx.recyclerview.widget.GridLayoutManager
import com.example.centerofcat.R
import com.example.centerofcat.app.ui.MainCatFragment
import com.example.centerofcat.app.ui.adapters.CatListAdapter
import com.example.centerofcat.databinding.FragmentCatListBinding
import com.example.centerofcat.domain.entities.CatInfo


class CatsListFragment : MainCatFragment() {
    private lateinit var catsListViewModel: CatsListViewModel
    private lateinit var binding: FragmentCatListBinding
    private var waitCallback: Boolean = true
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCatListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        catsListViewModel =
            ViewModelProvider(this).get(CatsListViewModel::class.java)
        app.component.injectAdapter(this)
        setCatListsObservers()
        catsListViewModel.firstOn()
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
                catsListViewModel.loadCats(page = 0)
            }

            override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<CatInfo>) {
                p += 1
                callBackRange = callback
                catsListViewModel.loadCats(page = p)
            }
        }
        return dataSource
    }

    private fun setCatListsObservers() {
        catsListViewModel.catListInitial.observe(viewLifecycleOwner, {
            callBackInitial.onResult(it, 0)
            waitCallback = true
        })
        catsListViewModel.catListRange.observe(viewLifecycleOwner, {
            callBackRange.onResult(it)
        })
    }

    private fun setClickObservers() {
        catsListViewModel.bundleForDetailLiveData.observe(viewLifecycleOwner, {
            goToDetailFragment(it)
        })
        catsListViewModel.dialogLiveData.observe(viewLifecycleOwner, {
            showDialog(it)
        })
    }

    private fun adapterSettings() {
        val layoutManager = GridLayoutManager(context, 2)
        binding.rvCatList.layoutManager = layoutManager
        setOnClicksListeners(adapter)
        refreshListForChange(adapter)
        binding.rvCatList.adapter = adapter
        addFilters()
        catsListViewModel.refreshView.observe(viewLifecycleOwner, Observer {
            if (catsListViewModel.pagedCat == null) {
                catsListViewModel.pagedCat = makeChange(makeDataSource())
            }
            adapter.submitList(catsListViewModel.pagedCat)
        })
    }

    private fun setOnClicksListeners(adapter: CatListAdapter) {
        adapter.onCatClickListener = object : CatListAdapter.OnCatClickListener {
            override fun onCatClick(catInfo: CatInfo) {
                catsListViewModel.onCatClick(catInfo)
            }

            override fun onCatLongClick(catInfo: CatInfo) {
                catsListViewModel.setOnCatLongClick(catInfo, catsListViewModel, 1)
            }
        }
    }

    private fun goToDetailFragment(bundle: Bundle) {
        findNavController().navigate(
            R.id.navigation_detail,
            bundle
        )
    }

    private fun addFilters() {
        addBreedsAdapter()

        addCategoriesAdapter()

        addOrderAdapter()
    }

    private fun spinnerAdapterMake(tap: Int): ArrayAdapter<String>? {
        when (tap) {
            1 -> {
                return context?.let {
                    ArrayAdapter<String>(
                        it,
                        android.R.layout.simple_spinner_item,
                        catsListViewModel.breedsCats
                    )
                }
            }
            2 -> {
                return context?.let {
                    ArrayAdapter<String>(
                        it,
                        android.R.layout.simple_spinner_item,
                        catsListViewModel.categoriesCats
                    )
                }
            }
            else -> {
                return context?.let {
                    ArrayAdapter<String>(
                        it,
                        android.R.layout.simple_spinner_item,
                        catsListViewModel.order
                    )
                }
            }
        }
    }

    private fun refreshListForChange(adapter: CatListAdapter) {
        catsListViewModel.refreshPagedList.observe(viewLifecycleOwner, {
            catsListViewModel.pagedCat = makeChange(makeDataSource())
            adapter.submitList(catsListViewModel.pagedCat)
        })
    }

    private fun addBreedsAdapter() {
        var f1 = 0
        val spinnerAdapter = spinnerAdapterMake(1)
        catsListViewModel.loadBreedsCats()
        catsListViewModel.breedsCatLiveData.observe(viewLifecycleOwner, Observer {
            catsListViewModel.breedsCats.addAll(it[0])
            catsListViewModel.idsCats.addAll(it[1])
            spinnerAdapter?.notifyDataSetChanged()
        })
        spinnerAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerOfBreed.adapter = spinnerAdapter
        binding.spinnerOfBreed.setSelection(0, false)
        binding.spinnerOfBreed.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    catsListViewModel.breedChoose = catsListViewModel.idsCats[p2]
                    if (waitCallback) {
                        waitCallback = false
                        catsListViewModel.refreshPagedList()
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    catsListViewModel.breedChoose = ""
                }
            }
    }

    private fun addCategoriesAdapter() {
        val spinnerCategoryAdapter = spinnerAdapterMake(2)
        spinnerCategoryAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerOfCategories.adapter = spinnerCategoryAdapter
        binding.spinnerOfCategories.setSelection(0, false)
        binding.spinnerOfCategories.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    catsListViewModel.categoryy = catsListViewModel.categoriesIdCats[p2]
                    if (waitCallback) {
                        waitCallback = false
                        catsListViewModel.refreshPagedList()
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    catsListViewModel.categoryy = ""
                }
            }
    }

    private fun addOrderAdapter() {
        val spinnerOrderAdapter = spinnerAdapterMake(3)
        spinnerOrderAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerOfOrder.adapter = spinnerOrderAdapter
        binding.spinnerOfOrder.setSelection(0, false)
        binding.spinnerOfOrder.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    catsListViewModel.orderr = catsListViewModel.order[p2]
                    if (waitCallback) {
                        waitCallback = false
                        catsListViewModel.refreshPagedList()
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    catsListViewModel.orderr = ""
                }
            }
    }
}
