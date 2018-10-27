package com.rizk.tubes.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.rizk.tubes.util.Log
import com.rizk.tubes.util.ViewUtils
import kotlin.math.roundToInt

/**
 * The primary layout manager for the whole game.
 */
class Grid: ViewGroup {

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr : Int) : super(context, attributeSet, defStyleAttr)

    private val TAG:String = this.javaClass.simpleName;

    private val SIZE = 3;

    //TODO allow a non-square grid
    private val NUM_CELLS = SIZE.times(SIZE)

    private val NUM_TUBES = (NUM_CELLS) - 4;

    private val gridLines: FloatArray = FloatArray(4 * (SIZE - 1) * 2)

    private val cellCheck : BooleanArray = BooleanArray(NUM_CELLS)

    private var cellWidth : Int = 0
    private var cellHeight: Int = 0

    private val paint: Paint = Paint()

    /**
     * Drag and Drop Support
     * TODO support multitouch
     * TODO move this stuff into its own class
     */
    private var selectedTubeForMovement : Tube? = null;
    private var start: MotionEvent? = null

    private val listener =  object : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            // todo figure out which tube was moved.
            findViewByTouchEvent(e)
            return true
        }
        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            postInvalidate()
            return true
        }

        private fun findViewByTouchEvent(motionEvent: MotionEvent) : Boolean {
            val x = motionEvent.x
            val y = motionEvent.y
            //TODO I can optimize this search if I have a tree representation of the children which of course we do!
            // can also use top level dimensions
            for (i in 0 until childCount) {
                val t = getChildAt(i) as Tube
                if (t.left < x && t.right > x) {
                    if (t.top < y && t.bottom > y) {
                        selectedTubeForMovement = t
                        start = motionEvent
                        return true
                    }
                }
            }
            return false
        }
    }

    private val mDetector: GestureDetector = GestureDetector(context, listener)

    init {
        initPaint()
        initTubes()
    }

    private fun initTubes() {
        for (i in 0 until NUM_TUBES) {
            val location : Int = (Math.random() * NUM_CELLS).toInt()
            cellCheck.set(location, true)
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
        cellWidth = MeasureSpec.getSize(widthMeasureSpec) / SIZE
        cellHeight = MeasureSpec.getSize(heightMeasureSpec) / SIZE
        for (i in 0 until childCount) {
            val child : Tube = getChildAt(i) as Tube // THIS WILL NOT HOLD FOREVER
            child.measure(MeasureSpec.makeMeasureSpec(cellWidth, MeasureSpec.EXACTLY),  MeasureSpec.makeMeasureSpec(cellHeight, MeasureSpec.EXACTLY));
        }
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        layoutGridLines()
        for (i in 0 until childCount) {
            val v = getChildAt(i) as Tube
            if (v.visibility != View.GONE) {
                val measuredWidth =  v.measuredWidth
                val measuredHeight = v.measuredHeight
                var (leftPos, topPos) = ViewUtils.oneDimensionToTwoDimension(i, SIZE)
                leftPos = leftPos.times(measuredWidth)
                topPos = topPos.times(measuredHeight)
                Log.v(TAG, "left : $leftPos top: $topPos")
                v.layout(leftPos, topPos, leftPos +  measuredWidth, topPos + measuredHeight)
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

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        mDetector.onTouchEvent(event).let {
            // Already picking up ACTION_DOWN
            when (event?.action) {
                MotionEvent.ACTION_UP ->  {

                    start?.let {
                        // TODO here's what we're doing: if we can move to event's cell, swap those two (if there's a tube there)
                        handleMove(it, event)
                        return true;
                    }
                    return false
                }
            }
            return false
        }
    }

    private fun handleMove(start: MotionEvent, end : MotionEvent) {
        val srcCell = findCellByCoords(start.x.roundToInt(), start.y.roundToInt())
        val destCell = findCellByCoords(end.x.roundToInt(), end.y.roundToInt())
        if (srcCell.equals(destCell)) {
            return
        }
        val destPiece = getGamePieceAt(destCell.first, destCell.second)
        val srcPiece = getGamePieceAt(srcCell.first, srcCell.second)
        if (!srcPiece.isMoveable() || !destPiece.isMoveable()) {
            return
        }
        // go ahead and swap the two pieces
    }


    /**
     * returns Pair of i,j
     */
    private fun findCellByCoords(x: Int, y: Int) : Pair<Int, Int> = Pair(x/cellWidth, y/cellHeight);

    private fun getGamePieceAt(i : Int, j : Int) : GamePiece = getChildAt(ViewUtils.twoDimensionToOneDimension(i, j, SIZE)) as GamePiece

}
