package com.example.centerofcat.app.ui.loadCat

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.paging.PositionalDataSource
import androidx.recyclerview.widget.GridLayoutManager
import com.example.centerofcat.R
import com.example.centerofcat.app.ui.MainCatFragment
import com.example.centerofcat.app.ui.adapters.CatListAdapter
import com.example.centerofcat.databinding.FragmentLoadBinding
import com.example.centerofcat.domain.entities.CatInfo
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*


class LoadCatFragment : MainCatFragment() {
    private var uriCat: Uri? = null
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        actionBarSetText()
        binding.gallery.setOnClickListener {
            openGalleryForImage()
        }
        binding.camera.setOnClickListener {
            dispatchTakePictureIntent()
        }
        setClickObservers()
        app.component.injectAdapter(this)
        setCatListsObservers()
        loadCatViewModel.firstOn()
        adapterSettings()
        setMessageLiveData()
    }

    private fun setMessageLiveData() {
        loadCatViewModel.messageLiveData.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun actionBarSetText() {
        binding.include8.actionBarTab.text = "Загрузить Котиков"
    }

    private fun makeDataSource(): PositionalDataSource<CatInfo> {
        val dataSource = object : PositionalDataSource<CatInfo>() {
            private var p = 0
            override fun loadInitial(
                params: LoadInitialParams,
                callback: LoadInitialCallback<CatInfo>
            ) {
                p = 0
                callBackInitial = callback
                loadCatViewModel.loadCats(page = 0)

            }

            override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<CatInfo>) {
                p += 1
                callBackRange = callback
                loadCatViewModel.loadCats(page = p)
            }
        }
        return dataSource
    }

    private fun setCatListsObservers() {
        loadCatViewModel.catListInitial.observe(viewLifecycleOwner, {
            callBackInitial.onResult(it, 0)
        })
        loadCatViewModel.catListRange.observe(viewLifecycleOwner, {
            callBackRange.onResult(it)
        })
    }

    private fun goToDetailFragment(bundle: Bundle) {
        findNavController().navigate(
            R.id.navigation_detail,
            bundle
        )
    }

    private fun adapterSettings() {
        val layoutManager = GridLayoutManager(context, 2)
        setOnClicksListener(adapter)
        refreshListForChange(adapter)
        binding.rvCatLoadList.layoutManager = layoutManager
        binding.rvCatLoadList.adapter = adapter
        loadCatViewModel.refreshView.observe(viewLifecycleOwner, Observer {
            if (loadCatViewModel.pagedCat == null) {
                loadCatViewModel.pagedCat = makeChange(makeDataSource())
            }
            adapter.submitList(loadCatViewModel.pagedCat)
        })
    }

    private fun refreshListForChange(adapter: CatListAdapter) {
        loadCatViewModel.refreshPagedList.observe(viewLifecycleOwner, {
            loadCatViewModel.pagedCat = makeChange(makeDataSource())
            adapter.submitList(loadCatViewModel.pagedCat)
        })
    }

    private fun setClickObservers() {
        loadCatViewModel.bundleForDetailLiveData.observe(viewLifecycleOwner, {
            goToDetailFragment(it)
        })
        loadCatViewModel.dialogLiveData.observe(viewLifecycleOwner, {
            showDialog(it)
        })
    }

    private fun setOnClicksListener(adapter: CatListAdapter) {
        adapter.onCatClickListener = object : CatListAdapter.OnCatClickListener {
            override fun onCatClick(catInfo: CatInfo) {
                loadCatViewModel.analysisCat(catInfo.id)
                loadCatViewModel.analysisCatLiveData.observe(viewLifecycleOwner, Observer
                { it1 ->
                    loadCatViewModel.onCatClick(catInfo, it1)
                })
            }

            override fun onCatLongClick(catInfo: CatInfo) {
                loadCatViewModel.onCatLongClick(catInfo, loadCatViewModel, 3)

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
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            uriCat = data?.data
            val file = File(uriCat?.path)
            val iStream = uriCat?.let { context?.contentResolver?.openInputStream(it) }
            val inputData: ByteArray? = iStream?.let { getBytes(it) }
            val requestFile: RequestBody? =
                inputData?.toRequestBody("image/jpeg".toMediaTypeOrNull())
            val filePart =
                requestFile?.let { MultipartBody.Part.createFormData("file", file.name, it) }
            postCat(filePart)
        } else if (resultCode == Activity.RESULT_OK && requestCode == 1) {
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
                        createImageFile(),
                        it
                    )
                }
            postCat(filePart)
        }
    }

    private fun postCat(filePart: MultipartBody.Part?) {
        if (filePart != null) {
            loadCatViewModel.onActivityResult(filePart)
        }
    }

    private fun getBytes(inputStream: InputStream): ByteArray? {
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)
        var len: Int
        while (inputStream.read(buffer).also { len = it } != -1) {
            byteBuffer.write(buffer, 0, len)
        }
        return byteBuffer.toByteArray()
    }

    private fun createImageFile(): String {
        @SuppressLint("SimpleDateFormat") val timeStamp =
            SimpleDateFormat("yyyyMMdd_HHmmss").format(
                Date()
            )
        val imageFileName = "CamPhoto$timeStamp"
        val image = File.createTempFile(
            imageFileName,
            ".jpg"
        )
        return image.absolutePath
    }

}