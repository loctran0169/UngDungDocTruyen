package com.example.truyenqq.ui.fragment.user

import android.content.Context
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
import com.example.truyenqq.databinding.FragmentRegisterBinding
import kotlinx.android.synthetic.main.fragment_register.*


class FragmentRegister : Fragment() {
    val viewModel: ViewModelUser by lazy {
        ViewModelProviders
                .of(activity!!)
                .get(ViewModelUser::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this@FragmentRegister
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnRegisterSend.setOnClickListener {
            viewModel.sendRegister(editUserRegister.editText?.text.toString(), editPasswordRegister.editText?.text.toString())
                    .observe(this@FragmentRegister,
                            Observer {
                                if (it == null)
                                else if (it.error == null) {
                                    Toast.makeText(activity!!, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
                                    this@FragmentRegister.activity!!.onBackPressed()
                                } else if (it.error != null) {
                                    val dialog = AlertDialog.Builder(context!!)
                                    var message = ""
                                    if (it.error!!.email != null)
                                        message += "${it.error!!.email}\n"
                                    if (it.error!!.password != null)
                                        message += it.error!!.password + "\n"
                                    if (it.error!!.username != null)
                                        message += it.error!!.username
                                    dialog.setMessage(message)
                                    val dislay = dialog.create()
                                    dislay.setTitle("Thông báo")
                                    dislay.show()
                                    viewModel.register.value = null
                                }
                            })
        }
        editUserRegister.editText?.addTextChangedListener(object : TextWatcher{
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