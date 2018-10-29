package com.rizk.tubes.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.rizk.tubes.util.Log
import com.rizk.tubes.util.ViewUtils

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
    private var selectedTubeForMovement : Tube? = null
    private var selectedTubeDx : Float = 0f
    private var selectedTubeDy : Float = 0f
    private var start: MotionEvent? = null

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

    private fun swap(piece1: GamePiece, piece2: GamePiece)  {
        Log.v(TAG, "swap time")
        var pid1 = piece1.pieceId
        var pid2 = piece2.pieceId
        removeView(piece1)
        removeView(piece2)
        requestLayout()
//        addView(piece1, pid2)
//        addView(piece2, pid1)
//        requestLayout()
    }

    /// --- EVERYTHING BELOW THIS IS GESTURE STUFF AND CAN PROBABLY JUST MOVE TO SOMEWHERE ELSE
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                findViewByTouchEvent(event)
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                selectedTubeForMovement!!.animate()
                    .x(event.rawX + selectedTubeDx - (selectedTubeForMovement!!.getWidth() / 2))
                    .y(event.rawY + selectedTubeDy - (selectedTubeForMovement!!.getHeight() / 2))
                    .setDuration(0)
                    .start();
            }

            MotionEvent.ACTION_UP ->  {
                if (start != null) {
                    handleMove(start!!, event)
                    return true
                }
            }
            else -> return false
        }
        return true
    }

    //if we can move to event's cell, swap those two (if there's a tube there)
    private fun handleMove(s: MotionEvent, end : MotionEvent) {
        val srcCell = findCellByCoords(s.rawX, s.rawY)
        val destCell = findCellByCoords(end.rawX, end.rawY)
        Log.v(TAG, "srcCell $srcCell destCell $destCell")
        if (srcCell.equals(destCell)) {
            return
        }
        val destPiece = getGamePieceAt(destCell.first, destCell.second)
        val srcPiece = getGamePieceAt(srcCell.first, srcCell.second)
        if (srcPiece.isMoveable() && destPiece.isMoveable()) {
            swap(srcPiece, destPiece)
        }
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
                    start = MotionEvent.obtain(motionEvent)
                    selectedTubeDx = motionEvent.rawX - t.x
                    selectedTubeDy = motionEvent.rawY - t.y
                    return true
                }
            }
        }
        return false
    }

    /**
     * returns Pair of i,j
     */
    private fun findCellByCoords(x: Float, y: Float) : Pair<Int, Int> = Pair((x/cellWidth).toInt(), (y/cellHeight).toInt())

    private fun getGamePieceAt(i : Int, j : Int) : Tube = getChildAt(ViewUtils.twoDimensionToOneDimension(i, j, SIZE)) as Tube

}
