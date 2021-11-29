package com.example.minesweeper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var minefieldView: MinefieldView
    private lateinit var totalMines: TextView
    private lateinit var markedMines: TextView
    private lateinit var resetButton: Button
    private lateinit var changeMode: Button


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
    }
}