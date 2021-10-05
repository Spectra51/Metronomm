package com.flycode.metronom

import android.app.ProgressDialog.show
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.graphics.drawable.toDrawable
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.w3c.dom.Text
import java.util.*
import kotlin.properties.Delegates
import kotlin.random.Random.Default.nextInt



class MainActivity : AppCompatActivity() {


    lateinit var textViewFractionSizeMain: TextView
    var linearLayoutSize: LinearLayout? = null
    var linearLayoutDrawing: LinearLayout? = null
    var imageButtonSize: ImageButton? = null
    var imageButtonDrawing: ImageButton? = null
    var imageButtonSetting: ImageButton? = null
    var imageButtonFraction4: ImageButton? = null
    var imageButtonFraction1: ImageButton? = null
    var imageButtonFraction2: ImageButton? = null
    var imageButtonFraction3: ImageButton? = null
var flag: Boolean? = null
var flag2: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.getDefaultNightMode())
        textViewFractionSizeMain = findViewById(R.id.textViewFractionSize)
        flag = true
        flag2 = false


        //вызов всплывающего экрана с размером
        imageButtonSize = findViewById(R.id.imageButtonSize)
        imageButtonSize?.setOnClickListener {
            supportFragmentManager.let {
                SizeBottomSheetDialog.newInstance(Bundle()).apply {
                    show(it, tag)
                }

                //прокрутка значений
                linearLayoutSize = findViewById(R.id.linearLayoutSize)
                val layoutInflater = LayoutInflater.from(this)
                val view: View = layoutInflater.inflate(R.layout.size, linearLayoutSize, false)
                linearLayoutSize?.addView(view)

                textViewFractionSizeMain.setText(SizeBottomSheetDialog.textViewFractionSize)
            }

        }

        //вызов всплывающего экрана с нотами
        imageButtonDrawing = findViewById(R.id.imageButtonDrawing)
        imageButtonDrawing?.setOnClickListener {
            supportFragmentManager.let {
                DrawingBottomSheetDialog.newDInstance(Bundle()).apply {
                    show(it, tag)
                }

                //прокрутка значений
                linearLayoutDrawing = findViewById(R.id.linearLayoutDrawing)
                val layoutInflater = LayoutInflater.from(this)
                val view: View =
                    layoutInflater.inflate(R.layout.drawing, linearLayoutDrawing, false)
                linearLayoutDrawing?.addView(view)
            }
        }

       //открыть экран настройки
        imageButtonSetting = findViewById(R.id.imageButtonSetting)
        imageButtonSetting?.setOnClickListener {
            this.startActivity(Intent(this, Setting::class.java))

        }

       //изменение сильная/слабая доля визуал

        imageButtonFraction4 = findViewById(R.id.imageButtonFraction4)
        imageButtonFraction1 = findViewById(R.id.imageButtonFraction1)
        imageButtonFraction2 = findViewById(R.id.imageButtonFraction2)
        imageButtonFraction3 = findViewById(R.id.imageButtonFraction3)

        imageButtonFraction1?.setOnClickListener {

            if (flag!!) {
                imageButtonFraction1!!.setImageResource(R.drawable.rec1)
                flag = false
                flag2 = false
            }else if (flag2!!){
                imageButtonFraction1!!.setImageResource(R.drawable.rec3)
                flag = true }
            else{
                imageButtonFraction1!!.setImageResource(R.drawable.rec2)
                flag2 = true
            }
        }



        imageButtonFraction2?.setOnClickListener {
            if (flag!!) {
                imageButtonFraction2!!.setImageResource(R.drawable.rec1)
                flag = false
                flag2 = false
            }else if (flag2!!){
                imageButtonFraction2!!.setImageResource(R.drawable.rec3)
                flag = true }
            else{
                imageButtonFraction2!!.setImageResource(R.drawable.rec2)
                flag2 = true
            }
        }

        imageButtonFraction3?.setOnClickListener {
            if (flag!!) {
                imageButtonFraction3!!.setImageResource(R.drawable.rec1)
                flag = false
                flag2 = false
            }else if (flag2!!){
                imageButtonFraction3!!.setImageResource(R.drawable.rec3)
                flag = true }
            else{
                imageButtonFraction3!!.setImageResource(R.drawable.rec2)
                flag2 = true
            }
        }

        imageButtonFraction4?.setOnClickListener {
            if (flag!!) {
                imageButtonFraction4!!.setImageResource(R.drawable.rec1)
                flag = false
                flag2 = false
            } else if (flag2!!) {
                imageButtonFraction4!!.setImageResource(R.drawable.rec3)
                flag = true
            } else {
                imageButtonFraction4!!.setImageResource(R.drawable.rec2)
                flag2 = true
            }
        }
    }



}
