package com.example.centerofcat.ui.loadCat

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.centerofcat.R
import com.example.centerofcat.databinding.FragmentLoadBinding
import com.example.centerofcat.domain.entities.CatInfo
import com.example.centerofcat.ui.CatDialog
import com.example.centerofcat.ui.adapters.CatListAdapter
import java.io.File


class LoadCatFragment : Fragment() {

    var uriCat: Uri? = null
    private val catDiffUtilCallback = com.example.centerofcat.ui.adapters.CatDiffUtilCallback()
    private lateinit var loadCatViewModel: LoadCatViewModel
    private lateinit var binding: FragmentLoadBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        loadCatViewModel =
            ViewModelProvider(this).get(LoadCatViewModel::class.java)

        binding = FragmentLoadBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.button.setOnClickListener {
            openGalleryForImage()
        }

        val adapter = CatListAdapter(catDiffUtilCallback)


        val layoutManager = GridLayoutManager(context, 2)
        binding.rvCatLoadList.layoutManager = layoutManager

//        adapter.submitList(pagedList)
        binding.rvCatLoadList.adapter = adapter


        loadCatViewModel.catListInfo.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })


    }

    private fun setOnClicksListener(adapter: CatListAdapter) {
        adapter.onCatClickListener = object : CatListAdapter.OnCatClickListener {
            override fun onCatClick(catInfo: CatInfo) {
                Log.i("kpop", catInfo.id)
                loadCatViewModel.analysisCat(catInfo.id) {

                    val analysisToDetail = Bundle()
                    val infoAboutAnalysis = arrayListOf<String>()
                    Log.i("kpop", it.toString())
                    it[0].labels?.forEach{
                        infoAboutAnalysis.add(it.name + " С уверенностью в " + it.confidence + "%")
                    }
                    analysisToDetail.putStringArrayList("infoAnalysis", infoAboutAnalysis)
                    findNavController().navigate(R.id.navigation_detail, analysisToDetail)

                }

            }

            override fun onCatLongClick(catInfo: CatInfo) {
                val dialog = CatDialog(catInfo, loadCatViewModel, 3)
                activity?.supportFragmentManager.let {
                    if (it != null) {
                        dialog.show(it, "dialog")
                    }
                }
            }

        }

    }


    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        val REQUEST_CODE = 100
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == requestCode) {
            Glide.with(binding.root.context).load(data?.data).centerCrop().into(binding.imageView3)
            binding.imageView3.setImageURI(data?.data) // handle chosen image
            uriCat = data?.data
            val file = File(uriCat?.path)
//            loadCatViewModel.postLoadCat(file)
// ремонтируется
        }


    }
}