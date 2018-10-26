package com.rizk.tubes

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.rizk.tubes.ui.Grid


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var grid = findViewById<Grid>(R.id.grid)
    }

}
