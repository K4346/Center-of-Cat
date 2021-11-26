package com.example.centerofcat

import android.app.Application
import com.example.centerofcat.app.ui.catList.CatsListViewModel
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test

class UnitTest {
    private val app = Application()
    private val catsListViewModel = CatsListViewModel(app)
    private val order = arrayOf("", "ASC", "DESC", "RAND")
    private val categoriesCats = arrayListOf<String>(
        "",
        "boxes",
        "clothes",
        "hats",
        "sinks",
        "space",
        "sunglasses",
        "ties"
    )
    private val categoriesIdCats = arrayListOf<String>("", "5", "15", "1", "14", "2", "4", "7")

    @Test
    fun spinnersContent_isCorrect() {
        assertArrayEquals(catsListViewModel.order, order)
        assertEquals(catsListViewModel.categoriesCats, categoriesCats)
        assertEquals(catsListViewModel.categoriesIdCats, categoriesIdCats)
    }
}