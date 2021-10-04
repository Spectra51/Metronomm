package com.flycode.metronom

import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ScrollView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

//всплывающий экран с нотами
class DrawingBottomSheetDialog : BottomSheetDialogFragment(){

   var scrollView3: ScrollView? = null
    var imageViewDrawingNotes: ImageView? = null
    var imageView: ImageView? = null
    var imageView2: ImageView? = null
    var imageView3: ImageView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.drawing, container, false)
        imageViewDrawingNotes = view?.findViewById(R.id.imageViewDrawingNotes)
        scrollView3 = view?.findViewById(R.id.scrollView3)
        imageView = view?.findViewById(R.id.imageView)
        imageView2 = view?.findViewById(R.id.imageView2)
        imageView3 = view?.findViewById(R.id.imageView3)


        imageView?.setOnClickListener {
            imageViewDrawingNotes!!.setImageResource(R.drawable.note1)
            this.startActivity(Intent(requireContext(), MainActivity::class.java))
        }

        imageView2?.setOnClickListener {
            imageViewDrawingNotes!!.setImageResource(R.drawable.note2)
            this.startActivity(Intent(requireContext(), MainActivity::class.java))
        }

        imageView3?.setOnClickListener {
            imageViewDrawingNotes!!.setImageResource(R.drawable.note3)
            this.startActivity(Intent(requireContext(), MainActivity::class.java))
        }
    }

    companion object {

        @JvmStatic
        fun newDInstance(bundle: Bundle): DrawingBottomSheetDialog {
            val fragment = DrawingBottomSheetDialog()
            fragment.arguments = bundle
            return fragment
        }
    }
}
