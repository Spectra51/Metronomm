package com.flycode.metronom.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.DialogFragmentNavigator
import androidx.navigation.fragment.findNavController
import com.flycode.metronom.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SheetSizeFragment: BottomSheetDialogFragment() {

    companion object{
        const val sizeKey = "SIZE_KEY"
        const val addDrawingKey = "ADD_DRAWING_KEY"
    }

    lateinit var textSize2: TextView
    lateinit var textSize3: TextView
    lateinit var textSize4: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_size, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textSize2 = view.findViewById(R.id.txt_2)
        textSize3 = view.findViewById(R.id.txt_3)
        textSize4 = view.findViewById(R.id.txt_4)

        val resourceNote = arguments?.getInt(addDrawingKey)
        Log.i("My", "${resourceNote}")

        textSize2.setOnClickListener {
            findNavController().navigate(R.id.action_sheetSizeFragment_to_metronomFragment,
            bundleOf(sizeKey to "2/4", addDrawingKey to resourceNote)
            )
        }

        textSize3.setOnClickListener {
            findNavController().navigate(R.id.action_sheetSizeFragment_to_metronomFragment,
                bundleOf(sizeKey to "3/4", addDrawingKey to resourceNote)
            )
        }

        textSize4.setOnClickListener {
            findNavController().navigate(R.id.action_sheetSizeFragment_to_metronomFragment,
                bundleOf(sizeKey to "4/4", addDrawingKey to resourceNote)
            )
        }
    }

}