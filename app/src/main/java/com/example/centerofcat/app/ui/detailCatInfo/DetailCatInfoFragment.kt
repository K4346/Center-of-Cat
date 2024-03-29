package com.example.centerofcat.app.ui.detailCatInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.centerofcat.databinding.FragmentDetalCatFragmentBinding

class DetailCatInfoFragment() : Fragment() {
    private val detailViewModel: DetailViewModel by viewModels()
    private lateinit var binding: FragmentDetalCatFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetalCatFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInfoAboutCat()
        setAnalysis()

    }

    private fun setInfoAboutCat() {
        val detailInformation = requireArguments().getStringArrayList("infoAboutCat")
        Glide.with(binding.root.context).load(detailInformation?.get(0)).centerCrop()
            .into(binding.detailPhoto)
        binding.mainToolbar.title = detailInformation?.get(1)
        detailInformation?.let {
            binding.description.text = binding.description.text.toString() + "\n" + it[2]
        }
        binding.like.setOnClickListener {
            detailInformation?.get(1)?.let { it1 -> detailViewModel.onActivityResult(it1, 1) }
            Toast.makeText(binding.root.context, "Like", Toast.LENGTH_SHORT / 2).show()
        }
        binding.dislike.setOnClickListener {
            detailInformation?.get(1)?.let { it1 -> detailViewModel.onActivityResult(it1, 0) }
            Toast.makeText(binding.root.context, "Dislike", Toast.LENGTH_SHORT / 2).show()
        }
    }

    private fun setAnalysis() {
        val analysisOfCat = requireArguments().getStringArrayList("infoAnalysis")
        if (analysisOfCat != null) {
            binding.description.text =
                binding.description.text.toString() + "\n" + "Анализ Изображения:"
            analysisOfCat.forEach {
                binding.description.text =
                    binding.description.text.toString() + "\n" + it
            }
        }
    }
}
