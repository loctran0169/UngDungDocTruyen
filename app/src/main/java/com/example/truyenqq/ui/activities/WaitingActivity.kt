package com.example.truyenqq.ui.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.truyenqq.R
import com.example.truyenqq.ui.activities.main.MainActivity

class WaitingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting)
        Handler().postDelayed({
            val inten = Intent(this, MainActivity::class.java)
            startActivity(inten)
            finish()
        }, 1000)
    }
}
