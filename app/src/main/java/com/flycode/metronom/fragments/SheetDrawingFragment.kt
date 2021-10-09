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

        // Нажатие на 1-ю ноту
        imageViewNote1.setOnClickListener {
            // previousBackStackEntry - получаем предыдущий фрагмент (главный фрагмент) в стеке
            // savedStateHandle - берем обработчик состояния фрагмента и меняем его значение под выбранным ключем
            // (в главном фрагменте данные поменяются даже если не вызвать findNavController().popBackStack())
            // popBackStack() - шаг назад (с фрагментами)
            findNavController().previousBackStackEntry?.savedStateHandle?.apply {
                this.set(drawingKey,R.drawable.note11)
            }
            findNavController().popBackStack()
        }

        // Нажатие на 2-ю ноту
        imageViewNote2.setOnClickListener {
            findNavController().previousBackStackEntry?.savedStateHandle?.apply {
                this.set(drawingKey,R.drawable.note22)
            }
            findNavController().popBackStack()
        }

        // Нажатие на 3-ю ноту
        imageViewNote3.setOnClickListener {
            findNavController().previousBackStackEntry?.savedStateHandle?.apply {
                this.set(drawingKey,R.drawable.note33)
            }
            findNavController().popBackStack()
        }
    }
}