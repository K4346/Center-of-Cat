package com.example.centerofcat.app.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModel
import com.example.centerofcat.app.ui.catFavourites.FavouritesCatsViewModel
import com.example.centerofcat.app.ui.catList.CatsListViewModel
import com.example.centerofcat.app.ui.loadCat.LoadCatViewModel
import com.example.centerofcat.domain.entities.CatInfo

class CatDialog(
    private val catInfo: CatInfo,
    private val viewModel: ViewModel,
    private val tap: Int
) :
    DialogFragment() {
    private val catNames = arrayOf("Добавить в избранное", "Погладить")

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            if (tap == 2)
                catNames[0] = "Удалить из избранного("
            else if (tap == 3)
                catNames[0] = "Удалить из загруженных("
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Что хотите с ним сделать?")
                .setItems(
                    catNames
                ) { _, which ->
                    if (which == 0)
                        when (tap) {
                            1 -> {
                                catInfo.id.let { it1 ->
                                    viewModel as CatsListViewModel
                                    viewModel.addCatInFavourites(it1)
                                }
                            }
                            2 -> {
                                catInfo.id.let { it1 ->
                                    viewModel as FavouritesCatsViewModel
                                    viewModel.deleteCatInFavourites(it1)
                                }
                            }
                            3 -> {
                                viewModel as LoadCatViewModel
                                viewModel.deleteCatInLoads(catInfo.id)
                            }
                        }
                    else Toast.makeText(activity, "МЯУ", Toast.LENGTH_SHORT).show()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}