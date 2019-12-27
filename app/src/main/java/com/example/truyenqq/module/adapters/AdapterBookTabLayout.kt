package com.example.truyenqq.module.adapters


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.truyenqq.module.models.MysharedPreferences
import com.example.truyenqq.module.models.USER_ID
import com.example.truyenqq.ui.fragment.book.FragmentBookPage


class AdapterBookTabLayout(fragment: FragmentManager, val context: Context) : FragmentPagerAdapter(fragment) {
    val share = MysharedPreferences(context).getShare
    val title = listOf("Theo Dõi", "Lịch Sử", "Tải Xuống")
    override fun getItem(position: Int): Fragment {
        val fragment = FragmentBookPage()
        val bundle = Bundle()
        bundle.putInt("tag", position)
        if (share.getString(USER_ID, null).isNullOrEmpty())
            bundle.putBoolean("login", false)
        else
            bundle.putBoolean("login", true)
        fragment.arguments = bundle
        return fragment
    }

    override fun getCount(): Int {
        return title.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return title[position]
    }
}
