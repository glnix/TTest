package ru.goryachev.tochka.feature.main.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.goryachev.tochka.feature.global.presentation.viewmodel.BaseViewModel
import ru.goryachev.tochka.model.system.flow.FlowRouter
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MainScreenViewModel(val router: FlowRouter) : BaseViewModel(router) {


}

class MainScreenViewModelFactory @Inject constructor(private val router: FlowRouter) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainScreenViewModel(router) as T
    }
}