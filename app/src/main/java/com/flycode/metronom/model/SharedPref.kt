package com.flycode.metronom.model

import android.content.Context

class SharedPref(context: Context) {

    companion object{
        const val THEME_SETTING = "theme_setting" // ключ для создания SharedPreferences
        const val IS_DARK_THEME = "is_dark_theme" // ключ для булевой переменной: тёмная тема или нет
    }

    // Создаём SharedPreferences
    val preferences = context.getSharedPreferences(THEME_SETTING, Context.MODE_PRIVATE)

    // Метод для установки значения в SharedPreferences.
    fun setPreferences(isDarkTheme: Boolean){
        val editor = preferences.edit()
        editor.putBoolean(IS_DARK_THEME, isDarkTheme)
        editor.apply()
    }

    // Метод для взятия значения из SharedPreferences. Если значения нет, то вернётся false.
    fun getPreferences(): Boolean{
        return preferences.getBoolean(IS_DARK_THEME, false)
    }


}