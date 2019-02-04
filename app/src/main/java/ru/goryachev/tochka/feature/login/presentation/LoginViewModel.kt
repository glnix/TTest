package ru.goryachev.tochka.feature.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.goryachev.tochka.Screens
import ru.goryachev.tochka.feature.global.presentation.viewmodel.BaseViewModel
import ru.goryachev.tochka.model.system.flow.FlowRouter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginViewModel(private val router: FlowRouter) : BaseViewModel(router) {

    fun loginVk() {
        router.startFlow(Screens.FLOW_VK_LOGIN)
    }

    fun loginFb() {
        router.startFlow(Screens.FLOW_FB_LOGIN)
    }

    fun loginGoogle() {
        router.startFlow(Screens.FLOW_GOOGLE_LOGIN)
    }

}


class LoginViewModelFactory @Inject constructor(private val router: FlowRouter) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(router) as T
    }
}