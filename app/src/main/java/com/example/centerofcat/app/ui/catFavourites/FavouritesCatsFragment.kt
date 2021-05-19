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
import com.example.centerofcat.app.App
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
        setClickObservers()
        setOnClicksListeners(adapter)
        adapterSettings()
    }

    private fun actionBarSetText() {
        binding.include8.actionBarTab.text = "Избранное"
    }

    private fun adapterSettings() {
        val layoutManager = GridLayoutManager(context, 2)
        binding.rvCatFavouritesList.layoutManager = layoutManager
        binding.rvCatFavouritesList.adapter = adapter
        catsFavouritesCatsViewModel.catPagedListInfo.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    private fun setClickObservers() {
        catsFavouritesCatsViewModel.bundleForDetailLiveData.observe(viewLifecycleOwner, {
            if (catsFavouritesCatsViewModel.flagForClick) {
                goToDetailFragment(it)
            }
            catsFavouritesCatsViewModel.changeJumpFlag()
        })

        catsFavouritesCatsViewModel.dialogLiveData.observe(viewLifecycleOwner, {
            if (catsFavouritesCatsViewModel.flagForClick) {
                showDialog(it)
            }
            catsFavouritesCatsViewModel.changeJumpFlag()

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
