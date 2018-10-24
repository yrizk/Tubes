package com.rizk.tubes.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import android.view.ViewGroup
import com.rizk.tubes.util.Log

/**
 * Makes a tic-tac-toe style grid.
 * Parent of the tubes.
 */
class Grid(context: Context?) : ViewGroup(context) {

    private val TAG:String = this.javaClass.simpleName;

    private val SIZE = 3;

    private val NUM_TUBES = SIZE * SIZE - 4;

    private val gridLines: FloatArray = FloatArray(4 * (SIZE - 1) * 2)

    private val paint: Paint = Paint()

    init {
        initPaint()
        initTubes()
    }


    private fun initPaint() {
        paint.color = Color.RED
        paint.strokeWidth = 10f
    }

    private fun initTubes() {
        for (i in 0 until (NUM_TUBES)) {
//            addView(Tube(context, width / SIZE, height / SIZE))
        }
    }

    // do not allow scrolling
    override fun shouldDelayChildPressedState(): Boolean {
        return false;
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        Log.v(TAG, "width $width height $height oldwidth: $oldw oldheight $oldh")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
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
        for (i in 0 until childCount) {
            val v = getChildAt(i)
            if (v.visibility != View.GONE) {
                val lp : LayoutParams = v.layoutParams
                val measuredWidth =   v.measuredWidth
                val measuredHeight = v.measuredHeight
                v.layout(0, 0, measuredWidth, measuredHeight)
            }
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        canvas.drawLines(gridLines, paint)
    }
}
