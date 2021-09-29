package com.flycode.metronom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SizeBottomSheetDialog : BottomSheetDialogFragment(){


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.size, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): SizeBottomSheetDialog {
            val fragment = SizeBottomSheetDialog()
            fragment.arguments = bundle
            return fragment
        }
    }
}