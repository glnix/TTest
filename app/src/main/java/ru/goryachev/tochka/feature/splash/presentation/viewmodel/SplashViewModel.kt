package ru.goryachev.tochka.feature.splash.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.Observable
import ru.goryachev.tochka.Screens
import ru.goryachev.tochka.feature.global.presentation.RxError
import ru.goryachev.tochka.feature.global.presentation.viewmodel.BaseViewModel
import ru.goryachev.tochka.feature.login.domain.LoginType
import ru.goryachev.tochka.model.system.flow.FlowRouter
import ru.goryachev.tochka.model.system.prefs.Preferences
import ru.goryachev.tochka.model.system.rx.SchedulersProvider
import ru.goryachev.tochka.model.system.rx.subscribe
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SplashViewModel(val router: FlowRouter,
                      val schedulersProvider: SchedulersProvider,
                      val prefs: Preferences) : BaseViewModel(router) {

    companion object {
        private const val TIME_LOADING = 1000L
    }

    fun onViewCreated() {
        Observable.timer(TIME_LOADING, TimeUnit.MILLISECONDS, schedulersProvider.computation())
                .observeOn(schedulersProvider.main())
                .compose(lifecycle())
                .subscribe({ startFlow() }, RxError.doNothing())
    }

    private fun startFlow() {
        val authState = prefs.authState
        if (authState.isEmpty() || authState == LoginType.NON_AUTH.toString()) router.startNewRootFlow(Screens.FLOW_LOGIN)
        else router.startNewRootFlow(Screens.FLOW_MAIN)
    }

    override fun onBackPressed() {/*nothing*/
    }
}

class SplashViewModelFactory @Inject constructor(private val router: FlowRouter,
                                                 private val schedulersProvider: SchedulersProvider,
                                                 private val prefs: Preferences) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SplashViewModel(router, schedulersProvider, prefs) as T
    }
}