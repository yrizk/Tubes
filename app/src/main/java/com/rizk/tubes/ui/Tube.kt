package com.rizk.tubes.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.view.View
import com.rizk.tubes.util.Log


class Tube(context: Context?, w: Int, h: Int) : View(context) {
    private val TAG = "Tube"
    private val cellWidth: Int
    private val cellHeight: Int

    init {
        this.cellWidth = w
        this.cellHeight = h
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.v(TAG, "Setting width to $cellWidth and height to $cellHeight")
        setMeasuredDimension(cellWidth, cellHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        Log.v(TAG, "onDraw")
        canvas.drawColor(Color.CYAN)
    }
}