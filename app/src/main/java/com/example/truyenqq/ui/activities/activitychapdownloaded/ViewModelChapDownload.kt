package com.example.truyenqq.ui.activities.activitychapdownloaded

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.truyenqq.module.local.ImageChapRepository

class ViewModelReadDownloadFactory(val application: Application, val context: Context, val bookId: String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ViewModelChapDownload(application, context, bookId) as T
    }
}

class ViewModelChapDownload(val application: Application, val context: Context, val bookId: String) : ViewModel() {
    val repo: ImageChapRepository by lazy {
        ImageChapRepository(application)
    }
    val select = mutableListOf<String>()
}