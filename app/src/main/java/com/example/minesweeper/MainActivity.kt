package com.example.minesweeper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var minefieldView: MinefieldView
    private lateinit var totalMines: TextView
    private lateinit var resetButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        minefieldView = findViewById<MinefieldView>(R.id.minefieldView)
        totalMines = findViewById<TextView>(R.id.totalMines)
        resetButton = findViewById<Button>(R.id.reset)

        minefieldView.setTotalMines(totalMines)

        // adding functionality when user clicks add buttons
        resetButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                minefieldView.resetGame()

            }
        })
    }
}