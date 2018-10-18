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

    private val gridLines: FloatArray = FloatArray(4 * 4)

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
        var leftVerticalLine = width / 3f
        var rightVerticalLine = leftVerticalLine * 2
        var topHorizontalLine = height / 3f
        var bottomHorizontalLine = topHorizontalLine * 2
        Log.v(TAG, "leftVerticalLine $leftVerticalLine, rightVerticalLine $rightVerticalLine topHorizontalLine $topHorizontalLine, bottomHorizontalLine $bottomHorizontalLine")
        gridLines.set(0, 0f)
        gridLines.set(1, topHorizontalLine)
        gridLines.set(2, width.toFloat())
        gridLines.set(3, topHorizontalLine)

        gridLines.set(4, 0f)
        gridLines.set(5, bottomHorizontalLine)
        gridLines.set(6, width.toFloat())
        gridLines.set(7, bottomHorizontalLine)

        gridLines.set(8, leftVerticalLine)
        gridLines.set(9, 0f)
        gridLines.set(10, leftVerticalLine)
        gridLines.set(11, height.toFloat())

        gridLines.set(12, rightVerticalLine)
        gridLines.set(13, 0f)
        gridLines.set(14, rightVerticalLine)
        gridLines.set(15, height.toFloat())
        //todo: don't forget about laying the children
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        //todo use the batch drawLine call
        canvas.drawLines(gridLines, paint)
    }

}