package com.example.truyenqq.ui.activities.newactivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.truyenqq.module.models.LoadMoreObject
import com.example.truyenqq.module.models.StoryInformation
import com.example.truyenqq.module.networks.ApiManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class ViewModelNewUpdate : ViewModel() {
    private val compo by lazy { CompositeDisposable() }
    private val apiManager: ApiManager by lazy { ApiManager() }

    var offsetNew = 0
    var _New = MutableLiveData<List<StoryInformation>>().apply { value = itemsNew }
    var itemsNew = mutableListOf<StoryInformation>()
    val loadMore = MutableLiveData<LoadMoreObject>()

    fun loadSubsribe(category: String?, col: String) {
        val loadMore = itemsNew.isNotEmpty()
        compo.add(
            apiManager.getListNewUpdate(offsetNew, _col = col, _arrayCategory = category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (!loadMore) {
                        itemsNew.clear()
                        itemsNew.addAll(it.list)
                        _New.value = itemsNew
                    } else {
                        val start = itemsNew.lastIndex
                        val end = start + it.list.size
                        itemsNew.addAll(it.list)
                        this.loadMore.value = LoadMoreObject(start, end)
                    }
                    offsetNew += 20
                }, {

                })
        )
    }

}