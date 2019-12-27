package com.example.truyenqq.module.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.truyenqq.R
import com.example.truyenqq.module.models.StoryInformation
import com.example.truyenqq.ui.activities.story.ActivityStory
import com.github.siyamed.shapeimageview.RoundedImageView


class AdapterVerticalRestFull(var context: Context, var items: List<StoryInformation>) :
    RecyclerView.Adapter<AdapterVerticalRestFull.BaseItem>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseItem {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_story_vertical, null)
        return BaseItem(view)
    }

    override fun getItemCount(): Int {
        if (items.isNullOrEmpty()) return 0
        return items.size
    }

    override fun onBindViewHolder(holder: BaseItem, position: Int) {
        val p0 = items[position]
        holder.itemName.text = p0.name
        try {
            Glide.with(context)
                .load("https://truyenqq.com/ebook/163x212/" + p0.image)
                .into(holder.itemImage)
        } finally {
//            Glide.with(context)
//                .load("http://i.mangaqq.com/ebook/190x247/" + p0.image + "?thang=t515")
//                .into(holder.itemImage)
        }
        holder.itemTime.text = timeStampToString((System.currentTimeMillis() / 1000).toInt() - p0.modified!!.toInt())
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ActivityStory::class.java)
            val bundle = Bundle()
            bundle.putString("book_id", p0.id)
            intent.putExtra("kind", bundle)
            context.startActivity(intent)
        }
    }

    fun updateData(list: List<StoryInformation>) {
        items = list
        notifyDataSetChanged()
    }

    inner class BaseItem(view: View) : RecyclerView.ViewHolder(view) {
        val itemImage = view.findViewById<RoundedImageView>(R.id.imgStoryItemVertical)
        val itemName = view.findViewById<TextView>(R.id.tvStoryName)
        val itemTime = view.findViewById<TextView>(R.id.tvStoryTime)
    }
}