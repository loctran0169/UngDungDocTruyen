package com.example.truyenqq.ui.activities.personal

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.truyenqq.R
import com.example.truyenqq.module.models.AVATAR
import com.example.truyenqq.module.models.DataLogin
import com.example.truyenqq.module.models.MysharedPreferences
import com.example.truyenqq.module.models.USER_ID
import kotlinx.android.synthetic.main.activity_infor.*
import java.util.*


class ActivityPersonalInformation : AppCompatActivity() {
    val viewModel: ViewModelInformation by lazy {
        ViewModelProviders
            .of(this, ViewModelInformationFactory(this))
            .get(ViewModelInformation::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_infor)
        tvPreviousInfo.setOnClickListener {
            onBackPressed()
        }
        val share = MysharedPreferences(this)
        bindData(share.loadData()!!)
        viewModel.data.observe(this@ActivityPersonalInformation, androidx.lifecycle.Observer {
            if (it != null) {
                if (it.success != null) {
                    share.saveData(it.data!!)
                    Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show()
                    bindData(it.data!!)
                } else {
                    val dialog = AlertDialog.Builder(this)
                    dialog.setMessage("Lưu thông tin thất bại \nVui lòng kiềm tra lại thông tin hoặc đường truyền.")
                    val dislay = dialog.create()
                    dislay.show()
                }
            }
        })
        edtBirthDate.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                selectDay()
            }
        }
        btn_save_info.setOnClickListener {
            when {
                edtLastName.text.toString().isEmpty() -> Toast.makeText(
                    this,
                    "Họ không được để trống",
                    Toast.LENGTH_SHORT
                ).show()
                edtName.text.toString().isEmpty() -> Toast.makeText(
                    this,
                    "Tên không được để trống",
                    Toast.LENGTH_SHORT
                ).show()
                else -> {
                    viewModel.sendChangeInformation(
                        share.getShare.getString(USER_ID, null)!!,
                        edtName.text.toString(),
                        edtLastName.text.toString(),
                        if (checkboxMale.isChecked) "1" else "0",
                        share.getShare.getString(AVATAR, null),
                        edtPhone.text.toString(),
                        edtBirthDate.text.toString().split("-")[0],
                        edtBirthDate.text.toString().split("-")[1],
                        edtBirthDate.text.toString().split("-")[2]
                    )
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.N)
    fun selectDay() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            edtBirthDate.setText("$dayOfMonth-${monthOfYear + 1}-$year")
        }, year, month, day)

        dpd.show()
    }

    @SuppressLint("SetTextI18n")
    fun bindData(data: DataLogin) {
        edtName.setText(data.first_name)
        edtLastName.setText(data.last_name)
        edtPhone.setText(data.phone)
        edtMail.setText(data.email)
        if (data.birthday_string!!.split("-")[0].toInt() > 32)
            edtBirthDate.setText(
                data.birthday_string!!.split("-")[2] + "-" + data.birthday_string!!.split("-")[1] + "-" + data.birthday_string!!.split(
                    "-"
                )[0]
            )
        else
            edtBirthDate.setText(
                data.birthday_string!!.split("-")[0] + "-" + data.birthday_string!!.split("-")[1] + "-" + data.birthday_string!!.split(
                    "-"
                )[2]
            )
        if (data.sex == "1")
            checkboxMale.isChecked = true
        else
            checkboxFemale.isChecked = true
        Glide.with(this)
            .load("http://avatar.mangaqq.com/160x160/" + data.avatar)
            .error(R.drawable.ic_noavatar)
            .into(imgAvatar)
    }

}