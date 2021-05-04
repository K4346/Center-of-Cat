package com.example.centerofcat.ui.adapters


import androidx.recyclerview.widget.DiffUtil
import com.example.centerofcat.domain.entities.CatInfo


class CatDiffUtilCallback : DiffUtil.ItemCallback<CatInfo>() {


    override fun areItemsTheSame(oldItem: CatInfo, newItem: CatInfo): Boolean {

        return oldItem.id === newItem.id
    }

    override fun areContentsTheSame(oldItem: CatInfo, newItem: CatInfo): Boolean {

        return (oldItem.id == newItem.id)
                && (oldItem.url == newItem.url)
    }
}



