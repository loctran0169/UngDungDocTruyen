package com.example.truyenqq

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.truyenqq.module.models.MysharedPreferences
import com.example.truyenqq.module.models.USER_ID
import com.example.truyenqq.ui.activities.ActivityUser
import com.example.truyenqq.ui.activities.changepassword.ActivityChangePassWord
import com.example.truyenqq.ui.activities.main.ViewModelHome
import com.example.truyenqq.ui.activities.newactivity.ActivityNewUpdate
import com.example.truyenqq.ui.activities.personal.ActivityPersonalInformation
import com.example.truyenqq.ui.activities.rank.ActivityRank
import com.example.truyenqq.ui.fragment.book.FragmentBook
import com.example.truyenqq.ui.fragment.category.FragmentCategory
import com.example.truyenqq.ui.fragment.home.FragmentHome
import com.example.truyenqq.ui.fragment.search.FragmentSearch
import com.example.truyenqq.ui.fragment.user.FragmentUser
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT: Long = 3000
    val viewModel: ViewModelHome by lazy {
        ViewModelProviders
            .of(this)
            .get(ViewModelHome::class.java)
    }
    var flag = false
    var backTime = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewModelProviders
            .of(this)
            .get(ViewModelHome::class.java)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.frmMain, FragmentHome())
                .commit()
        }
        viewModel.isShow.observe(this@MainActivity, Observer {
            botNavigation.selectedItemId = it
        })
        val share = MysharedPreferences(this)
        viewModel.dataLogin.value = share.loadData()
        botNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                viewModel.isShow.value -> true
                R.id.navHome -> {
                    showFragment(FragmentHome())
                    viewModel.isShow.value = it.itemId
                    true
                }
                R.id.navCategory -> {
                    showFragment(FragmentCategory())
                    viewModel.isShow.value = it.itemId
                    true
                }
                R.id.navSearch -> {
                    showFragment(FragmentSearch())
                    viewModel.isShow.value = it.itemId
                    true
                }
                R.id.navBookcase -> {
                    showFragment(FragmentBook())
                    viewModel.isShow.value = it.itemId
                    true
                }
                R.id.navUser -> {
                    if (share.getShare.getString(USER_ID, null) == null) {
                        flag = true
                        val intend1 = Intent(this, ActivityUser::class.java)
                        startActivity(intend1)
                    } else {
                        showFragment(FragmentUser())
                        viewModel.isShow.value = it.itemId
                    }
                    true
                }

                else -> true
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            requestPermissions(permission, 101)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 101) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() - backTime > 2000) {
            Toast.makeText(this, "Ấn lần nữa để thoát", Toast.LENGTH_SHORT).show()
        } else {
            super.onBackPressed()
        }
        backTime = System.currentTimeMillis()
    }

    override fun onRestart() {
        super.onRestart()
        val share = MysharedPreferences(this)
        if (share.getShare.getString(USER_ID, null) == null) {
            viewModel.dataLogin.value = share.loadData()
            botNavigation.selectedItemId = viewModel.isShow.value!!
        } else {
            viewModel.dataLogin.value = share.loadData()
            if (viewModel.dataLogin.value == null) {
                showFragment(FragmentHome())
                viewModel.isShow.value = R.id.navHome
            } else if (flag) {
                showFragment(FragmentHome())
                viewModel.isShow.value = R.id.navHome
            }
        }
        flag = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("SelectedItemId", viewModel.isShow.value!!)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        botNavigation.selectedItemId = savedInstanceState.getInt("SelectedItemId")
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frmMain, fragment)
            .commit()
    }

    fun openActivityRank(view: View) {
        val intent = Intent(this, ActivityRank::class.java)
        startActivity(intent)
    }

    fun openActivityNewUpdateStory(view: View) {
        val intent = Intent(this, ActivityNewUpdate::class.java)
        val bundle = Bundle()
        bundle.putString("name", "Truyện mới cập nhật")
        bundle.putString("category", "")
        bundle.putString("col", "modified")
        intent.putExtra("kind", bundle)
        startActivity(intent)
    }

    fun openActivityMale(view: View) {
        val intent = Intent(this, ActivityNewUpdate::class.java)
        val bundle = Bundle()
        bundle.putString("name", "Truyện con trai")
        bundle.putString("category", "26,27,30,31,32,41,43,47,48,50,57,85,97")
        bundle.putString("col", "modified")
        intent.putExtra("kind", bundle)
        startActivity(intent)
    }

    fun openActivityFemale(view: View) {
        val intent = Intent(this, ActivityNewUpdate::class.java)
        val bundle = Bundle()
        bundle.putString("name", "Truyện con gái")
        bundle.putString("category", "28,29,36,37,38,39,42,46,51,52,54,75,90,93")
        bundle.putString("col", "modified")
        intent.putExtra("kind", bundle)
        startActivity(intent)
    }

    fun showNewStory(view: View) {
        val intent = Intent(this, ActivityNewUpdate::class.java)
        val bundle = Bundle()
        bundle.putString("name", "Truyện mới")
        bundle.putString("category", "")
        bundle.putString("col", "created")
        intent.putExtra("kind", bundle)
        startActivity(intent)
    }

    fun logOut(view: View) {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Đăng xuất ?")
            .setNegativeButton("Có") { _, _ ->
                val share = MysharedPreferences(this)
                share.removeAll()
                showFragment(FragmentHome())
                viewModel.isShow.value = R.id.navHome
            }
            .setPositiveButton("Không") { _, _ ->

            }
        val display = dialog.create()
        display.show()
    }

    fun changePassWord(view: View) {
        val intent = Intent(this, ActivityChangePassWord::class.java)
        startActivity(intent)
    }

    fun changeInfor(view: View) {
        val intent = Intent(this, ActivityPersonalInformation::class.java)
        startActivity(intent)
    }

    fun openFacebook(view: View) {
        val inten = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/fantruyenqq/"))
        startActivity(inten)
    }
}

