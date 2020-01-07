package com.example.truyenqq.ui.fragment.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.truyenqq.databinding.FragmentCategoryBinding
import com.example.truyenqq.module.adapters.AdapterCategory
import com.example.truyenqq.module.adapters.SpaceItem
import com.example.truyenqq.module.models.Category
import com.example.truyenqq.ui.activities.main.ViewModelHome
import kotlinx.android.synthetic.main.fragment_category.*


class FragmentCategory : Fragment() {

    val viewModel: ViewModelHome by lazy {
        ViewModelProviders
            .of(activity!!)
            .get(ViewModelHome::class.java)
    }
    private val adapterCategory: AdapterCategory by lazy {
        AdapterCategory(this@FragmentCategory, mutableListOf())
    }
    var list: List<Category> = emptyList()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentCategoryBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.svCategory.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                adapterCategory.updateData(list as MutableList<Category>)
                adapterCategory.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                // filter recycler view when text is changed
                adapterCategory.updateData(list.toMutableList())
                adapterCategory.filter.filter(query)
                return false
            }
        })
        binding.lifecycleOwner = this@FragmentCategory
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rcvCategory.run {
            layoutManager = GridLayoutManager(activity!!, 2)
            adapter = adapterCategory
            addItemDecoration(SpaceItem(4))
        }
        viewModel.sLoadingCategory.observe(this@FragmentCategory, Observer {
            if (it.list.isNotEmpty()) {
                list = it.list
                adapterCategory.updateData(it.list as MutableList<Category>)
                progressBarCategory.visibility = View.INVISIBLE
            }
        })
    }
}
