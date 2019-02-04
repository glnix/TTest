package ru.goryachev.tochka.model.system.prefs

import android.content.Context
import javax.inject.Inject


class Preferences @Inject constructor(context: Context) {
    private val appContext: Context = context.applicationContext
    var authState by appContext.bindPreference("")
}