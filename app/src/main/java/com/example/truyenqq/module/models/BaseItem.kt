package com.example.truyenqq.module.models

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseItem(view : View): RecyclerView.ViewHolder(view) {
    abstract fun bind(position: Int)
}
