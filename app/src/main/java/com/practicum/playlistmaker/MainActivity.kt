package com.practicum.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val button1 = findViewById<Button>(R.id.mode1)
        val imageClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, "Это кнопка поиска", Toast.LENGTH_SHORT).show()
            }
        }
        button1.setOnClickListener(imageClickListener)

        val button2 = findViewById<Button>(R.id.mode2)
        button2.setOnClickListener { Toast.makeText(this@MainActivity, "Это кнопка медиатеки",
            Toast.LENGTH_SHORT).show() }

        val button3 = findViewById<Button>(R.id.mode3)
        button3.setOnClickListener { Toast.makeText(this@MainActivity, "Это кринж кнопка", Toast.LENGTH_SHORT).show() }
    }
}