package com.example.truyenqq.ui.fragment.user

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.truyenqq.databinding.FragmentLoginBinding
import com.example.truyenqq.module.models.MysharedPreferences
import com.example.truyenqq.module.models.USER_ID
import kotlinx.android.synthetic.main.fragment_login.*


class FragmentLogin : Fragment() {
    val viewModel: ViewModelUser by lazy {
        ViewModelProviders
            .of(activity!!)
            .get(ViewModelUser::class.java)
    }
    private val sharedPreferences: MysharedPreferences by lazy {
        MysharedPreferences(activity!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this@FragmentLogin
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.login.observe(this@FragmentLogin, Observer {
            if (it != null) {
                if (it.error == null && sharedPreferences.getShare.getString(USER_ID, null) == null) {
                    println("### birht ${it}")
                    sharedPreferences.saveData(it)
                    Toast.makeText(activity!!, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()
                    viewModel.isLogin.value = true
                } else {
                    val dialog = AlertDialog.Builder(context!!)
                    dialog.setMessage("Tài khoản hoặc mật khẩu không chính xác")
                    val dislay = dialog.create()
                    dislay.setTitle("Thông báo")
                    dislay.show()
                }
            }
        })
        btnLogin.setOnClickListener {
            viewModel.sendLogin(editUser.editText?.text.toString(), editPassword.editText?.text.toString())
        }
        editUser.editText?.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                viewModel.setEmail(s!!)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
    }
}