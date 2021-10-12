package com.flycode.metronom.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.navigation.fragment.findNavController
import com.flycode.metronom.R
import com.flycode.metronom.model.SharedPref
import moxy.MvpAppCompatFragment

class SettingsFragment: MvpAppCompatFragment(R.layout.fragment_settings) {

    lateinit var imageButtonClose: ImageButton
    lateinit var switchTheme: SwitchCompat

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageButtonClose = view.findViewById(R.id.imageButtonClose)
        switchTheme = view.findViewById(R.id.switchTheme)

        if (SharedPref(requireContext()).getPreferences()){
            switchTheme.setChecked(true)
        }

        imageButtonClose.setOnClickListener {
            findNavController().popBackStack()
        }

        switchTheme.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked){
                SharedPref(requireContext()).setPreferences(true)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else{
                SharedPref(requireContext()).setPreferences(false)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}