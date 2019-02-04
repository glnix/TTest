package ru.goryachev.tochka.di

import android.content.Context
import toothpick.Toothpick

object DI {
    const val SCOPE_APP = "SCOPE_APP"
    const val SCOPE_FLOW_MAIN = "SCOPE_FLOW_MAIN"

    fun initAppScope(context: Context) {
        Toothpick.openScope(SCOPE_APP).apply {
            installModules(moduleApp(context), moduleNetwork(), moduleSocial(context))
        }
    }
}