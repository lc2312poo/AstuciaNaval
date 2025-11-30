package com.example.astucianaval

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import java.util.*

object LocaleHelper {

    @SuppressLint("NewApi")
    fun setLocale(context: Context, lang: String): Context {
        val locale = Locale(lang)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        return context.createConfigurationContext(config)
    }
}
