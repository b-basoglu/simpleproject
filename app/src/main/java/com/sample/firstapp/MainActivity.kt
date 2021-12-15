package com.sample.firstapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sample.firstapp.ui.main.MainFragment
import firstapp.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }
}