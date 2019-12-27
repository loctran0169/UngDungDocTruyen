package com.example.truyenqq.ui.fragment.book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.truyenqq.R
import com.example.truyenqq.module.adapters.AdapterBookTabLayout
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_book.*

@Suppress("UNREACHABLE_CODE")
class FragmentBook : Fragment() {

    val viewModel: ViewModelBook by lazy {
        ViewModelProviders
            .of(activity!!, ViewModelBookFactory(activity!!))
            .get(ViewModelBook::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_book, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, null)
        viewPagerBook.adapter = AdapterBookTabLayout(childFragmentManager, context!!)
        tabLayoutBook.tabMode = TabLayout.MODE_FIXED
        tabLayoutBook.setupWithViewPager(viewPagerBook)
    }
}