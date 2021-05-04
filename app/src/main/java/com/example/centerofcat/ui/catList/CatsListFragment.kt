package com.example.centerofcat.ui.catList

import android.os.Bundle
import android.util.Log
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

    //    val adapter = CatAdapterWithRecycler()
    private val catDiffUtilCallback = com.example.centerofcat.ui.adapters.CatDiffUtilCallback()
    private lateinit var binding: FragmentCatListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCatListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
//        return inflater.inflate(R.layout.fragment_cat_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        catsListViewModel =
            ViewModelProvider(this).get(CatsListViewModel::class.java)


        val layoutManager = GridLayoutManager(context, 2)
        binding.rvCatList.layoutManager = layoutManager

        addFilters()

        val adapter = CatListAdapter(catDiffUtilCallback)

        adapter.onCatClickListener = object : CatListAdapter.OnCatClickListener {
            override fun onCatClick(catInfo: CatInfo) {
                val idToDetail = Bundle()
                val infoAboutCat = arrayListOf<String>(catInfo.url, catInfo.id, catInfo.createdAt)
                Log.i("kpop", catInfo.url + catInfo.id + catInfo.createdAt)
                idToDetail.putStringArrayList("infoAboutCat", infoAboutCat)
                idToDetail.putString("i", "infoAboutasssssssCat")
//                Toast.makeText(requireContext(), catInfo.id, Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.navigation_detail, idToDetail)

            }

            override fun onCatLongClick(catInfo: CatInfo) {
                val dialog = CatDialog(catInfo, catsListViewModel)
                activity?.supportFragmentManager.let {
                    if (it != null) {
                        dialog.show(it, "dialog")
                    }
                }


            }

        }

        binding.rvCatList.adapter = adapter
        catsListViewModel.catListInfo.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it) // the magic!
        })

//        return root
    }

    private fun addFilters() {

        val idsCats = ArrayList<String>()
        idsCats.add("")
        val breedsCats = ArrayList<String>()
        breedsCats.add("")


        val spinnerAdapter =
            context?.let {
                ArrayAdapter<String>(
                    it,
                    android.R.layout.simple_spinner_item,
                    breedsCats
                )
            }

        catsListViewModel.loadBreedsCats {
            breedsCats.addAll(it[0])
            idsCats.addAll(it[1])
            spinnerAdapter?.notifyDataSetChanged()
        }

        spinnerAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerOfBreed.adapter = spinnerAdapter


        binding.spinnerOfBreed.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    catsListViewModel.breedChoose = idsCats[p2]
                    Log.i("kpop", catsListViewModel.breedChoose)
                    catsListViewModel.catListInfo.value = catsListViewModel.makeChange()
//                    catsListViewModel.loadCats(page = 0,breed = idsCats[p2],order = "",
//                        category = "", onComplete = {})

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    catsListViewModel.breedChoose = ""
//                    catsListViewModel.catListInfo.value=catsListViewModel.makeChange()
                }
            }


        val categoriesCats = arrayListOf<String>(
            "",
            "boxes",
            "clothes",
            "hats",
            "sinks",
            "space",
            "sunglasses",
            "ties"
        )
        val categoriesIdCats = arrayListOf<String>("", "5", "15", "1", "14", "2", "4", "7")


        val spinnerCategoryAdapter =
            context?.let {
                ArrayAdapter<String>(
                    it,
                    android.R.layout.simple_spinner_item,
                    categoriesCats
                )
            }

        spinnerAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerOfCategories.adapter = spinnerCategoryAdapter


        binding.spinnerOfCategories.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    catsListViewModel.categoryy = categoriesIdCats[p2]
                    Log.i("kpop", catsListViewModel.categoryy)
                    catsListViewModel.catListInfo.value = catsListViewModel.makeChange()
//                    catsListViewModel.loadCats(page = 0,breed = idsCats[p2],order = "",
//                        category = "", onComplete = {})

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    catsListViewModel.categoryy = ""
//                    catsListViewModel.catListInfo.value=catsListViewModel.makeChange()
                }
            }


        val order = arrayOf("", "ASC", "DESC", "RAND")
        val spinnerOrderAdapter =
            context?.let { ArrayAdapter<String>(it, android.R.layout.simple_spinner_item, order) }
        spinnerOrderAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerOfOrder.adapter = spinnerOrderAdapter
        binding.spinnerOfOrder.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    catsListViewModel.orderr = order[p2]
                    catsListViewModel.catListInfo.value = catsListViewModel.makeChange()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    catsListViewModel.orderr = ""
                }
            }


    }

    override fun onDestroyView() {
        super.onDestroyView()
//            binding = null
    }
}
//package com.example.centerofcat.ui.catList
//
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Toast
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.Observer
//import androidx.lifecycle.ViewModelProvider
//import androidx.paging.PagedList
//import androidx.recyclerview.widget.GridLayoutManager
//import com.ethanhua.skeleton.Skeleton
//import com.example.centerofcat.R
//import com.example.centerofcat.databinding.FragmentCatListBinding
//import com.example.centerofcat.domain.entities.CatInfo
//import com.example.centerofcat.ui.CatDialog
//import com.example.centerofcat.ui.adapters.CatListAdapter
//import com.example.centerofcat.ui.adapters.CatPositionDataSource
//import com.example.centerofcat.ui.adapters.MainThreadExecutor
//import java.util.concurrent.Executors
//
//class CatsListFragment : Fragment() {
//
//    private lateinit var catsListViewModel: CatsListViewModel
//
//    //    val adapter = CatAdapterWithRecycler()
//    private val catDiffUtilCallback = com.example.centerofcat.ui.adapters.CatDiffUtilCallback()
//    private lateinit var binding: FragmentCatListBinding
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        binding = FragmentCatListBinding.inflate(inflater, container, false)
//        val view = binding.root
//        return view
////        return inflater.inflate(R.layout.fragment_cat_list, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//
//
//        catsListViewModel =
//            ViewModelProvider(this).get(CatsListViewModel::class.java)
//
//        val dataSource: CatPositionDataSource = CatPositionDataSource(catsListViewModel)
//        val config: PagedList.Config = PagedList.Config.Builder()
//            .setEnablePlaceholders(false)
//            .setPageSize(10)
//            .setInitialLoadSizeHint(10)
//            .build();
//        val pagedList: PagedList<CatInfo> = PagedList.Builder(dataSource, config)
//            .setNotifyExecutor(MainThreadExecutor())
//            .setFetchExecutor(Executors.newSingleThreadExecutor())
//            .build();
//
//
//        val layoutManager = GridLayoutManager(context, 2)
//        binding.rvCatList.layoutManager = layoutManager
//
//
//        val adapter = CatListAdapter(catDiffUtilCallback)
//        val skeletonScreen = Skeleton.bind(binding.rvCatList)
//            .adapter(adapter)
//            .load(R.layout.image_item)
//            .show();
//        adapter.onCatClickListener = object : CatListAdapter.OnCatClickListener {
//            override fun onCatClick(catInfo: CatInfo) {
//                Toast.makeText(requireContext(), catInfo.id, Toast.LENGTH_SHORT).show()
//
//            }
//
//            override fun onCatLongClick(catInfo: CatInfo) {
//                val dialog=CatDialog(catInfo,catsListViewModel)
//                activity?.supportFragmentManager.let {
//                    if (it != null) {
//                        dialog.show(it,"dialog")
//                    }
//                }
//
//
//            }
//
//        }
//        adapter.submitList(pagedList)
//        binding.rvCatList.adapter = adapter
//        catsListViewModel.catListInfo.observe(viewLifecycleOwner, Observer {
//
////            val position: Int = adapter.catsList.size
////            adapter.catsList.addAll(it)
////            adapter.notifyItemRangeInserted(position, it.size)
//            Log.i("kpop", it.toString())
//        })
////        return root
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
////            binding = null
//    }
//}