package com.flycode.metronom

import android.app.ProgressDialog.show
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    var imageButtonSize: ImageButton = findViewById(R.id.imageButtonSize)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageButtonSize?.setOnClickListener {
            size()
            supportFragmentManager.let {
                SizeOptionsBottomSheetFragment.newInstance(Bundle()).apply {
                    show(it, tag)
                }
            }
        }
    }
  fun onItemClick(param:String) {
        when(param){
            "2"->{
                //Handle data
            }
            "3"->{
                //Handle data
            }
            "4"->{
                //Handle data
            }
            else->{
                //Handle data
            }
        }
    }

    fun size() {
        var sizeclk: String
        var textViewFractionSize: TextView = findViewById(R.id.textViewFractionSize)
        var txt_2: TextView = findViewById(R.id.txt_2)
        var txt_3: TextView = findViewById(R.id.txt_3)
        var txt_4: TextView = findViewById(R.id.txt_4)
        //   sizeclk =
        when (sizeclk) {
            txt_2.toString() -> textViewFractionSize.text = "2/4"
            txt_3.toString() -> textViewFractionSize.text = "3/4"
            txt_4.toString() -> textViewFractionSize.text = "4/4"
            else -> print("-")
        }
    }

}

