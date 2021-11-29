package com.example.minesweeper


import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.MotionEvent
import android.widget.TextView
import android.widget.Toast


class MinefieldView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val paint = Paint()
    private var size = 100f

    //user click coordinates
    private var x = -1
    private var y = -1

    //used to link textview with customview
    private var totalMinesUpdate: TextView? = null

    //2d array used to store cell values, 0 for covered,1 for uncovered
    private var gridArray = Array(10, { IntArray(10) })

    //2d array used to store mines on field, 0 for no mine,1 for mine
    private var mineArray = Array(10, { IntArray(10) })

    //flag used to reset game
    private var gameOk = true




    override fun onDraw(canvas: Canvas) {
        // call the super method to keep any drawing from the parent side.
        super.onDraw(canvas)
        drawMinefield(canvas)

    }

    // function that will draw minefield in app
    private fun drawMinefield(canvas: Canvas) {
        //adding mines to field
        val mineCount = checkMineAmount(mineArray)
        if(mineCount == 0){
            addMines(mineArray)
        }

        //cell border
        paint.color= Color.WHITE
        paint.style = Paint.Style.FILL
        canvas.drawRect(0f, 0f, 1000f, 1000f, paint)

        var mineHit = false

        var row = 0
        var column = 0
        //rows
        for (j in gridArray){
            //columns
            for(i in j){
                // if value on grid is 1 that will draw uncovered cell
                if(gridArray[row][column] == 1) {
                    //check if the cell is a mine
                    if(mineArray[row][column] == 1) {
                        //mine background
                        paint.color= Color.RED
                        paint.style = Paint.Style.FILL
                        canvas.drawRect(5f + column * size, 5f + row * size, 95f + column * size, 95f + row * size, paint)
                        paint.color= Color.BLACK
                        paint.textSize = 50f
                        val text = "M"
                        canvas.drawText(text,(5f + column * size)+25, (5f + row * size)+60, paint)
                        column++

                        //flag to see if mine is hit,if true than reveal all mines
                        mineHit = true
                        //popup for user
                        Toast.makeText(this.context, "Game Over! Reset to play again.", Toast.LENGTH_SHORT).show()
                        //set reset flag
                        this.gameOk = false
                    }
                    else{
                        //uncovered cell background
                        paint.color= Color.LTGRAY
                        paint.style = Paint.Style.FILL
                        canvas.drawRect(5f + column * size, 5f + row * size, 95f + column * size, 95f + row * size, paint)
                        column++
                    }
                }
                // value is 0 and it covered cell will be drawn
                else{
                    //covered cell background
                    paint.color= Color.BLACK
                    paint.style = Paint.Style.FILL
                    canvas.drawRect(5f + column * size, 5f + row * size, 95f + column * size, 95f + row * size, paint)
                    column++
                }
            }
            column = 0
            row++
        }
        // reveal all mines if mine is hit
        if (mineHit == true){
            revealMines(canvas)
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

                //mine count
                var mineCount = checkMineAmount(mineArray)
                //update total mines value
                totalMinesUpdate?.setText("Mines: " + mineCount.toString())

                // only if user click inside canvas gridArray will be updated
                if((x < 1000 && y < 1000) && gameOk){
                    // changes cell from covered to uncovered
                    gridArray[y/100][x/100] = 1
                    invalidate()
                }
            }
        }
        return true
    }
    //function used to update totalMines textView
    fun setTotalMines(totalMines: TextView?) {
        totalMinesUpdate = totalMines
    }

    //function which will add 20 mines randomly to mineArray
    private fun addMines(arr: Array<IntArray>){
        var mineCount = 0
        for (i in 1..100000) {
            if (mineCount < 20){
                val row = (0..9).random() // generated random from 0 to 9
                val column = (0..9).random()
                if(arr[row][column] == 0){
                    arr[row][column] = 1
                    mineCount++
                }
            }
        }
    }

    //function which will check how many mines are in array
    private fun checkMineAmount(arr: Array<IntArray>): Int {
        var count = 0

        var row = 0
        var column = 0
        //rows
        for (j in arr){
            //columns
            for(i in j){
                // if value on grid is 1 that means mine found
                if(arr[row][column] == 1) {
                    count++

                }
                column++
            }
            column = 0
            row++
        }
        return count
    }

    //function that reveals all mines on minefield
    fun revealMines(canvas: Canvas){
        var row = 0
        var column = 0
        //rows
        for (j in mineArray){
            //columns
            for(i in j){
                // if value on grid is 1 that means mine found
                if(mineArray[row][column] == 1) {
                    paint.color= Color.RED
                    paint.style = Paint.Style.FILL
                    canvas.drawRect(5f + column * size, 5f + row * size, 95f + column * size, 95f + row * size, paint)
                    paint.color= Color.BLACK
                    paint.textSize = 50f
                    var text = "M"
                    canvas.drawText(text,(5f + column * size)+25, (5f + row * size)+60, paint)
                }
                column++
            }
            column = 0
            row++
        }
    }

    //function triggered with reset button
    fun resetGame() {
        //reset grid array
        this.gridArray = Array(10, { IntArray(10) })

        //reset mine array
        this.mineArray = Array(10, { IntArray(10) })

        //reset flag
        this.gameOk = true

        invalidate()
    }

}