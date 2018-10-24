package com.rizk.tubes

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import com.rizk.tubes.ui.Grid
import com.rizk.tubes.ui.Tube

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val grid = Grid(this)
        grid.addView(Tube(this), ViewGroup.MarginLayoutParams(100, 100))
        setContentView(grid)
    }
}
