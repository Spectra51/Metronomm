package com.flycode.metronom

import android.app.ProgressDialog.show
import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.w3c.dom.Text
import java.util.*
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {


    lateinit var textViewFractionSizeMain: TextView
    var linearLayoutSize: LinearLayout? = null
    var linearLayoutDrawing: LinearLayout? = null
    var imageButtonSize: ImageButton? = null
    var imageButtonDrawing: ImageButton? = null
    var scrollView2: ScrollView? = null
    var txt_2: TextView? = null
    var txt_3: TextView? = null
    var txt_4: TextView? = null
    var imageButtonSetting: ImageButton? = null
    var txt_double4: TextView? = null
    var imageButtonFraction4: ImageButton? = null
    var imageButtonFraction1: ImageButton? = null
    var imageButtonFraction2: ImageButton? = null
    var imageButtonFraction3: ImageButton? = null
    var dd: Array<Image>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textViewFractionSizeMain = findViewById(R.id.textViewFractionSize)
        var ss: Boolean? = null

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


         ss = false
        imageButtonFraction1?.setOnClickListener {

            if (ss!!){
                imageButtonFraction1!!.setImageResource(R.drawable.rec1)
                ss = false
            }else{
                imageButtonFraction1!!.setImageResource(R.drawable.rec2)
                ss = true
            }

            if (ss!!){
                imageButtonFraction1!!.setImageResource(R.drawable.rec3)
                ss = false
            }else{
                imageButtonFraction1!!.setImageResource(R.drawable.rec1)
                ss = true
            }
        }





        /* imageButtonFraction1?.setOnClickListener {
             if (flag){
                 imageButtonFraction1!!.setImageResource(R.drawable.rectangle_sv)
                 flag = false
             }else{
                 imageButtonFraction1!!.setImageResource(R.drawable.rectangle)
                 flag = true
             }
         }

         imageButtonFraction2?.setOnClickListener {
             if (flag){
                 imageButtonFraction2!!.setImageResource(R.drawable.rectangle_sv)
                 flag = false
             }else{
                 imageButtonFraction2!!.setImageResource(R.drawable.rectangle)
                 flag = true
             }
         }

         imageButtonFraction3?.setOnClickListener {
             if (flag){
                 imageButtonFraction3!!.setImageResource(R.drawable.rectangle_sv)
                 flag = false
             }else{
                 imageButtonFraction3!!.setImageResource(R.drawable.rectangle)
                 flag = true
             }
         }

         imageButtonFraction20?.setOnClickListener {
             if (flag){
                 imageButtonFraction20!!.setImageResource(R.drawable.rectangle_sv)
                 flag = false
             }else{
                 imageButtonFraction20!!.setImageResource(R.drawable.rectangle)
                 flag = true
             }
         }

         imageButtonFraction21?.setOnClickListener {
             if (flag){
                 imageButtonFraction21!!.setImageResource(R.drawable.rectangle_sv)
                 flag = false
             }else{
                 imageButtonFraction21!!.setImageResource(R.drawable.rectangle)
                 flag = true
             }
         }

         imageButtonFraction22?.setOnClickListener {
             if (flag){
                 imageButtonFraction22!!.setImageResource(R.drawable.rectangle_sv)
                 flag = false
             }else{
                 imageButtonFraction22!!.setImageResource(R.drawable.rectangle)
                 flag = true
             }
         }

         imageButtonFraction23?.setOnClickListener {
             if (flag){
                 imageButtonFraction23!!.setImageResource(R.drawable.rectangle_sv)
                 flag = false
             }else{
                 imageButtonFraction23!!.setImageResource(R.drawable.rectangle)
                 flag = true
             }
         }*/

    }





}
