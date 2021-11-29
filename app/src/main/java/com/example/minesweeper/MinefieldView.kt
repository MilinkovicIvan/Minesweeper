package com.example.minesweeper
//Ivan Milinkovic, 2991905

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
    //var below used to keep track of marked mines for textview, and both for win condition
    private var markedMinesCount = 0
    private var uncoveredCellsCount = 0
    private var markedMinesUpdate: TextView? = null

    //2d array used to store cell values, 0 for covered,1 for uncovered
    private var gridArray = Array(10, { IntArray(10) })

    //2d array used to store mines on field, 0 for no mine,1 for mine
    private var mineArray = Array(10, { IntArray(10) })

    //flag used to reset game
    private var gameOk = true

    //flag to keep track of current mode,default uncover mode
    private var mode = true


    override fun onDraw(canvas: Canvas) {
        // call the super method to keep any drawing from the parent side.
        super.onDraw(canvas)
        drawMinefield(canvas)

    }

    // function that will draw minefield in app
    private fun drawMinefield(canvas: Canvas) {
        //check to see if win condition is there
        if(uncoveredCellsCount + markedMinesCount == 80){
            winCondition()
        }
        //adding mines to field
        val mineCount = checkMineAmount(mineArray)
        if(mineCount == 0){
            addMines(mineArray)
        }

        //cell border
        paint.color= Color.WHITE
        paint.style = Paint.Style.FILL
        canvas.drawRect(0f, 0f, 1000f, 1000f, paint)

        //mine hit flag
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

                        //check how many neighbour cells have mine and add that nb to uncovered cell
                        paint.color= Color.BLUE
                        paint.textSize = 50f
                        val nbOfMinesAround = checkMinesAround(column,row, mineArray)
                        if(nbOfMinesAround>0){
                            canvas.drawText(nbOfMinesAround.toString(),(5f + column * size)+35, (5f + row * size)+60, paint)
                        }
                        column++
                    }
                }
                //value is 2 and marked cell will be drawn
                else if(gridArray[row][column] == 2) {
                    //marked cell background
                    paint.color= Color.YELLOW
                    paint.style = Paint.Style.FILL
                    canvas.drawRect(5f + column * size, 5f + row * size, 95f + column * size, 95f + row * size, paint)
                    column++
                }
                // value is 0 and covered cell will be drawn
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

    private fun winCondition() {
        this.gameOk = false
        Toast.makeText(this.context, "You won! Reset to play again.", Toast.LENGTH_SHORT).show()
    }

    //function that will check how many mines are around specific cell,returns int amount
    private fun checkMinesAround(column: Int, row: Int, mineArray: Array<IntArray>): Int {
        var amount = 0

        //row above check 3 positions
        if((row-1 < 0 || column-1 < 0) || (row-1 > 9 || column-1 > 9)){
            //skip,out of grid
        }
        else if(mineArray[row-1][column-1] == 1){
            amount++
        }

        if((row-1 < 0 || column < 0) || (row-1 > 9 || column > 9) ){
            //skip,out of grid
        }
        else if(mineArray[row-1][column] == 1){
            amount++
        }

        if((row-1 < 0 || column+1 < 0) || (row-1 > 9 || column+1 > 9)){
            //skip,out of grid
        }
        else if(mineArray[row-1][column+1] == 1){
            amount++
        }

        //middle row check only left and right
        if((row < 0 || column-1 < 0) || (row > 9 || column-1 > 9)){
            //skip,out of grid
        }
        else if(mineArray[row][column-1] == 1){
            amount++
        }

        if((row < 0 || column+1 < 0) || (row > 9 || column+1 > 9)){
            //skip,out of grid
        }
        else if(mineArray[row][column+1] == 1){
            amount++
        }

        //row bellow check 3 positions
        if((row+1 < 0 || column-1 < 0) || (row+1 > 9 || column-1 > 9)){
            //skip,out of grid
        }
        else if(mineArray[row+1][column-1] == 1){
            amount++
        }

        if((row+1 < 0 || column < 0) || (row+1 > 9 || column > 9)){
            //skip,out of grid
        }
        else if(mineArray[row+1][column] == 1){
            amount++
        }

        if((row+1 < 0 || column+1 < 0) || (row+1 > 9 || column+1 > 9)){
            //skip,out of grid
        }
        else if(mineArray[row+1][column+1] == 1){
            amount++
        }

        return amount
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
                if((x < 1000 && y < 1000) && gameOk){
                    //uncover mode
                    if(mode){
                        //if cell is marked do nothing
                        if(gridArray[y/100][x/100] == 2){
                            //do nothing
                        }
                        else{
                            // changes cell from covered to uncovered
                            gridArray[y/100][x/100] = 1
                            uncoveredCellsCount++
                        }
                    }
                    //marking mode
                    else{
                        //uncovered case,do nothing
                        if(gridArray[y/100][x/100] == 1){
                            //do nothing
                        }
                        //if already marked then switch to uncovered
                        else if(gridArray[y/100][x/100] == 2){
                            gridArray[y/100][x/100] = 0
                            markedMinesCount--
                        }
                        else{
                            // changes cell from covered to marked
                            gridArray[y/100][x/100] = 2
                            markedMinesCount++
                        }
                    }
                    invalidate()
                }
                //mine count
                var mineCount = checkMineAmount(mineArray)
                //update total mines textview
                totalMinesUpdate?.setText("Total mines: " + mineCount.toString())
                //update marked mines textview
                markedMinesUpdate?.setText("Marked mines: " + markedMinesCount.toString())
            }
        }
        return true
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

    //function for game over
    fun gameOver(){
        this.gameOk = false
    }

    //function triggered with reset button
    fun resetGame() {
        //reset grid array
        this.gridArray = Array(10, { IntArray(10) })

        //reset mine array
        this.mineArray = Array(10, { IntArray(10) })

        //reset flag
        this.gameOk = true

        //reset counts
        this.markedMinesCount = 0
        this.uncoveredCellsCount = 0

        //update marked mines textview
        markedMinesUpdate?.setText("Marked mines: " + markedMinesCount.toString())

        invalidate()
    }

    //function that will change mode,triggered with button press
    fun changeMode(b: Boolean) {
        this.mode = b
    }

    //function used to update totalMines textView
    fun setTotalMines(totalMines: TextView?) {
        totalMinesUpdate = totalMines
    }

    //function used to update markedMines textView
    fun setMarkedMines(markedMines: TextView?) {
        markedMinesUpdate = markedMines
    }

}