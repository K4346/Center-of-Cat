package com.example.centerofcat.ui.loadCat

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.centerofcat.databinding.FragmentLoadBinding
import java.io.File


class LoadCatFragment : Fragment() {
    var uriCat: Uri? = null

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

            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            //Тип получаемых объектов - image:
            //Тип получаемых объектов - image:
            photoPickerIntent.type = "image/*"
            //Запускаем переход с ожиданием обратного результата в виде информации об изображении:
            //Запускаем переход с ожиданием обратного результата в виде информации об изображении:
            openGalleryForImage()


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

        }


    }
}