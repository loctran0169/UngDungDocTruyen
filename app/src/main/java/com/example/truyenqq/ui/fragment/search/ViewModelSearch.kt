package com.example.truyenqq.ui.fragment.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.truyenqq.module.models.ListStory
import com.example.truyenqq.module.networks.ApiManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ViewModelSearch : ViewModel() {
    private val compo by lazy { CompositeDisposable() }
    private val apiManager: ApiManager by lazy { ApiManager() }

    var data = MutableLiveData<ListStory>().apply { value = null }

    fun loadQuery(query: String) {
        compo.clear()
        compo.add(
            apiManager.getListSearch(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    data.value = it
                }, {

                })
        )
    }
}