package com.flycode.metronom.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.graphics.drawable.toDrawable
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.flycode.metronom.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SheetDrawingFragment: BottomSheetDialogFragment() {

    companion object{
        const val drawingKey = "DRAWING_KEY"
        const val addSizeKey = "ADD_SIZE_KEY"
    }

    lateinit var imageViewNote1: ImageView
    lateinit var imageViewNote2: ImageView
    lateinit var imageViewNote3: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_drawing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageViewNote1 = view.findViewById(R.id.imageViewNote1)
        imageViewNote2 = view.findViewById(R.id.imageViewNote2)
        imageViewNote3 = view.findViewById(R.id.imageViewNote3)

        val size = arguments?.getString(addSizeKey).toString()

        imageViewNote1.setOnClickListener {
            //получаем предыдущий фрагментв стеке
            // берем обработчик его состояни и меняет его значение под выбранным ключем ключ
            // (в том фрагменте данные поменются даже если не вызвать findNavController().popBackStack())
            // для наглядности только тут не вызываем его
            findNavController().previousBackStackEntry?.savedStateHandle?.apply {
                this.set(drawingKey,R.drawable.note11)
                this.set(addSizeKey,size)
            }
//            findNavController().popBackStack()
        }

        imageViewNote2.setOnClickListener {
            findNavController().previousBackStackEntry?.savedStateHandle?.apply {
                this.set(drawingKey,R.drawable.note22)
                this.set(addSizeKey,size)
            }
            findNavController().popBackStack()
        }

        imageViewNote3.setOnClickListener {
            findNavController().previousBackStackEntry?.savedStateHandle?.apply {
                this.set(drawingKey,R.drawable.note33)
                this.set(addSizeKey,size)
            }
            findNavController().popBackStack()
        }
    }
}