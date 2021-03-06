package com.example.truyenqq.ui.fragment.search

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.truyenqq.R
import com.example.truyenqq.module.adapters.AdapterHorizontal
import com.example.truyenqq.module.networks.ApiManager
import kotlinx.android.synthetic.main.fragment_search.*


class FragmentSearch : Fragment() {

    val adapterHorizontal: AdapterHorizontal by lazy {
        AdapterHorizontal(activity!!, mutableListOf())
    }

    private val apiManager: ApiManager by lazy { ApiManager() }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    val viewModel: ViewModelSearch by lazy {
        ViewModelProviders
            .of(activity!!)
            .get(ViewModelSearch::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rcvHistory.run {
            adapter = adapterHorizontal
            layoutManager = LinearLayoutManager(activity!!)
        }
        viewModel.data.observe(this@FragmentSearch, Observer {
            if (it != null)
                adapterHorizontal.updateData(it.list)
        })
        svBook.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            @SuppressLint("CheckResult")
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.loadQuery(query)
                return false
            }

            @SuppressLint("CheckResult")
            override fun onQueryTextChange(query: String): Boolean {
                if (query.isEmpty())
                    return false
                viewModel.loadQuery(query)
                return false
            }
        })
        rcvHistory.setOnTouchListener(object : View.OnTouchListener {
            @SuppressLint("ClickableViewAccessibility")
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                hideKeyBoard()
                return true
            }
        })
    }

    fun hideKeyBoard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
    }
}