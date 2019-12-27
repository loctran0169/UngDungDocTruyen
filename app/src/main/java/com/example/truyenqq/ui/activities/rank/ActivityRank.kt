package com.example.truyenqq.ui.activities.rank

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.truyenqq.R
import com.example.truyenqq.module.adapters.AdapterRankTabLayout
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_rank.*

class ActivityRank : AppCompatActivity() {
    val viewModel: ViewModelRank by lazy {
        ViewModelProviders
            .of(this)
            .get(ViewModelRank::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rank)
        setSupportActionBar(toolBarRank)
        val listTop = listOf("Ngày", "Tuần", "Tháng", "Yêu Thích")
        btnBackRank.setOnClickListener {
            onBackPressed()
        }
        viewPager.adapter = AdapterRankTabLayout(
            title = listTop,
            fragment = supportFragmentManager
        )
        tabLayout.tabMode = TabLayout.MODE_FIXED
        tabLayout.setupWithViewPager(viewPager)
    }
}