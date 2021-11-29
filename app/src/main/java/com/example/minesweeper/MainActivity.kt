package com.example.minesweeper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var minefieldView: MinefieldView
    private lateinit var totalMines: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        minefieldView = findViewById<MinefieldView>(R.id.minefieldView)
        totalMines = findViewById<TextView>(R.id.totalMines)

        minefieldView.setTotalMines(totalMines)
    }
}