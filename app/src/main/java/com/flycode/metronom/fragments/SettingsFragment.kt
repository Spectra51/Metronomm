package com.flycode.metronom.fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController
import com.flycode.metronom.R
import moxy.MvpAppCompatFragment

class SettingsFragment: MvpAppCompatFragment(R.layout.fragment_settings) {

    lateinit var imageButtonClose: ImageButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageButtonClose = view.findViewById(R.id.imageButtonClose)

        imageButtonClose.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}