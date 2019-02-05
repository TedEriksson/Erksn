package io.erksn.portfolio.ui.main

import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity
import io.erksn.portfolio.R

class MainActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
