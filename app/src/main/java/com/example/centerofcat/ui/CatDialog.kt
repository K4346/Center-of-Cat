package com.example.centerofcat.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.centerofcat.domain.entities.CatInfo

class CatDialog(private val catInfo: CatInfo, private val viewModel: BaseViewModel,) :
    DialogFragment() {
    private val catNames = arrayOf("Добавить в избранное", "Погладить")

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            if (catInfo.image != null)
                catNames[0] = "Удалить из избранного("
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Что хотите с ним сделать?")
                .setItems(
                    catNames
                ) { _, which ->
                    if (which == 0)
                        if (catInfo.image == null) {
                            catInfo.id?.let { it1 -> viewModel.addCatInFavourites(it1) }
                        } else {
                            catInfo.id?.let { it1 ->
                                viewModel.deleteCatInFavourites(it1)
                            }
                        }
                    else
                        Toast.makeText(activity, "МЯУ", Toast.LENGTH_SHORT).show()

                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}