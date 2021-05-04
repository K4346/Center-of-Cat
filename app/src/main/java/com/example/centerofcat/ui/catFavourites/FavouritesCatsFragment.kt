package com.example.centerofcat.ui.catFavourites

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import com.ethanhua.skeleton.Skeleton
import com.example.centerofcat.R
import com.example.centerofcat.databinding.FragmentCatListBinding
import com.example.centerofcat.databinding.FragmentFavoritesBinding
import com.example.centerofcat.domain.entities.CatInfo
import com.example.centerofcat.ui.CatDialog
import com.example.centerofcat.ui.adapters.CatListAdapter
import com.example.centerofcat.ui.adapters.CatPositionDataSource
import com.example.centerofcat.ui.adapters.MainThreadExecutor
import java.util.concurrent.Executors

class FavouritesCatsFragment : Fragment() {

    private lateinit var catsFavouritesCatsViewModel: FavouritesCatsViewModel
    private val catDiffUtilCallback = com.example.centerofcat.ui.adapters.CatDiffUtilCallback()
    private lateinit var binding: FragmentFavoritesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
//        return inflater.inflate(R.layout.fragment_cat_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        catsFavouritesCatsViewModel =
            ViewModelProvider(this).get(FavouritesCatsViewModel::class.java)


        val adapter = CatListAdapter(catDiffUtilCallback)

        adapter.onCatClickListener = object : CatListAdapter.OnCatClickListener {
            override fun onCatClick(catInfo: CatInfo) {
                Toast.makeText(requireContext(), catInfo.id, Toast.LENGTH_SHORT).show()
//                catInfo.id?.let { it1 -> catsListViewModel.addCatInFavourites(it1) }
            }

            override fun onCatLongClick(catInfo: CatInfo) {
                val dialog = CatDialog(catInfo, catsFavouritesCatsViewModel)
                activity?.supportFragmentManager.let {
                    if (it != null) {
                        dialog.show(it, "dialog")
                    }

//                    binding.rvCatFavouritesList.adapter = adapter
                }
            }
        }

        val layoutManager = GridLayoutManager(context, 2)
        binding.rvCatFavouritesList.layoutManager = layoutManager

//        adapter.submitList(pagedList)
        binding.rvCatFavouritesList.adapter = adapter


        catsFavouritesCatsViewModel.catListInfo.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it) // the magic!
        })


    }


override fun onDestroyView() {
    super.onDestroyView()
//            binding = null
}


}
