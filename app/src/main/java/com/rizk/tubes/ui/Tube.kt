package com.rizk.tubes.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import com.rizk.tubes.util.Log


class Tube : View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr : Int) : super(context, attributeSet, defStyleAttr)

    private val TAG = "Tube"

//
//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        Log.v("Tube onMeasure w", MeasureSpec.toString(widthMeasureSpec));
//        Log.v("Tube onMeasure h", MeasureSpec.toString(heightMeasureSpec));
//        var parentWidth = MeasureSpec.getSize(widthMeasureSpec);
//        var parentHeight = MeasureSpec.getSize(heightMeasureSpec);
//        super.onMeasure(MeasureSpec.makeMeasureSpec(parentWidth / 3, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(parentHeight / 3, MeasureSpec.EXACTLY))
//    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        Log.v(TAG, "onDraw")
        canvas.drawColor(Color.CYAN)
    }
}