package com.nassau.checkinschool.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.nassau.checkinschool.R

class CheckActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check)
        Handler(Looper.getMainLooper()).postDelayed({
            finish()
        }, 5000)
    }
}