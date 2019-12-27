package com.example.truyenqq.ui.activities.changepassword

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.truyenqq.module.models.ChangePassWord
import com.example.truyenqq.module.networks.ApiManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ViewModelChangePassWord : ViewModel() {
    private val compo by lazy { CompositeDisposable() }
    private val apiManager: ApiManager by lazy { ApiManager() }

    val changePassword = MutableLiveData<ChangePassWord>().apply { value = null }

    fun sendChangePassword(userId: String, old: String, new1: String, new2: String,avatar : String?) {
        compo.add(
            apiManager.sendChangePassWord(userId, old, new1, new2,avatar)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    changePassword.value = it
                }, {
                })
        )
    }
}