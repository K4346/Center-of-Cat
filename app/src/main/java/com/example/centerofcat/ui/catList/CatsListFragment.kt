package com.example.centerofcat.ui.catList

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
import com.example.centerofcat.databinding.FragmentCatListBinding
import com.example.centerofcat.domain.entities.CatInfo
import com.example.centerofcat.ui.CatDialog
import com.example.centerofcat.ui.adapters.CatListAdapter


class CatsListFragment : Fragment() {
    private lateinit var catsListViewModel: CatsListViewModel
    private val catDiffUtilCallback = com.example.centerofcat.ui.adapters.CatDiffUtilCallback()
    private lateinit var binding: FragmentCatListBinding

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
        val layoutManager = GridLayoutManager(context, 2)
        binding.rvCatList.layoutManager = layoutManager
        addFilters()
        val adapter = CatListAdapter(catDiffUtilCallback)
        setOnClicksListeners(adapter)
        binding.rvCatList.adapter = adapter
        catsListViewModel.catListInfo.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    private fun setOnClicksListeners(adapter: CatListAdapter) {
        adapter.onCatClickListener = object : CatListAdapter.OnCatClickListener {
            override fun onCatClick(catInfo: CatInfo) {
                val idToDetail = Bundle()
                val infoAboutCat = arrayListOf<String>(catInfo.url, catInfo.id, "")
                idToDetail.putStringArrayList("infoAboutCat", infoAboutCat)
                findNavController().navigate(R.id.navigation_detail, idToDetail)
            }

            override fun onCatLongClick(catInfo: CatInfo) {
                val dialog = CatDialog(catInfo, catsListViewModel, 1)
                activity?.supportFragmentManager.let {
                    if (it != null) {
                        dialog.show(it, "dialog")
                    }
                }
            }
        }
    }

    private fun addFilters() {
        var f1 = 0
        var f2 = 0
        var f3 = 0
        val spinnerAdapter =
            context?.let {
                ArrayAdapter<String>(
                    it,
                    android.R.layout.simple_spinner_item,
                    catsListViewModel.breedsCats
                )
            }
        catsListViewModel.loadBreedsCats {
            catsListViewModel.breedsCats.addAll(it[0])
            catsListViewModel.idsCats.addAll(it[1])
            spinnerAdapter?.notifyDataSetChanged()
        }
        spinnerAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerOfBreed.adapter = spinnerAdapter
        binding.spinnerOfBreed.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    catsListViewModel.breedChoose = catsListViewModel.idsCats[p2]
                    if (f1 != 0) {
                        catsListViewModel.catListInfo.value = catsListViewModel.makeChange()
                    }
                    f1 = 1
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    catsListViewModel.breedChoose = ""
                }
            }

        val spinnerCategoryAdapter =
            context?.let {
                ArrayAdapter<String>(
                    it,
                    android.R.layout.simple_spinner_item,
                    catsListViewModel.categoriesCats
                )
            }
        spinnerAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerOfCategories.adapter = spinnerCategoryAdapter
        binding.spinnerOfCategories.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    catsListViewModel.categoryy = catsListViewModel.categoriesIdCats[p2]
                    if (f2 != 0) {
                        catsListViewModel.catListInfo.value = catsListViewModel.makeChange()
                    }
                    f2 = 1
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    catsListViewModel.categoryy = ""
                }
            }


        val spinnerOrderAdapter =
            context?.let {
                ArrayAdapter<String>(
                    it,
                    android.R.layout.simple_spinner_item,
                    catsListViewModel.order
                )
            }
        spinnerOrderAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerOfOrder.adapter = spinnerOrderAdapter
        binding.spinnerOfOrder.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    catsListViewModel.orderr = catsListViewModel.order[p2]
                    if (f3 != 0) {
                        catsListViewModel.catListInfo.value = catsListViewModel.makeChange()
                    }
                    f3 = 1
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    catsListViewModel.orderr = ""
                }
            }
    }
}
