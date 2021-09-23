package com.flycode.metronom.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import com.flycode.metronom.MainActivity
import com.flycode.metronom.R
import moxy.MvpAppCompatFragment

class MetronomFragment: MvpAppCompatFragment(R.layout.fragment_metronom) {

    lateinit var btnUp: ImageView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnUp = view.findViewById(R.id.imageBtnUp)

        btnUp.setOnClickListener {
            Log.i("My", "btn")
            Toast.makeText(requireContext(), "Up", Toast.LENGTH_SHORT).show()
        }
    }

}