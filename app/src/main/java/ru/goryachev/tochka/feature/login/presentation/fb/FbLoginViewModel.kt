package ru.goryachev.tochka.feature.login.presentation.fb

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.goryachev.tochka.Screens
import ru.goryachev.tochka.feature.global.presentation.viewmodel.BaseViewModel
import ru.goryachev.tochka.feature.login.domain.LoginType
import ru.goryachev.tochka.model.system.flow.FlowRouter
import ru.goryachev.tochka.model.system.prefs.Preferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FbLoginViewModel(val router: FlowRouter, val prefs: Preferences) : BaseViewModel(router) {

    fun onResult() {
        saveAuth()
        router.startNewRootFlow(Screens.FLOW_MAIN)
    }

    fun onError() {
        router.finishFlow()
    }

    private fun saveAuth() {
        prefs.authState = LoginType.FB.toString()
    }
}

class FbLoginViewModelFactory @Inject constructor(private val router: FlowRouter,
                                                  private val prefs: Preferences) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FbLoginViewModel(router, prefs) as T
    }
}