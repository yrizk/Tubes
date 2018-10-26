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

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        Log.v(TAG, "onDraw")
        canvas.drawColor(Color.CYAN)
    }
}