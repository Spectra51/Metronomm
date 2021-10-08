package com.flycode.metronom

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

const val PREFS_NAME = "theme_prefs"
const val KEY_THEME = "prefs.theme"
const val THEME_UNDEFINED = -1
const val THEME_LIGHT = 0
const val THEME_DARK = 1

//настройки
class Setting: AppCompatActivity()  {

    var switchTheme: Switch? = null
    private val sharedPrefs by lazy {  getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE) }

    var imageButtonClose: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setting)
      //  initThemeListener()
       // initTheme()
        imageButtonClose = findViewById(R.id.imageButtonClose)
        imageButtonClose?.setOnClickListener {
            this.startActivity(Intent(this, MainActivity::class.java))
        }

    }
/*
//смена темы

    fun initThemeListener(){
        switchTheme = findViewById(R.id.switchTheme)
        switchTheme?.setOnCheckedChangeListener { _, checkedId ->
            when (switchTheme?.isChecked) {
                false -> setTheme(AppCompatDelegate.MODE_NIGHT_NO, THEME_LIGHT)
                true -> setTheme(AppCompatDelegate.MODE_NIGHT_YES, THEME_DARK)
            }
        }
    }

    fun setTheme(themeMode: Int, prefsMode: Int) {
        AppCompatDelegate.setDefaultNightMode(themeMode)
        saveTheme(prefsMode)
    }

    fun initTheme() {
        when (getSavedTheme()) {
            THEME_LIGHT -> switchTheme?.isChecked = false
            THEME_DARK -> switchTheme?.isChecked = true
            THEME_UNDEFINED -> {
                when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> switchTheme?.isChecked = false
                    Configuration.UI_MODE_NIGHT_YES -> switchTheme?.isChecked = true
                }
            }
        }
    }

    fun saveTheme(theme: Int) = sharedPrefs.edit().putInt(KEY_THEME, theme).apply()

    fun getSavedTheme() = sharedPrefs.getInt(KEY_THEME, THEME_UNDEFINED)


*/

}