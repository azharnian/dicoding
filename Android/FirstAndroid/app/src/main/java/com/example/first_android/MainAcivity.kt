package com.example.first_android

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val text = findViewById<TextView>(R.id.txtHello)
        val button = findViewById<Button>(R.id.btnClick)

        button.setOnClickListener {
            text.text = "Button Clicked!"
        }
    }
}