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

        textSize2.setOnClickListener {
            findNavController().previousBackStackEntry?.savedStateHandle?.apply {
                this.set(sizeKey, "2/4")
            }
            findNavController().popBackStack()
        }

        textSize3.setOnClickListener {
            findNavController().previousBackStackEntry?.savedStateHandle?.apply {
                this.set(sizeKey, "3/4")
            }
            findNavController().popBackStack()
        }

        textSize4.setOnClickListener {
            findNavController().previousBackStackEntry?.savedStateHandle?.apply {
                this.set(sizeKey, "4/4")
            }
            findNavController().popBackStack()
        }
    }

}