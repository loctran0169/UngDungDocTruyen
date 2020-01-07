package com.example.truyenqq.ui.fragment.user

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.truyenqq.databinding.FragmentForgotpasswordBinding
import com.example.truyenqq.module.models.ForgotPassword
import kotlinx.android.synthetic.main.fragment_forgotpassword.*


class FragmentForgotPassword : Fragment() {
    val viewModel: ViewModelUser by lazy {
        ViewModelProviders
            .of(activity!!)
            .get(ViewModelUser::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentForgotpasswordBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this@FragmentForgotPassword
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSendForgot.setOnClickListener {
            if (editUser2.editText?.text.toString().isNullOrEmpty())
                Toast.makeText(activity, "Chưa nhập email", Toast.LENGTH_SHORT).show()
            else
                viewModel.sendForgotPassword(editUser2.editText?.text.toString()).observe(this@FragmentForgotPassword, Observer {
                    if (it.success == "1")
                    else if (!it.success.isNullOrEmpty()) {
                        val dialog = AlertDialog.Builder(context!!)
                        dialog.setMessage("Mật khẩu đã được gửi đến email của bạn.")
                            .setPositiveButton("ok") { dialogInterface: DialogInterface, i: Int ->

                            }
                        val dislay = dialog.create()
                        dislay.setTitle("Thông báo")
                        dislay.show()
                        viewModel.forgotPassword.value = ForgotPassword("")
                    } else if (it.success == null) {
                        val dialog = AlertDialog.Builder(context!!)
                        dialog.setMessage("Email không tồn tại trong hệ thống")
                        val dislay = dialog.create()
                        dislay.setTitle("Thông báo")
                        dislay.show()
                        viewModel.forgotPassword.value = ForgotPassword("")
                    }
                })
        }
        editUser2.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.setEmail(s!!)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })
        groupInfo.setOnClickListener {
            hideKeyBoard()
        }
    }
    fun hideKeyBoard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
    }
}