package ru.goryachev.tochka.feature.global.presentation.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import ru.goryachev.tochka.model.system.flow.FlowRouter
import ru.goryachev.tochka.model.ui.rx.LifecycleProvider
import ru.goryachev.tochka.model.ui.rx.LifecycleTransformer

abstract class BaseViewModel(private val router: FlowRouter) : ViewModel(),LifecycleObserver {

    private val provider = LifecycleProvider()
    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    fun <T> lifecycle(): LifecycleTransformer<T, T> = provider.lifecycle()

    override fun onCleared() {
        provider.unsubscribe()
        super.onCleared()
    }

    open fun onBackPressed() {
        router.finishFlow()
    }
}