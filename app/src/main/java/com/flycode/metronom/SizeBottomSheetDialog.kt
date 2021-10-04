package com.flycode.metronom

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ScrollView
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.w3c.dom.Text

//всплывающий экран с размером
class SizeBottomSheetDialog : BottomSheetDialogFragment(){

    var txt_double4: TextView? = null
    var scrollView2: ScrollView? = null
    var txt_2: TextView? = null
    var txt_3: TextView? = null
    var txt_4: TextView? = null
    var imageButtonFraction4: ImageButton? = null
    var imageButtonFraction3: ImageButton? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.size, container, false)
       // sizeChoice(textViewFractionSize)
        txt_double4 = view?.findViewById(R.id.txt_double4)
        scrollView2 = view?.findViewById(R.id.scrollView2)
        txt_2 = view?.findViewById(R.id.txt_2)
        txt_3 = view?.findViewById(R.id.txt_3)
        txt_4 = view?.findViewById(R.id.txt_4)

        txt_2?.setOnClickListener {
            textViewFractionSize = "2/4"
            this.startActivity(Intent(requireContext(), MainActivity::class.java))
            imageButtonFraction4?.visibility = View.GONE
            imageButtonFraction3?.visibility = View.GONE
        }

        txt_3?.setOnClickListener {
            textViewFractionSize = "3/4"
            this.startActivity(Intent(requireContext(), MainActivity::class.java))
            imageButtonFraction4?.visibility = View.GONE
        }

        txt_4?.setOnClickListener {
            textViewFractionSize = "4/4"
            this.startActivity(Intent(requireContext(), MainActivity::class.java))

        }
    }

    companion object {
        var textViewFractionSize: String = "q"

        @JvmStatic
        fun newInstance(bundle: Bundle): SizeBottomSheetDialog {
            val fragment = SizeBottomSheetDialog()
            fragment.arguments = bundle
            return fragment
        }
    }




}