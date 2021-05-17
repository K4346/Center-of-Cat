package com.example.centerofcat.app.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.centerofcat.databinding.ImageItemBinding
import com.example.centerofcat.domain.entities.CatInfo

class CatViewHolder(
    private val binding: ImageItemBinding,
    private val onCatClickListener: CatListAdapter.OnCatClickListener?
) :
    RecyclerView.ViewHolder(binding.root) {
    val image = binding.imageView

    fun bind(item: CatInfo?) {
        if (item?.image == null) {
//            Picasso.get().load(getItem(position)?.url).fit().into(holder.image)
            Glide.with(binding.root.context)
                .load(item?.url)
                .centerCrop()
                .into(image)
        } else {
//            Picasso.get().load(getItem(position)?.image?.url).fit().into(holder.image)
            Glide.with(binding.root.context)
                .load(item.image.url)
                .centerCrop()
                .into(image)
        }
        setClickListener(item)

    }

    private fun setClickListener(item: CatInfo?) {

        itemView.setOnClickListener {
            item?.let { it1 -> onCatClickListener?.onCatClick(it1) }
        }
        itemView.setOnLongClickListener() {
            item?.let { it1 ->
                onCatClickListener?.onCatLongClick(it1)
                true
            } == true
        }
    }
}