package com.example.truyenqq.module.adapters

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.example.truyenqq.R
import com.example.truyenqq.module.local.ImageChapRepository
import com.example.truyenqq.module.local.ImageStorageManager
import com.example.truyenqq.module.local.StoryChap
import com.example.truyenqq.ui.activities.activitychapdownloaded.ActivityChapDownload
import com.github.siyamed.shapeimageview.RoundedImageView
import java.io.IOException


class AdapterHorizontalDownload(
    val context: Context,
    val application: Application,
    var list: List<StoryChap>
) : RecyclerView.Adapter<AdapterHorizontalDownload.ViewHolder>() {
    val repo: ImageChapRepository by lazy {
        ImageChapRepository(application)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_story_horizontal, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val p0 = list[position]
        holder.name.text = p0.name
        repo.countStory(p0.id)?.observeForever {
            holder.chap.text="Số chương: "+it.size
        }
        try {
            holder.image.setImageBitmap(ImageStorageManager.getImageFromInternalStorage(context, p0.id))
        }catch (ex : IOException){

        }
        holder.itemView.setOnClickListener {
            val intent1 = Intent(context, ActivityChapDownload::class.java)
            val bundle = Bundle()
            bundle.putString("book_id",p0.id )
            bundle.putString("name", p0.name)
            intent1.putExtra("manga", bundle)
            context.startActivity(intent1)
        }
        holder.time.isInvisible=true
        holder.views.isInvisible=true
        holder.like.isInvisible=true
        holder.sub.isInvisible=true
    }

    fun updateData(items: List<StoryChap>) {
        list = items
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.findViewById<TextView>(R.id.tvStoryNameHorizontal)
        val chap = view.findViewById<TextView>(R.id.tvChapHorizontal)
        val image = view.findViewById<RoundedImageView>(R.id.imgStoryItemHorizontal)
        val time = view.findViewById<TextView>(R.id.tvStoryTimeHorizontal)
        val views = view.findViewById<TextView>(R.id.tvViewHorizontal)
        val like = view.findViewById<TextView>(R.id.tvLikeHorizontal)
        val sub = view.findViewById<TextView>(R.id.tvSubscribeHorizontal)
    }
}