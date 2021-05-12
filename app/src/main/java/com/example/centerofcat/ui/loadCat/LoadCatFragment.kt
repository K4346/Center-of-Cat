package com.example.centerofcat.ui.loadCat

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.centerofcat.R
import com.example.centerofcat.databinding.FragmentLoadBinding
import com.example.centerofcat.domain.entities.CatInfo
import com.example.centerofcat.ui.CatDialog
import com.example.centerofcat.ui.adapters.CatListAdapter
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
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
        binding.gallery.setOnClickListener {
            openGalleryForImage()
        }
        binding.camera.setOnClickListener {
            dispatchTakePictureIntent()
        }
        val adapter = CatListAdapter(catDiffUtilCallback)
        setOnClicksListener(adapter)
        val layoutManager = GridLayoutManager(context, 2)
        binding.rvCatLoadList.layoutManager = layoutManager
        binding.rvCatLoadList.adapter = adapter

        loadCatViewModel.catListInfo.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        })
        loadCatViewModel.liveDataForNotPost.observe(viewLifecycleOwner, Observer {
            if (it) {
                Toast.makeText(context, "Кот загрузился", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "На фото нет кота", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setOnClicksListener(adapter: CatListAdapter) {
        adapter.onCatClickListener = object : CatListAdapter.OnCatClickListener {
            override fun onCatClick(catInfo: CatInfo) {
                Log.i("kpop", catInfo.id)
                val analysisToDetail = Bundle()
                val infoAboutAnalysis = arrayListOf<String>()
                loadCatViewModel.analysisCat(catInfo.id) { it1 ->
                    Log.i("kpopq", it1.toString())
                    it1[0].labels?.forEach {
                        infoAboutAnalysis.add(it.name + " - С уверенностью в " + it.confidence + "%")
                    }
                    analysisToDetail.putStringArrayList("infoAnalysis", infoAboutAnalysis)
                    val infoAboutCat = arrayListOf<String>(
                        catInfo.url,
                        catInfo.id,
                        ""
                    )
                    Log.i("kpop", catInfo.url + catInfo.id + catInfo.created_at)
                    analysisToDetail.putStringArrayList("infoAboutCat", infoAboutCat)
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
        val requestCode = 100
        startActivityForResult(intent, requestCode)
    }

    private fun dispatchTakePictureIntent() {
        val requestImageCapture = 1
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, requestImageCapture)
        } catch (e: ActivityNotFoundException) {
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && resultCode == 100) {
            uriCat = data?.data
            val file = File(uriCat?.path)
            val iStream = uriCat?.let { context?.contentResolver?.openInputStream(it) }
            val inputData: ByteArray? = iStream?.let { loadCatViewModel.getBytes(it) }
            val requestFile: RequestBody? =
                inputData?.toRequestBody("image/jpeg".toMediaTypeOrNull())
            val filePart =
                requestFile?.let { MultipartBody.Part.createFormData("file", file.name, it) }
            Log.i("kpop", filePart.toString())
            if (filePart != null) {
                loadCatViewModel.postLoadCat(filePart)
            }
        } else if (resultCode == Activity.RESULT_OK) {
            Log.i("kpop", resultCode.toString())
            val extras = data?.extras
            val imageBitmap = extras!!["data"] as Bitmap?
            val stream = ByteArrayOutputStream()
            imageBitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val inputData: ByteArray? = stream.toByteArray()
            imageBitmap.recycle()
            val requestFile: RequestBody? =
                inputData?.toRequestBody("image/jpg".toMediaTypeOrNull())
            val filePart =
                requestFile?.let {
                    MultipartBody.Part.createFormData(
                        "file",
                        loadCatViewModel.createImageFile(),
                        it
                    )
                }
            if (filePart != null) {
                loadCatViewModel.postLoadCat(filePart)
            }
        }
    }
}