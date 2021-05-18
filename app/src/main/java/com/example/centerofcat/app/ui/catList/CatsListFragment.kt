package com.example.centerofcat.app.ui.catList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.centerofcat.R
import com.example.centerofcat.app.app
import com.example.centerofcat.app.ui.adapters.CatListAdapter
import com.example.centerofcat.databinding.FragmentCatListBinding
import com.example.centerofcat.domain.entities.CatInfo
import javax.inject.Inject


class CatsListFragment : Fragment() {
    private lateinit var catsListViewModel: CatsListViewModel
    private lateinit var binding: FragmentCatListBinding

    @Inject
    lateinit var adapter: CatListAdapter
    val app = app()
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
        adapterSettings()
    }

    private fun adapterSettings() {
        val layoutManager = GridLayoutManager(context, 2)
        binding.rvCatList.layoutManager = layoutManager
        setOnClicksListeners(adapter)
        binding.rvCatList.adapter = adapter
        addFilters()
        catsListViewModel.catPagedListInfo.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    private fun setOnClicksListeners(adapter: CatListAdapter) {
        adapter.onCatClickListener = object : CatListAdapter.OnCatClickListener {
            override fun onCatClick(catInfo: CatInfo) {
                findNavController().navigate(
                    R.id.navigation_detail,
                    catsListViewModel.onCatClick(catInfo)
                )
            }

            override fun onCatLongClick(catInfo: CatInfo) {
                activity?.supportFragmentManager.let {
                    if (it != null) {
                        catsListViewModel.setOnCatLongClick(catInfo, catsListViewModel, 1)
                            .show(it, "dialog")
                    }
                }
            }
        }
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
        binding.spinnerOfBreed.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    catsListViewModel.breedChoose = catsListViewModel.idsCats[p2]
                    if (f1 != 0) {
                        catsListViewModel.catPagedListInfo.value =
                            catsListViewModel.makeChange()
                    }
                    f1 = 1
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    catsListViewModel.breedChoose = ""
                }
            }
    }

    private fun addCategoriesAdapter() {
        var f2 = 0
        val spinnerCategoryAdapter = spinnerAdapterMake(2)
        spinnerCategoryAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerOfCategories.adapter = spinnerCategoryAdapter
        binding.spinnerOfCategories.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    catsListViewModel.categoryy = catsListViewModel.categoriesIdCats[p2]
                    if (f2 != 0) {
                        catsListViewModel.catPagedListInfo.value = catsListViewModel.makeChange()
                    }
                    f2 = 1
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    catsListViewModel.categoryy = ""
                }
            }
    }

    private fun addOrderAdapter() {
        var f3 = 0
        val spinnerOrderAdapter = spinnerAdapterMake(3)
        spinnerOrderAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerOfOrder.adapter = spinnerOrderAdapter
        binding.spinnerOfOrder.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    catsListViewModel.orderr = catsListViewModel.order[p2]
                    if (f3 != 0) {
                        catsListViewModel.catPagedListInfo.value = catsListViewModel.makeChange()
                    }
                    f3 = 1
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    catsListViewModel.orderr = ""
                }
            }
    }
}
