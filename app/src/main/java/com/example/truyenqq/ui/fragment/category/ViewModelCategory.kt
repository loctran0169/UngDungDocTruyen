package com.example.truyenqq.ui.fragment.category

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.truyenqq.module.models.CategoryList
import com.example.truyenqq.module.networks.ApiManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ViewModelCategory : ViewModel() {
    private val compo by lazy { CompositeDisposable() }
    private val apiManager: ApiManager by lazy { ApiManager() }
    val sLoadingCategory = MutableLiveData<CategoryList>().apply { value = CategoryList(mutableListOf()) }

    init {
        loadCategory()
    }

    fun loadCategory() {
        compo.add(
            apiManager.getListCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    sLoadingCategory.value = it
                }, {
                })
        )
    }

}