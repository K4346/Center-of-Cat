package com.example.centerofcat.ui.adapters


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.centerofcat.databinding.ImageItemBinding
import com.example.centerofcat.domain.entities.CatInfo


class CatListAdapter(private val callback: CatDiffUtilCallback) :
    PagedListAdapter<CatInfo, CatListAdapter.CatListHolder>(callback) {

    var onCatClickListener: OnCatClickListener? = null

    inner class CatListHolder(val binding: ImageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val image = binding.imageView
        fun bind() {

            if (getItem(adapterPosition)?.image == null) {
//            Picasso.get().load(getItem(position)?.url).fit().into(holder.image)
                Glide.with(binding.root.context)
                    .load(getItem(adapterPosition)?.url)
                    .centerCrop()
                    .into(image)
            } else {
//            Picasso.get().load(getItem(position)?.image?.url).fit().into(holder.image)
                Glide.with(binding.root.context)
                    .load(getItem(adapterPosition)?.image?.url)
                    .centerCrop()
                    .into(image)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatListHolder {
        val binding = ImageItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        Log.i("kpop", "cooooooool Joker")
//        return CatListHolder(binding)
        return CatListHolder(binding)
    }

    override fun onBindViewHolder(holder: CatListHolder, position: Int) {
        holder.bind()

        holder.itemView.setOnClickListener {
            getItem(position)?.let { it1 -> onCatClickListener?.onCatClick(it1) }
        }
        holder.itemView.setOnLongClickListener() {
            getItem(position)?.let { it1 ->
                onCatClickListener?.onCatLongClick(it1)
                true
            } == true
        }
        Log.i("kpop", "cool Joker")
    }

    interface OnCatClickListener {
        fun onCatClick(catInfo: CatInfo)
        fun onCatLongClick(catInfo: CatInfo)
    }


}

//
//class CatListAdapter() : RecyclerView.Adapter<CatListAdapter.CatListHolder>() {
//
//    var catList = ArrayList<CatInfo>()
//        set(value) {
//            field=value
//            notifyDataSetChanged()
//        }
//
//
//    override fun getItemCount():Int= 0
//
//
//    inner class CatListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatListHolder {
//       return CatListHolder(LayoutInflater.from(parent.context).inflate(R.layout.image_item,parent,true))
//    }
//
//    override fun onBindViewHolder(holder: CatListHolder, position: Int) {
//
//    }
//
//}
//

