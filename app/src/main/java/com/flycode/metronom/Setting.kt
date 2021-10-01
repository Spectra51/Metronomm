package com.flycode.metronom

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

//настройки
class Setting: AppCompatActivity()  {

    var imageButtonClose: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setting)

        imageButtonClose = findViewById(R.id.imageButtonClose)
        imageButtonClose?.setOnClickListener {
            this.startActivity(Intent(this, MainActivity::class.java))
        }
    }
}