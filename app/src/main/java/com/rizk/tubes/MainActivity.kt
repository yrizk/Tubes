package com.rizk.tubes

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import com.rizk.tubes.ui.Grid
import com.rizk.tubes.ui.Tube


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var grid = findViewById<Grid>(R.id.grid)
        grid.addView(Tube(this), ViewGroup.MarginLayoutParams(grid.measuredWidth / 3, grid.measuredHeight / 3))
    }
}
