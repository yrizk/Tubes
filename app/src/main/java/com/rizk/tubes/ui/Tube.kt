package com.rizk.tubes.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View



class Tube : View {
     val tubeId : Int
        get() : Int = field


    private val TAG = "Tube"

    constructor(context: Context, id : Int) : super(context)

    private val paint = Paint()
    init {
        this.tubeId = id
        paint.color = Color.BLACK
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        canvas.drawText(tubeId.toString(), x, y, paint)
        canvas.drawColor(Color.CYAN)
    }
}