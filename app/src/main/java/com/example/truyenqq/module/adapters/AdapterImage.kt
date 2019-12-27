package com.example.truyenqq.module.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.example.truyenqq.R
import com.github.chrisbanes.photoview.PhotoView


class AdapterImage(val context: Context, var list: List<String>?) : RecyclerView.Adapter<AdapterImage.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_image_photoview, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (list.isNullOrEmpty()) return 0
        return list!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            Glide.with(context)
                .load(list!![position])
                .format(DecodeFormat.PREFER_ARGB_8888)
                .dontTransform()
                .error(R.drawable.ic_errorload)
                .into(holder.image)
        } catch (ex: Exception) {

        }
    }

    fun updateData(items: List<String>?) {
        list = items
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var image = view.findViewById<PhotoView>(R.id.ptimgImg)
    }
}