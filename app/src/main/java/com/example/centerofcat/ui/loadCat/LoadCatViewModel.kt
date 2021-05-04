package com.example.centerofcat.ui.loadCat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.centerofcat.domain.entities.CatInfo
import com.example.centerofcat.ui.BaseViewModel

class LoadCatViewModel : BaseViewModel() {


    override fun loadCats(
        page: Int,
        order: String,
        breed: String,
        category: String,
        onComplete: (List<CatInfo>) -> Unit
    ) {

    }
}