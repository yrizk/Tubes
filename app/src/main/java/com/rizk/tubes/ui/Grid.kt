package com.rizk.tubes.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.rizk.tubes.util.Log

/**
 * The primary layout manager for the whole game.
 */
class Grid: ViewGroup {

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr : Int) : super(context, attributeSet, defStyleAttr)

    private val TAG:String = this.javaClass.simpleName;

    private val SIZE = 3;

    private val NUM_TUBES = (SIZE * SIZE) - 4;

    private val gridLines: FloatArray = FloatArray(4 * (SIZE - 1) * 2)

    private var currentCell = 0;

    private val paint: Paint = Paint()

    init {
        initPaint()
        initTubes()
    }

    private fun initTubes() {
        for (i in 0 until NUM_TUBES) {
            addView(Tube(context, i))
        }
    }


    private fun initPaint() {
        paint.color = Color.RED
        paint.strokeWidth = 10f
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
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.v(TAG, "widthMeasureSpec: ${MeasureSpec.toString(widthMeasureSpec)} , heightMeasureSpec: ${MeasureSpec.toString(heightMeasureSpec)}")
        var cellWidth = MeasureSpec.getSize(widthMeasureSpec) / SIZE
        var cellHeight = MeasureSpec.getSize(heightMeasureSpec) / SIZE
        for (i in 0 until childCount) {
            val child : Tube = getChildAt(i) as Tube // THIS WILL NOT HOLD FOREVER
            child.measure(MeasureSpec.makeMeasureSpec(cellWidth, MeasureSpec.EXACTLY),  MeasureSpec.makeMeasureSpec(cellHeight, MeasureSpec.EXACTLY));
        }
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        layoutGridLines()
        val i = 0
        val j = 0
        for (i in 0 until childCount) {
            val v = getChildAt(i) as Tube
            if (v.visibility != View.GONE) {
                val measuredWidth =  v.measuredWidth
                val measuredHeight = v.measuredHeight
                val leftPos = (i % SIZE).times(measuredWidth)
                val topPos = (i / SIZE).times(measuredHeight)
                Log.v(TAG, "left : $leftPos top: $topPos")
                v.layout(leftPos, topPos, leftPos +  measuredWidth, topPos + measuredHeight)
                currentCell = i
            }
        }
    }

    private fun layoutGridLines() {
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
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        canvas.drawLines(gridLines, paint)
    }
}
