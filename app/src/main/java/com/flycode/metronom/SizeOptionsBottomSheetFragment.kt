package com.flycode.metronom

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class SizeOptionsBottomSheetFragment : BottomSheetDialogFragment() {

    var txt_2: TextView = requireView().findViewById(R.id.txt_2)
    var txt_3: TextView = requireView().findViewById(R.id.txt_3)
    var txt_4: TextView = requireView().findViewById(R.id.txt_4)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.size, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
    }

    private fun setUpViews() {

        txt_2.setOnClickListener {
            dismissAllowingStateLoss()
            mListener?.onItemClick("2")

        }

        txt_3.setOnClickListener {
            dismissAllowingStateLoss()
            mListener?.onItemClick("3")
        }

        txt_4.setOnClickListener {
            dismissAllowingStateLoss()
            mListener?.onItemClick("4")

        }

    }

    private var mListener: ItemClickListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ItemClickListener) {
            mListener = context as ItemClickListener
        } else {
            throw RuntimeException(
                context.toString()
                    .toString() + " must implement ItemClickListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }
    interface ItemClickListener {
        fun onItemClick(item: String)
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): SizeOptionsBottomSheetFragment {
            val fragment = SizeOptionsBottomSheetFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}