package com.rizk.tubes.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.rizk.tubes.util.Log;

/**
 * Makes a tic-tac-toe style grid.
 * Parent of the tubes.
 */
class Grid(context: Context?) : ViewGroup(context) {

    private val TAG:String = this.javaClass.simpleName;

    // at 1/3rd marker of the width
    private var leftVerticalLine: Float = 0f

    // at 2/3rd marker of the width
    private var rightVerticalLine: Float = 0f

    private val paint: Paint = Paint()

    init {
        paint.color = Color.RED
    }

    // do not allow scrolling
    override fun shouldDelayChildPressedState(): Boolean {
        return false;
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.v(TAG, "onMeasure")
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        // normally, we'd obey padding set, but I don't plan on even using that, so we can skip that calculation.
        leftVerticalLine = width / 3f;
        rightVerticalLine = leftVerticalLine * 2;
        Log.v(TAG, "leftVerticalLine $leftVerticalLine, rightVerticalLine $rightVerticalLine")
        //todo: don't forget about laying the children
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        Log.v(TAG, "onDraw")
        //todo use the batch drawLine call
        canvas.drawLine(leftVerticalLine, 0f, leftVerticalLine, height.toFloat(), paint)
        canvas.drawLine(rightVerticalLine, 0f, rightVerticalLine, height.toFloat(), paint)
    }

}