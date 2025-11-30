package com.example.astucianaval

import android.content.Context

object LanguagePreferences {

    private const val PREFS_NAME = "language_prefs"
    private const val KEY_LANGUAGE = "selected_language"

    fun saveLanguage(context: Context, lang: String) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_LANGUAGE, lang)
            .apply()
    }

    fun getLanguage(context: Context): String {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_LANGUAGE, "es") ?: "es"
    }
}
