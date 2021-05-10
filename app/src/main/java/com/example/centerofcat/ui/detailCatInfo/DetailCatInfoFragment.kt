package com.example.centerofcat.ui.detailCatInfo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.centerofcat.databinding.FragmentDetalCatFragmentBinding
import com.example.centerofcat.ui.catList.CatsListViewModel

class DetailCatInfoFragment : Fragment() {
    private lateinit var binding: FragmentDetalCatFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetalCatFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val catsListViewModel =
            ViewModelProvider(this).get(CatsListViewModel::class.java)

       setInfoAboutCat(catsListViewModel)
    }


    private fun setInfoAboutCat(catsListViewModel:CatsListViewModel){
        val detailInformation = requireArguments().getStringArrayList("infoAboutCat")
        Log.i("kpop", detailInformation.toString())

        Glide.with(binding.root.context).load(detailInformation?.get(0)).centerCrop()
            .into(binding.detailPhoto)
        binding.mainToolbar.title="asdddddddd"
        binding.mainToolbar.title=detailInformation?.get(1)
        detailInformation?.let { binding.description.text=binding.description.text.toString()+"\n"+ it[2] }


        binding.like.setOnClickListener {
            detailInformation?.get(1)?.let { it1 -> catsListViewModel.makeVoteForTheCat(it1, 1) }
            Toast.makeText(binding.root.context, "Like", Toast.LENGTH_SHORT / 2).show()
        }
        binding.dislike.setOnClickListener {
            detailInformation?.get(1)?.let { it1 -> catsListViewModel.makeVoteForTheCat(it1, 0) }
            Toast.makeText(binding.root.context, "Dislike", Toast.LENGTH_SHORT / 2).show()
        }
    }
}
