package com.flycode.metronom

import android.app.ProgressDialog.show
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    var linearLayoutSize: LinearLayout? = null
    var linearLayoutDrawing: LinearLayout? = null


    lateinit var imageButtonSize: ImageButton
    lateinit var imageButtonDrawing: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)





        imageButtonSize = findViewById(R.id.imageButtonSize)
        imageButtonSize?.setOnClickListener {
            supportFragmentManager.let {
                SizeBottomSheetDialog.newInstance(Bundle()).apply {
                    show(it, tag)
                }
                linearLayoutSize = findViewById(R.id.linearLayoutSize)
                val layoutInflater = LayoutInflater.from(this)
                val view: View = layoutInflater.inflate(R.layout.size, linearLayoutSize, false)
                linearLayoutSize?.addView(view)
            }
        }

        imageButtonDrawing = findViewById(R.id.imageButtonDrawing)
        imageButtonDrawing?.setOnClickListener {
            supportFragmentManager.let {
                DrawingBottomSheetDialog.newDInstance(Bundle()).apply {
                    show(it, tag)
                }
                linearLayoutDrawing = findViewById(R.id.linearLayoutDrawing)
                val layoutInflater = LayoutInflater.from(this)
                val view: View = layoutInflater.inflate(R.layout.drawing, linearLayoutDrawing, false)
                linearLayoutDrawing?.addView(view)
            }
        }

    }


}
