package com.example.truyenqq.ui.activities.activitychapdownloaded

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.truyenqq.R
import com.example.truyenqq.module.adapters.AdapterListChapDownload
import com.example.truyenqq.module.adapters.SpaceItem
import com.example.truyenqq.module.local.ImageChapRepository
import kotlinx.android.synthetic.main.activity_chap_downloaded.*


class ActivityChapDownload : AppCompatActivity() {
    val adapterListChapDownload: AdapterListChapDownload by lazy {
        AdapterListChapDownload(
            this,
            intent.getBundleExtra("manga")!!.getString("book_id")!!,
            mutableListOf(),
            viewModel.select
        )
    }
    private val repo: ImageChapRepository by lazy {
        ImageChapRepository(application)
    }
    val viewModel: ViewModelChapDownload by lazy {
        ViewModelProviders
            .of(
                this,
                ViewModelReadDownloadFactory(application, this, intent.getBundleExtra("manga")!!.getString("book_id")!!)
            )
            .get(ViewModelChapDownload::class.java)
    }
    var bookId = ""
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chap_downloaded)
        toolbarTextReading2.maxLines = 1
        toolbarTextReading2.text = intent.getBundleExtra("manga")!!.getString("name")!!
        refreshReading2.isRefreshing = false
        refreshReading2.isEnabled = false
        bookId = intent.getBundleExtra("manga")!!.getString("book_id")!!
        rcvImage2.run {
            layoutManager = LinearLayoutManager(this@ActivityChapDownload)
            adapter = adapterListChapDownload
            addItemDecoration(SpaceItem(1))
        }
        repo.countStory(bookId)?.observeForever {
            if (it != null)
                adapterListChapDownload.updateData(it)
        }
        btnBackReading2.setOnClickListener {
            onBackPressed()
        }
        tvDelete.setOnClickListener {
            if (viewModel.select.size == 0)
                Toast.makeText(this, "Không có tập được chọn!", Toast.LENGTH_SHORT).show()
            else {
                val dialog = AlertDialog.Builder(this)
                dialog.setTitle("Xóa các tập đã chọn ?")
                    .setNegativeButton("Có") { _, _ ->
                        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 101)
                        } else {
                            for (i in viewModel.select) {
                                repo.deleteImageWithId(viewModel.bookId, i)
                            }
                        }
                    }
                    .setPositiveButton("Không") { _, _ ->

                    }
                val display = dialog.create()
                display.show()
            }
        }
    }
}