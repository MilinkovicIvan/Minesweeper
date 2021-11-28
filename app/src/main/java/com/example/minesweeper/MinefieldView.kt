package com.example.minesweeper

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class MinefieldView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val paint = Paint()
    //private var size = 0

    override fun onDraw(canvas: Canvas) {
        // call the super method to keep any drawing from the parent side.
        super.onDraw(canvas)

        drawMinefield(canvas)
    }

    private fun drawMinefield(canvas: Canvas) {
        //mine border
        paint.color= Color.WHITE
        paint.style = Paint.Style.FILL
        canvas.drawRect(0f, 0f, 1000f, 1000f, paint)


        //mine background
        paint.color= Color.BLACK
        paint.style = Paint.Style.FILL
        canvas.drawRect(5f, 5f, 995f, 995f, paint)

    }
}