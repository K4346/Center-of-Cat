package com.example.centerofcat.app.ui.catFavourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.centerofcat.R
import com.example.centerofcat.app.app
import com.example.centerofcat.app.ui.CatDialog
import com.example.centerofcat.app.ui.adapters.CatListAdapter
import com.example.centerofcat.databinding.FragmentFavoritesBinding
import com.example.centerofcat.domain.entities.CatInfo
import javax.inject.Inject

class FavouritesCatsFragment : Fragment() {

    private lateinit var catsFavouritesCatsViewModel: FavouritesCatsViewModel
    private lateinit var binding: FragmentFavoritesBinding

    @Inject
    lateinit var adapter: CatListAdapter
    val app = app()

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
        binding.include8.actionBarTab.text = "Избранное"
        catsFavouritesCatsViewModel =
            ViewModelProvider(this).get(FavouritesCatsViewModel::class.java)
        app.component.injectAdapter(this)
        setOnClicksListeners(adapter)
        val layoutManager = GridLayoutManager(context, 2)
        binding.rvCatFavouritesList.layoutManager = layoutManager
        binding.rvCatFavouritesList.adapter = adapter

        catsFavouritesCatsViewModel.catPagedListInfo.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    private fun setOnClicksListeners(adapter: CatListAdapter) {
        adapter.onCatClickListener = object : CatListAdapter.OnCatClickListener {
            override fun onCatClick(catInfo: CatInfo) {
                val idToDetail = Bundle()
                val infoAboutCat = arrayListOf<String>(
                    catInfo.image?.url.toString(),
                    catInfo.image?.id.toString(),
                    catInfo.created_at
                )
                idToDetail.putStringArrayList("infoAboutCat", infoAboutCat)
                findNavController().navigate(R.id.navigation_detail, idToDetail)
            }

            override fun onCatLongClick(catInfo: CatInfo) {
                val dialog = CatDialog(catInfo, catsFavouritesCatsViewModel, 2)
                activity?.supportFragmentManager.let {
                    if (it != null) {
                        dialog.show(it, "dialog")
                    }
                }
            }
        }
    }
}
