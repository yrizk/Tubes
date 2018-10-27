package com.rizk.tubes.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.TypedValue
import android.view.View
import com.rizk.tubes.util.Log



abstract class GamePiece(context: Context, id: Int) : View(context) {

    val pieceId : Int
        get() = field

    var paint: Paint = Paint()

    init {
        this.pieceId = id
        paint.color = Color.BLACK
        paint.textSize =
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                20f,
                resources.displayMetrics
            )

        paint.style = Paint.Style.FILL
        paint.typeface = Typeface.MONOSPACE
        paint.isAntiAlias = true
    }

    abstract fun isMoveable() : Boolean
}


class Tube(context: Context, id: Int) : GamePiece(context, id) {

    private val TAG = "Tube"

    init {
        Log.v(TAG, "id: $pieceId")
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        canvas.save()
        canvas.drawColor(Color.CYAN)
        canvas.drawText(pieceId.toString(), 100f, 100f, paint)
        canvas.restore()
    }

    override fun isMoveable() : Boolean = true
}