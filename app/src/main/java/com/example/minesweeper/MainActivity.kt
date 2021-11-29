package com.example.minesweeper
//Ivan Milinkovic, 2991905

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var minefieldView: MinefieldView
    private lateinit var totalMines: TextView
    private lateinit var markedMines: TextView
    private lateinit var resetButton: Button
    private lateinit var changeMode: Button
    private lateinit var timer: TextView
    private var timerStarted = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // true for uncover mode, false for marking mode
        var modeFlag = true

        minefieldView = findViewById<MinefieldView>(R.id.minefieldView)
        totalMines = findViewById<TextView>(R.id.totalMines)
        markedMines = findViewById<TextView>(R.id.markedMines)
        resetButton = findViewById<Button>(R.id.reset)
        changeMode = findViewById<Button>(R.id.changeMode)
        timer = findViewById<TextView>(R.id.timer)

        //linking textviews with customview
        minefieldView.setTotalMines(totalMines)
        minefieldView.setMarkedMines(markedMines)

        // adding functionality when user clicks reset button
        resetButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                minefieldView.resetGame()
                //reset mode as well
                modeFlag = true
                changeMode.setText("Uncover mode")
                minefieldView.changeMode(true)

                //reset timer
                timer.text = "03:00"
                timer.setTextColor(Color.parseColor("#000000"))
                startTimer()
                timerStarted = true
            }
        })

        // adding functionality when user clicks changeMode button
        changeMode.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                //marking mode
                if(modeFlag){
                    modeFlag = false
                    changeMode.setText("Marking mode")
                    minefieldView.changeMode(false)
                }
                //uncover mode
                else{
                    modeFlag = true
                    changeMode.setText("Uncover mode")
                    minefieldView.changeMode(true)
                }

            }
        })

        //timer start/stop
        if(!timerStarted){
            startTimer()
        }
        else{
            stopTimer()
        }
    }
    //timer
    private var countDownTimer = object : CountDownTimer(1000*180, 1000){
        override fun onTick(millisUntilFinished: Long) {
            timer.text = getString(R.string.timeFormat,
                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60)

            //Log.i("TAG", millisUntilFinished.toString() + " Time")

            //add some drama in last min with red color timer
            if(millisUntilFinished < 61000){
                //set text to be red
                timer.setTextColor(Color.parseColor("#FF0000"))
            }
        }
        //on timer finish game over
        override fun onFinish() {
            timerStarted = false
            minefieldView.gameOver()
            Toast.makeText(getApplicationContext(), "Game Over! Reset to play again.", Toast.LENGTH_SHORT).show()
        }
    }

    //function to start timer
    private fun startTimer(){
        countDownTimer.start()
        timerStarted = true
    }
    //function to stop timer
    private fun stopTimer(){
        countDownTimer.cancel()
        timerStarted = false
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer.cancel()
    }
}