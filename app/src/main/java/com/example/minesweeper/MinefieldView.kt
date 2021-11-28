package com.example.minesweeper


import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.MotionEvent


class MinefieldView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val paint = Paint()
    private var size = 100f

    //user click coordinates
    private var x = -1
    private var y = -1

    //2d array used to store cell values, 0 for covered,1 for uncovered
    private val gridArray = Array(10, { IntArray(10) })



    override fun onDraw(canvas: Canvas) {
        // call the super method to keep any drawing from the parent side.
        super.onDraw(canvas)
        drawMinefield(canvas)
    }

    // function that will draw minefield in app
    private fun drawMinefield(canvas: Canvas) {
        //mine border
        paint.color= Color.WHITE
        paint.style = Paint.Style.FILL
        canvas.drawRect(0f, 0f, 1000f, 1000f, paint)

        var row = 0
        var column = 0
        //rows
        for (j in gridArray){
            //columns
            for(i in j){
                // if value on grid is 1 that will draw uncovered cell
                if(gridArray[row][column] == 1) {
                    //mine background
                    paint.color= Color.LTGRAY
                    paint.style = Paint.Style.FILL
                    canvas.drawRect(5f + column * size, 5f + row * size, 95f + column * size, 95f + row * size, paint)
                    column++
                }
                // value is 0 and it covered cell will be drawn
                else{
                    //mine background
                    paint.color= Color.BLACK
                    paint.style = Paint.Style.FILL
                    canvas.drawRect(5f + column * size, 5f + row * size, 95f + column * size, 95f + row * size, paint)
                    column++
                }
            }
            column = 0
            row++
        }
    }

    //function that will provide coordinates of user clicking on cells
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                x = event.x.toInt()
                y = event.y.toInt()
                //Log.i("TAG", x.toString() + " x")
                //Log.i("TAG", y.toString() + " y")

                // only if user click inside canvas gridArray will be updated
                if(x < 1000 && y < 1000){
                    // changes cell from covered to uncovered
                    gridArray[y/100][x/100] = 1
                    invalidate()
                }
            }
        }
        return true
    }

}