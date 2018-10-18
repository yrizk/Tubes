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

    // at 1/3rd of the height
    private var topHorizontalLine: Float = 0f

    // at 2/3rd of the height
    private var bottomHorizontalLine: Float = 0f;

    private val paint: Paint = Paint()

    init {
        paint.color = Color.RED
        paint.strokeWidth = 10f
    }

    // do not allow scrolling
    override fun shouldDelayChildPressedState(): Boolean {
        return false;
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        // normally, we'd obey padding set, but I don't plan on even using that, so we can skip that calculation.
        leftVerticalLine = width / 3f
        rightVerticalLine = leftVerticalLine * 2
        topHorizontalLine = height / 3f
        bottomHorizontalLine = topHorizontalLine * 2
        Log.v(TAG, "leftVerticalLine $leftVerticalLine, rightVerticalLine $rightVerticalLine topHorizontalLine $topHorizontalLine, bottomHorizontalLine $bottomHorizontalLine")
        //todo: don't forget about laying the children
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        //todo use the batch drawLine call
        canvas.drawLine(leftVerticalLine, 0f, leftVerticalLine, height.toFloat(), paint)
        canvas.drawLine(rightVerticalLine, 0f, rightVerticalLine, height.toFloat(), paint)
        canvas.drawLine(0f, topHorizontalLine, width.toFloat(), topHorizontalLine, paint)
        canvas.drawLine(0f, bottomHorizontalLine, width.toFloat(), bottomHorizontalLine, paint)
    }

}