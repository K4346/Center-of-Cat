package com.example.centerofcat.app.ui.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.example.centerofcat.databinding.ImageItemBinding
import com.example.centerofcat.domain.entities.CatInfo

class CatListAdapter(callback: CatDiffUtilCallback) :
    PagedListAdapter<CatInfo, CatViewHolder>(callback) {

    var onCatClickListener: OnCatClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder {
        val binding = ImageItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return CatViewHolder(binding, onCatClickListener)
    }

    override fun onBindViewHolder(holder: CatViewHolder, position: Int) {
        holder.bind(getItem(position))

    }

    interface OnCatClickListener {
        fun onCatClick(catInfo: CatInfo)
        fun onCatLongClick(catInfo: CatInfo)
    }
}
