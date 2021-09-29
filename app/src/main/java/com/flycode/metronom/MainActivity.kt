package com.flycode.metronom

import android.app.ProgressDialog.show
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

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
            }
        }

        imageButtonDrawing = findViewById(R.id.imageButtonDrawing)

        imageButtonDrawing?.setOnClickListener {
            supportFragmentManager.let {
                DrawingBottomSheetDialog.newDInstance(Bundle()).apply {
                    show(it, tag)
                }
            }
        }

    }


}
