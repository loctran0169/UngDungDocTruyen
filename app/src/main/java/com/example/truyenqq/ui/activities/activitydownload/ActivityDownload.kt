package com.example.truyenqq.ui.activities.activitydownload

import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.truyenqq.R
import com.example.truyenqq.module.adapters.AdapterChapDownload
import com.example.truyenqq.module.adapters.SpaceItem
import com.example.truyenqq.module.local.ImageChapRepository
import com.example.truyenqq.module.local.ImageStorageManager
import com.example.truyenqq.module.models.Chap
import com.example.truyenqq.module.services.ServiceDownload
import kotlinx.android.synthetic.main.activity_download.*

class ActivityDownload : AppCompatActivity() {
    val viewModel: ViewModelDownload by lazy {
        ViewModelProviders
            .of(
                this,
                ViewModelDownloadFactory(application, this, intent.getBundleExtra("manga")!!.getString("book_id")!!)
            )
            .get(ViewModelDownload::class.java)
    }
    private val repo: ImageChapRepository by lazy {
        ImageChapRepository(application)
    }
    private val adapterChap: AdapterChapDownload by lazy {
        AdapterChapDownload(this, mutableListOf(), viewModel.select, viewModel.downloaded)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)

        rcvDownload.run {
            adapter = adapterChap
            layoutManager = GridLayoutManager(this@ActivityDownload, 3)
            addItemDecoration(SpaceItem(4))
        }
        viewModel.listChap.observe(this@ActivityDownload, Observer {
            if (it != null)
                adapterChap.updateData(it)
        })
        btnDownload.setOnClickListener {
            when {
                checkServiceRunning(ServiceDownload::class.java) -> Toast.makeText(
                    this,
                    "Vui lòng chờ tải xong danh sách trước",
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.select.isEmpty() -> Toast.makeText(this, "Chưa chọn Chap", Toast.LENGTH_SHORT).show()
                else -> {
                    repo.getDataStory(intent.getBundleExtra("manga")!!.getString("book_id")!!)
                        ?.observe(this@ActivityDownload, Observer {
                            if (it == null) {
                                if (viewModel.bitmap.value != null) {
                                    viewModel.insertStory()
                                    ImageStorageManager.saveToInternalStorage(
                                        this,
                                        viewModel.bitmap.value!!,
                                        viewModel.bookId
                                    )
                                }
                            }
                            viewModel.saveAndRunServiceDownload(viewModel.select)
                        })
                    Toast.makeText(this, "Đang tải vui lòng không thoát app", Toast.LENGTH_SHORT).show()
                }
            }
        }
        var all = false
        btnSelectAll.setOnClickListener {
            if (viewModel.downloaded.size == viewModel.listChap.value?.size ?: 0)
            else if (!all) {
                all = true
                btnSelectAll.text = "Hủy chọn"
                viewModel.select.clear()
                viewModel.select.addAll(
                    viewModel.listChap.value!!.map { chap: Chap -> chap.order }.toCollection(arrayListOf())
                )
            } else {
                all = false
                btnSelectAll.text = "Chọn tất cả"
                viewModel.select.clear()
            }
            adapterChap.notifyDataSetChanged()
        }
        tvBackDownload.setOnClickListener {
            onBackPressed()
        }
    }

    fun <T : Any> checkServiceRunning(service: Class<T>): Boolean {
        return (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
            .getRunningServices(Integer.MAX_VALUE)
            .any { it.service.className == service.name }
    }
}