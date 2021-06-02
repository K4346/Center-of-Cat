package com.example.centerofcat

import android.app.Application
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.centerofcat.app.ui.loadCat.LoadCatViewModel
import com.example.centerofcat.domain.entities.CatInfo
import com.example.centerofcat.domain.entities.analysis.AnalysisCat
import com.example.centerofcat.domain.entities.analysis.LabelInAnalysisCat
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

//@RunWith(AndroidJUnit4::class)
//class LoadCatTest {
//    private val app= Application()
//    val loadCatViewModel= LoadCatViewModel(app)
//
//
//    @Test
//    fun onCatClickFunInLoadCatViewModel() {
//        val catInfo= CatInfo(url = "123",id ="456")
//        val label= arrayListOf<LabelInAnalysisCat>(LabelInAnalysisCat(name = "MrCat",confidence = 120.toFloat()))
//        val analysisCat= arrayListOf<AnalysisCat>(AnalysisCat(labels = label))
//        val list= arrayListOf<AnalysisCat>()
//        loadCatViewModel.onCatClick(catInfo,analysisCat)
//
//        val detailInfo= loadCatViewModel.bundleForDetailLiveData.value?.getStringArrayList("infoAboutCat")
//        Assert.assertEquals(detailInfo?.get(0).toString(),"123")
////        val analysisOfCat = requireArguments().getStringArrayList("infoAnalysis")
//
//    }}