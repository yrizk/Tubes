package com.rizk.tubes

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.rizk.tubes.ui.Grid

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(Grid(this))
    }
}
