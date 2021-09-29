package com.flycode.metronom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DrawingBottomSheetDialog : BottomSheetDialogFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.drawing, container, false)
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
